package com.github.elrol.elrolsutilities.libs.text;

import com.github.elrol.elrolsutilities.config.FeatureConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class Msgs {
    public static BaseComponent welcome(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.welcome", a);
        return new TextComponent("Welcome " + a + "'s to the server!");
    }

    public static BaseComponent welcome_home(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.welcome-home", a);
        return new TextComponent("Welcome back to " + a + ".");
    }

    public static BaseComponent accepted_tp(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.accepted-tp", a);
        return new TextComponent("You accepted " + a + "'s tp request.");
    }

    public static BaseComponent accepted_your_tp(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.accepted-your-tp", a);
        return new TextComponent(a + " has accepted your tp request.");
    }

    public static BaseComponent welcome_back() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.welcome-back");
        return new TextComponent("Welcome back.");
    }

    public static BaseComponent delhome(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.deleted-home", a);
        return new TextComponent("The home \"" + a + "\" has been removed.");
    }

    public static BaseComponent delwarp(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.deleted-warp", a);
        return new TextComponent("The warp \"" + a + "\" has been removed.");
    }

    public static BaseComponent valid_homes(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.valid-homes", a);
        return new TextComponent("Current Homes: " + a + ".");
    }

    public static BaseComponent permissions() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.permissions");
        return new TextComponent("Your Current Permissions:");
    }

    public static BaseComponent permission_other(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.permission-other", a);
        return new TextComponent(a + "'s Current Permissions:");
    }

    public static BaseComponent added_perm(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.added-permission", a, b);
        return new TextComponent("Added permission: " + a + " to " + b + ".");
    }

    public static BaseComponent cleared_perms(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.cleared-perms", a);
        return new TextComponent("Cleared all perms from " + a + ".");
    }

    public static BaseComponent set_home(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.set-home", a);
        return new TextComponent("The home \"" + a + "\" has been set at the current location.");
    }

    public static BaseComponent set_spawn() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.set-spawn");
        return new TextComponent("Server spawn point set successfully.");
    }

    public static BaseComponent set_warp(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.set-warp", a);
        return new TextComponent("Warp \"" + a + "\" has been set to your current location.");
    }

    public static BaseComponent spawn() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.spawn");
        return new TextComponent("Welcome to Spawn!");
    }

    public static BaseComponent welcome_warp(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.welcome-warp", a);
        return new TextComponent("Welcome to " + a +".");
    }

    public static BaseComponent valid_warps(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.valid-warps", a);
        return new TextComponent("Valid Warps are: " + a + ".");
    }

    public static BaseComponent rtp(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rtp", a);
        return new TextComponent("Randomly Teleported to: " + a + ".");
    }

    public static BaseComponent tpa_sent(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.tpa-sent", a);
        return new TextComponent("Requested to teleport to " + a + ".");
    }

    public static BaseComponent tpa_received(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.tpa-received", a);
        return new TextComponent(a + " has requested to teleport to you, use /tpaccept or /tpdeny.");
    }

    public static BaseComponent tpa_here_sent(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.tpa-here-sent", a);
        return new TextComponent("Requested for " + a + " to teleport to you.");
    }

    public static BaseComponent tpa_here_received(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.tpa-here-received", a);
        return new TextComponent(a + " has requested that you teleport to them, use /tpaccept or /tpdeny.");
    }

    public static BaseComponent rank_check(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-check", a);
        return new TextComponent("Your current ranks are: " + a + ".");
    }

    public static BaseComponent rank_check_other(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-check", a, b);
        return new TextComponent(a + "'s current ranks are: " + b + ".");
    }

    public static BaseComponent rank_made(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-made", a);
        return new TextComponent("The rank %s has been created.");
    }

    public static BaseComponent rank_removed(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-removed", a);
        return new TextComponent("The rank " + a + " has been removed.");
    }

    public static BaseComponent rank_perm_added(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-perm-added", a, b);
        return new TextComponent("The permission " + a + " has been added to the " + b + " rank.");
    }

    public static BaseComponent rank_perm_removed(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-perm-removed", a, b);
        return new TextComponent("The permission " + a + " has been removed from the " + b + " rank.");
    }

    public static BaseComponent player_rank_added(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.player-rank-added", a, b);
        return new TextComponent(a + " now has the " + b + " rank.");
    }

    public static BaseComponent player_rank_removed(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.player-rank-removed", a, b);
        return new TextComponent(a + " no longer has the " + b + " rank.");
    }

    public static BaseComponent rank_weight(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-weight", a, b);
        return new TextComponent(a + "'s weight has been set to " + b + ".");
    }

    public static BaseComponent removed_perm(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.removed-perm", a, b);
        return new TextComponent("The " + a + " permission was removed from " + b + ".");
    }

    public static BaseComponent item_repaired(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.item-repaired", a);
        return new TextComponent("Your " + a + " has been fully repaired.");
    }

    public static BaseComponent all_item_repaired(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.all-items-repaired", a);
        return new TextComponent("You have repaired a total of " + a + " items.");
    }

    public static BaseComponent rank_prefix(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-prefix", a, b);
        return new TextComponent("The prefix of rank " + a + " has been set to " + b + ".");
    }

    public static BaseComponent rank_suffix(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.rank-suffix", a, b);
        return new TextComponent("The suffix of rank " + a + " has been set to " + b + ".");
    }

    public static BaseComponent nickname_cleared() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.nickname-cleared");
        return new TextComponent("Your nickname has been cleared.");
    }

    public static BaseComponent nickname_set(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.nickname-set", a + ChatFormatting.RESET);
        return new TextComponent("Your nickname has been set to " + a + ".");
    }

    public static BaseComponent bombed(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.bombed", a);
        return new TextComponent(a + " was bombed.");
    }

    public static BaseComponent boom() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.boom");
        return new TextComponent("BOOM!");
    }

    public static BaseComponent smite(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.smite", a);
        return new TextComponent(a + " has been smitten.");
    }

    public static BaseComponent smitten() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.smitten");
        return new TextComponent("Thou hast been smitten!");
    }

    public static BaseComponent healed_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.healed-self");
        return new TextComponent("You have healed yourself.");
    }

    public static BaseComponent healed_other(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.healed-other", a);
        return new TextComponent("You have healed " + a + ".");
    }

    public static BaseComponent healed() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.healed");
        return new TextComponent("You have been healed.");
    }

    public static BaseComponent flood(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.flood", a);
        return new TextComponent("You have flooded " + a + ".");
    }

    public static BaseComponent flooded() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.flodded");
        return new TextComponent("Blub Blub!");
    }

    public static BaseComponent fed_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.fed-self");
        return new TextComponent("You have sated your own hunger.");
    }

    public static BaseComponent feed(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.feed", a);
        return new TextComponent("You have sated " + a + "'s hunger.");
    }

    public static BaseComponent fed() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.fed");
        return new TextComponent("You have been fed.");
    }

    public static BaseComponent fly_self(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.fly-self", a);
        return new TextComponent("You have " + a + " flying for yourself.");
    }

    public static BaseComponent fly(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.fly", a);
        return new TextComponent("Your ability to fly has been " + a + ".");
    }

    public static BaseComponent fly_other(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.fly-other", a, b);
        return new TextComponent("You have " + a + " flying for " + b + ".");
    }

    public static BaseComponent god_self(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.god-self", a);
        return new TextComponent("You have " + a + " Godmode for yourself.");
    }

    public static BaseComponent god(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.god", a);
        return new TextComponent("Godmode has been " + a + ".");
    }

    public static BaseComponent god_other(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.god-other", a, b);
        return new TextComponent("You have " + a + " Godmode for " + b + ".");
    }

    public static BaseComponent kit_created(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-created", a);
        return new TextComponent("The Kit " + a + " has been created.");
    }

    public static BaseComponent kit_deleted(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-deleted", a);
        return new TextComponent("The Kit " + a + " has been deleted.");
    }

    public static BaseComponent kit_item_added(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-added-item", a, b);
        return new TextComponent(a + " has been added to the Kit " + b + ".");
    }

    public static BaseComponent kit_item_removed(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-removed-item", a, b);
        return new TextComponent(a + " has been removed from the Kit " + b + ".");
    }

    public static BaseComponent kit_loaded(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-loaded", a);
        return new TextComponent("The " + a + " Kit has been loaded.");
    }

    public static BaseComponent received_kit(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.received-kit", a);
        return new TextComponent("The Kit " + a + " has been received.");
    }

    public static BaseComponent kit_perms(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-permission", a, b);
        return new TextComponent("The permission for Kit " + a + " is " + b + ".");
    }

    public static BaseComponent kit_cooldown_set(int i, String b) {
        String a = ChatFormatting.GREEN + "" + i + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-cooldown-set", ChatFormatting.GREEN + "" + a + ChatFormatting.RESET, b);
        return new TextComponent("The cooldown for Kit " + a + " has been set to " + b + ".");
    }

    public static BaseComponent kit_cooldown_cleared(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-cooldown-clear", a);
        return new TextComponent("The cooldown for Kit " + a + " has been removed.");
    }

    public static BaseComponent kit_info(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-info", a);
        return new TextComponent("The Kit " + a + " contains: ");
    }

    public static BaseComponent set_gamemode(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.set-gamemode", a);
        return new TextComponent("Set your gamemode to " + a + ".");
    }

    public static BaseComponent toggled_msg(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.toggled-msg", a);
        return new TextComponent("You have " + a + " messaging.");
    }

    public static BaseComponent muted_player(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.muted-player", a, b);
        return new TextComponent("You have muted " + a + " for " + b + ".");
    }

    public static BaseComponent muted_player(String a, String b, String c) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        c = ChatFormatting.GREEN + c + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.changed-player-mute", a, b, c);
        return new TextComponent("You have changed " + a + " mute from " + b + " to " + c + ".");
    }

    public static BaseComponent unmuted() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.unmuted");
        return new TextComponent("You have been unmuted.");
    }

    public static BaseComponent kit_in_cd(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.kit-in-cooldown", a, b);
        return new TextComponent("You can't claim the kit " + a + " for another " + b + ".");
    }

    public static BaseComponent custperm() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.custperm");
        return new TextComponent("Now run the command you wish to apply the permission to.");
    }

    public static BaseComponent custperm_1(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.custperm-1", a);
        return new TextComponent("Next enter the permission you want set to the command: " + a + ".");
    }

    public static BaseComponent custperm_2(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.custperm-2", a, b);
        return new TextComponent("Permission set successfully: '" + a + "':'" + b + "'");
    }

    public static BaseComponent permission_list() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.permission-list");
        return new TextComponent("Valid permissions:");
    }

    public static BaseComponent custperm_cancel() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.custperm-cancel");
        return new TextComponent("Permission creation canceled.");
    }

    public static BaseComponent unmuted_player(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.unmuted-player", a);
        return new TextComponent("You have unmuted " + a + ".");
    }

    public static BaseComponent cooldownEnded(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.cooldown-end", a);
        return new TextComponent("You can now use " + a + " again.");
    }

    public static BaseComponent setFirstJoinKit(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.set-first-join-kit", a);
        return new TextComponent("The " + a + " Kit has been set as the First Join Kit.");
    }

    public static BaseComponent parentRankAdd(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.add-parent-rank", a, b);
        return new TextComponent("The " + a + " Rank will now inherit permissions from " + b + ".");
    }

    public static BaseComponent parentRankRemove(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.remove-parent-rank", a, b);
        return new TextComponent("The " + a + " Rank will no longer inherit permissions from " + b + ".");
    }

    public static BaseComponent reloaded(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.msg.reloaded");
        return new TextComponent("ServerUtils data has been reloaded.");
    }

    //Custom welcome messages
    public static BaseComponent jnemWelcome(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.jnem-welcome", a);
        return new TextComponent("");
    }


    //new stuff
    public static BaseComponent rankup(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.rank-up");
        return new TextComponent("You have ranked up, use /rankup to pick a rank.");
    }

    public static BaseComponent rankAddedToTree(String a, String b){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.tree-rank-added", a, b);
        return new TextComponent("Rank " + a + " has been added to rank " + b + "'s tree.");
    }

    public static BaseComponent rankRemoveFromTree(String a, String b){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.tree-rank-removed", a, b);
        return new TextComponent("Rank " + a + " has been removed from rank " + b + "'s tree.");
    }

    public static BaseComponent rankTimeSet(String a, int i){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        String b = ChatFormatting.GREEN + "" + i + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.tree-time-set", a, b);
        return new TextComponent("Rank " + a + "'s autorank time has been set to " + b + " ticks.");
    }

    public static BaseComponent chunk_claimed() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.chunk-claimed");
        return new TextComponent("You have claimed this chunk!");
    }

    public static BaseComponent trusted_player(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.trusted", a);
        return new TextComponent("You have trusted \" + a + \" to your claims.");
    }

    public static BaseComponent trusted_by_player(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.trusted_by", a);
        return new TextComponent(a + " has trusted you to their claims.");
    }

    public static BaseComponent untrusted_player(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.untrusted", a);
        return new TextComponent("You have removed " + a + " from your claim.");
    }

    public static BaseComponent enter_claim(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.enter-claim", a);
        return new TextComponent("You have entered " + a + " claim.");
    }

    public static BaseComponent exit_claim(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.exit-claim", a);
        return new TextComponent("You have left " + a + " claim.");
    }

    public static BaseComponent enter_exit_claim(String a, String b){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.enter-exit-claim", a, b);
        return new TextComponent("You have left " + a + " claim, and have entered " + b + " claim.");
    }

    public static BaseComponent claim_flag_check(String a, boolean bool){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        String b = ChatFormatting.GREEN + "" + bool + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.claim-flag-check", a, b);
        return new TextComponent("The " + a + " flag is set to " + b + ".");
    }

    public static BaseComponent set_claim_flag(String a, boolean bool){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        String b = ChatFormatting.GREEN + "" + bool + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.set-claim-flag", a, b);
        return new TextComponent(a + " has been set to " + b + ".");
    }

    public static BaseComponent claim_flags(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.claim-flags", a);
        return new TextComponent("Valid claim flags: " + a + ".");
    }

    //New stuff
    public static BaseComponent entity_wipe(int hc, int pc, int ic) {
        String hcs = (hc == -1 ? "No" : "" + hc);
        String pcs = (pc == -1 ? "No" : "" + pc);
        String ics = (ic == -1 ? "No" : "" + ic);
        String a = ChatFormatting.GREEN + hcs + ChatFormatting.RESET;
        String b = ChatFormatting.GREEN + pcs + ChatFormatting.RESET;
        String c = ChatFormatting.GREEN + ics + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.entity_wipe", a, b, c);
        return new TextComponent("Cleared " + a + " hostile mobs, " + b + " passive mobs, and " + c + " items");
    }

    public static BaseComponent wipe_warn(boolean hostile, boolean passive, boolean items){
        String h = (hostile ? "Hostile" : "");
        String p = (passive ? "Passive" : "");
        String i = (items ? "Item" : "");
        String arg = h;
        if(!h.isEmpty() && (!p.isEmpty() || !i.isEmpty())) arg += ", ";
        arg += p;
        if(!p.isEmpty() && !i.isEmpty()) arg += ", ";
        arg += i;
        String a = ChatFormatting.GREEN + arg + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.wipe_warn", a);
        return new TextComponent("Clearing all " + a + " entities in 1 min");
    }

    public static BaseComponent chunk_unclaimed(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.unclaim");
        return new TextComponent("This chunk has been unclaimed.");
    }

    public static BaseComponent set_cost(String a, int i) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        String b = ChatFormatting.GREEN + String.valueOf(i) + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.kit-setcost", a, b);
        return new TextComponent("Kit " + a + "'s cost has been set to " + b);
    }

    public static BaseComponent paid_player(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.paid-player", a, b);
        return new TextComponent("You have paid " + a + " " + b + ".");
    }

    public static BaseComponent paid_by(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.paid-by", a, b);
        return new TextComponent(a + " has paid you " + b + ".");
    }

    public static BaseComponent paid_self(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.paid-self", a);
        return new TextComponent("You have paid yourself " + a + ".");
    }

    public static BaseComponent charged_player(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.charged-player", a, b);
        return new TextComponent("You have charged " + a + " " + b + ".");
    }

    public static BaseComponent charged_by(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.charged-by", a, b);
        return new TextComponent(a + " has charged you " + b + ".");
    }

    public static BaseComponent charged_self(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.charged-self", a);
        return new TextComponent("You have charged yourself " + a + ".");
    }

    public static BaseComponent bal_other(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.bal-other", a, b);
        return new TextComponent(a + "'s balance is " + b + ".");
    }

    public static BaseComponent bal_self(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.bal-self", a);
        return new TextComponent("Your balance is " + a + ".");
    }

    public static BaseComponent dropped_kit(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.kit-dropped", a);
        return new TextComponent("Your inventory was too full to claim all of Kit " + a + ". Some of it was dropped at your feet.");
    }

    public static BaseComponent price_change(String a, String b, String c) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        c = ChatFormatting.GREEN + c + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.price-change", a, b, c);
        return new TextComponent("The prices of " + a + " are now " + b + " to buy, and " + c + " to sell.");
    }

    public static BaseComponent items_sold(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.items-sold", a, b);
        return new TextComponent("You sold " + a + " for " + b + ".");
    }

    public static BaseComponent set_bypass(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.bypass", a);
        return new TextComponent("Claim Bypass has been " + a);
    }

    public static BaseComponent chunks_unclaimed(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.unclaimall", a);
        return new TextComponent("All of " + a + " claims have been unclaimed.");
    }

    public static BaseComponent chunks_unclaimed(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.unclaimall.self");
        return new TextComponent("All of your claims have been unclaimed.");
    }

    public static BaseComponent rank_cmd_added(String a, String b){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.cmd.added", a, b);
        return new TextComponent("The command /" + a + " has been added to the " + b + " rank.");
    }

    public static BaseComponent rank_cmd_removed(String a, String b){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.cmd.removed", a, b);
        return new TextComponent("The command /" + a + " has been removed from the " + b + " rank.");
    }

    public static BaseComponent rank_cmd_cleared(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.cmd.cleared", a);
        return new TextComponent("The commands have been removed from the " + a + " rank.");
    }

    public static BaseComponent staff_chat(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.staff_chat", a);
        return new TextComponent("Staff chat has been " + a + ".");
    }

    public static BaseComponent setMOTD(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.set_motd");
        return new TextComponent("The MOTD has been set.");
    }

    public static BaseComponent jailed(String a, String b, String c){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        c = ChatFormatting.GREEN + c + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.jailed", a, b, c);
        return new TextComponent(a + " has been sent to the " + b + " Jail and put in Cell #" + c);
    }

    public static BaseComponent cell_added(String a, String b){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.cell_added", a, b);
        return new TextComponent("Cell #" + a + " has been made for the " + b + " Jail.");
    }

    public static BaseComponent cell_removed(String a, String b){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = ChatFormatting.GREEN + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.cell_removed", a, b);
        return new TextComponent("Cell #" + a + " has been removed from the " + b + " Jail.");
    }

    public static BaseComponent kit_cleared(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.kit_cleared", a);
        return new TextComponent("Kit " + a + "'s items have been cleared.");
    }

    public static BaseComponent sign_linked(String a){
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.sign_linked", a);
        return new TextComponent("Location linked to your " + a + " Sign!");
    }
    public static BaseComponent selected_sign(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.selected-sign", a);
        return new TextComponent("Sign has been selected. Right click on a block with redstone to link it.");
    }

    public static BaseComponent created_shop(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.created-shop", a);
        return new TextComponent("New ChestShop created!");
    }

    public static BaseComponent removed_shop(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.removed-shop", a);
        return new TextComponent("This shop has been removed.");
    }

    public static BaseComponent rank_cost(String a, float b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        String c = TextUtils.parseCurrency(b, false);
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.rank-cost", a, c);
        return new TextComponent("The cost to rank up to " + a + " is " + c);
    }

    public static BaseComponent verification(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.verification", a);
        return new TextComponent("Run `/link " + a + "` on the server to link to your Discord account.");
    }

    public static BaseComponent verified(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.verified", a);
        return new TextComponent("Your Minecraft account [" + a + "] has been linked successfully.");
    }

    public static BaseComponent setTitle(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.set-title", a);
        return new TextComponent("You have set your title to: " + a);
    }

    public static BaseComponent titleCreated(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.title-created", a, b);
        return new TextComponent("You have created the " + a + " title: " + b + ".");
    }

    public static BaseComponent titleDeleted(String a, String b) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        b = b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.title-deleted", a, b);
        return new TextComponent("You have deleted the " + a + " title: " + b + ".");
    }

    public static BaseComponent titles(String a) {
        a = ChatFormatting.GREEN + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.titles", a);
        return new TextComponent("Unlocked Titles: " + a);
    }

    public static BaseComponent unsetTitle() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.msg.unset-title");
        return new TextComponent("Your title has been cleared.");
    }
}