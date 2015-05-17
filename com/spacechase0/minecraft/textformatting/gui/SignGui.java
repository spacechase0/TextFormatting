package com.spacechase0.minecraft.textformatting.gui;

import java.lang.reflect.Field;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedField;
import com.spacechase0.minecraft.textformatting.ColorData;

public class SignGui extends GuiEditSign
{
	public SignGui( TileEntitySign theSign )
	{
		super( theSign );
		sign = theSign;
	}
	
	@Override
    public void initGui()
	{
		super.initGui();
		
		buttonList.add(new GuiButton(ColorData.formatButtonId, width / 2 - 100, height / 4 + 120 + 20, ColorData.formatButtonStr));
	}
	
	@Override
    protected void actionPerformed( GuiButton button )
    {
		super.actionPerformed( button );
		if( button.enabled && button.id == ColorData.formatButtonId )
		{
			if( sign.signText[ getEditLine() ].length() < 15)
			{
				sign.signText[ getEditLine() ] += ColorData.formatSymbol;
			}
		}
    }
	
	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        
		ColorData.calculateColorHeight( height );
		ColorData.drawGui( this, fontRendererObj );

		String lengthStr = ColorData.getLengthStr( sign.signText[ getEditLine() ].length(), 16, 15 );
		fontRendererObj.drawString( lengthStr, ( width / 2 ) + ( 50 ) + ColorData.padding, ( height / 2 ) - ( 50 ) + ( 11 * getEditLine() ), 0 );
    }
	
	@Override
    public void mouseClicked( int i, int j, int k )
    {
		super.mouseClicked( i, j, k );

		if ( sign.signText[ getEditLine() ].length() >= 14 )
		{
			return;
		}

		char col = ColorData.getClickedCode( i, j, width );
		if ( col != 'z' )
		{
			sign.signText[ getEditLine() ] += ColorData.formatSymbol;
			sign.signText[ getEditLine() ] += col;
		}
    }
	
	public int getEditLine()
	{
		try
		{
			Class c = GuiEditSign.class;
			Field f = c.getDeclaredField( ObfuscatedField.fromMcp( "net/minecraft/client/gui/inventory/GuiEditSign", "editLine" ).srgName );
			f.setAccessible( true );
			return ( Integer ) f.get( this );
		}
		catch ( Exception exception )
		{
			//exception.printStackTrace();
		}
		
		return 0;
	}
	
	public TileEntitySign sign;
}
