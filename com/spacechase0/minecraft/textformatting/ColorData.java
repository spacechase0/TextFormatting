package com.spacechase0.minecraft.textformatting;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class ColorData
{
    public static int[] colors;
    public static char[] colorCodes;
    public static String[] formats;
    public static char[] formatCodes;

    public static int posX = 5;
    public static int posY = 5;

    public static int buttonWidth = 50;
    public static int buttonHeight = 20;

    public static int padding = 3;
    
    public static String formatButtonStr = "Insert Format Symbol";
    public static int formatButtonId = 10;
    
    public static String formatSymbol = new String( new char[] { ( char ) 167 } );
    
    public static char getClickedCode( int x, int y, int width )
    {
        int loopY = posY;
        for ( int colorLoop = 0; colorLoop < 16; colorLoop += 1, loopY += buttonHeight + padding )
        {
			if ( x >= posX && y >= loopY &&
			     x < posX + buttonWidth &&
			     y < loopY + buttonHeight )
			{
				return colorCodes[ colorLoop ];
			}
			
			if ( colorLoop < 6 )
			{
				int px = width - buttonWidth - padding;
				if ( x >= px && y >= loopY &&
					 x < px + buttonWidth &&
					 y < loopY + buttonHeight )
				{
					return formatCodes[ colorLoop ];
				}
			}
        }
        
        return 'z';
    }
    
    public static void calculateColorHeight( int height )
    {
    	buttonHeight = height - ( posY * 2 );
        buttonHeight -= ( 16 - 1 ) * padding;
        buttonHeight /= 16;
    }
    
    public static String getLengthStr( int length, int max, int col )
    {
    	String str = formatSymbol;
		str += colorCodes[ col ];
		str += "(";
		str += length;
		str += "/" + max + ")";
		
		return str;
    }
    
    public static void drawGui( GuiScreen gui, FontRenderer fontRenderer )
    {
    	int loopY = posY;
        for ( int colorLoop = 0; colorLoop < 16; colorLoop += 1, loopY += buttonHeight + padding )
        {
			gui.drawRect( posX, loopY, posX + buttonWidth, loopY + buttonHeight, colors[ colorLoop ] );
			
			if ( colorLoop < 6 )
			{
				int x = gui.width - buttonWidth - padding;
				String str = formatSymbol;
				str += formatCodes[ colorLoop ];
				str += formats[ colorLoop ];
				
				gui.drawRect( x, loopY, x + buttonWidth, loopY + buttonHeight, colors[ 15 ] );
				fontRenderer.drawString( str, x + ( ( buttonWidth - fontRenderer.getStringWidth( formats[ colorLoop ] ) ) / 2 ), loopY + ( ( buttonHeight - 8 ) / 2 ), 0);
			}
		}
    }
    
    static 
    {
        colors = new int[ 16 ];
        colors[ 0 ] = 0xff000000;
        colors[ 1 ] = 0xff0000BF;
        colors[ 2 ] = 0xff00BF00;
        colors[ 3 ] = 0xff00BFBF;
        colors[ 4 ] = 0xffBF0000;
        colors[ 5 ] = 0xffBF00BF;
        colors[ 6 ] = 0xffffaa00;
        colors[ 7 ] = 0xffBFBFBF;
        colors[ 8 ] = 0xff404040;
        colors[ 9 ] = 0xff4040ff;
        colors[ 10 ] = 0xff49ff40;
        colors[ 11 ] = 0xff40ffff;
        colors[ 12 ] = 0xffff4040;
        colors[ 13 ] = 0xffff40ff;
        colors[ 14 ] = 0xffffff40;
        colors[ 15 ] = 0xffffffff;
		
		colorCodes = new char[ 16 ];
		for ( int i = 0; i < 16; ++i )
		{
			if ( i < 10 )
			{
				colorCodes[ i ] = ( char )( '0' + i );
			}
			else
			{
				colorCodes[ i ] = ( char )( 'a' + ( i - 10 ) );
			}
		}
		
		formats = new String[ 6 ];
		formats[ 0 ] = "Random";
		formats[ 1 ] = "Bold";
		formats[ 2 ] = "Strike";
		formats[ 3 ] = "Underline";
		formats[ 4 ] = "Italics";
		formats[ 5 ] = "Reset";
		
		formatCodes = new char[ 6 ];
		formatCodes[ 0 ] = 'k';
		formatCodes[ 1 ] = 'l';
		formatCodes[ 2 ] = 'm';
		formatCodes[ 3 ] = 'n';
		formatCodes[ 4 ] = 'o';
		formatCodes[ 5 ] = 'r';

		posX = 5;
		posY = 5;

		buttonWidth = 50;
		buttonHeight = 20;

		padding = 3;
		
		formatButtonStr = "Insert Format Symbol";
		formatButtonId = 10;
    }
}
