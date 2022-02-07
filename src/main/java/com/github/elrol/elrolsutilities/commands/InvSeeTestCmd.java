package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.data.TargetInventory;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InvSeeTestCmd extends _CmdBase {
    public InvSeeTestCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            if (this.name.isEmpty()) {
                this.name = a;
            }
            Logger.debug("Registering Alias \"" + a + "\" for Command \"" + this.name + "\"");
            dispatcher.register((Commands.literal("invseetest")
            		.executes(this::execute))
            		.then(Commands.argument("player", StringArgumentType.string())
            				.executes(c -> this.execute(c, StringArgumentType.getString(c, "player")))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, String name) {
        Optional<GameProfile> profile = Main.mcServer.getProfileCache().get(name);
        if(profile.isEmpty()){
            Main.getLogger().error("Game Profile was Null.");
            return 0;
        }
        UUID uuid = profile.get().getId();
        if(uuid == null) return 0;
        CompoundTag nbt = new CompoundTag();
        File data = new File(Main.dir, "/playerdata/" + uuid.toString() + ".dat");
        Main.getLogger().info(data.getAbsolutePath());
        try{
            nbt = NbtIo.read(data);
        } catch (EOFException e) {
            Main.getLogger().info("Finished reading file?");
            Main.getLogger().info(nbt.toString());
            return 1;
        } catch(IOException e) {
            e.printStackTrace();
            return 0;
        }
        //CommandDelay.init(this, source, new CommandRunnable(source, player), false);
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "Opens the inventory of the specified player."
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer player;
        ServerPlayer source;

        public CommandRunnable(ServerPlayer source, ServerPlayer target) {
            this.player = target;
            this.source = source;
        }

        @Override
        public void run() {
            source.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return player.getDisplayName();
                }

                @Override
                @ParametersAreNonnullByDefault
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player p) {
                    return new ChestMenu(MenuType.GENERIC_9x5, id, playerInventory, new TargetInventory(player), 5);
                }
            });

            Logger.debug(Methods.getDisplayName(this.player));
            if (source.getUUID().equals(player.getUUID())) {
                TextUtils.err(this.player, Errs.bombed_self());
            } else {
                //player.getInventory() =
                TextUtils.msg(this.source, Msgs.bombed(Methods.getDisplayName(this.player)));
                TextUtils.msg(this.player, Msgs.boom());
            }
        }
    }

}

