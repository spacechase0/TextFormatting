package com.spacechase0.minecraft.textformatting.asm;

import static org.objectweb.asm.Opcodes.*;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedType;
import com.spacechase0.minecraft.spacecore.util.FileUtils;
import com.spacechase0.minecraft.textformatting.TextFormattingLog;

public class CommandFilterTransformer implements IClassTransformer
{
	@Override
	public byte[] transform( String name, String transformedName, byte[] bytes )
	{
		if ( transformedName.equals( "net.minecraft.network.NetHandlerPlayServer" ) )
		{
			TextFormattingLog.info( "Text Formatting using ASM to make sign json filtering allow formatting (besides commands)..." );
			TextFormattingLog.info( "Using class " + name );
			
	        ClassNode classNode = new ClassNode();
	        ClassReader classReader = new ClassReader( bytes );
	        classReader.accept( classNode, 0 );
	        
	        Iterator< MethodNode > it = classNode.methods.iterator();
	        while ( it.hasNext() )
	        {
	        	MethodNode method = it.next();
	        	ObfuscatedMethod obfMethod = ObfuscatedMethod.fromObf( classNode.name, method.name, method.desc );
	        	ObfuscatedMethod mapped = ObfuscatedMethod.fromMcp( "net/minecraft/network/NetHandlerPlayServer", "processUpdateSign", "(Lnet/minecraft/network/play/client/C12PacketUpdateSign;)V" );
	        	if ( obfMethod.mcpName.equals( mapped.mcpName ) && obfMethod.deobfDesc.equals( mapped.deobfDesc ) )
	        	{
	        		TextFormattingLog.info( "Found NetHandlerPlayServer.processUpdateSign(...)." );
	        		transformMethod( name, method );
	        		break;
	        	}
	        }
	        
	        ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS );
	        classNode.accept( writer );
	        bytes = writer.toByteArray();
		}
		
		return bytes;
	}
	
	private void transformMethod( String name, MethodNode method )
	{
		ObfuscatedType iChatComponent = ObfuscatedType.fromDeobf( "net/minecraft/util/IChatComponent" );
		ObfuscatedType chatComponentText = ObfuscatedType.fromDeobf( "net/minecraft/util/ChatComponentText" );
		//System.out.println( "Using method " + method.name );
		for ( int i = 0; i < method.instructions.size(); ++i )
		{
			AbstractInsnNode ins = method.instructions.get( i );
			if ( ins.getOpcode() == NEW )
			{
				TypeInsnNode node = ( TypeInsnNode ) ins;
				System.out.println( node.desc +" =? " + chatComponentText );
				if ( node.desc.equals( chatComponentText.obfName ) )
				{
					TextFormattingLog.info( "Found new ChatComponentText, doing things now..." );
					while ( true )
					{
						AbstractInsnNode currIns = method.instructions.get( i );
						method.instructions.remove( currIns );
						
						if ( doesInitClass( chatComponentText, currIns ) )
						{
							break;
						}
					}
					
					InsnList inject = new InsnList();
					inject.add( new VarInsnNode( ALOAD, 6  ) );
					inject.add( new VarInsnNode( ILOAD, 7 ) );
					inject.add( new InsnNode( AALOAD ) ); // ^^, ^, and <- load the chat line
					inject.add( new MethodInsnNode( INVOKESTATIC, "com/spacechase0/minecraft/textformatting/ColorData", "removeCommandComponents", "(L" + iChatComponent.obfName + ";)L" + iChatComponent.obfName + ";", false ) );
					method.instructions.insertBefore( method.instructions.get( i ), inject );
					break;
				}
			}
		}
	}
	
	private static boolean doesInitClass( ObfuscatedType type, AbstractInsnNode ins )
	{
		if ( ins.getOpcode() != INVOKESPECIAL ) return false;
		MethodInsnNode node = ( MethodInsnNode ) ins;
		
		return ( node.owner.equals( type.obfName ) && node.name.equals( "<init>" ) );
	}
}
