package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.enums.ClaimFlagKeys;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.Logger;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModSuggestions {
    public static CompletableFuture<Suggestions> suggestHomes(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        IPlayerData data;
        try {
            data = Main.database.get((context.getSource()).getPlayerOrException().getUUID());
        }
        catch (CommandSyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return SharedSuggestionProvider.suggest(data.getHomeNames(), builder);
    }

    public static CompletableFuture<Suggestions> suggestPerms(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Main.permRegistry.getPerms(), builder);
    }

    public static CompletableFuture<Suggestions> suggestCurrentRanks(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            List<String> ranks = Main.database.get(context.getSource().getPlayerOrException().getUUID()).getRanks();
            Logger.log(ranks.toString());
            return SharedSuggestionProvider.suggest(ranks, builder);
        } catch (CommandSyntaxException e) {
            return suggestRanks(context, builder);
        }
    }

    public static CompletableFuture<Suggestions> suggestRanks(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Ranks.rankMap.keySet(), builder);
    }

    public static CompletableFuture<Suggestions> suggestWarps(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            List<String> warps = new ArrayList<>();
            IPlayerData data = Main.database.get(context.getSource().getPlayerOrException().getUUID());
            Main.serverData.getWarpNames().forEach(warp -> {
                if(data.hasPermOrOp("serverutils.warp." + warp)) warps.add(warp);
            });
            return SharedSuggestionProvider.suggest(warps,builder);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return SharedSuggestionProvider.suggest(Main.serverData.getWarpNames(), builder);
    }

    public static CompletableFuture<Suggestions> suggestRankups(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            List<String> suggest = Main.database.get(context.getSource().getPlayerOrException().getUUID()).getDomRank().getNextRanks();
            SharedSuggestionProvider.suggest(suggest, builder);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return SharedSuggestionProvider.suggest(new ArrayList<>(), builder);
    }

    public static CompletableFuture<Suggestions> suggestKits(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Main.kitMap.keySet(), builder);
    }

    public static CompletableFuture<Suggestions> suggestPlayers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ArrayList<String> userNames = new ArrayList<>();
        for (IPlayerData data : Main.database.getDatabase().values()) {
            if (data.getUsername().isEmpty()) continue;
            userNames.add(data.getUsername());
        }
        return SharedSuggestionProvider.suggest(userNames, builder);
    }

    public static CompletableFuture<Suggestions> suggestFlags(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ClaimFlagKeys.list(), builder);
    }

    public static CompletableFuture<Suggestions> suggestClearlagTypes(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Arrays.asList("hostile", "passive", "item"), builder);
    }

    public static CompletableFuture<Suggestions> suggestJails(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Main.serverData.getJailMap().keySet(), builder);
    }

    public static CompletableFuture<Suggestions> suggestTitles(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Main.serverData.getTitleMap().keySet(), builder);
    }
}

