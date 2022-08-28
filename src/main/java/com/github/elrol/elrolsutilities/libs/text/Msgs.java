package com.github.elrol.elrolsutilities.libs.text;

import com.github.elrol.elrolsutilities.config.FeatureConfig;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class Msgs {
    public static Msg welcome = new Msg("serverutils.command.msg.welcome", "Welcome %s to the server!");
    public static Msg welcomeHome = new Msg("serverutils.command.msg.welcome-home", "Welcome back to %s.");
    public static Msg acceptedTp = new Msg("serverutils.command.msg.accepted-tp", "You accepted %s's tp request.");
    public static Msg acceptedYourTp = new Msg("serverutils.command.msg.accepted-your-tp", "%s has accepted your tp request.");
    public static Msg welcomeBack = new Msg("serverutils.command.msg.welcome-back", "Welcome back.");
    public static Msg delHome = new Msg("serverutils.command.msg.deleted-home", "The home \"%s\" has been removed.");
    public static Msg delWarp = new Msg("serverutils.command.msg.deleted-warp", "The warp \"%s\" has been removed.");
    public static Msg validHomes = new Msg("serverutils.command.msg.valid-homes", "Current Homes: %s");
    public static Msg permission = new Msg("serverutils.command.msg.permissions", "Your Current Permissions:");
    public static Msg permissionOther = new Msg("serverutils.command.msg.permission-other", "%s's Current Permissions:");
    public static Msg addedPerm = new Msg("serverutils.command.msg.added-permission", "Added permission: %s to %s.");
    public static Msg clearedPerms = new Msg("serverutils.command.msg.cleared-perms", "Cleared all perms from %s.");
    public static Msg setHome = new Msg("serverutils.command.msg.set-home", "The home \"%s\" has been set at the current location.");
    public static Msg setSpawn = new Msg("serverutils.command.msg.set-spawn", "Server spawn point set successfully.");
    public static Msg setWarp = new Msg("serverutils.command.msg.set-warp", "Warp \"%s\" has been set to your current location.");
    public static Msg spawn = new Msg("serverutils.command.msg.spawn", "Welcome to Spawn!");
    public static Msg warpWelcome = new Msg("serverutils.command.msg.welcome-warp", "Welcome to %s.");
    public static Msg validWarps = new Msg("serverutils.command.msg.valid-warps", "Valid Warps are: %s");

    public static Msg rtp = new Msg("serverutils.command.msg.rtp","Randomly Teleported to: %s.");
    public static Msg tpa_sent = new Msg("serverutils.command.msg.tpa-sent","Requested to teleport to %s.");
    public static Msg tpa_received = new Msg("serverutils.command.msg.tpa-received", " has requested to teleport to you, use /tpaccept or /tpdeny.");
    public static Msg tpa_here_sent = new Msg("serverutils.command.msg.tpa-here-sent","Requested for %s to teleport to you.");
    public static Msg tpa_here_received = new Msg("serverutils.command.msg.tpa-here-received","%s has requested that you teleport to them, use /tpaccept or /tpdeny.");
    public static Msg rank_check = new Msg("serverutils.command.msg.rank-check","Your current ranks are: %s.");
    public static Msg rank_check_other = new Msg("serverutils.command.msg.rank-check","%s's current ranks are: %s.");
    public static Msg rank_made = new Msg("serverutils.command.msg.rank-made","The rank %s has been created.");
    public static Msg rank_removed = new Msg("serverutils.command.msg.rank-removed","The rank %s has been removed.");
    public static Msg rank_perm_added = new Msg("serverutils.command.msg.rank-perm-added","The permission %s has been added to the %s rank.");
    public static Msg rank_perm_removed = new Msg("serverutils.command.msg.rank-perm-removed","The permission %s has been removed from the %s rank.");
    public static Msg player_rank_added = new Msg("serverutils.command.msg.player-rank-added","%s now has the %s rank.");
    public static Msg player_rank_removed = new Msg("serverutils.command.msg.player-rank-removed","%s no longer has the %s rank.");
    public static Msg rank_weight = new Msg("serverutils.command.msg.rank-weight","%s's weight has been set to %s.");
    public static Msg removed_perm = new Msg("serverutils.command.msg.removed-perm","The %s permission was removed from %s.");
    public static Msg item_repaired = new Msg("serverutils.command.msg.item-repaired","Your %s has been fully repaired.");
    public static Msg all_item_repaired = new Msg("serverutils.command.msg.all-items-repaired","You have repaired a total of %s items.");
    public static Msg rank_prefix = new Msg("serverutils.command.msg.rank-prefix","The prefix of rank %s has been set to %s.");
    public static Msg rank_suffix = new Msg("serverutils.command.msg.rank-suffix","The suffix of rank %s has been set to %s.");
    public static Msg nickname_cleared = new Msg("serverutils.command.msg.nickname-cleared","Your nickname has been cleared.");
    public static Msg nickname_set = new Msg("serverutils.command.msg.nickname-set","Your nickname has been set to %s.");
    public static Msg bombed = new Msg("serverutils.command.msg.bombed","%s was bombed.");
    public static Msg boom = new Msg("serverutils.command.msg.boom","BOOM!");
    public static Msg smite = new Msg("serverutils.command.msg.smite","%s has been smitten.");
    public static Msg smitten = new Msg("serverutils.command.msg.smitten","Thou hast been smitten!");
    public static Msg healed_self = new Msg("serverutils.command.msg.healed-self","You have healed yourself.");
    public static Msg healed_other = new Msg("serverutils.command.msg.healed-other","You have healed %s.");
    public static Msg healed = new Msg("serverutils.command.msg.healed","You have been healed.");
    public static Msg flood = new Msg("serverutils.command.msg.flood","You have flooded %s.");
    public static Msg flooded = new Msg("serverutils.command.msg.flodded","Blub Blub!");
    public static Msg fed_self = new Msg("serverutils.command.msg.fed-self","You have sated your own hunger.");
    public static Msg feed = new Msg("serverutils.command.msg.feed","You have sated %s's hunger.");
    public static Msg fed = new Msg("serverutils.command.msg.fed","You have been fed.");
    public static Msg fly_self = new Msg("serverutils.command.msg.fly-self","You have %s flying for yourself.");
    public static Msg fly = new Msg("serverutils.command.msg.fly","Your ability to fly has been %s.");
    public static Msg fly_other = new Msg("serverutils.command.msg.fly-other","You have %s flying for %s.");
    public static Msg god_self = new Msg("serverutils.command.msg.god-self","You have %s Godmode for yourself.");
    public static Msg god = new Msg("serverutils.command.msg.god","Godmode has been %s.");
    public static Msg god_other = new Msg("serverutils.command.msg.god-other","You have %s Godmode for %s.");
    public static Msg kit_created = new Msg("serverutils.command.msg.kit-created","The Kit %s has been created.");
    public static Msg kit_deleted = new Msg("serverutils.command.msg.kit-deleted","The Kit %s has been deleted.");
    public static Msg kit_item_added = new Msg("serverutils.command.msg.kit-added-item","%s has been added to the Kit %s.");
    public static Msg kit_item_removed = new Msg("serverutils.command.msg.kit-removed-item","%s has been removed from the Kit %s.");
    public static Msg kit_loaded = new Msg("serverutils.command.msg.kit-loaded","The %s Kit has been loaded.");
    public static Msg received_kit = new Msg("serverutils.command.msg.received-kit","The Kit %s has been received.");
    public static Msg kit_perms = new Msg("serverutils.command.msg.kit-permission","The permission for Kit %s is %s.");
    public static Msg kit_cooldown_set = new Msg("serverutils.command.msg.kit-cooldown-set", "The cooldown for Kit %s has been set to %s.");
    public static Msg kit_cooldown_cleared = new Msg("serverutils.command.msg.kit-cooldown-clear","The cooldown for Kit %s has been removed.");
    public static Msg kit_info = new Msg("serverutils.command.msg.kit-info","The Kit %s contains: ");
    public static Msg set_gamemode = new Msg("serverutils.command.msg.set-gamemode","Set your gamemode to %s.");
    public static Msg toggled_msg = new Msg("serverutils.command.msg.toggled-msg","You have %s messaging.");
    public static Msg muted_player = new Msg("serverutils.command.msg.muted-player","You have muted %s for %s.");
    public static Msg muted_player_change = new Msg("serverutils.command.msg.changed-player-mute", "You have changed %s mute from %s to %s.");
    public static Msg unmuted = new Msg("serverutils.command.msg.unmuted","You have been unmuted.");
    public static Msg kit_in_cd = new Msg("serverutils.command.msg.kit-in-cooldown","You can't claim the kit %s for another %s.");
    public static Msg custperm = new Msg("serverutils.command.msg.custperm","Now run the command you wish to apply the permission to.");
    public static Msg custperm_1 = new Msg("serverutils.command.msg.custperm-1","Next enter the permission you want set to the command: %s.");
    public static Msg custperm_2 = new Msg("serverutils.command.msg.custperm-2","Permission set successfully: '%s':'%s'");
    public static Msg permission_list = new Msg("serverutils.command.msg.permission-list","Valid permissions:");
    public static Msg custperm_cancel = new Msg("serverutils.command.msg.custperm-cancel","Permission creation canceled.");
    public static Msg unmuted_player = new Msg("serverutils.command.msg.unmuted-player","You have unmuted %s.");
    public static Msg cooldownEnded = new Msg("serverutils.command.msg.cooldown-end","You can now use %s again.");
    public static Msg setFirstJoinKit = new Msg("serverutils.command.msg.set-first-join-kit","The %s Kit has been set as the First Join Kit.");
    public static Msg parentRankAdd = new Msg("serverutils.command.msg.add-parent-rank","The %s Rank will now inherit permissions from %s.");
    public static Msg parentRankRemove = new Msg("serverutils.command.msg.remove-parent-rank","The %s Rank will no longer inherit permissions from %s.");
    public static Msg reloaded = new Msg("serverutils.command.msg.reloaded","ServerUtils data has been reloaded.");
    public static Msg jnemWelcome = new Msg("serverutils.server.msg.jnem-welcome","");
    public static Msg rankup = new Msg("serverutils.server.msg.rank-up","You have ranked up, use /rankup to pick a rank.");
    public static Msg rankAddedToTree = new Msg("serverutils.server.msg.tree-rank-added","Rank %s has been added to rank %s's tree.");
    public static Msg rankRemoveFromTree = new Msg("serverutils.server.msg.tree-rank-removed","Rank %s has been removed from rank %s's tree.");
    public static Msg rankTimeSet = new Msg("serverutils.server.msg.tree-time-set","Rank %s's autorank time has been set to %s ticks.");
    public static Msg chunk_claimed = new Msg("serverutils.server.msg.chunk-claimed","You have claimed this chunk!");
    public static Msg trusted_player = new Msg("serverutils.server.msg.trusted","You have trusted %s to your claims.");
    public static Msg trusted_by_player = new Msg("serverutils.server.msg.trusted_by"," has trusted you to their claims.");
    public static Msg untrusted_player = new Msg("serverutils.server.msg.untrusted","You have removed %s from your claim.");
    public static Msg enter_claim = new Msg("serverutils.server.msg.enter-claim","You have entered %s claim.");
    public static Msg exit_claim = new Msg("serverutils.server.msg.exit-claim","You have left %s claim.");
    public static Msg enter_exit_claim = new Msg("serverutils.server.msg.enter-exit-claim","You have left %s claimnd have entered %s claim.");
    public static Msg claim_flag_check = new Msg("serverutils.server.msg.claim-flag-check","The %s flag is set to %s.");
    public static Msg set_claim_flag = new Msg("serverutils.server.msg.set-claim-flag","%s has been set to %s.");
    public static Msg claim_flags = new Msg("serverutils.server.msg.claim-flags","Valid claim flags: %s.");
    public static Msg entity_wipe = new Msg("serverutils.server.msg.entity_wipe", "Cleared %s hostile mobs, %s passive mobs, and %s items");
    public static Msg wipe_warn = new Msg("serverutils.server.msg.wipe_warn","Clearing all %s, %s, and %s entities in 1 min");
    public static Msg chunk_unclaimed = new Msg("serverutils.server.msg.unclaim","This chunk has been unclaimed.");
    public static Msg set_cost = new Msg("serverutils.server.msg.kit-setcost","Kit %s's cost has been set to %s");
    public static Msg paid_player = new Msg("serverutils.server.msg.paid-player","You have paid %s %s.");
    public static Msg paid_by = new Msg("serverutils.server.msg.paid-by","%s has paid you %s.");
    public static Msg paid_self = new Msg("serverutils.server.msg.paid-self","You have paid yourself %s.");
    public static Msg charged_player = new Msg("serverutils.server.msg.charged-player","You have charged %s %s.");
    public static Msg charged_by = new Msg("serverutils.server.msg.charged-by","%s has charged you %s.");
    public static Msg charged_self = new Msg("serverutils.server.msg.charged-self","You have charged yourself %s.");
    public static Msg bal_other = new Msg("serverutils.server.msg.bal-other","%s's balance is %s.");
    public static Msg bal_self = new Msg("serverutils.server.msg.bal-self","Your balance is %s.");
    public static Msg dropped_kit = new Msg("serverutils.server.msg.kit-dropped","Your inventory was too full to claim all of Kit %s. Some of it was dropped at your feet.");
    public static Msg price_change = new Msg("serverutils.server.msg.price-change", "The prices of %s are now %s to buy and %s to sell.");
    public static Msg items_sold = new Msg("serverutils.server.msg.items-sold","You sold %s for %s.");
    public static Msg set_bypass = new Msg("serverutils.server.msg.bypass","Claim Bypass has been ");
    public static Msg chunks_unclaimed_other = new Msg("serverutils.server.msg.unclaimall","All of %s's claims have been unclaimed.");
    public static Msg chunks_unclaimed = new Msg("serverutils.server.msg.unclaimall.self","All of your claims have been unclaimed.");
    public static Msg rank_cmd_added = new Msg("serverutils.server.msg.cmd.added","The command /%s has been added to the %s rank.");
    public static Msg rank_cmd_removed = new Msg("serverutils.server.msg.cmd.removed","The command /%s has been removed from the %s rank.");
    public static Msg rank_cmd_cleared = new Msg("serverutils.server.msg.cmd.cleared","The commands have been removed from the %s rank.");
    public static Msg staff_chat = new Msg("serverutils.server.msg.staff_chat","Staff chat has been %s.");
    public static Msg setMOTD = new Msg("serverutils.server.msg.set_motd","The MOTD has been set.");
    public static Msg jailed = new Msg("serverutils.server.msg.jailed", "%s has been sent to the %s Jail and put in Cell #%s");
    public static Msg cell_added = new Msg("serverutils.server.msg.cell_added","Cell #%s has been made for the %s Jail.");
    public static Msg cell_removed = new Msg("serverutils.server.msg.cell_removed","Cell #%s has been removed from the %s Jail.");
    public static Msg kit_cleared = new Msg("serverutils.server.msg.kit_cleared","Kit %s's items have been cleared.");
    public static Msg sign_linked = new Msg("serverutils.server.msg.sign_linked","Location linked to your %s Sign!");
    public static Msg selected_sign = new Msg("serverutils.server.msg.selected-sign","Sign has been selected. Right click on a block with redstone to link it.");
    public static Msg created_shop = new Msg("serverutils.server.msg.created-shop","New ChestShop created!");
    public static Msg removed_shop = new Msg("serverutils.server.msg.removed-shop","This shop has been removed.");
    public static Msg rank_cost = new Msg("serverutils.server.msg.rank-cost", "The cost to rank up to %s is %s");
    public static Msg verification = new Msg("serverutils.server.msg.verification","Run `/link %s` on the server to link to your Discord account.");
    public static Msg verified = new Msg("serverutils.server.msg.verified","Your Minecraft account [%s] has been linked successfully.");
    public static Msg setTitle = new Msg("serverutils.server.msg.set-title","You have set your title to: %s");
    public static Msg titleCreated = new Msg("serverutils.server.msg.title-created","You have created the %s title: %s.");
    public static Msg titleDeleted = new Msg("serverutils.server.msg.title-deleted","You have deleted the %s title: %s.");
    public static Msg titles = new Msg("serverutils.server.msg.titles","Unlocked Titles: %s");
    public static Msg unsetTitle = new Msg("serverutils.server.msg.unset-title","Your title has been cleared.");
    public static Msg unjailed = new Msg("serverutils.server.msg.unjailed","You have been released from jail.");
    public static Msg unjailed_player = new Msg("serverutils.server.msg.unjailed_player","You have pardoned %s from their jail sentence.");
    public static Msg jail_created = new Msg("serverutils.server.msg.jail_created","You have created the %s jail.");
    public static Msg jail_deleted = new Msg("serverutils.server.msg.jail_deleted","You have deleted the %s jail.");
    public static Msg itemModelInfo = new Msg("serverutils.server.msg.item_model_info", "%s has a CustomModelData of: %s");
    public static Msg itemLoreAdd = new Msg("serverutils.server.msg.item_lore_add", "Added %s to %s's lore.");
    public static Msg itemLoreClear = new Msg("serverutils.server.msg.item_lore_clear", "Cleared %s's lore.");
    public static Msg itemLoreRemove = new Msg("serverutils.server.msg.item_lore_remove", "Removed line %s from %s's lore.");
    public static Msg itemLoreSet = new Msg("serverutils.server.msg.item_lore_set", "Set line %s of %s's lore to %s.");

    public static Msg copiedItem = new Msg("serverutils.server.msg.copied_item", "Copied %s to clipboard.");
    public static Msg setItemModel = new Msg("serverutils.server.msg.set_model", "Set %s's CustomModelData to: %s");
    public static Msg clearItemModel = new Msg("serverutils.server.msg.cleared_model", "Cleared %s's CustomModelData");
    public static Msg setItemName = new Msg("serverutils.server.msg.set_item_name", "Set items name to %s");
    public static Msg voteLinks = new Msg("serverutils.server.msg.vote-links", "Vote Links:");
    public static Msg redeemVotes = new Msg("serverutils.server.msg.redeem-votes", "You have %s vote rewards remaining. Use /vote redeem to collect.");
    public static Msg roleAdded = new Msg("serverutils.server.msg.role-added", "You have added the %s role from the %s server to the %s rank.");

    public static class Msg {

        final String id;
        final String text;

        public Msg(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public TextComponent get(String... args) {
            for (int i = 0; i < args.length; i++) {
                args[i] = "&a" + args[i] + "&r";
            }
            if(FeatureConfig.translation_enable.get())
                return new TranslationTextComponent(id, (Object[]) args);
            String output = text;
            for (String arg : args) {
                output = output.replaceFirst("%s", arg);
            }
            return new StringTextComponent(TextUtils.format(output));
        }
    }
}