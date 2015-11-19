package com.spacechase0.minecraft.textformatting.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class UnfilteredTextField extends GuiTextField {

	public UnfilteredTextField(int par0, FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5) {
		super(par0, par1FontRenderer, par2, par3, par4, par5);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void writeText(String par1Str)
    {
        String s1 = "";
        String s2 = par1Str;//ChatAllowedCharacters.filerAllowedCharacters(par1Str);
        int i = this.getCursorPosition() < this.getSelectionEnd() ? this.getCursorPosition() : this.getSelectionEnd();
        int j = this.getCursorPosition() < this.getSelectionEnd() ? this.getSelectionEnd() : this.getCursorPosition();
        int k = this.getMaxStringLength() - this.getText().length() - (i - this.getSelectionEnd());
        boolean flag = false;

        if (this.getText().length() > 0)
        {
            s1 = s1 + this.getText().substring(0, i);
        }

        int l;

        if (k < s2.length())
        {
            s1 = s1 + s2.substring(0, k);
            l = k;
        }
        else
        {
            s1 = s1 + s2;
            l = s2.length();
        }

        if (this.getText().length() > 0 && j < this.getText().length())
        {
            s1 = s1 + this.getText().substring(j);
        }

        setTextPreserve( s1 ); // What was this for?
        this.moveCursorBy(i - this.getSelectionEnd() + l);
    }
	
	public void setTextPreserve( String str )
	{
		int pos = getCursorPosition();
		setText( str );
		setCursorPosition( pos );
	}
}
