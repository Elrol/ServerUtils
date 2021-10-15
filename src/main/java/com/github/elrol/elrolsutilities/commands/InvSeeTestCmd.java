package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.data.TargetInventory;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.util.List;
import java.util.UUID;

public class InvSeeTestCmd extends _CmdBase {
    public InvSeeTestCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
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

    protected int execute(CommandContext<CommandSource> c, String name) {
        Main.mcServer.getPlayerList();
        GameProfile profile = Main.mcServer.getProfileCache().get(name);
        if(profile == null){
            Main.getLogger().error("Game Profile was Null.");
            return 0;
        }
        UUID uuid = profile.getId();
        if(uuid == null) return 0;
        CompoundNBT nbt = new CompoundNBT();
        File data = new File(Main.dir, "/playerdata/" + uuid.toString() + ".dat");
        Main.getLogger().info(data.getAbsolutePath());
        try{
            nbt = CompressedStreamTools.read(data);
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
    protected int execute(CommandContext<CommandSource> c) {
        String[] help = new String[]{
                "Opens the inventory of the specified player."
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;
        ServerPlayerEntity source;

        public CommandRunnable(ServerPlayerEntity source, ServerPlayerEntity target) {
            this.player = target;
            this.source = source;
        }

        @Override
        public void run() {
            source.openMenu(new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return player.getDisplayName();
                }

                @Override
                @ParametersAreNonnullByDefault
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity p) {
                    return new ChestContainer(ContainerType.GENERIC_9x5, id, playerInventory, new TargetInventory(player), 5);
                }
            });

            Logger.debug(Methods.getDisplayName(this.player));
            if (source.getUUID().equals(player.getUUID())) {
                TextUtils.err(this.player, Errs.bombed_self());
            } else {
                //player.inventory =
                TextUtils.msg(this.source, Msgs.bombed(Methods.getDisplayName(this.player)));
                TextUtils.msg(this.player, Msgs.boom());
            }
        }
    }

}

