package com.github.elrol.elrolsutilities.libs.text;

import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class Errs {
    public static BaseComponent no_permission() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-permission");
        return new TextComponent("You do not have permission to use this command.");
    }

    public static BaseComponent not_player() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.not-player");
        return new TextComponent("Must be a player to use this command.");
    }

    public static BaseComponent no_back_location() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-back-location");
        return new TextComponent("There is no place to send you back to.");
    }

    public static BaseComponent denied_tp(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.denied-tp", a);
        return new TextComponent("You have denied " + a + "'s tp request.");
    }

    public static BaseComponent denied_your_tp(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.denied-your-tp", a);
        return new TextComponent(a + " has denied your tp request.");
    }

    public static BaseComponent tp_expired(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.tp-expired", a);
        return new TextComponent("Your pending tp request from " + a + " has expired.");
    }

    public static BaseComponent your_tp_expired(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.your-tp-expired", a);
        return new TextComponent("Your pending tp request to " + a + " has expired.");
    }

    public static BaseComponent home_not_found(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.home-not-found", a);
        return new TextComponent("The home \""+ a +"\" was not found.");
    }

    public static BaseComponent warp_not_found(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.warp-not-found", a);
        return new TextComponent("The warp \"" + a + "\" was not found.");
    }

    public static BaseComponent no_homes() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-homes");
        return new TextComponent("You don't have any homes currently set. Use /sethome to make a new home.");
    }

    public static BaseComponent empty_perm() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.empty-perm");
        return new TextComponent("You must define a permission to add.");
    }

    public static BaseComponent dupe_perm(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.dupe-perm", a, b);
        return new TextComponent(a + " already has the perm: " + b + ".");
    }

    public static BaseComponent invalid_perm(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.invalid-perm", a);
        return new TextComponent("Invalid permission node: " + a +".");
    }

    public static BaseComponent invalid_perm() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.invalid-perms");
        return new TextComponent("Invalid permission node.");
    }

    public static BaseComponent max_home() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.max-homes");
        return new TextComponent("You already have the max amount of homes you are allowed.");
    }

    public static BaseComponent no_warps() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-warps");
        return new TextComponent("There are no warps currently set.");
    }

    public static BaseComponent delay_running() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.delay-running");
        return new TextComponent("Command delay already running, wait until its finished.");
    }

    public static BaseComponent rtp_warning() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.rtp-warning");
        return new TextComponent("The RTP Commands will attempt to put you in a safe location, however, since the blocks are not currently loaded, there may be some danger to using the rtp commands. You have been warned.");
    }

    public static BaseComponent rtp_error() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.rtp-error");
        return new TextComponent("A safe location could not be found. Try again.");
    }

    public static BaseComponent no_pending_tpa() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-pending-tpa");
        return new TextComponent("You do not have a pending tp request.");
    }

    public static BaseComponent rank_exists(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.rank-exists", a);
        return new TextComponent("The rank " + a + " already exists.");
    }

    public static BaseComponent rank_doesnt_exist(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.rank-doesnt-exist", a);
        return new TextComponent("The rank " + a + " doesn't exist.");
    }

    public static BaseComponent no_rank_name() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-rank-name");
        return new TextComponent("The rank name can not be blank.");
    }

    public static BaseComponent rank_perm_exists(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.rank-perm-exists", a, b);
        return new TextComponent("The " + a + " rank already has " + b + " permission.");
    }

    public static BaseComponent rank_perm_doesnt_exists(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.rank-perm-doesnt-exists", a, b);
        return new TextComponent("The " + a + " rank does not have the permission " + b + ".");
    }

    public static BaseComponent player_has_rank(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.player-has-rank", a, b);
        return new TextComponent(a + " already has the " + b + " rank.");
    }

    public static BaseComponent player_missing_rank(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.missing-rank", a, b);
        return new TextComponent(a + " does not have the " + b + " rank.");
    }

    public static BaseComponent missing_perm(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.missing-perm", a, b);
        return new TextComponent(a + " does not have the " + b + " permission.");
    }

    public static BaseComponent no_warp_name() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-warp-name");
        return new TextComponent("The warp name can not be blank.");
    }

    public static BaseComponent item_not_damageable() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.not-damageable");
        return new TextComponent("The currently equipped item is not repairable.");
    }

    public static BaseComponent cooldown(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.cooldown", a, b);
        return new TextComponent("The command /" + a + " can not be used for another " + b + " seconds");
    }

    public static BaseComponent repair_blacklist(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.repair-blacklist", a);
        return new TextComponent(a + " can't be repaired.");
    }

    public static BaseComponent player_moved() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.player-moved");
        return new TextComponent("Your position changed. Command execution terminated.");
    }

    public static BaseComponent bombed_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.bombed-self");
        return new TextComponent("You bombed yourself. Not a very smart idea, is it?");
    }

    public static BaseComponent smitten_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.smitten-self");
        return new TextComponent("You smitten yourself. Not a very smart idea, is it?");
    }

    public static BaseComponent flooded_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.flooded-self");
        return new TextComponent("You flooded yourself. Not a very smart idea, is it?");
    }

    public static BaseComponent kit_exists(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.kit-exists", a);
        return new TextComponent("The Kit " + a + " already exists.");
    }

    public static BaseComponent kit_doesnt_exist(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.kit-doesnt-exist", a);
        return new TextComponent("The Kit " + a + " does not exist.");
    }

    public static BaseComponent empty_hand() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.empty-hand");
        return new TextComponent("There is no item in your main hand.");
    }

    public static BaseComponent kit_full(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.kit-full", a);
        return new TextComponent("The Kit " + a + " has no more room for items.");
    }

    public static BaseComponent kit_missing_item(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.kit-missing-items", a, b);
        return new TextComponent("The Kit " + a + " does not have " + b + " in it.");
    }

    public static BaseComponent no_responder() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-responder");
        return new TextComponent("There is no one for you to respond to.");
    }

    public static BaseComponent disabled_msg(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.msg-disabled", a);
        return new TextComponent(a + " has disabled messages.");
    }

    public static BaseComponent muted(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.muted", a);
        return new TextComponent("You have been muted for " + a);
    }

    public static BaseComponent mute_changed(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.mute-changed", a, b);
        return new TextComponent("Your mute has changed from " + a + " to " + b + ".");
    }

    public static BaseComponent are_muted(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.are-muted", a);
        return new TextComponent("You are muted for another " + a + ".");
    }

    public static BaseComponent custperm_space(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.custperm-space", a);
        return new TextComponent("The permission you entered is not a valid format: " + a + ".");
    }

    public static BaseComponent invalid_cmd() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.invalid-cmd");
        return new TextComponent("Invalid Command. Try again, or say 'cancel'");
    }

    public static BaseComponent player_not_found(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.missing-player", a);
        return new TextComponent("The player " + a + " was not found.");
    }

    //new stuff
    public static BaseComponent rank_not_allowed(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.rank-not-allowed", a);
        return new TextComponent("You can not rank up to the " + a + " rank.");
    }

    public static BaseComponent early_rankup(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.early-rank", a);
        return new TextComponent("You can not rank up to the " + a + " rank currently.");
    }

    public static BaseComponent chunk_claimed(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.chunk-claimed", ChatFormatting.DARK_RED + a + ChatFormatting.RESET);
        return new TextComponent("This chunk is claimed by " + a + ".");
    }

    public static BaseComponent max_claim(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.max-claim");
        return new TextComponent("You already have hit your claim limit.");
    }

    public static BaseComponent not_trusted(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.not-trusted", a);
        return new TextComponent(a + " is not trusted to your claim.");
    }

    public static BaseComponent is_trusted(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.is-trusted", a);
        return new TextComponent(a + " is already trusted to your claim.");
    }

    public static BaseComponent untrusted_by_player(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.untrusted", a);
        return new TextComponent("You have been removed from " + a + "'s claim.");
    }

    public static BaseComponent no_entry(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-entry", a);
        return new TextComponent("You are not allowed to enter " + a + "'s claim.");
    }

    public static BaseComponent no_flag(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-flag", a);
        return new TextComponent(a + " is not a valid claim flag.");
    }

    //new stuff
    public static BaseComponent no_wipe_type(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-wipe-type");
        return new TextComponent("No entity type defined.");
    }

    public static BaseComponent chunk_not_yours(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.chunk-not-yours", a);
        return new TextComponent("You can't unclaim " + a + "'s claims.");
    }

    public static BaseComponent chunk_not_claimed(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.chunk-not-claimed");
        return new TextComponent("This chunk has not been claimed.");
    }

    public static BaseComponent not_enough_funds(double i, double j) {
        String a = ChatFormatting.DARK_RED + TextUtils.parseCurrency(i, true) + ChatFormatting.RESET;
        String b = ChatFormatting.DARK_RED + TextUtils.parseCurrency(j, true) + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.not-enough-funds", a, b);
        return new TextComponent("You don't have " + a + ". Your current balance is: " + b);
    }

    public static BaseComponent cant_sell(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.cant-sell", a);
        return new TextComponent(a + " can't be sold to the server.");
    }

    public static BaseComponent kit_claimed(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
                return new TranslatableComponent("serverutils.command.err.kit-claimed", a);
        return new TextComponent("Kit " + a + " has already been claimed.");
    }

    public static BaseComponent sign_missing(Location loc) {
        String a = ChatFormatting.DARK_RED + loc.toString() + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.sign-missing", a);
        return new TextComponent("The sign was missing at " + a);
    }

    public static BaseComponent shop_error(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.shop-error", a);
        return new TextComponent("There was an error making the " + a + " Sign.");
    }

    public static BaseComponent missing_items(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.missing-items", a);
        return new TextComponent("You are don't have the right items for this " + a + " Sign.");
    }

    public static BaseComponent container_missing(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.container_missing", a);
        return new TextComponent("The container for this " + a + " Shop is missing. Contact the shop owner.");
    }

    public static BaseComponent afford_shop(String a, String b, String c) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        c = ChatFormatting.DARK_RED + c + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.afford_shop", a, b, c);
        return new TextComponent("You can't afford a " + a + " Sign. Cost: " + b + ", Balance: " + c + ".");
    }

    public static BaseComponent container_empty() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.shop_empty");
        return new TextComponent("The container that you had tried to link was empty.");
    }

    public static BaseComponent chest_owned() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.chest_owned");
        return new TextComponent("This chest is already part of someone else's shop.");
    }

    public static BaseComponent bypass_not_enabled() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.bypass_not_enabled");
        return new TextComponent("In order to do this you need to enable bypass.");
    }

    public static BaseComponent rank_cmd_null(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.rank_cmd_null", a, b);
        return new TextComponent("The rank " + a + " does not have a command with the " + b + " id.");
    }

    public static BaseComponent cant_select_mulit() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.cant_select_multi");
        return new TextComponent("You don't have permission to select multiple players.");
    }

    public static BaseComponent player_missing() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.player_missing");
        return new TextComponent("That player can't be found.");
    }

    public static BaseComponent jail_missing() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.jail_missing");
        return new TextComponent("That Jail can't be found.");
    }

    public static BaseComponent cell_missing() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.cell_missing");
        return new TextComponent("That cell can't be found.");
    }

    public static BaseComponent jail_exists(String a) {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.jail_exists", a);
        return new TextComponent("The " + a + " Jail already exists.");
    }

    public static BaseComponent jailed(long i) {
        String a = ChatFormatting.DARK_RED + String.valueOf(i) + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.jailed", a);
        return new TextComponent("You are Jailed for another " + a + " minute(s).");
    }

    public static BaseComponent not_linked(String a) {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.sign_not_linked");
        return new TextComponent("This sign hasn't been linked yet.");
    }

    public static BaseComponent sign_full() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.sign_full");
        return new TextComponent("This Sign is full and can't accept more items. Please Contact the shop owner.");
    }

    public static BaseComponent shop_missing_funds(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.shop_missing_funds", a);
        return new TextComponent("This shop is missing funds. Contact " + a + " for help.");
    }

    public static BaseComponent shop_missing_stock(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.missing_stock");
        return new TextComponent("This " + a + " shop is missing stock.");
    }

    public static BaseComponent missing_starting_stock() {
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.server.err.missing_starting_stock");
        return new TextComponent("You can't link shop to an empty inventory.");
    }

    public static BaseComponent max_shops(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.max-shops");
        return new TextComponent("You already have hit your shop limit.");
    }

    public static BaseComponent noTitlePerm(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.no-title-perm", a);
        return new TextComponent("You don't have the permission to use the " + a + " title.");
    }

    public static BaseComponent titleMissing(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.title-missing", a);
        return new TextComponent("The " + a + " title doesn't exist.");
    }

    public static BaseComponent titleExists(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.title-exists", a);
        return new TextComponent("The " + a + " title already exists.");
    }

    public static BaseComponent titleEmpty(){
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.title-empty");
        return new TextComponent("The title can't be empty.");
    }

    public static BaseComponent not_jailed(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslatableComponent("serverutils.command.err.not-jailed", a);
        return new TextComponent(a + " is not currently in jail.");
    }

    public static Err heldItemMissing = new Err("serverutils.command.err.item-missing", "You aren't holding an item.");
    public static Err noModelData = new Err("serverutils.command.err.model-not-set", "%s has no CustomModelData");

    public static class Err {

        final String id;
        final String text;

        public Err(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public BaseComponent get(String... args) {
            for (int i = 0; i < args.length; i++) {
                args[i] = ChatFormatting.DARK_RED + args[i] + ChatFormatting.RESET;
            }
            if(FeatureConfig.translation_enable.get())
                return new TranslatableComponent(id, (Object[]) args);
            String output = text;
            for (String arg : args) {
                output = output.replaceFirst("%s", arg);
            }
            return new TextComponent(output);
        }
    }
}