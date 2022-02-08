package com.github.elrol.elrolsutilities.libs;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

public class SignUtils {

    public static String readLine(SignTileEntity sign, int line) {
        CompoundNBT nbt = new CompoundNBT();
        //sign.save(nbt);
        String s = nbt.getString("Text" + line);
        IFormattableTextComponent text = TextComponent.Serializer.fromJson(s);
        if(text == null) return "";
        String rawText = text.getString();
        return TextFormatting.stripFormatting(rawText);
    }
}
