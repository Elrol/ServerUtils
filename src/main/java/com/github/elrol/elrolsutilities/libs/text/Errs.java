package com.github.elrol.elrolsutilities.libs.text;

import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class Errs {
    public static TextComponent no_permission() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-permission");
        return new StringTextComponent("You do not have permission to use this command.");
    }

    public static TextComponent not_player() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.not-player");
        return new StringTextComponent("Must be a player to use this command.");
    }

    public static TextComponent no_back_location() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-back-location");
        return new StringTextComponent("There is no place to send you back to.");
    }

    public static TextComponent denied_tp(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.denied-tp", a);
        return new StringTextComponent("You have denied " + a + "'s tp request.");
    }

    public static TextComponent denied_your_tp(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.denied-your-tp", a);
        return new StringTextComponent(a + " has denied your tp request.");
    }

    public static TextComponent tp_expired(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.tp-expired", a);
        return new StringTextComponent("Your pending tp request from " + a + " has expired.");
    }

    public static TextComponent your_tp_expired(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.your-tp-expired", a);
        return new StringTextComponent("Your pending tp request to " + a + " has expired.");
    }

    public static TextComponent home_not_found(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.home-not-found", a);
        return new StringTextComponent("The home \""+ a +"\" was not found.");
    }

    public static TextComponent warp_not_found(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.warp-not-found", a);
        return new StringTextComponent("The warp \"" + a + "\" was not found.");
    }

    public static TextComponent no_homes() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-homes");
        return new StringTextComponent("You don't have any homes currently set. Use /sethome to make a new home.");
    }

    public static TextComponent empty_perm() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.empty-perm");
        return new StringTextComponent("You must define a permission to add.");
    }

    public static TextComponent dupe_perm(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.dupe-perm", a, b);
        return new StringTextComponent(a + " already has the perm: " + b + ".");
    }

    public static TextComponent invalid_perm(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.invalid-perm", a);
        return new StringTextComponent("Invalid permission node: " + a +".");
    }

    public static TextComponent invalid_perm() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.invalid-perms");
        return new StringTextComponent("Invalid permission node.");
    }

    public static TextComponent max_home() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.max-homes");
        return new StringTextComponent("You already have the max amount of homes you are allowed.");
    }

    public static TextComponent no_warps() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-warps");
        return new StringTextComponent("There are no warps currently set.");
    }

    public static TextComponent delay_running() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.delay-running");
        return new StringTextComponent("Command delay already running, wait until its finished.");
    }

    public static TextComponent rtp_warning() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.rtp-warning");
        return new StringTextComponent("The RTP Commands will attempt to put you in a safe location, however, since the blocks are not currently loaded, there may be some danger to using the rtp commands. You have been warned.");
    }

    public static TextComponent rtp_error() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.rtp-error");
        return new StringTextComponent("A safe location could not be found. Try again.");
    }

    public static TextComponent no_pending_tpa() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-pending-tpa");
        return new StringTextComponent("You do not have a pending tp request.");
    }

    public static TextComponent rank_exists(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.rank-exists", a);
        return new StringTextComponent("The rank " + a + " already exists.");
    }

    public static TextComponent rank_doesnt_exist(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.rank-doesnt-exist", a);
        return new StringTextComponent("The rank " + a + " doesn't exist.");
    }

    public static TextComponent no_rank_name() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-rank-name");
        return new StringTextComponent("The rank name can not be blank.");
    }

    public static TextComponent rank_perm_exists(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.rank-perm-exists", a, b);
        return new StringTextComponent("The " + a + " rank already has " + b + " permission.");
    }

    public static TextComponent rank_perm_doesnt_exists(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.rank-perm-doesnt-exists", a, b);
        return new StringTextComponent("The " + a + " rank does not have the permission " + b + ".");
    }

    public static TextComponent player_has_rank(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.player-has-rank", a, b);
        return new StringTextComponent(a + " already has the " + b + " rank.");
    }

    public static TextComponent player_missing_rank(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.missing-rank", a, b);
        return new StringTextComponent(a + " does not have the " + b + " rank.");
    }

    public static TextComponent missing_perm(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.missing-perm", a, b);
        return new StringTextComponent(a + " does not have the " + b + " permission.");
    }

    public static TextComponent no_warp_name() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-warp-name");
        return new StringTextComponent("The warp name can not be blank.");
    }

    public static TextComponent item_not_damageable() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.not-damageable");
        return new StringTextComponent("The currently equipped item is not repairable.");
    }

    public static TextComponent cooldown(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.cooldown", a, b);
        return new StringTextComponent("The command /" + a + " can not be used for another " + b + " seconds");
    }

    public static TextComponent repair_blacklist(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.repair-blacklist", a);
        return new StringTextComponent(a + " can't be repaired.");
    }

    public static TextComponent player_moved() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.player-moved");
        return new StringTextComponent("Your position changed. Command execution terminated.");
    }

    public static TextComponent bombed_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.bombed-self");
        return new StringTextComponent("You bombed yourself. Not a very smart idea, is it?");
    }

    public static TextComponent smitten_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.smitten-self");
        return new StringTextComponent("You smitten yourself. Not a very smart idea, is it?");
    }

    public static TextComponent flooded_self() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.flooded-self");
        return new StringTextComponent("You flooded yourself. Not a very smart idea, is it?");
    }

    public static TextComponent kit_exists(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.kit-exists", a);
        return new StringTextComponent("The Kit " + a + " already exists.");
    }

    public static TextComponent kit_doesnt_exist(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.kit-doesnt-exist", a);
        return new StringTextComponent("The Kit " + a + " does not exist.");
    }

    public static TextComponent empty_hand() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.empty-hand");
        return new StringTextComponent("There is no item in your main hand.");
    }

    public static TextComponent kit_full(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.kit-full", a);
        return new StringTextComponent("The Kit " + a + " has no more room for items.");
    }

    public static TextComponent kit_missing_item(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.kit-missing-items", a, b);
        return new StringTextComponent("The Kit " + a + " does not have " + b + " in it.");
    }

    public static TextComponent no_responder() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-responder");
        return new StringTextComponent("There is no one for you to respond to.");
    }

    public static TextComponent disabled_msg(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.msg-disabled", a);
        return new StringTextComponent(a + " has disabled messages.");
    }

    public static TextComponent muted(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.muted", a);
        return new StringTextComponent("You have been muted for " + a);
    }

    public static TextComponent mute_changed(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.mute-changed", a, b);
        return new StringTextComponent("Your mute has changed from " + a + " to " + b + ".");
    }

    public static TextComponent are_muted(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.are-muted", a);
        return new StringTextComponent("You are muted for another " + a + ".");
    }

    public static TextComponent custperm_space(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.custperm-space", a);
        return new StringTextComponent("The permission you entered is not a valid format: " + a + ".");
    }

    public static TextComponent invalid_cmd() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.invalid-cmd");
        return new StringTextComponent("Invalid Command. Try again, or say 'cancel'");
    }

    public static TextComponent player_not_found(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.missing-player", a);
        return new StringTextComponent("The player " + a + " was not found.");
    }

    //new stuff
    public static TextComponent rank_not_allowed(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.rank-not-allowed", a);
        return new StringTextComponent("You can not rank up to the " + a + " rank.");
    }

    public static TextComponent early_rankup(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.early-rank", a);
        return new StringTextComponent("You can not rank up to the " + a + " rank currently.");
    }

    public static TextComponent chunk_claimed(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.chunk-claimed", TextFormatting.DARK_RED + a + TextFormatting.RESET);
        return new StringTextComponent("This chunk is claimed by " + a + ".");
    }

    public static TextComponent max_claim(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.max-claim");
        return new StringTextComponent("You already have hit your claim limit.");
    }

    public static TextComponent not_trusted(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.not-trusted", a);
        return new StringTextComponent(a + " is not trusted to your claim.");
    }

    public static TextComponent is_trusted(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.is-trusted", a);
        return new StringTextComponent(a + " is already trusted to your claim.");
    }

    public static TextComponent untrusted_by_player(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.untrusted", a);
        return new StringTextComponent("You have been removed from " + a + "'s claim.");
    }

    public static TextComponent no_entry(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-entry", a);
        return new StringTextComponent("You are not allowed to enter " + a + "'s claim.");
    }

    public static TextComponent no_flag(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-flag", a);
        return new StringTextComponent(a + " is not a valid claim flag.");
    }

    //new stuff
    public static TextComponent no_wipe_type(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-wipe-type");
        return new StringTextComponent("No entity type defined.");
    }

    public static TextComponent chunk_not_yours(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.chunk-not-yours", a);
        return new StringTextComponent("You can't unclaim " + a + "'s claims.");
    }

    public static TextComponent chunk_not_claimed(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.chunk-not-claimed");
        return new StringTextComponent("This chunk has not been claimed.");
    }

    public static TextComponent not_enough_funds(double i, double j) {
        String a = TextFormatting.DARK_RED + TextUtils.parseCurrency(i, true) + TextFormatting.RESET;
        String b = TextFormatting.DARK_RED + TextUtils.parseCurrency(j, true) + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.not-enough-funds", a, b);
        return new StringTextComponent("You don't have " + a + ". Your current balance is: " + b);
    }

    public static TextComponent cant_sell(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.cant-sell", a);
        return new StringTextComponent(a + " can't be sold to the server.");
    }

    public static TextComponent kit_claimed(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
                return new TranslationTextComponent("serverutils.command.err.kit-claimed", a);
        return new StringTextComponent("Kit " + a + " has already been claimed.");
    }

    public static TextComponent sign_missing(Location loc) {
        String a = TextFormatting.DARK_RED + loc.toString() + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.sign-missing", a);
        return new StringTextComponent("The sign was missing at " + a);
    }

    public static TextComponent shop_error(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.shop-error", a);
        return new StringTextComponent("There was an error making the " + a + " Sign.");
    }

    public static TextComponent missing_items(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.missing-items", a);
        return new StringTextComponent("You are don't have the right items for this " + a + " Sign.");
    }

    public static TextComponent container_missing(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.container_missing", a);
        return new StringTextComponent("The container for this " + a + " Shop is missing. Contact the shop owner.");
    }

    public static TextComponent afford_shop(String a, String b, String c) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        c = TextFormatting.DARK_RED + c + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.afford_shop", a, b, c);
        return new StringTextComponent("You can't afford a " + a + " Sign. Cost: " + b + ", Balance: " + c + ".");
    }

    public static TextComponent container_empty() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.shop_empty");
        return new StringTextComponent("The container that you had tried to link was empty.");
    }

    public static TextComponent chest_owned() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.chest_owned");
        return new StringTextComponent("This chest is already part of someone else's shop.");
    }

    public static TextComponent bypass_not_enabled() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.bypass_not_enabled");
        return new StringTextComponent("In order to do this you need to enable bypass.");
    }

    public static TextComponent rank_cmd_null(String a, String b) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        b = TextFormatting.DARK_RED + b + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.rank_cmd_null", a, b);
        return new StringTextComponent("The rank " + a + " does not have a command with the " + b + " id.");
    }

    public static TextComponent cant_select_mulit() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.cant_select_multi");
        return new StringTextComponent("You don't have permission to select multiple players.");
    }

    public static TextComponent player_missing() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.player_missing");
        return new StringTextComponent("That player can't be found.");
    }

    public static TextComponent jail_missing() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.jail_missing");
        return new StringTextComponent("That Jail can't be found.");
    }

    public static TextComponent cell_missing() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.cell_missing");
        return new StringTextComponent("That cell can't be found.");
    }

    public static TextComponent jail_exists(String a) {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.jail_exists", a);
        return new StringTextComponent("The " + a + " Jail already exists.");
    }

    public static TextComponent jailed(long i) {
        String a = TextFormatting.DARK_RED + String.valueOf(i) + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.jailed", a);
        return new StringTextComponent("You are Jailed for another " + a + " minute(s).");
    }

    public static TextComponent not_linked(String a) {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.sign_not_linked");
        return new StringTextComponent("This sign hasn't been linked yet.");
    }

    public static TextComponent sign_full() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.sign_full");
        return new StringTextComponent("This Sign is full and can't accept more items. Please Contact the shop owner.");
    }

    public static TextComponent shop_missing_funds(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.shop_missing_funds", a);
        return new StringTextComponent("This shop is missing funds. Contact " + a + " for help.");
    }

    public static TextComponent shop_missing_stock(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.missing_stock");
        return new StringTextComponent("This " + a + " shop is missing stock.");
    }

    public static TextComponent missing_starting_stock() {
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.server.err.missing_starting_stock");
        return new StringTextComponent("You can't link shop to an empty inventory.");
    }

    public static TextComponent max_shops(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.max-shops");
        return new StringTextComponent("You already have hit your shop limit.");
    }

    public static TextComponent noTitlePerm(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.no-title-perm", a);
        return new StringTextComponent("You don't have the permission to use the " + a + " title.");
    }

    public static TextComponent titleMissing(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.title-missing", a);
        return new StringTextComponent("The " + a + " title doesn't exist.");
    }

    public static TextComponent titleExists(String a){
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.title-exists", a);
        return new StringTextComponent("The " + a + " title already exists.");
    }

    public static TextComponent titleEmpty(){
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.title-empty");
        return new StringTextComponent("The title can't be empty.");
    }

    public static TextComponent not_jailed(String a) {
        a = TextFormatting.DARK_RED + a + TextFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return new TranslationTextComponent("serverutils.command.err.not-jailed", a);
        return new StringTextComponent(a + " is not currently in jail.");
    }

    public static Err heldItemMissing = new Err("serverutils.command.err.item-missing", "You aren't holding an item.");
    public static Err noModelData = new Err("serverutils.command.err.model-not-set", "%s has no CustomModelData");
    public static Err no_perms_for_warp = new Err("serverutils.command.err.no-perms-for-warp", "You do not have access to the %s warp");
    public static Err role_missing = new Err("serverutils.command.err.role-missing","The Discord role couldn't be found. Check the ids and try again.");
    public static Err no_vote_rewards_left = new Err("serverutils.command.err.no-vote-rewards-left", "You do not have any vote rewards. Use /vote to earn some.");

    public static class Err {

        final String id;
        final String text;

        public Err(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public TextComponent get(String... args) {
            for (int i = 0; i < args.length; i++) {
                args[i] = TextFormatting.DARK_RED + args[i] + TextFormatting.RESET;
            }
            if(FeatureConfig.translation_enable.get())
                return new TranslationTextComponent(id, (Object[]) args);
            String output = text;
            for (int i = 0; i < args.length; i++) {
                output = output.replaceFirst("%s", args[i]);
            }
            return new StringTextComponent(output);
        }
    }
}

