package com.spacechase0.minecraft.textformatting;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spacechase0.minecraft.spacecore.BaseMod;
/*import com.spacechase0.minecraft.textformatting.block.CommandBlock;
import com.spacechase0.minecraft.textformatting.item.SignItem;
import com.spacechase0.minecraft.textformatting.item.WritableBookItem;
import com.spacechase0.minecraft.textformatting.item.WrittenBookItem;*/

/* Notes

Item.writableBook ItemWritablebook
Item.writtenBook ItemEditableBook
Item.sign ItemSign

TileEntityCommandblock
TileEntitySign

Block.signPost (true)
Block.signWall (false)
Block.commandBlock
Block.anvil

GuiRepair ContainerRepair
GuiCommandBlock
GuiEditSign
GuiScreenBook
GuiChat ?

*/

// 2.3.0 - Changed the way formatting works in signs (plan to improve and copy to others later). Updated for Minecraft 1.8.

@Mod( modid = "SC0_TextFormatting", useMetadata = true, dependencies="required-after:SC0_SpaceCore" )
public class TextFormatting extends BaseMod
{
	public TextFormatting()
	{
		super( "textformatting" );
	}
	
	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		super.preInit( event );
	}
	
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		super.init( event );
		
		/*
		if ( shouldDo( "commandBlock" ) )
		{
			Block.blocksList[ Block.commandBlock.blockID ] = null;
			commandBlock = new CommandBlock( Block.commandBlock.blockID );
		}
		
		if ( shouldDo( "sign" ) )
		{
			Item.itemsList[ Item.sign.itemID ] = null;
			signItem = new SignItem( Item.sign.itemID - 256 );
		}

		if ( shouldDo( "book" ) )
		{
			Item.itemsList[ Item.writableBook.itemID ] = null;
			writableBookItem = new WritableBookItem( Item.writableBook.itemID - 256 );
		}
		*/
		
		if ( shouldDo( "bookUnsigning" ) )
		{
			//Item.itemsList[ Item.writtenBook.itemID ] = null;
			//writtenBookItem = new WrittenBookItem( Item.writtenBook.itemID - 256 );
			
			GameRegistry.addRecipe( new BookUnsigningRecipe() );
		}
		
		if ( shouldDo( "bookAuthorChanging" ) )
		{
			GameRegistry.addRecipe( new BookAuthorRecipe() );
		}
		
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
		super.postInit( event );
	}
	
	private boolean shouldDo( String str )
	{
		return config.get( Configuration.CATEGORY_GENERAL, str, true ).getBoolean( true );
	}

	public Configuration config;
	/*
	private CommandBlock commandBlock;
	
	private SignItem signItem;
	private WritableBookItem writableBookItem;
	private WrittenBookItem writtenBookItem;*/
}
