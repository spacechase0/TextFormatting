package com.spacechase0.minecraft.textformatting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BookUnsigningRecipe implements IRecipe
{
	@Override
	public boolean matches( InventoryCrafting inv, World world )
	{
    	return ( doCheck( inv ) != null );
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
    	ItemStack item = doCheck( inv );
    	if ( item == null )
    	{
    		return null;
    	}
    	
    	ItemStack newItem = new ItemStack( Items.writable_book, item.stackSize, item.getItemDamage() );
    	newItem.setTagCompound( ( NBTTagCompound ) item.getTagCompound().copy() );
    	
    	return newItem;
	}

	@Override
	public int getRecipeSize()
	{
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
    
    private ItemStack doCheck( InventoryCrafting inv )
    {
    	ItemStack item = null;
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
            	item = stack;
            }
        	//System.out.println( "item crafting grid:"+stack );
        }
        //System.out.println("count="+count+" "+"item="+item);
        
        return ( count == 1 ) ? item : null;
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
