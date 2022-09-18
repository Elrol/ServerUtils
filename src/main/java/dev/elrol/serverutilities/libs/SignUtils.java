package dev.elrol.serverutilities.libs;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class SignUtils {

    public static String readLine(SignBlockEntity sign, int line) {
        CompoundTag nbt = new CompoundTag();
        //sign.save(nbt);
        String s = nbt.getString("Text" + line);
        Component text = Component.Serializer.fromJson(s);
        if(text == null) return "";
        String rawText = text.getString();
        return ChatFormatting.stripFormatting(rawText);
    }
}
