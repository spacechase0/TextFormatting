package com.spacechase0.minecraft.textformatting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BookAuthorRecipe implements IRecipe
{
	@Override
	public boolean matches( InventoryCrafting inv, World world )
	{
    	return ( doCheck( inv ) != null );
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
    	ItemStack[] item = doCheck( inv );
    	if ( item == null )
    	{
    		return null;
    	}
    	
    	for ( int i = 0; i < item.length; ++i )
    	{
    		if ( item[ i ] == null )
    		{
    			return null;
    		}
    	}
    	
    	ItemStack newItem = new ItemStack( Items.writable_book, item[ 0 ].stackSize, item[ 0 ].getItemDamage() );
    	newItem.setTagCompound( ( NBTTagCompound ) item[ 0 ].getTagCompound().copy() );
    	newItem.getTagCompound().setString( "author", item[ 1 ].getDisplayName() );
    	
    	return newItem;
	}

	@Override
	public int getRecipeSize()
	{
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
    
    private ItemStack[] doCheck( InventoryCrafting inv )
    {
    	ItemStack bookItem = null;
    	ItemStack tagItem = null;
    	int count = 0;
        for ( int i = 0; i < inv.getSizeInventory(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );
            if ( stack == null )
            {
            	continue;
            }
            ++count;
            
            if ( stack.getItem() == Items.written_book )
            {
            	bookItem = stack;
            }
            else if ( stack.getItem() == Items.name_tag )
            {
            	tagItem = stack;
            }
        }
        
        if ( bookItem == null || tagItem == null )
        {
        	return null;
        }

        return ( count == 2 ) ? new ItemStack[] { bookItem, tagItem } : null;
    }
    
	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting craftInv)
	{
        ItemStack[] stacks = new ItemStack[craftInv.getSizeInventory()];

        for (int i = 0; i < stacks.length; ++i)
        {
            ItemStack itemstack = craftInv.getStackInSlot(i);
            stacks[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return stacks;
	}
}
