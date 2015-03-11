package com.spacechase0.minecraft.textformatting.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions( { "com.spacechase0.minecraft.textformatting" } )
public class TextFormattingCorePlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]
			   {
			   	"com.spacechase0.minecraft.textformatting.asm.GuiOpeningTransformer",
			   	"com.spacechase0.minecraft.textformatting.asm.BookGuiTransformer",
			   	"com.spacechase0.minecraft.textformatting.asm.AllowedCharactersTransformer"
		       };
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
	}
}
