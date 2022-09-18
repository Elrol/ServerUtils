package dev.elrol.serverutilities.api.data;

import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.init.ITextUtils;
import net.minecraft.network.chat.Component;

import java.util.regex.Matcher;

public class Msg {
    final String id;
    final String text;
    private final ITextUtils textUtils = IElrolAPI.getInstance().getTextUtils();

    public Msg(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public Component get(String... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = "&a" + args[i] + "&r";
        }
        if(IElrolAPI.getInstance().enableTranslations()) {
            for (int i = 0; i < args.length; i++) {
                args[i] = textUtils.format(args[i]);
            }
            return Component.translatable(id, (Object[]) args);
        }
        String output = text;
        for (String arg : args) {
            output = output.replaceFirst("%s", Matcher.quoteReplacement(arg));
        }
        return Component.literal(textUtils.format(output));
    }
}
