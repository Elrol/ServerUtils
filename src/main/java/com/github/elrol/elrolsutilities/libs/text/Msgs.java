package com.github.elrol.elrolsutilities.libs.text;

import com.github.elrol.elrolsutilities.config.FeatureConfig;
import net.minecraft.util.text.*;

public class Msgs {
    public static TextComponent welcome(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.welcome", a);
        return new StringTextComponent("Welcome " + a + "'s to the server!");
    }

    public static TextComponent welcome_home(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.welcome-home", a);
        return new StringTextComponent("Welcome back to " + a + ".");
    }

    public static TextComponent accepted_tp(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.accepted-tp", a);
        return new StringTextComponent("You accepted " + a + "'s tp request.");
    }

    public static TextComponent accepted_your_tp(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.accepted-your-tp", a);
        return new StringTextComponent(a + " has accepted your tp request.");
    }

    public static TextComponent welcome_back() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.welcome-back");
        return new StringTextComponent("Welcome back.");
    }

    public static TextComponent delhome(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.deleted-home", a);
        return new StringTextComponent("The home \"" + a + "\" has been removed.");
    }

    public static TextComponent delwarp(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.deleted-warp", a);
        return new StringTextComponent("The warp \"" + a + "\" has been removed.");
    }

    public static TextComponent valid_homes(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.valid-homes", a);
        return new StringTextComponent("Your current homes are: " + a + ".");
    }

    public static TextComponent permissions() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.permissions");
        return new StringTextComponent("Your Current Permissions:");
    }

    public static TextComponent permission_other(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.permission-other", a);
        return new StringTextComponent(a + "'s Current Permissions:");
    }

    public static TextComponent added_perm(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.added-permission", a, b);
        return new StringTextComponent("Added permission: " + a + " to " + b + ".");
    }

    public static TextComponent cleared_perms(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.cleared-perms", a);
        return new StringTextComponent("Cleared all perms from " + a + ".");
    }

    public static TextComponent set_home(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.set-home", a);
        return new StringTextComponent("The home \"" + a + "\" has been set at the current location.");
    }

    public static TextComponent set_spawn() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.set-spawn");
        return new StringTextComponent("Server spawn point set successfully.");
    }

    public static TextComponent set_warp(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.set-warp", a);
        return new StringTextComponent("Warp \"" + a + "\" has been set to your current location.");
    }

    public static TextComponent spawn() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.spawn");
        return new StringTextComponent("Welcome to Spawn!");
    }

    public static TextComponent welcome_warp(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.welcome-warp", a);
        return new StringTextComponent("Welcome to " + a +".");
    }

    public static TextComponent valid_warps(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.valid-warps", a);
        return new StringTextComponent("Valid Warps are: " + a + ".");
    }

    public static TextComponent rtp(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rtp", a);
        return new StringTextComponent("Randomly Teleported to: " + a + ".");
    }

    public static TextComponent tpa_sent(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.tpa-sent", a);
        return new StringTextComponent("Requested to teleport to " + a + ".");
    }

    public static TextComponent tpa_received(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.tpa-received", a);
        return new StringTextComponent(a + " has requested to teleport to you, use /tpaccept or /tpdeny.");
    }

    public static TextComponent tpa_here_sent(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.tpa-here-sent", a);
        return new StringTextComponent("Requested for " + a + " to teleport to you.");
    }

    public static TextComponent tpa_here_received(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.tpa-here-received", a);
        return new StringTextComponent(a + " has requested that you teleport to them, use /tpaccept or /tpdeny.");
    }

    public static TextComponent rank_check(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-check", a);
        return new StringTextComponent("Your current ranks are: " + a + ".");
    }

    public static TextComponent rank_check_other(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-check", a, b);
        return new StringTextComponent(a + "'s current ranks are: " + b + ".");
    }

    public static TextComponent rank_made(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-made", a);
        return new StringTextComponent("The rank %s has been created.");
    }

    public static TextComponent rank_removed(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-removed", a);
        return new StringTextComponent("The rank " + a + " has been removed.");
    }

    public static TextComponent rank_perm_added(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-perm-added", a, b);
        return new StringTextComponent("The permission " + a + " has been added to the " + b + " rank.");
    }

    public static TextComponent rank_perm_removed(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-perm-removed", a, b);
        return new StringTextComponent("The permission " + a + " has been removed from the " + b + " rank.");
    }

    public static TextComponent player_rank_added(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.player-rank-added", a, b);
        return new StringTextComponent(a + " now has the " + b + " rank.");
    }

    public static TextComponent player_rank_removed(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.player-rank-removed", a, b);
        return new StringTextComponent(a + " no longer has the " + b + " rank.");
    }

    public static TextComponent rank_weight(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-weight", a, b);
        return new StringTextComponent(a + "'s weight has been set to " + b + ".");
    }

    public static TextComponent removed_perm(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.removed-perm", a, b);
        return new StringTextComponent("The " + a + " permission was removed from " + b + ".");
    }

    public static TextComponent item_repaired(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.item-repaired", a);
        return new StringTextComponent("Your " + a + " has been fully repaired.");
    }

    public static TextComponent all_item_repaired(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.all-items-repaired", a);
        return new StringTextComponent("You have repaired a total of " + a + " items.");
    }

    public static TextComponent rank_prefix(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-prefix", a, b);
        return new StringTextComponent("The prefix of rank " + a + " has been set to " + b + ".");
    }

    public static TextComponent rank_suffix(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.rank-suffix", a, b);
        return new StringTextComponent("The suffix of rank " + a + " has been set to " + b + ".");
    }

    public static TextComponent nickname_cleared() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.nickname-cleared");
        return new StringTextComponent("Your nickname has been cleared.");
    }

    public static TextComponent nickname_set(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.nickname-set", a + TextFormatting.RESET);
        return new StringTextComponent("Your nickname has been set to " + a + ".");
    }

    public static TextComponent bombed(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.bombed", a);
        return new StringTextComponent(a + " was bombed.");
    }

    public static TextComponent boom() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.boom");
        return new StringTextComponent("BOOM!");
    }

    public static TextComponent smite(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.smite", a);
        return new StringTextComponent(a + " has been smitten.");
    }

    public static TextComponent smitten() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.smitten");
        return new StringTextComponent("Thou hast been smitten!");
    }

    public static TextComponent healed_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.healed-self");
        return new StringTextComponent("You have healed yourself.");
    }

    public static TextComponent healed_other(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.healed-other", a);
        return new StringTextComponent("You have healed " + a + ".");
    }

    public static TextComponent healed() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.healed");
        return new StringTextComponent("You have been healed.");
    }

    public static TextComponent flood(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.flood", a);
        return new StringTextComponent("You have flooded " + a + ".");
    }

    public static TextComponent flooded() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.flodded");
        return new StringTextComponent("Blub Blub!");
    }

    public static TextComponent fed_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.fed-self");
        return new StringTextComponent("You have sated your own hunger.");
    }

    public static TextComponent feed(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.feed", a);
        return new StringTextComponent("You have sated " + a + "'s hunger.");
    }

    public static TextComponent fed() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.fed");
        return new StringTextComponent("You have been fed.");
    }

    public static TextComponent fly_self(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.fly-self", a);
        return new StringTextComponent("You have " + a + " flying for yourself.");
    }

    public static TextComponent fly(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.fly", a);
        return new StringTextComponent("Your ability to fly has been " + a + ".");
    }

    public static TextComponent fly_other(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.fly-other", a, b);
        return new StringTextComponent("You have " + a + " flying for " + b + ".");
    }

    public static TextComponent god_self(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.god-self", a);
        return new StringTextComponent("You have " + a + " Godmode for yourself.");
    }

    public static TextComponent god(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.god", a);
        return new StringTextComponent("Godmode has been " + a + ".");
    }

    public static TextComponent god_other(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.god-other", a, b);
        return new StringTextComponent("You have " + a + " Godmode for " + b + ".");
    }

    public static TextComponent kit_created(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-created", a);
        return new StringTextComponent("The Kit " + a + " has been created.");
    }

    public static TextComponent kit_deleted(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-deleted", a);
        return new StringTextComponent("The Kit " + a + " has been deleted.");
    }

    public static TextComponent kit_item_added(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-added-item", a, b);
        return new StringTextComponent(a + " has been added to the Kit " + b + ".");
    }

    public static TextComponent kit_item_removed(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-removed-item", a, b);
        return new StringTextComponent(a + " has been removed from the Kit " + b + ".");
    }

    public static TextComponent kit_loaded(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-loaded", a);
        return new StringTextComponent("The " + a + " Kit has been loaded.");
    }

    public static TextComponent received_kit(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.received-kit", a);
        return new StringTextComponent("The Kit " + a + " has been received.");
    }

    public static TextComponent kit_perms(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-permission", a, b);
        return new StringTextComponent("The permission for Kit " + a + " is " + b + ".");
    }

    public static TextComponent kit_cooldown_set(int i, String b) {
        String a = TextFormatting.GREEN + "" + i + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-cooldown-set", TextFormatting.GREEN + "" + a + TextFormatting.RESET, b);
        return new StringTextComponent("The cooldown for Kit " + a + " has been set to " + b + ".");
    }

    public static TextComponent kit_cooldown_cleared(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-cooldown-clear", a);
        return new StringTextComponent("The cooldown for Kit " + a + " has been removed.");
    }

    public static TextComponent kit_info(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-info", a);
        return new StringTextComponent("The Kit " + a + " contains: ");
    }

    public static TextComponent set_gamemode(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.set-gamemode", a);
        return new StringTextComponent("Set your gamemode to " + a + ".");
    }

    public static TextComponent toggled_msg(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.toggled-msg", a);
        return new StringTextComponent("You have " + a + " messaging.");
    }

    public static TextComponent muted_player(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.muted-player", a, b);
        return new StringTextComponent("You have muted " + a + " for " + b + ".");
    }

    public static TextComponent muted_player(String a, String b, String c) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        c = TextFormatting.GREEN + c + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.changed-player-mute", a, b, c);
        return new StringTextComponent("You have changed " + a + " mute from " + b + " to " + c + ".");
    }

    public static TextComponent unmuted() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.unmuted");
        return new StringTextComponent("You have been unmuted.");
    }

    public static TextComponent kit_in_cd(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.kit-in-cooldown", a, b);
        return new StringTextComponent("You can't claim the kit " + a + " for another " + b + ".");
    }

    public static TextComponent custperm() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.custperm");
        return new StringTextComponent("Now run the command you wish to apply the permission to.");
    }

    public static TextComponent custperm_1(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.custperm-1", a);
        return new StringTextComponent("Next enter the permission you want set to the command: " + a + ".");
    }

    public static TextComponent custperm_2(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.custperm-2", a, b);
        return new StringTextComponent("Permission set successfully: '" + a + "':'" + b + "'");
    }

    public static TextComponent permission_list() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.permission-list");
        return new StringTextComponent("Valid permissions:");
    }

    public static TextComponent custperm_cancel() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.custperm-cancel");
        return new StringTextComponent("Permission creation canceled.");
    }

    public static TextComponent unmuted_player(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.unmuted-player", a);
        return new StringTextComponent("You have unmuted " + a + ".");
    }

    public static TextComponent cooldownEnded(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.cooldown-end", a);
        return new StringTextComponent("You can now use " + a + " again.");
    }

    public static TextComponent setFirstJoinKit(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.set-first-join-kit", a);
        return new StringTextComponent("The " + a + " Kit has been set as the First Join Kit.");
    }

    public static TextComponent parentRankAdd(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.add-parent-rank", a, b);
        return new StringTextComponent("The " + a + " Rank will now inherit permissions from " + b + ".");
    }

    public static TextComponent parentRankRemove(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.remove-parent-rank", a, b);
        return new StringTextComponent("The " + a + " Rank will no longer inherit permissions from " + b + ".");
    }

    public static TextComponent reloaded(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.msg.reloaded");
        return new StringTextComponent("ServerUtils data has been reloaded.");
    }

    //Custom welcome messages
    public static TextComponent jnemWelcome(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.jnem-welcome", a);
        return new StringTextComponent("");
    }


    //new stuff
    public static TextComponent rankup(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.rank-up");
        return new StringTextComponent("You have ranked up, use /rankup to pick a rank.");
    }

    public static TextComponent rankAddedToTree(String a, String b){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.tree-rank-added", a, b);
        return new StringTextComponent("Rank " + a + " has been added to rank " + b + "'s tree.");
    }

    public static TextComponent rankRemoveFromTree(String a, String b){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.tree-rank-removed", a, b);
        return new StringTextComponent("Rank " + a + " has been removed from rank " + b + "'s tree.");
    }

    public static TextComponent rankTimeSet(String a, int i){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        String b = TextFormatting.GREEN + "" + i + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.tree-time-set", a, b);
        return new StringTextComponent("Rank " + a + "'s autorank time has been set to " + b + " ticks.");
    }

    public static TextComponent chunk_claimed() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.chunk-claimed");
        return new StringTextComponent("You have claimed this chunk!");
    }

    public static TextComponent trusted_player(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.trusted", a);
        return new StringTextComponent("You have trusted \" + a + \" to your claims.");
    }

    public static TextComponent trusted_by_player(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.trusted_by", a);
        return new StringTextComponent(a + " has trusted you to their claims.");
    }

    public static TextComponent untrusted_player(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.untrusted", a);
        return new StringTextComponent("You have removed " + a + " from your claim.");
    }

    public static TextComponent enter_claim(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.enter-claim", a);
        return new StringTextComponent("You have entered " + a + " claim.");
    }

    public static TextComponent exit_claim(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.exit-claim", a);
        return new StringTextComponent("You have left " + a + " claim.");
    }

    public static TextComponent enter_exit_claim(String a, String b){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.enter-exit-claim", a, b);
        return new StringTextComponent("You have left " + a + " claim, and have entered " + b + " claim.");
    }

    public static TextComponent claim_flag_check(String a, boolean bool){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        String b = TextFormatting.GREEN + "" + bool + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.claim-flag-check", a, b);
        return new StringTextComponent("The " + a + " flag is set to " + b + ".");
    }

    public static TextComponent set_claim_flag(String a, boolean bool){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        String b = TextFormatting.GREEN + "" + bool + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.set-claim-flag", a, b);
        return new StringTextComponent(a + " has been set to " + b + ".");
    }

    public static TextComponent claim_flags(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.claim-flags", a);
        return new StringTextComponent("Valid claim flags: " + a + ".");
    }

    //New stuff
    public static TextComponent entity_wipe(int hc, int pc, int ic) {
        String hcs = (hc == -1 ? "No" : "" + hc);
        String pcs = (pc == -1 ? "No" : "" + pc);
        String ics = (ic == -1 ? "No" : "" + ic);
        String a = TextFormatting.GREEN + hcs + TextFormatting.RESET;
        String b = TextFormatting.GREEN + pcs + TextFormatting.RESET;
        String c = TextFormatting.GREEN + ics + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.entity_wipe", a, b, c);
        return new StringTextComponent("Cleared " + a + " hostile mobs, " + b + " passive mobs, and " + c + " items");
    }

    public static TextComponent wipe_warn(boolean hostile, boolean passive, boolean items){
        String h = (hostile ? "Hostile" : "");
        String p = (passive ? "Passive" : "");
        String i = (items ? "Item" : "");
        String arg = h;
        if(!h.isEmpty() && (!p.isEmpty() || !i.isEmpty())) arg += ", ";
        arg += p;
        if(!p.isEmpty() && !i.isEmpty()) arg += ", ";
        arg += i;
        String a = TextFormatting.GREEN + arg + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.wipe_warn", a);
        return new StringTextComponent("Clearing all " + a + " entities in 1 min");
    }

    public static TextComponent chunk_unclaimed(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.unclaim");
        return new StringTextComponent("This chunk has been unclaimed.");
    }

    public static TextComponent set_cost(String a, int i) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        String b = TextFormatting.GREEN + String.valueOf(i) + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.kit-setcost", a, b);
        return new StringTextComponent("Kit " + a + "'s cost has been set to " + b);
    }

    public static TextComponent paid_player(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.paid-player", a, b);
        return new StringTextComponent("You have paid " + a + " " + b + ".");
    }

    public static TextComponent paid_by(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.paid-by", a, b);
        return new StringTextComponent(a + " has paid you " + b + ".");
    }

    public static TextComponent paid_self(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.paid-self", a);
        return new StringTextComponent("You have paid yourself " + a + ".");
    }

    public static TextComponent charged_player(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.charged-player", a, b);
        return new StringTextComponent("You have charged " + a + " " + b + ".");
    }

    public static TextComponent charged_by(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.charged-by", a, b);
        return new StringTextComponent(a + " has charged you " + b + ".");
    }

    public static TextComponent charged_self(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.charged-self", a);
        return new StringTextComponent("You have charged yourself " + a + ".");
    }

    public static TextComponent bal_other(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.bal-other", a, b);
        return new StringTextComponent(a + "'s balance is " + b + ".");
    }

    public static TextComponent bal_self(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.bal-self", a);
        return new StringTextComponent("Your balance is " + a + ".");
    }

    public static TextComponent dropped_kit(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.kit-dropped", a);
        return new StringTextComponent("Your inventory was too full to claim all of Kit " + a + ". Some of it was dropped at your feet.");
    }

    public static TextComponent price_change(String a, String b, String c) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        c = TextFormatting.GREEN + c + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.price-change", a, b, c);
        return new StringTextComponent("The prices of " + a + " are now " + b + " to buy, and " + c + " to sell.");
    }

    public static TextComponent items_sold(String a, String b) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.items-sold", a, b);
        return new StringTextComponent("You sold " + a + " for " + b + ".");
    }

    public static TextComponent set_bypass(String a) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.bypass", a);
        return new StringTextComponent("Claim Bypass has been " + a);
    }

    public static TextComponent selected_sign() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.selected-sign");
        return new StringTextComponent("This sign has been selected. Right click on the chest to link it to the shop.");
    }

    public static TextComponent created_shop() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.created-shop");
        return new StringTextComponent("New ChestShop created!");
    }

    public static TextComponent linked_chest() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.linked-chest");
        return new StringTextComponent("This chest has been linked to the shop.");
    }

    public static TextComponent removed_shop() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.removed-shop");
        return new StringTextComponent("This shop has been removed.");
    }

    public static TextComponent sold_to_shop(String a, int i, String c) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        String b = TextFormatting.GREEN + String.valueOf(i) + TextFormatting.RESET;
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.sold-to-shop", a, b ,c);
        return new StringTextComponent("You sold " + a + " [" + b + "] for " + c + ".");
    }

    public static TextComponent bought_from_shop(String a, int i, String c) {
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        String b = TextFormatting.GREEN + String.valueOf(i) + TextFormatting.RESET;
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.bought-from-shop", a, b ,c);
        return new StringTextComponent("You bought " + a + " [" + b + "] for " + c + ".");
    }

    public static TextComponent chunks_unclaimed(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.unclaimall", a);
        return new StringTextComponent("All of " + a + " claims have been unclaimed.");
    }

    public static TextComponent chunks_unclaimed(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.unclaimall.self");
        return new StringTextComponent("All of your claims have been unclaimed.");
    }

    public static TextComponent rank_cmd_added(String a, String b){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.cmd.added", a, b);
        return new StringTextComponent("The command /" + a + " has been added to the " + b + " rank.");
    }

    public static TextComponent rank_cmd_removed(String a, String b){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.cmd.removed", a, b);
        return new StringTextComponent("The command /" + a + " has been removed from the " + b + " rank.");
    }

    public static TextComponent rank_cmd_cleared(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.cmd.cleared", a);
        return new StringTextComponent("The commands have been removed from the " + a + " rank.");
    }

    public static TextComponent staff_chat(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.staff_chat", a);
        return new StringTextComponent("Staff chat has been " + a + ".");
    }

    public static TextComponent setMOTD(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.set_motd");
        return new StringTextComponent("The MOTD has been set.");
    }

    public static TextComponent jailed(String a, String b, String c){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        c = TextFormatting.GREEN + c + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.jailed", a, b, c);
        return new StringTextComponent(a + " has been sent to the " + b + " Jail and put in Cell #" + c);
    }

    public static TextComponent cell_added(String a, String b){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.cell_added", a, b);
        return new StringTextComponent("Cell #" + a + " has been made for the " + b + " Jail.");
    }

    public static TextComponent cell_removed(String a, String b){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        b = TextFormatting.GREEN + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.cell_removed", a, b);
        return new StringTextComponent("Cell #" + a + " has been removed from the " + b + " Jail.");
    }

    public static TextComponent kit_cleared(String a){
        a = TextFormatting.GREEN + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.msg.kit_cleared", a);
        return new StringTextComponent("Kit " + a + "'s items have been cleared.");
    }

}