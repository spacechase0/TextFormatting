package com.spacechase0.minecraft.textformatting.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;

import com.spacechase0.minecraft.textformatting.ColorData;

public class CommandBlockGui extends GuiCommandBlock
{
	public CommandBlockGui( CommandBlockLogic theCmd )
	{
		super( theCmd );
		cmd = theCmd;
		
		/*
		lines.add( "@p -- Nearest player" );
		lines.add( "@r -- Random player" );
		lines.add( "@a -- All players" );
		*/
		lines.add( "[" );
		lines.add( "     x  -- Search center X" );
		lines.add( "     y  -- Search center Y" );
		lines.add( "     z  -- Search center Z" );
		lines.add( "     r  -- Max search radius" );
		lines.add( "     rm -- Min search radius" );
		lines.add( "     c  -- Player amount, negative starts from end" );
		lines.add( "     l  -- Max XP level" );
		lines.add( "     lm -- Min XP level" );
		lines.add( "     team -- Team required (or !excluded)" );
		lines.add( "     name -- Name required (or !excluded)" );
		lines.add( "     score_<NAME>     -- Max value for objective <NAME>" );
		lines.add( "     score_<NAME>_min -- Min value for objective <NAME>" );
		lines.add( "]" );
	}
	
	@Override
    public void initGui()
	{
		super.initGui();
		
		buttonList.add(new GuiButton(ColorData.formatButtonId, width / 2 - 100, height / 4 + 132 + 20, ColorData.formatButtonStr));

    	try
    	{
            text = new UnfilteredTextField(this.fontRendererObj, this.width / 2 - 150, 60, 300, 20);
            text.setMaxStringLength(32767);
            text.setFocused(true);
            text.setText( cmd.func_145753_i() ); // getCommand
            
	    	Class c = GuiCommandBlock.class;
	    	Field f = c.getDeclaredFields()[ 1 ];
	    	f.setAccessible( true );
	    	f.set( this, text );
    	}
    	catch ( Exception exception )
    	{
    		exception.printStackTrace();
    	}
	}
	
	@Override
    protected void actionPerformed( GuiButton button )
    {
		super.actionPerformed( button );
		if ( button.enabled && button.id == ColorData.formatButtonId )
		{
			text.writeText( ColorData.formatSymbol );
		}
    }
	
	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        
		ColorData.calculateColorHeight( height );
		ColorData.drawGui( this, fontRendererObj );

		String lengthStr = ColorData.getLengthStr( text.getText().length(), text.getMaxStringLength(), 15 );
		fontRendererObj.drawString( lengthStr, ( width / 2 ) - 150, 81, 0 );
		
		int x = ( width / 2 ) - 150;
		int y = 119 + 11;
		for ( int i = 0; i < lines.size(); ++i )
		{
			fontRendererObj.drawString( lines.get( i ), x, y, 10526880 );
			y += 12;
		}
    }
	
	@Override
    public void mouseClicked( int i, int j, int k )
    {
		char col = ColorData.getClickedCode( i, j, width );
		if ( col != 'z' )
		{
			text.writeText( ColorData.formatSymbol + col );
		}
		else
		{
			super.mouseClicked( i, j, k );
		}
    }
	
	public final CommandBlockLogic cmd;
	public GuiTextField text;
	
	private List< String > lines = new ArrayList< String >();
}
