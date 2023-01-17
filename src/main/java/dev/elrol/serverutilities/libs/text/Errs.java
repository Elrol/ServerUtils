package dev.elrol.serverutilities.libs.text;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.config.FeatureConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class Errs {
    public static Component no_permission() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-permission");
        return Component.literal("You do not have permission to use this command.");
    }

    public static Component not_player() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.not-player");
        return Component.literal("Must be a player to use this command.");
    }

    public static Component no_back_location() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-back-location");
        return Component.literal("There is no place to send you back to.");
    }

    public static Component denied_tp(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.denied-tp", a);
        return Component.literal("You have denied " + a + "'s tp request.");
    }

    public static Component denied_your_tp(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.denied-your-tp", a);
        return Component.literal(a + " has denied your tp request.");
    }

    public static Component tp_expired(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.tp-expired", a);
        return Component.literal("Your pending tp request from " + a + " has expired.");
    }

    public static Component your_tp_expired(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.your-tp-expired", a);
        return Component.literal("Your pending tp request to " + a + " has expired.");
    }

    public static Component home_not_found(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.home-not-found", a);
        return Component.literal("The home \""+ a +"\" was not found.");
    }

    public static Component warp_not_found(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.warp-not-found", a);
        return Component.literal("The warp \"" + a + "\" was not found.");
    }

    public static Component no_homes() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-homes");
        return Component.literal("You don't have any homes currently set. Use /sethome to make a new home.");
    }

    public static Component empty_perm() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.empty-perm");
        return Component.literal("You must define a permission to add.");
    }

    public static Component dupe_perm(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.dupe-perm", a, b);
        return Component.literal(a + " already has the perm: " + b + ".");
    }

    public static Component invalid_perm(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.invalid-perm", a);
        return Component.literal("Invalid permission node: " + a +".");
    }

    public static Component invalid_perm() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.invalid-perms");
        return Component.literal("Invalid permission node.");
    }

    public static Component max_home() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.max-homes");
        return Component.literal("You already have the max amount of homes you are allowed.");
    }

    public static Component no_warps() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-warps");
        return Component.literal("There are no warps currently set.");
    }

    public static Component delay_running() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.delay-running");
        return Component.literal("Command delay already running, wait until its finished.");
    }

    public static Component rtp_warning() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.rtp-warning");
        return Component.literal("The RTP Commands will attempt to put you in a safe location, however, since the blocks are not currently loaded, there may be some danger to using the rtp commands. You have been warned.");
    }

    public static Component rtp_error() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.rtp-error");
        return Component.literal("A safe location could not be found. Try again.");
    }

    public static Component no_pending_tpa() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-pending-tpa");
        return Component.literal("You do not have a pending tp request.");
    }

    public static Component rank_exists(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.rank-exists", a);
        return Component.literal("The rank " + a + " already exists.");
    }

    public static Component rank_doesnt_exist(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.rank-doesnt-exist", a);
        return Component.literal("The rank " + a + " doesn't exist.");
    }

    public static Component no_rank_name() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-rank-name");
        return Component.literal("The rank name can not be blank.");
    }

    public static Component rank_perm_exists(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.rank-perm-exists", a, b);
        return Component.literal("The " + a + " rank already has " + b + " permission.");
    }

    public static Component rank_perm_doesnt_exists(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.rank-perm-doesnt-exists", a, b);
        return Component.literal("The " + a + " rank does not have the permission " + b + ".");
    }

    public static Component player_has_rank(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.player-has-rank", a, b);
        return Component.literal(a + " already has the " + b + " rank.");
    }

    public static Component player_missing_rank(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.missing-rank", a, b);
        return Component.literal(a + " does not have the " + b + " rank.");
    }

    public static Component missing_perm(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.missing-perm", a, b);
        return Component.literal(a + " does not have the " + b + " permission.");
    }

    public static Component no_warp_name() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-warp-name");
        return Component.literal("The warp name can not be blank.");
    }

    public static Component item_not_damageable() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.not-damageable");
        return Component.literal("The currently equipped item is not repairable.");
    }

    public static Component cooldown(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.cooldown", a, b);
        return Component.literal("The command /" + a + " can not be used for another " + b + " seconds");
    }

    public static Component repair_blacklist(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.repair-blacklist", a);
        return Component.literal(a + " can't be repaired.");
    }

    public static Component player_moved() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.player-moved");
        return Component.literal("Your position changed. Command execution terminated.");
    }

    public static Component bombed_self() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.bombed-self");
        return Component.literal("You bombed yourself. Not a very smart idea, is it?");
    }

    public static Component smitten_self() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.smitten-self");
        return Component.literal("You smitten yourself. Not a very smart idea, is it?");
    }

    public static Component flooded_self() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.flooded-self");
        return Component.literal("You flooded yourself. Not a very smart idea, is it?");
    }

    public static Component kit_exists(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.kit-exists", a);
        return Component.literal("The Kit " + a + " already exists.");
    }

    public static Component kit_doesnt_exist(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.kit-doesnt-exist", a);
        return Component.literal("The Kit " + a + " does not exist.");
    }

    public static Component empty_hand() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.empty-hand");
        return Component.literal("There is no item in your main hand.");
    }

    public static Component kit_full(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.kit-full", a);
        return Component.literal("The Kit " + a + " has no more room for items.");
    }

    public static Component kit_missing_item(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.kit-missing-items", a, b);
        return Component.literal("The Kit " + a + " does not have " + b + " in it.");
    }

    public static Component no_responder() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-responder");
        return Component.literal("There is no one for you to respond to.");
    }

    public static Component disabled_msg(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.msg-disabled", a);
        return Component.literal(a + " has disabled messages.");
    }

    public static Component muted(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.muted", a);
        return Component.literal("You have been muted for " + a);
    }

    public static Component mute_changed(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.mute-changed", a, b);
        return Component.literal("Your mute has changed from " + a + " to " + b + ".");
    }

    public static Component are_muted(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.are-muted", a);
        return Component.literal("You are muted for another " + a + ".");
    }

    public static Component custperm_space(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.custperm-space", a);
        return Component.literal("The permission you entered is not a valid format: " + a + ".");
    }

    public static Component invalid_cmd() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.invalid-cmd");
        return Component.literal("Invalid Command. Try again, or say 'cancel'");
    }

    public static Component player_not_found(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.missing-player", a);
        return Component.literal("The player " + a + " was not found.");
    }

    //new stuff
    public static Component rank_not_allowed(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.rank-not-allowed", a);
        return Component.literal("You can not rank up to the " + a + " rank.");
    }

    public static Component early_rankup(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.early-rank", a);
        return Component.literal("You can not rank up to the " + a + " rank currently.");
    }

    public static Component chunk_claimed(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.chunk-claimed", ChatFormatting.DARK_RED + a + ChatFormatting.RESET);
        return Component.literal("This chunk is claimed by " + a + ".");
    }

    public static Component max_claim(){
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.max-claim");
        return Component.literal("You already have hit your claim limit.");
    }

    public static Component not_trusted(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.not-trusted", a);
        return Component.literal(a + " is not trusted to your claim.");
    }

    public static Component is_trusted(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.is-trusted", a);
        return Component.literal(a + " is already trusted to your claim.");
    }

    public static Component untrusted_by_player(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.untrusted", a);
        return Component.literal("You have been removed from " + a + "'s claim.");
    }

    public static Component no_entry(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-entry", a);
        return Component.literal("You are not allowed to enter " + a + "'s claim.");
    }

    public static Component no_flag(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-flag", a);
        return Component.literal(a + " is not a valid claim flag.");
    }

    //new stuff
    public static Component no_wipe_type(){
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-wipe-type");
        return Component.literal("No entity type defined.");
    }

    public static Component chunk_not_yours(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.chunk-not-yours", a);
        return Component.literal("You can't unclaim " + a + "'s claims.");
    }

    public static Component chunk_not_claimed(){
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.chunk-not-claimed");
        return Component.literal("This chunk has not been claimed.");
    }

    public static Component not_enough_funds(double i, double j) {
        String a = ChatFormatting.DARK_RED + Main.textUtils.parseCurrency(i, true) + ChatFormatting.RESET;
        String b = ChatFormatting.DARK_RED + Main.textUtils.parseCurrency(j, true) + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.not-enough-funds", a, b);
        return Component.literal("You don't have " + a + ". Your current balance is: " + b);
    }

    public static Component cant_sell(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.cant-sell", a);
        return Component.literal(a + " can't be sold to the server.");
    }

    public static Component kit_claimed(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
                return Component.translatable("serverutils.command.err.kit-claimed", a);
        return Component.literal("Kit " + a + " has already been claimed.");
    }

    public static Component sign_missing(Location loc) {
        String a = ChatFormatting.DARK_RED + loc.toString() + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.sign-missing", a);
        return Component.literal("The sign was missing at " + a);
    }

    public static Component shop_error(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.shop-error", a);
        return Component.literal("There was an error making the " + a + " Sign.");
    }

    public static Component missing_items(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.missing-items", a);
        return Component.literal("You are don't have the right items for this " + a + " Sign.");
    }

    public static Component container_missing(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.container_missing", a);
        return Component.literal("The container for this " + a + " Shop is missing. Contact the shop owner.");
    }

    public static Component afford_shop(String a, String b, String c) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        c = ChatFormatting.DARK_RED + c + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.afford_shop", a, b, c);
        return Component.literal("You can't afford a " + a + " Sign. Cost: " + b + ", Balance: " + c + ".");
    }

    public static Component container_empty() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.shop_empty");
        return Component.literal("The container that you had tried to link was empty.");
    }

    public static Component chest_owned() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.chest_owned");
        return Component.literal("This chest is already part of someone else's shop.");
    }

    public static Component bypass_not_enabled() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.bypass_not_enabled");
        return Component.literal("In order to do this you need to enable bypass.");
    }

    public static Component rank_cmd_null(String a, String b) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        b = ChatFormatting.DARK_RED + b + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.rank_cmd_null", a, b);
        return Component.literal("The rank " + a + " does not have a command with the " + b + " id.");
    }

    public static Component cant_select_mulit() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.cant_select_multi");
        return Component.literal("You don't have permission to select multiple players.");
    }

    public static Component player_missing() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.player_missing");
        return Component.literal("That player can't be found.");
    }

    public static Component jail_missing() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.jail_missing");
        return Component.literal("That Jail can't be found.");
    }

    public static Component cell_missing() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.cell_missing");
        return Component.literal("That cell can't be found.");
    }

    public static Component jail_exists(String a) {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.jail_exists", a);
        return Component.literal("The " + a + " Jail already exists.");
    }

    public static Component jailed(long i) {
        String a = ChatFormatting.DARK_RED + String.valueOf(i) + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.jailed", a);
        return Component.literal("You are Jailed for another " + a + " minute(s).");
    }

    public static Component not_linked(String a) {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.sign_not_linked");
        return Component.literal("This sign hasn't been linked yet.");
    }

    public static Component sign_full() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.sign_full");
        return Component.literal("This Sign is full and can't accept more items. Please Contact the shop owner.");
    }

    public static Component shop_missing_funds(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.shop_missing_funds", a);
        return Component.literal("This shop is missing funds. Contact " + a + " for help.");
    }

    public static Component shop_missing_stock(String a) {
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.missing_stock");
        return Component.literal("This " + a + " shop is missing stock.");
    }

    public static Component missing_starting_stock() {
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.server.err.missing_starting_stock");
        return Component.literal("You can't link shop to an empty inventory.");
    }

    public static Component max_shops(){
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.max-shops");
        return Component.literal("You already have hit your shop limit.");
    }

    public static Component noTitlePerm(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.no-title-perm", a);
        return Component.literal("You don't have the permission to use the " + a + " title.");
    }

    public static Component titleMissing(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.title-missing", a);
        return Component.literal("The " + a + " title doesn't exist.");
    }

    public static Component titleExists(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.title-exists", a);
        return Component.literal("The " + a + " title already exists.");
    }

    public static Component titleEmpty(){
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.title-empty");
        return Component.literal("The title can't be empty.");
    }

    public static Component not_jailed(String a){
        a = ChatFormatting.DARK_RED + a + ChatFormatting.RESET;
        if(FeatureConfig.translation_enable.get())
            return Component.translatable("serverutils.command.err.not-jailed", a);
        return Component.literal(a + " is not currently in jail.");
    }

    //added errs
    public static Err heldItemMissing = new Err("serverutils.command.err.item-missing", "You aren't holding an item.");
    public static Err no_perms_for_warp = new Err("serverutils.command.err.no-perms-for-warp", "You do not have access to the %s warp");
    public static Err role_missing = new Err("serverutils.command.err.role-missing","The Discord role couldn't be found. Check the ids and try again.");
    public static Err no_vote_rewards_left = new Err("serverutils.command.err.no-vote-rewards-left", "You do not have any vote rewards. Use /vote to earn some.");
    public static Err not_enough_claims = new Err("serverutils.command.err.not-enough-claims", "You need to have %s more claim blocks to claim this area.");
    public static Err cant_use_cmd_here = new Err("serverutils.command.err.cant-use-cmd-here", "That command is not allowed in this dimension");

    //new errs
    public static Err cant_change_higher_rank = new Err("serverutils.command.err.rank_too_high", "You can only add/remove ranks that are lower than your current rank");

    public static class Err {

        final String id;
        final String text;

        public Err(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public Component get(String... args) {
            for (int i = 0; i < args.length; i++) {
                args[i] = ChatFormatting.DARK_RED + args[i] + ChatFormatting.RESET;
            }
            if(FeatureConfig.translation_enable.get())
                return Component.translatable(id, (Object[]) args);
            String output = text;
            for (int i = 0; i < args.length; i++) {
                output = output.replaceFirst("%s", args[i]);
            }
            return Component.literal(output);
        }
    }
}