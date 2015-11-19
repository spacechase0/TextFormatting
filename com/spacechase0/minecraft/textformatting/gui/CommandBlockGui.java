package com.spacechase0.minecraft.textformatting.gui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.command.server.CommandBlockLogic;

import com.spacechase0.minecraft.textformatting.ColorData;

public class CommandBlockGui extends GuiCommandBlock
{
	public CommandBlockGui( CommandBlockLogic theCmd )
	{
		super( theCmd );
		cmd = theCmd;
	}
	
	@Override
    public void initGui()
	{
		super.initGui();
		
		buttonList.add(new GuiButton(ColorData.FORMAT_BUTTON_ID, width / 2 - 100, height / 4 + 132 + 20, ColorData.FORMAT_BUTTON_STR));

    	try
    	{
            text = new UnfilteredTextField(2, this.fontRendererObj, this.width / 2 - 150, 60, 300, 20);
            text.setMaxStringLength(32767);
            text.setFocused(true);
            text.setText( cmd.getCustomName() ); // getCommand
            
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
    protected void actionPerformed( GuiButton button ) throws IOException
    {
		super.actionPerformed( button );
		if ( button.enabled && button.id == ColorData.FORMAT_BUTTON_ID )
		{
			text.writeText( ColorData.FORMAT_SYMBOL );
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
    }
	
	@Override
    public void mouseClicked( int i, int j, int k ) throws IOException
    {
		char col = ColorData.getClickedCode( i, j, width );
		if ( col != 'z' )
		{
			text.writeText( ColorData.FORMAT_SYMBOL + col );
		}
		else
		{
			super.mouseClicked( i, j, k );
		}
    }
	
	public final CommandBlockLogic cmd;
	public GuiTextField text;
}
