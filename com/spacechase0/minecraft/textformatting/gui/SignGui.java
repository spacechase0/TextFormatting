package com.spacechase0.minecraft.textformatting.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedField;
import com.spacechase0.minecraft.textformatting.ColorData;

public class SignGui extends GuiEditSign
{
	public SignGui( TileEntitySign theSign )
	{
		super( theSign );
		sign = theSign;
		
		for ( int i = 0; i < sign.signText.length; ++i )
		{
			if ( sign.signText[ i ] == null )
			{
				sign.signText[ i ] = new ChatComponentText( "" );
			}
		}
	}
	
	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        
		ColorData.calculateColorHeight( height );
		ColorData.drawGui( this, fontRendererObj, flags );
    }
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 200)
        {
            setEditLine( getEditLine() - 1 & 3 );
        }
        else if (keyCode == 208 || keyCode == 28 || keyCode == 156)
        {
            setEditLine( getEditLine() + 1 & 3 );
        }
        else if (keyCode == 14 ) // Backspace
        {
        	List siblings = getChatComponentSiblings( ( ChatComponentStyle ) sign.signText[ getEditLine() ] );
        	if ( siblings.size() > 0 )
        	{
        		int index = siblings.size() - 1;
        		IChatComponent comp = ( IChatComponent ) siblings.get( index );
        		if ( comp instanceof ChatComponentText )
        		{
        			// For text components, delete a character. If it becomes empty, delete it.
        			ChatComponentText text = ( ChatComponentText ) comp;
        			String str = text.getChatComponentText_TextValue();
        			str = str.substring( 0, str.length() - 1 );
        			if ( str.length() > 0 )
        			{
        				ChatComponentText newText = new ChatComponentText( str );
        				newText.setChatStyle( text.getChatStyle() );
        				siblings.set( index, newText );
        			}
        			else
        			{
        				siblings.remove( index );
        			}
        		}
        		else
        		{
        			// Any other components are a unit, and so can be removed all at once.
        			// This is mainly just in case someone uses the GUI on something existing,
        			// since I haven't really added a way to add the other kinds (yet?)
        			siblings.remove( index );
        		}
        	}
        }
        else if ( keyCode == 1 ) // Escape?
        {
            this.actionPerformed( getDoneButton() );
        }
        else // A normal character
        {
        	String str = sign.signText[ getEditLine() ].getUnformattedText();
        	if ( fontRendererObj.getStringWidth( str + typedChar ) >= 90 ) return;
        	
        	// TODO: Less lazy implementation. You know, combining characters with the same style.
        	// But I'm tired. :P Better than no release?
        	// Also, probably should move the bit stuff to ColorData with the rest
        	// Maybe could also simply by just having a current ChatStyle, and modify rendering to accommodate?
        	ChatStyle style = new ChatStyle();
        	/* Styles
        	16 +
			formats[ 0 ] = "Random";
			formats[ 1 ] = "Bold";
			formats[ 2 ] = "Strike";
			formats[ 3 ] = "Underline";
			formats[ 4 ] = "Italics";
			formats[ 5 ] = "Reset";
			*/
        	style.setObfuscated   ( ( flags & ( 1 << ( 16 + 0 ) ) ) != 0 );
        	style.setBold         ( ( flags & ( 1 << ( 16 + 1 ) ) ) != 0 );
        	style.setStrikethrough( ( flags & ( 1 << ( 16 + 2 ) ) ) != 0 );
        	style.setUnderlined   ( ( flags & ( 1 << ( 16 + 3 ) ) ) != 0 );
        	style.setItalic       ( ( flags & ( 1 << ( 16 + 4 ) ) ) != 0 );
        	for ( int i = 0; i < 16; ++i )
        	{
        		if ( ( flags & ( 1 << i ) ) != 0 )
        		{
        			style.setColor( EnumChatFormatting.func_175744_a( i ) );
        		}
        	}
        	// I almost used logarithms to avoid writing this^ loop because of how tired I am.
        	// Seriously.
    		// char code = ColorData.colorCodes[ ( int )( Math.log10( flags & 0xFFFF ) / Math.log10( 2 ) ) ];
        	// Then I realized I'd have to write a loop anyways to map the char to the EnumChatFormatting.
        	
        	ChatComponentStyle text = new ChatComponentText( "" + typedChar );
        	text.setChatStyle( style );
        	sign.signText[ getEditLine() ].appendSibling( text );
        }
    }
	
	@Override
    public void mouseClicked( int i, int j, int k ) throws IOException
    {
		super.mouseClicked( i, j, k );
		flags = ColorData.doClickFlags( i, j, width, flags );
    }
	
	public GuiButton getDoneButton()
	{
		String field = ObfuscatedField.fromMcp( "net/minecraft/client/gui/inventory/GuiEditSign", "doneBtn" ).srgName;
		return ( GuiButton ) ReflectionHelper.getPrivateValue( GuiEditSign.class, this, field );
	}
	
	public int getEditLine()
	{
		String field = ObfuscatedField.fromMcp( "net/minecraft/client/gui/inventory/GuiEditSign", "editLine" ).srgName;
		return ( Integer ) ReflectionHelper.getPrivateValue( GuiEditSign.class, this, field );
	}
	
	public void setEditLine( int line )
	{
		String field = ObfuscatedField.fromMcp( "net/minecraft/client/gui/inventory/GuiEditSign", "editLine" ).srgName;
		ReflectionHelper.setPrivateValue( GuiEditSign.class, this, line, field );
	}
	
	private static List getChatComponentSiblings( ChatComponentStyle comp )
	{
		String field = ObfuscatedField.fromMcp( "net/minecraft/util/ChatComponentStyle", "siblings" ).srgName;
		return ( List ) ReflectionHelper.getPrivateValue( ChatComponentStyle.class, comp, field );
	}
	
	public TileEntitySign sign;
	private int flags = 0;
}
