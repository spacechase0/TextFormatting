package com.spacechase0.minecraft.textformatting.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.textformatting.ColorData;

public class BookGui extends GuiScreenBook {

	public BookGui( EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, boolean par3 )
	{
		super(par1EntityPlayer, par2ItemStack, par3);
		unsigned = par3;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
        if ( unsigned )
        {
        	int bookImageWidth = 192;
        	int bookImageHeight = 192;
        	buttonList.add(new GuiButton(ColorData.formatButtonId, width / 2 - 100, bookImageHeight + 20 + 4 + 4, ColorData.formatButtonStr));
        }
	}
	
	@Override
	public void actionPerformed( GuiButton button )
    {
		super.actionPerformed( button );
		if ( button.enabled && button.id == ColorData.formatButtonId && unsigned )
		{
			addToBook( ColorData.formatSymbol );
		}
    }
	
	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        
		ColorData.calculateColorHeight( height );
		ColorData.drawGui( this, fontRendererObj );
	}
	
	@Override
    public void mouseClicked( int i, int j, int k )
    {
		if ( !unsigned )
		{
			super.mouseClicked( i, j, k );
			return; 
		}

		char code = ColorData.getClickedCode( i, j, width );
		if ( code != 'z' )
		{
			String str = ColorData.formatSymbol;
			str += code;
			
			addToBook( str );
		}
		else
		{
			super.mouseClicked( i, j, k );
		}
    }

    public void addToBook( String str )
    {
    	// Causes issues
    	/*
    	for ( int i = 0; i < str.length(); ++i )
    	{
    		char cc = str.charAt( i );
    		int ci = Character.getNumericValue( cc );
    		keyTyped( cc, ci );
    	}
    	*/
    	//*
    	if ( isEditingTitle() )
    	{
	    	try
	    	{
		    	Class c = GuiScreenBook.class;
		    	Field f = c.getDeclaredFields()[ 13 ];
		    	f.setAccessible( true );
		    	f.set( this, ( ( String ) f.get( this ) ) + str );
	    	}
	    	catch ( Exception exception )
	    	{
	    		exception.printStackTrace();
	    	}
    	}
    	else
    	{
	    	try
	    	{
		    	Class c = GuiScreenBook.class;
		    	Method m = c.getDeclaredMethod( "func_146459_b", String.class );
		    	m.setAccessible( true );
		    	m.invoke( this, str );
	    	}
	    	catch ( Exception exception )
	    	{
	    		exception.printStackTrace();
	    	}
    	}
    	//*/
    }
    
    public boolean isEditingTitle()
    {
    	Iterator it = buttonList.iterator();
    	while ( it.hasNext() )
    	{
    		GuiButton button = ( GuiButton ) it.next();
    		if ( button.id == 5 ) // Finalize button
    		{
    			return button.visible; // Hidden if not editing title
    		}
    	}
    	
    	return false;
    }
	
	public final boolean unsigned;
}
