package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.ServerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TitlesCmd extends _CmdBase {
    private static final String basePerm = "title.";

    public TitlesCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register((Commands.literal(a)
                    .executes(this::execute))
                    .then(Commands.literal("set")
                            .then(Commands.argument("title", StringArgumentType.string())
                                    .suggests(ModSuggestions::suggestTitles)
                                    .executes(c -> title(c, StringArgumentType.getString(c, "title")))))
                    .then(Commands.literal("create")
                            .requires(c -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(c, CommandConfig.titles_admin.get()))
                            .then(Commands.argument("name", StringArgumentType.string())
                                    .then(Commands.argument("title", StringArgumentType.greedyString())
                                            .executes(c -> create(c, StringArgumentType.getString(c,"name"), StringArgumentType.getString(c, "title"))))))
                    .then(Commands.literal("delete")
                            .requires(c -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(c, CommandConfig.titles_admin.get()))
                            .then(Commands.argument("title", StringArgumentType.string())
                                    .suggests(ModSuggestions::suggestTitles)
                                    .executes(c -> delete(c, StringArgumentType.getString(c, "title"))))));
        }
    }

    protected int create(CommandContext<CommandSourceStack> c, String name, String title) {
        ServerData data = Main.serverData;
        name = name.toLowerCase();
        if(!data.getTitle(name).isEmpty()){
            TextUtils.err(c, Errs.titleExists(name));
            return 0;
        }
        if(title.isEmpty()) {
            TextUtils.err(c, Errs.titleEmpty());
            return 0;
        }
        data.addTitle(name, title);
        Main.permRegistry.add(basePerm + name, true);
        TextUtils.msg(c, Msgs.titleCreated(name, TextUtils.formatString(title)));
        data.save();
        return 1;
    }

    protected int delete(CommandContext<CommandSourceStack> c, String title) {
        ServerData data = Main.serverData;
        name = name.toLowerCase();
        if(data.getTitle(name).isEmpty()){
            TextUtils.err(c, Errs.titleMissing(name));
            return 0;
        }
        TextUtils.msg(c, Msgs.titleDeleted(title, TextUtils.formatString(data.getTitle(title))));
        data.deleteTitle(title);
        Main.permRegistry.remove(basePerm + title, true);
        data.save();
        return 1;
    }

    protected int title(CommandContext<CommandSourceStack> c, String title) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException ignored) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }

        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        if(Main.serverData.getTitle(title).isEmpty()) {
            TextUtils.err(player, Errs.titleMissing(title));
            return 0;
        }
        boolean perm = IElrolAPI.getInstance().getPermissionHandler().hasPermission(player, basePerm + title);
        if(perm) {
            CommandDelay.init(this, c.getSource(), new TitleRunnable(player, title), false);
            return 1;
        } else {
            TextUtils.err(player, Errs.noTitlePerm(title));
            return 0;
        }
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(player), false);
        return 1;
    }

    private record TitleRunnable(ServerPlayer player, String title) implements Runnable {
        @Override
        public void run() {
            IPlayerData data = IElrolAPI.getInstance().getPlayerDatabase().get(player.getUUID());
            String tag = Main.serverData.getTitle(title);
            data.setTitle(tag);
            data.save();
            TextUtils.msg(player, Msgs.setTitle(TextUtils.formatString(tag)));
        }
    }

    private static class CommandRunnable implements Runnable {
        ServerPlayer player;

        public CommandRunnable(ServerPlayer player) {
            this.player = player;
        }

        @Override
        public void run() {
            StringBuilder titles = new StringBuilder("\n");
            Main.serverData.getTitleMap().forEach((name, title) -> {
                boolean flag = IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.getUUID(), basePerm + name);
                Logger.log("You " + (flag ? "do" : "don't") + " have permission for the " + name + " title.");
                if(flag) titles.append("&a");
                else titles.append("&8");
                titles.append(name).append("&8: &7").append(title).append("\n");
            });
            TextUtils.msg(player, Msgs.titles(TextUtils.formatString(titles.toString())));
        }
    }

}

