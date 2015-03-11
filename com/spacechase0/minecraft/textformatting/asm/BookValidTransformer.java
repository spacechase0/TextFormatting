package com.spacechase0.minecraft.textformatting.asm;

import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.spacechase0.minecraft.textformatting.TextFormattingLog;

public class BookValidTransformer implements IClassTransformer
{
	@Override
	public byte[] transform( String name, String transformedName, byte[] bytes )
	{
		if ( transformedName.equals( "net.minecraft.item.ItemEditableBook" ) )
		{
			TextFormattingLog.info( "Text Formatting using ASM to remove the title length limit..." );
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
		for ( int i = 0; i < method.instructions.size(); ++i )
		{
			AbstractInsnNode ins = method.instructions.get( i );
			if ( ins.getOpcode() == BIPUSH )
			{
				IntInsnNode node = ( IntInsnNode ) ins;
				// "Remove" 16 page title limit
				if ( node.operand == 16 )
				{
					TextFormattingLog.info( "Found value 16, assuming it to be the title limit." );
					
					node.setOpcode( SIPUSH );
					node.operand = Short.MAX_VALUE;
				}
			}
		}
	}
}
