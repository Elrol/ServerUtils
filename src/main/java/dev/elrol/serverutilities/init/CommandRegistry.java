package dev.elrol.serverutilities.init;

import dev.elrol.serverutilities.api.commands.ICmdBase;
import dev.elrol.serverutilities.api.init.ICommandRegistry;
import dev.elrol.serverutilities.commands.*;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.libs.Logger;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry implements ICommandRegistry {
    public static BackCmd backCmd = new BackCmd(CommandConfig.back.delay, CommandConfig.back.cooldown, CommandConfig.back.aliases, CommandConfig.back.cost);
    public static BalCmd balCmd = new BalCmd(CommandConfig.bal.delay, CommandConfig.bal.cooldown, CommandConfig.bal.aliases, CommandConfig.bal.cost);
    public static BombCmd bombCmd = new BombCmd(CommandConfig.bomb.delay, CommandConfig.bomb.cooldown, CommandConfig.bomb.aliases, CommandConfig.bomb.cost);
    public static BroadcastCmd broadcastCmd = new BroadcastCmd(CommandConfig.broadcast.delay, CommandConfig.broadcast.cooldown, CommandConfig.broadcast.aliases, CommandConfig.broadcast.cost);
    public static BypassCmd bypassCmd = new BypassCmd(CommandConfig.bypass.delay, CommandConfig.bypass.cooldown, CommandConfig.bypass.aliases, CommandConfig.bypass.cost);
    public static ChestShopCmd chestShopCmd = new ChestShopCmd(CommandConfig.chestshop.delay, CommandConfig.chestshop.cooldown, CommandConfig.chestshop.aliases, CommandConfig.chestshop.cost);
    public static ClaimCmd claimCmd = new ClaimCmd(CommandConfig.claim.delay, CommandConfig.claim.cooldown, CommandConfig.claim.aliases, CommandConfig.claim.cost);
    public static ClaimflagCmd claimflagCmd = new ClaimflagCmd(CommandConfig.claimflag.delay, CommandConfig.claimflag.cooldown, CommandConfig.claimflag.aliases, CommandConfig.claimflag.cost);
    public static ClearLagCmd clearlagCmd = new ClearLagCmd(CommandConfig.clearlag.delay, CommandConfig.clearlag.cooldown, CommandConfig.clearlag.aliases, CommandConfig.clearlag.cost);
    public static DelHomeCmd delHomeCmd = new DelHomeCmd(CommandConfig.delhome.delay, CommandConfig.delhome.cooldown, CommandConfig.delhome.aliases, CommandConfig.delhome.cost);
    public static DelWarpCmd delWarpCmd = new DelWarpCmd(CommandConfig.delwarp.delay, CommandConfig.delwarp.cooldown, CommandConfig.delwarp.aliases, CommandConfig.delwarp.cost);
    public static EconCmd econCmd = new EconCmd(CommandConfig.econ.delay, CommandConfig.econ.cooldown, CommandConfig.econ.aliases, CommandConfig.econ.cost);
    public static EndChestCmd endChestCmd = new EndChestCmd(CommandConfig.endchest.delay, CommandConfig.endchest.cooldown, CommandConfig.endchest.aliases, CommandConfig.endchest.cost);
    public static FeedCmd feedCmd = new FeedCmd(CommandConfig.feed.delay, CommandConfig.feed.cooldown, CommandConfig.feed.aliases, CommandConfig.feed.cost);
    public static FloodCmd floodCmd = new FloodCmd(CommandConfig.flood.delay, CommandConfig.flood.cooldown, CommandConfig.flood.aliases, CommandConfig.flood.cost);
    public static FlyCmd flyCmd = new FlyCmd(CommandConfig.fly.delay, CommandConfig.fly.cooldown, CommandConfig.fly.aliases, CommandConfig.fly.cost);
    public static GamemodeCmd gamemodeCmd = new GamemodeCmd(CommandConfig.gamemode.delay, CommandConfig.gamemode.cooldown, CommandConfig.gamemode.aliases, CommandConfig.gamemode.cost);
    public static GodCmd godCmd = new GodCmd(CommandConfig.god.delay, CommandConfig.god.cooldown, CommandConfig.god.aliases, CommandConfig.god.cost);
    public static HealCmd healCmd = new HealCmd(CommandConfig.heal.delay, CommandConfig.heal.cooldown, CommandConfig.heal.aliases, CommandConfig.heal.cost);
    public static HomeCmd homeCmd = new HomeCmd(CommandConfig.home.delay, CommandConfig.home.cooldown, CommandConfig.home.aliases, CommandConfig.home.cost);
    public static HomesCmd homesCmd = new HomesCmd(CommandConfig.homes.delay, CommandConfig.homes.cooldown, CommandConfig.homes.aliases, CommandConfig.homes.cost);
    public static InvSeeCmd invSeeCmd = new InvSeeCmd(CommandConfig.invsee.delay, CommandConfig.invsee.cooldown, CommandConfig.invsee.aliases, CommandConfig.invsee.cost);
    public static ItemCmd itemCmd = new ItemCmd(CommandConfig.item.delay, CommandConfig.item.cooldown, CommandConfig.item.aliases, CommandConfig.item.cost);
    public static JailCmd jailCmd = new JailCmd(CommandConfig.jail.delay, CommandConfig.jail.cooldown, CommandConfig.jail.aliases, CommandConfig.jail.cost);
    // public static InvSeeTestCmd invSeeTestCmd = new InvSeeTestCmd(CommandConfig.invsee.perm, CommandConfig.invsee.delay, CommandConfig.invsee.cooldown, CommandConfig.invsee.aliases, CommandConfig.invsee.cost);
    public static KitCmd kitCmd = new KitCmd(CommandConfig.kit.delay, CommandConfig.kit.cooldown, CommandConfig.kit.aliases, CommandConfig.kit.cost);
    public static LinkCmd linkCmd = new LinkCmd(CommandConfig.link.delay, CommandConfig.link.cooldown, CommandConfig.link.aliases, CommandConfig.link.cost);
    public static MotdCmd motd = new MotdCmd(CommandConfig.motd.delay, CommandConfig.motd.cooldown, CommandConfig.motd.aliases, CommandConfig.motd.cost);
    public static MsgCmd msgCmd = new MsgCmd(CommandConfig.msg.delay, CommandConfig.msg.cooldown, CommandConfig.msg.aliases, CommandConfig.msg.cost);
    public static MsgToggleCmd msgToggleCmd = new MsgToggleCmd(CommandConfig.msg_toggle.delay, CommandConfig.msg_toggle.cooldown, CommandConfig.msg_toggle.aliases, CommandConfig.msg_toggle.cost);
    public static MuteCmd muteCmd = new MuteCmd(CommandConfig.mute.delay, CommandConfig.mute.cooldown, CommandConfig.mute.aliases, CommandConfig.mute.cost);
    public static NickNameCmd nickCmd = new NickNameCmd(CommandConfig.nick.delay, CommandConfig.nick.cooldown, CommandConfig.nick.aliases, CommandConfig.nick.cost);
    public static PayCmd payCmd = new PayCmd(CommandConfig.pay.delay, CommandConfig.pay.cooldown, CommandConfig.pay.aliases, CommandConfig.pay.cost);
    public static PermissionCmd permsCmd = new PermissionCmd(CommandConfig.perms.delay, CommandConfig.perms.cooldown, CommandConfig.perms.aliases, CommandConfig.perms.cost);
    public static PriceCmd priceCmd = new PriceCmd(CommandConfig.price.delay, CommandConfig.price.cooldown, CommandConfig.price.aliases, CommandConfig.price.cost);
    public static RankCmd rankCmd = new RankCmd(CommandConfig.rank.delay, CommandConfig.rank.cooldown, CommandConfig.rank.aliases, CommandConfig.rank.cost);
    public static RankupCmd rankupCmd = new RankupCmd(CommandConfig.rankup.delay, CommandConfig.rankup.cooldown, CommandConfig.rankup.aliases, CommandConfig.rankup.cost);
    public static ReloadCmd reloadCmd = new ReloadCmd(CommandConfig.reload.delay, CommandConfig.reload.cooldown, CommandConfig.reload.aliases, CommandConfig.reload.cost);
    public static RepairCmd repairCmd = new RepairCmd(CommandConfig.repair.delay, CommandConfig.repair.cooldown, CommandConfig.repair.aliases, CommandConfig.repair.cost);
    public static RespondCmd respondCmd = new RespondCmd(CommandConfig.respond.delay, CommandConfig.respond.cooldown, CommandConfig.respond.aliases, CommandConfig.respond.cost);
    public static RtpCmd rtpCmd = new RtpCmd(CommandConfig.rtp.delay, CommandConfig.rtp.cooldown, CommandConfig.rtp.aliases, CommandConfig.rtp.cost);
    public static RtpFarCmd rtpFarCmd = new RtpFarCmd(CommandConfig.rtpfar.delay, CommandConfig.rtpfar.cooldown, CommandConfig.rtpfar.aliases, CommandConfig.rtpfar.cost);
    public static RtpNearCmd rtpNearCmd = new RtpNearCmd(CommandConfig.rtpnear.delay, CommandConfig.rtpnear.cooldown, CommandConfig.rtpnear.aliases, CommandConfig.rtpnear.cost);
    public static SellCmd sellCmd = new SellCmd(CommandConfig.sell.delay, CommandConfig.sell.cooldown, CommandConfig.sell.aliases, CommandConfig.sell.cost);
    public static SetHomeCmd setHomeCmd = new SetHomeCmd(CommandConfig.sethome.delay, CommandConfig.sethome.cooldown, CommandConfig.sethome.aliases, CommandConfig.sethome.cost);
    public static SetSpawnCmd setSpawnCmd = new SetSpawnCmd(CommandConfig.setspawn.delay, CommandConfig.setspawn.cooldown, CommandConfig.setspawn.aliases, CommandConfig.setspawn.cost);
    public static SetWarpCmd setWarpCmd = new SetWarpCmd(CommandConfig.setwarp.delay, CommandConfig.setwarp.cooldown, CommandConfig.setwarp.aliases, CommandConfig.setwarp.cost);
    public static SmiteCmd smiteCmd = new SmiteCmd(CommandConfig.smite.delay, CommandConfig.smite.cooldown, CommandConfig.smite.aliases, CommandConfig.smite.cost);
    public static SpawnCmd spawnCmd = new SpawnCmd(CommandConfig.spawn.delay, CommandConfig.spawn.cooldown, CommandConfig.spawn.aliases, CommandConfig.spawn.cost);
    public static StaffChatCmd staffChatCmd = new StaffChatCmd(CommandConfig.staffchat.delay, CommandConfig.staffchat.cooldown, CommandConfig.staffchat.aliases, CommandConfig.staffchat.cost);
    public static SudoCmd sudoCmd = new SudoCmd(CommandConfig.sudo.delay, CommandConfig.sudo.cooldown, CommandConfig.sudo.aliases, CommandConfig.sudo.cost);
    public static TitlesCmd titlesCmd = new TitlesCmd(CommandConfig.titles.delay, CommandConfig.titles.cooldown, CommandConfig.titles.aliases, CommandConfig.titles.cost);
    public static TpaCmd tpaCmd = new TpaCmd(CommandConfig.tpa.delay, CommandConfig.tpa.cooldown, CommandConfig.tpa.aliases, CommandConfig.tpa.cost);
    public static TpAcceptCmd tpAcceptCmd = new TpAcceptCmd(CommandConfig.tp_accept.delay, CommandConfig.tp_accept.cooldown, CommandConfig.tp_accept.aliases, CommandConfig.tp_accept.cost);
    public static TpDenyCmd tpDenyCmd = new TpDenyCmd(CommandConfig.tp_deny.delay, CommandConfig.tp_deny.cooldown, CommandConfig.tp_deny.aliases, CommandConfig.tp_deny.cost);
    public static TpaHereCmd tpaHereCmd = new TpaHereCmd(CommandConfig.tp_here.delay, CommandConfig.tp_here.cooldown, CommandConfig.tp_here.aliases, CommandConfig.tp_here.cost);
    public static TrustCmd trustCmd = new TrustCmd(CommandConfig.trust.delay, CommandConfig.trust.cooldown, CommandConfig.trust.aliases, CommandConfig.trust.cost);
    public static UnclaimCmd unclaimCmd = new UnclaimCmd(CommandConfig.unclaim.delay, CommandConfig.unclaim.cooldown, CommandConfig.unclaim.aliases, CommandConfig.unclaim.cost);
    public static UnclaimAllCmd unclaimAllCmd = new UnclaimAllCmd(CommandConfig.unclaimAll.delay, CommandConfig.unclaimAll.cooldown, CommandConfig.unclaimAll.aliases, CommandConfig.unclaimAll.cost);
    public static UnjailCmd unjailCmd = new UnjailCmd(CommandConfig.unjail.delay, CommandConfig.unjail.cooldown, CommandConfig.unjail.aliases, CommandConfig.unjail.cost);
    public static UntrustCmd untrustCmd = new UntrustCmd(CommandConfig.untrust.delay, CommandConfig.untrust.cooldown, CommandConfig.untrust.aliases, CommandConfig.untrust.cost);
    public static VoteCmd voteCmd = new VoteCmd(CommandConfig.vote.delay, CommandConfig.vote.cooldown, CommandConfig.vote.aliases, CommandConfig.vote.cost);
    public static WarpCmd warpCmd = new WarpCmd(CommandConfig.warp.delay, CommandConfig.warp.cooldown, CommandConfig.warp.aliases, CommandConfig.warp.cost);
    public static WarpsCmd warpsCmd = new WarpsCmd(CommandConfig.warps.delay, CommandConfig.warps.cooldown, CommandConfig.warps.aliases, CommandConfig.warps.cost);

    public List<ICmdBase> cmds = new ArrayList<>();

    public void initCommand(ICmdBase cmd){
        if(!cmds.contains(cmd))
            cmds.add(cmd);
    }

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        Logger.log("Registering Commands");
        if (CommandConfig.back.enable.get()) backCmd.register(dispatcher);
        if (CommandConfig.bomb.enable.get()) bombCmd.register(dispatcher);
        if (CommandConfig.broadcast.enable.get()) broadcastCmd.register(dispatcher);
        if (CommandConfig.bypass.enable.get()) bypassCmd.register(dispatcher);
        if (CommandConfig.chestshop.enable.get()) chestShopCmd.register(dispatcher);
        if (CommandConfig.claim.enable.get()) claimCmd.register(dispatcher);
        if (CommandConfig.claimflag.enable.get()) claimflagCmd.register(dispatcher);
        if (CommandConfig.clearlag.enable.get()) clearlagCmd.register(dispatcher);
        if (CommandConfig.delhome.enable.get()) delHomeCmd.register(dispatcher);
        if (CommandConfig.delwarp.enable.get()) delWarpCmd.register(dispatcher);
        if (CommandConfig.endchest.enable.get()) endChestCmd.register(dispatcher);
        if (CommandConfig.feed.enable.get()) feedCmd.register(dispatcher);
        if (CommandConfig.flood.enable.get()) floodCmd.register(dispatcher);
        if (CommandConfig.fly.enable.get()) flyCmd.register(dispatcher);
        if (CommandConfig.gamemode.enable.get()) gamemodeCmd.register(dispatcher);
        if (CommandConfig.god.enable.get()) godCmd.register(dispatcher);
        if (CommandConfig.heal.enable.get()) healCmd.register(dispatcher);
        if (CommandConfig.home.enable.get()) homeCmd.register(dispatcher);
        if (CommandConfig.homes.enable.get()) homesCmd.register(dispatcher);
        if (CommandConfig.invsee.enable.get()) invSeeCmd.register(dispatcher);
        if (CommandConfig.item.enable.get()) itemCmd.register(dispatcher);
        if (CommandConfig.jail.enable.get()) jailCmd.register(dispatcher);
        //invSeeTestCmd.register(dispatcher);
        if (CommandConfig.kit.enable.get()) kitCmd.register(dispatcher);
        if (CommandConfig.motd.enable.get()) motd.register(dispatcher);
        if (CommandConfig.msg.enable.get()) msgCmd.register(dispatcher);
        if (CommandConfig.msg_toggle.enable.get()) msgToggleCmd.register(dispatcher);
        if (CommandConfig.mute.enable.get()) muteCmd.register(dispatcher);
        if (CommandConfig.nick.enable.get()) nickCmd.register(dispatcher);
        if (CommandConfig.perms.enable.get()) permsCmd.register(dispatcher);
        if (CommandConfig.rank.enable.get()) rankCmd.register(dispatcher);
        if (CommandConfig.rankup.enable.get()) rankupCmd.register(dispatcher);
        if (CommandConfig.respond.enable.get()) respondCmd.register(dispatcher);
        if (CommandConfig.reload.enable.get()) reloadCmd.register(dispatcher);
        if (CommandConfig.repair.enable.get()) repairCmd.register(dispatcher);
        if (CommandConfig.rtp.enable.get()) rtpCmd.register(dispatcher);
        if (CommandConfig.rtpfar.enable.get()) rtpFarCmd.register(dispatcher);
        if (CommandConfig.rtpnear.enable.get()) rtpNearCmd.register(dispatcher);
        if (CommandConfig.sethome.enable.get()) setHomeCmd.register(dispatcher);
        if (CommandConfig.setspawn.enable.get()) setSpawnCmd.register(dispatcher);
        if (CommandConfig.setwarp.enable.get()) setWarpCmd.register(dispatcher);
        if (CommandConfig.smite.enable.get()) smiteCmd.register(dispatcher);
        if (CommandConfig.spawn.enable.get()) spawnCmd.register(dispatcher);
        if (CommandConfig.staffchat.enable.get()) staffChatCmd.register(dispatcher);
        if (CommandConfig.sudo.enable.get()) sudoCmd.register(dispatcher);
        if (CommandConfig.titles.enable.get()) titlesCmd.register(dispatcher);
        if (CommandConfig.tpa.enable.get()) tpaCmd.register(dispatcher);
        if (CommandConfig.tp_accept.enable.get()) tpAcceptCmd.register(dispatcher);
        if (CommandConfig.tp_deny.enable.get()) tpDenyCmd.register(dispatcher);
        if (CommandConfig.tp_here.enable.get()) tpaHereCmd.register(dispatcher);
        if (CommandConfig.trust.enable.get()) trustCmd.register(dispatcher);
        if (CommandConfig.unclaim.enable.get()) unclaimCmd.register(dispatcher);
        if (CommandConfig.unclaimAll.enable.get()) unclaimAllCmd.register(dispatcher);
        if (CommandConfig.unjail.enable.get()) unjailCmd.register(dispatcher);
        if (CommandConfig.untrust.enable.get()) untrustCmd.register(dispatcher);
        if (CommandConfig.warps.enable.get()) warpsCmd.register(dispatcher);
        if (CommandConfig.warp.enable.get()) warpCmd.register(dispatcher);

        if (FeatureConfig.votingEnabled.get() && CommandConfig.vote.enable.get()) voteCmd.register(dispatcher);

        if (FeatureConfig.enable_economy.get()) {
            if (CommandConfig.bal.enable.get()) balCmd.register(dispatcher);
            if (CommandConfig.econ.enable.get()) econCmd.register(dispatcher);
            if (CommandConfig.pay.enable.get()) payCmd.register(dispatcher);
            if (CommandConfig.price.enable.get()) priceCmd.register(dispatcher);
            if (CommandConfig.sell.enable.get()) sellCmd.register(dispatcher);
        }

        if (FeatureConfig.discord_bot_enable.get()) {
            if (CommandConfig.link.enable.get()) linkCmd.register(dispatcher);
        }

        cmds.forEach(cmd->cmd.register(dispatcher));

        if(FeatureConfig.enable_global_perms.get()) {
            try {
                CommandUpdater.init(dispatcher);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }
}

