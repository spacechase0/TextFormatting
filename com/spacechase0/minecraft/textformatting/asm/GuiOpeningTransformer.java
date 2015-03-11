package com.spacechase0.minecraft.textformatting.asm;

import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedType;
import com.spacechase0.minecraft.spacecore.util.FileUtils;
import com.spacechase0.minecraft.textformatting.TextFormattingLog;

public class GuiOpeningTransformer implements IClassTransformer
{
	@Override
	public byte[] transform( String name, String transformedName, byte[] bytes )
	{
		//TextFormattingLog.info(name+" -> "+transformedName);
		if ( transformedName.equals( "net.minecraft.client.entity.EntityPlayerSP" ) )
		{
			TextFormattingLog.info( "Text Formatting using ASM to make sure our GUI is used..." );
			//FileUtils.saveBytes( "EntityPlayerSP.pre.class", bytes );
			bytes = transformClass( name, transformedName, bytes );
			//FileUtils.saveBytes( "EntityPlayerSP.post.class", bytes );
		}
		
		return bytes;
	}
	
	private byte[] transformClass( String obfName, String name, byte[] bytes )
	{
		TextFormattingLog.info( "Using class " + name );
		
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );
        
        Iterator< MethodNode > it = classNode.methods.iterator();
        while ( it.hasNext() )
        {
        	MethodNode method = it.next();
        	transformMethod( obfName, method );
        }
        
        ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
        classNode.accept( writer );
        return writer.toByteArray();
	}
	
	private void transformMethod( String obfClassName, MethodNode method )
	{
		for ( int i = 0; i < method.instructions.size(); ++i )
		{
			AbstractInsnNode ins = method.instructions.get( i );
			if ( ins.getOpcode() == NEW )
			{
				TypeInsnNode node = ( TypeInsnNode ) ins;
				ObfuscatedType type  = ObfuscatedType.fromObf( node.desc );
				if ( type.deobfName.equals( "net/minecraft/client/gui/inventory/GuiEditSign" ) )
				{
					TextFormattingLog.info( "Found vanilla sign GUI class, changing to ours..." );
					node.desc = "com/spacechase0/minecraft/textformatting/gui/SignGui";
				}
				else if ( type.deobfName.equals( "net/minecraft/client/gui/GuiCommandBlock" ) )
				{
					TextFormattingLog.info( "Found vanilla command block GUI class, changing to ours..." );
					node.desc = "com/spacechase0/minecraft/textformatting/gui/CommandBlockGui";
				}
				else if ( type.deobfName.equals( "net/minecraft/client/gui/GuiScreenBook" ) )
				{
					TextFormattingLog.info( "Found vanilla book GUI class, changing to ours..." );
					node.desc = "com/spacechase0/minecraft/textformatting/gui/BookGui";
				}
			}
			else if ( ins.getOpcode() == INVOKESPECIAL )
			{
				MethodInsnNode node = ( MethodInsnNode ) ins;
				ObfuscatedType type = ObfuscatedType.fromObf( node.owner );
				if ( type.deobfName.equals( "net/minecraft/client/gui/inventory/GuiEditSign" ) )
				{
					TextFormattingLog.info( "Found vanilla sign GUI <init>, changing to ours..." );
					node.owner = "com/spacechase0/minecraft/textformatting/gui/SignGui";
				}
				else if ( type.deobfName.equals( "net/minecraft/client/gui/GuiCommandBlock" ) )
				{
					TextFormattingLog.info( "Found vanilla command block GUI <init>, changing to ours..." );
					node.owner = "com/spacechase0/minecraft/textformatting/gui/CommandBlockGui";
				}
				else if ( type.deobfName.equals( "net/minecraft/client/gui/GuiScreenBook" ) )
				{
					TextFormattingLog.info( "Found vanilla book GUI <init>, changing to ours..." );
					node.owner = "com/spacechase0/minecraft/textformatting/gui/BookGui";
				}
			}
		}
	}
}
