package com.spacechase0.minecraft.textformatting.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.SIPUSH;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.spacechase0.minecraft.textformatting.TextFormattingLog;

public class AllowedCharactersTransformer implements IClassTransformer
{
	@Override
	public byte[] transform( String name, String transformedName, byte[] bytes )
	{
		if ( transformedName.equals( "net.minecraft.util.ChatAllowedCharacters" ) )
		{
			TextFormattingLog.info( "Text Formatting using ASM to allow the format symbol..." );
			bytes = transformClass( transformedName, bytes );
		}
		
		return bytes;
	}
	
	private byte[] transformClass( String name, byte[] bytes )
	{
		TextFormattingLog.info( "Using class " + name );
		
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );
        
        Iterator< MethodNode > it = classNode.methods.iterator();
        while ( it.hasNext() )
        {
        	MethodNode method = it.next();
        	transformMethod( method );
        }
        
        ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
        classNode.accept( writer );
        return writer.toByteArray();
	}
	
	private void transformMethod( MethodNode method )
	{
		//System.out.println( "Using method " + method.name );
		
		// getAllowedCharacters
		boolean foundClose = false;
		for ( int i = 0; i < method.instructions.size(); ++i )
		{
			AbstractInsnNode ins = method.instructions.get( i );
			if ( ins.getOpcode() == INVOKEVIRTUAL )
			{
				MethodInsnNode node = ( MethodInsnNode ) ins;
				if ( node.owner.equals( "java/io/BufferedReader" ) && node.name.equals( "close" ) )
				{
					foundClose = true;
				}
			}
			else if ( ins.getOpcode() == ALOAD && foundClose )
			{
				VarInsnNode node = ( VarInsnNode ) ins;
				if ( node.var == 0 )
				{
					injectFormatSymbol( method, i );
					foundClose = false;
				}
			}
		}
		
		// isAllowedCharacter
		for ( int i = 0; i < method.instructions.size(); ++i )
		{
			AbstractInsnNode ins = method.instructions.get( i );
			if ( ins.getOpcode() == SIPUSH )
			{
				// Remove limitation of character 167
				IntInsnNode node = ( IntInsnNode ) ins;
				if ( node.operand == 167 )
				{
					TextFormattingLog.info( "Found value 167, assuming it to be the formatting symbol." );
					node.operand = 0;
				}
			}
		}
	}
	
	private void injectFormatSymbol( MethodNode method, int index )
	{
		TextFormattingLog.info( "Injecting at index " + index );
		
		InsnList list = new InsnList();
		list.add( new TypeInsnNode( NEW, "java/lang/StringBuilder" ) );
		list.add( new InsnNode( DUP ) );
		list.add( new LdcInsnNode( "Adding format symbol to allowed characters list..." ) );
		list.add( new MethodInsnNode( INVOKESTATIC, "com/spacechase0/minecraft/textformatting/TextFormattingLog", "info", "(Ljava/lang/String;)V" ) );
		list.add( new VarInsnNode( ALOAD, 0 ) );
		list.add( new MethodInsnNode( INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V" ) );
		list.add( new FieldInsnNode( GETSTATIC, "com/spacechase0/minecraft/textformatting/ColorData", "formatSymbol", "Ljava/lang/String;" ) );
		list.add( new MethodInsnNode( INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;" ) );
		list.add( new MethodInsnNode( INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;" ) );
		list.add( new VarInsnNode( ASTORE, 0 ) );
		
		method.instructions.insertBefore( method.instructions.get( index ), list );
	}
}
