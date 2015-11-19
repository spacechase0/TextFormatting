package com.spacechase0.minecraft.textformatting.gui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
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
        	buttonList.add(new GuiButton(ColorData.FORMAT_BUTTON_ID, width / 2 - 100, bookImageHeight + 20 + 4 + 4, ColorData.FORMAT_BUTTON_STR));
        }
	}
	
	@Override
	public void actionPerformed( GuiButton button ) throws IOException
    {
		super.actionPerformed( button );
		if ( button.enabled && button.id == ColorData.FORMAT_BUTTON_ID && unsigned )
		{
			addToBook( ColorData.FORMAT_SYMBOL );
		}
    }
	
	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        
		ColorData.calculateColorHeight( height );
		ColorData.drawGui( this, fontRendererObj );
		
		if ( isEditingTitle() )
		{
			String lengthStr = ColorData.getLengthStr( getBookTitle().length(), 32, 8 );
			fontRendererObj.drawString( lengthStr, ( width / 2 ) - 24, 72, 0 );
		}
	}
	
	@Override
    public void mouseClicked( int i, int j, int k ) throws IOException
    {
		if ( !unsigned )
		{
			super.mouseClicked( i, j, k );
			return; 
		}

		char code = ColorData.getClickedCode( i, j, width );
		if ( code != 'z' )
		{
			String str = ColorData.FORMAT_SYMBOL;
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
		    	Method m = c.getDeclaredMethod( ObfuscatedMethod.fromMcp( "net/minecraft/client/gui/GuiScreenBook" , "pageInsertIntoCurrent", "V(Ljava/lang/String;)" ).srgName, String.class );
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
    
    public String getBookTitle()
    {
    	try
    	{
	    	Class c = GuiScreenBook.class;
	    	Field f = c.getDeclaredFields()[ 13 ];
	    	f.setAccessible( true );
	    	return ( String ) f.get( this );
    	}
    	catch ( Exception exception )
    	{
    		exception.printStackTrace();
    	}
    	
    	return "";
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
