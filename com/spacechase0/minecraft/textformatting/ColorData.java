package com.spacechase0.minecraft.textformatting;

import java.util.List;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedField;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ColorData
{
    public static final int[] COLORS;
    public static final char[] COLOR_CODES;
    public static final String[] FORMATS;
    public static final char[] FORMAT_CODES;

    public static final int POS_X = 5;
    public static final int POS_Y = 5;

    public static int buttonWidth = 50;
    public static int buttonHeight = 20;

    public static final int PADDING = 3;
    public static final int OUTLINE = 2;
    
    public static final String FORMAT_BUTTON_STR = "Insert Format Symbol"; // TODO: Translate
    public static final int FORMAT_BUTTON_ID = 10;
    
    public static final String FORMAT_SYMBOL = new String( new char[] { ( char ) 167 } );
    
    public static char getClickedCode( int x, int y, int width )
    {
        int loopY = POS_Y;
        for ( int colorLoop = 0; colorLoop < 16; colorLoop += 1, loopY += buttonHeight + PADDING )
        {
			if ( x >= POS_X && y >= loopY &&
			     x < POS_X + buttonWidth &&
			     y < loopY + buttonHeight )
			{
				return COLOR_CODES[ colorLoop ];
			}
			
			if ( colorLoop < 6 )
			{
				int px = width - buttonWidth - PADDING;
				if ( x >= px && y >= loopY &&
					 x < px + buttonWidth &&
					 y < loopY + buttonHeight )
				{
					return FORMAT_CODES[ colorLoop ];
				}
			}
        }
        
        return 'z';
    }
    
    // Suddenly I wish I had references. Or even a mutable Integer. Ugh.
    public static int doClickFlags( int x, int y, int width, int flags )
    {
        int loopY = POS_Y;
        for ( int colorLoop = 0; colorLoop < 16; colorLoop += 1, loopY += buttonHeight + PADDING )
        {
			if ( x >= POS_X && y >= loopY &&
			     x < POS_X + buttonWidth &&
			     y < loopY + buttonHeight )
			{
				// Keep general style flags, but clear other colors and toggle the selected one.
				if ( ( flags & ( 1 << colorLoop ) ) != 0 ) // If color already active
				{
					flags = ( flags & 0x3F0000 );
				}
				else
				{
					flags = ( flags & 0x3F0000 ) | ( 1 << colorLoop );
				}
			}
			
			if ( colorLoop < 6 )
			{
				int px = width - buttonWidth - PADDING;
				if ( x >= px && y >= loopY &&
					 x < px + buttonWidth &&
					 y < loopY + buttonHeight )
				{
					if ( colorLoop == 5 )
					{
						flags = 0; // There is a format code for this...
					}
					else
					{
						flags ^= 1 << ( 16 + colorLoop );
					}
				}
			}
        }
        
        return flags;
    }
    
    public static void calculateColorHeight( int height )
    {
    	buttonHeight = height - ( POS_Y * 2 );
        buttonHeight -= ( 16 - 1 ) * PADDING;
        buttonHeight /= 16;
    }
    
    public static String getLengthStr( int length, int max, int col )
    {
    	String str = FORMAT_SYMBOL;
		str += COLOR_CODES[ col ];
		str += "(";
		str += length;
		str += "/" + max + ")";
		
		return str;
    }
    
    // Traditional version
    public static void drawGui( GuiScreen gui, FontRenderer fontRenderer )
    {
    	drawGui( gui, fontRenderer, 0 );
    }
    
    // Toggle version
    public static void drawGui( GuiScreen gui, FontRenderer fontRenderer, int flags )
    {
    	int loopY = POS_Y;
        for ( int colorLoop = 0; colorLoop < 16; colorLoop += 1, loopY += buttonHeight + PADDING )
        {
			if ( ( flags & ( 1 << colorLoop ) ) != 0 )
			{
				gui.drawRect( POS_X - OUTLINE, loopY - OUTLINE, POS_X + buttonWidth + OUTLINE, loopY + buttonHeight + OUTLINE, COLORS[ colorLoop ] ^ 0x00FFFFFF );
			}
			gui.drawRect( POS_X, loopY, POS_X + buttonWidth, loopY + buttonHeight, COLORS[ colorLoop ] );
			
			if ( colorLoop < 6 )
			{
				int x = gui.width - buttonWidth - PADDING;
				String str = FORMAT_SYMBOL;
				str += FORMAT_CODES[ colorLoop ];
				str += FORMATS[ colorLoop ];

				if ( ( flags & ( 1 << ( colorLoop + 16 ) ) ) != 0 )
				{
					gui.drawRect( x - OUTLINE, loopY - OUTLINE, x + buttonWidth + OUTLINE, loopY + buttonHeight + OUTLINE, 0xFF888888 );
				}
				
				gui.drawRect( x, loopY, x + buttonWidth, loopY + buttonHeight, COLORS[ 15 ] );
				fontRenderer.drawString( str, x + ( ( buttonWidth - fontRenderer.getStringWidth( FORMATS[ colorLoop ] ) ) / 2 ), loopY + ( ( buttonHeight - 8 ) / 2 ), 0);
			}
		}
    }
    
    // Remove command-running capabilities from any components
    // Returns itself for ease of injecting
    public static IChatComponent removeCommandComponents( IChatComponent mainComp )
    {
    	List siblings = getChatComponentSiblings( ( ChatComponentStyle ) mainComp );
    	for ( int i = 0; i < siblings.size(); ++i )
    	{
    		IChatComponent comp = ( IChatComponent ) siblings.get( i );
    		removeCommandComponents( comp );
    	}
    	
    	if ( mainComp.getChatStyle().getChatClickEvent() != null )
    	{
    		mainComp.getChatStyle().setChatClickEvent( null );
    	}
    	
    	return mainComp;
    }
	
	private static List getChatComponentSiblings( ChatComponentStyle comp )
	{
		String field = ObfuscatedField.fromMcp( "net/minecraft/util/ChatComponentStyle", "siblings" ).srgName;
		return ( List ) ReflectionHelper.getPrivateValue( ChatComponentStyle.class, comp, field );
	}
    
    static 
    {
        COLORS = new int[ 16 ];
        COLORS[ 0 ] = 0xff000000;
        COLORS[ 1 ] = 0xff0000BF;
        COLORS[ 2 ] = 0xff00BF00;
        COLORS[ 3 ] = 0xff00BFBF;
        COLORS[ 4 ] = 0xffBF0000;
        COLORS[ 5 ] = 0xffBF00BF;
        COLORS[ 6 ] = 0xffffaa00;
        COLORS[ 7 ] = 0xffBFBFBF;
        COLORS[ 8 ] = 0xff404040;
        COLORS[ 9 ] = 0xff4040ff;
        COLORS[ 10 ] = 0xff49ff40;
        COLORS[ 11 ] = 0xff40ffff;
        COLORS[ 12 ] = 0xffff4040;
        COLORS[ 13 ] = 0xffff40ff;
        COLORS[ 14 ] = 0xffffff40;
        COLORS[ 15 ] = 0xffffffff;
		
		COLOR_CODES = new char[ 16 ];
		for ( int i = 0; i < 16; ++i )
		{
			if ( i < 10 )
			{
				COLOR_CODES[ i ] = ( char )( '0' + i );
			}
			else
			{
				COLOR_CODES[ i ] = ( char )( 'a' + ( i - 10 ) );
			}
		}
		
		FORMATS = new String[ 6 ];
		FORMATS[ 0 ] = "Random";
		FORMATS[ 1 ] = "Bold";
		FORMATS[ 2 ] = "Strike";
		FORMATS[ 3 ] = "Underline";
		FORMATS[ 4 ] = "Italics";
		FORMATS[ 5 ] = "Reset";
		
		FORMAT_CODES = new char[ 6 ];
		FORMAT_CODES[ 0 ] = 'k';
		FORMAT_CODES[ 1 ] = 'l';
		FORMAT_CODES[ 2 ] = 'm';
		FORMAT_CODES[ 3 ] = 'n';
		FORMAT_CODES[ 4 ] = 'o';
		FORMAT_CODES[ 5 ] = 'r';
    }
}
