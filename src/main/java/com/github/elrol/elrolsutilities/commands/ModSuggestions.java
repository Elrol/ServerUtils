package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.enums.ClaimFlagKeys;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModSuggestions {
    public static CompletableFuture<Suggestions> suggestHomes(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        IPlayerData data;
        try {
            data = Main.database.get((context.getSource()).getPlayerOrException().getUUID());
        }
        catch (CommandSyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return ISuggestionProvider.suggest(data.getHomeNames(), builder);
    }

    public static CompletableFuture<Suggestions> suggestPerms(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(Main.permRegistry.getPerms(), builder);
    }

    public static CompletableFuture<Suggestions> suggestCurrentRanks(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        try {
            List<String> ranks = Main.database.get(context.getSource().getPlayerOrException().getUUID()).getRanks();
            Logger.log(ranks.toString());
            return ISuggestionProvider.suggest(ranks, builder);
        } catch (CommandSyntaxException e) {
            return suggestRanks(context, builder);
        }
    }

    public static CompletableFuture<Suggestions> suggestRanks(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(Ranks.rankMap.keySet(), builder);
    }

    public static CompletableFuture<Suggestions> suggestWarps(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        try {
            List<String> warps = new ArrayList<>();
            IPlayerData data = Main.database.get(context.getSource().getPlayerOrException().getUUID());
            Main.serverData.getWarpNames().forEach(warp -> {
                if(data.hasPermOrOp("serverutils.warp." + warp)) warps.add(warp);
            });
            return ISuggestionProvider.suggest(warps,builder);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return ISuggestionProvider.suggest(Main.serverData.getWarpNames(), builder);
    }

    public static CompletableFuture<Suggestions> suggestRankups(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        try {
            List<String> suggest = Main.database.get(context.getSource().getPlayerOrException().getUUID()).getDomRank().getNextRanks();
            ISuggestionProvider.suggest(suggest, builder);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return ISuggestionProvider.suggest(new ArrayList<>(), builder);
    }

    public static CompletableFuture<Suggestions> suggestKits(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(Main.kitMap.keySet(), builder);
    }

    public static CompletableFuture<Suggestions> suggestPlayers(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        ArrayList<String> userNames = new ArrayList<>();
        for (IPlayerData data : Main.database.getDatabase().values()) {
            if (data.getUsername().isEmpty()) continue;
            userNames.add(data.getUsername());
        }
        return ISuggestionProvider.suggest(userNames, builder);
    }

    public static CompletableFuture<Suggestions> suggestFlags(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(ClaimFlagKeys.list(), builder);
    }

    public static CompletableFuture<Suggestions> suggestClearlagTypes(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(Arrays.asList("hostile", "passive", "item"), builder);
    }

    public static CompletableFuture<Suggestions> suggestJails(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(Main.serverData.getJailMap().keySet(), builder);
    }

    public static CompletableFuture<Suggestions> suggestTitles(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(Main.serverData.getTitleMap().keySet(), builder);
    }
}

