package com.github.elrol.elrolsutilities.discord.init;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class SlashCommands extends ListenerAdapter {

    public void init(Guild guild) {
        guild.upsertCommand("run", "runs a command on the server.")
                .addOption(OptionType.STRING, "command", "The command to execute on the server", true)
                .queue();
        guild.upsertCommand("link", "links your discord account with your minecraft account.")
                .addOption(OptionType.STRING, "verification", "The code to use to verify your accounts.", false)
                .queue();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        String cmd = event.getName();
        Logger.log("Discord Command: " + cmd);
        if(cmd.equalsIgnoreCase("run")) {
            OptionMapping option = event.getOption("command");
            String command = option == null ? "" : option.getAsString();
            CommandSourceStack console = Main.mcServer.createCommandSourceStack();
            Main.mcServer.getCommands().performCommand(console, command);
            event.reply("Command ran successfully")
                    .setEphemeral(true)
                    .queue();
        } else if(cmd.equalsIgnoreCase("link")) {
            OptionMapping option = event.getOption("verification");
            String code = option == null ? "" : option.getAsString();
            if(code.isEmpty()) {
                code = TextUtils.generateString();
                Main.serverData.discordVerifications.put(code, Objects.requireNonNull(event.getMember()).getIdLong());
                event.reply("Run `/link " + code + "` on the server to link to your Minecraft account.")
                        .setEphemeral(true)
                        .queue();
            } else {
                UUID uuid = Main.serverData.minecraftVerifications.get(code);
                if(uuid == null) {
                    event.reply("Invalid verification code. Please run /link then try again.")
                            .setEphemeral(true)
                            .queue();
                } else {
                    IPlayerData data = IElrolAPI.getInstance().getPlayerDatabase().get(uuid);
                    data.setDiscordID(Objects.requireNonNull(event.getMember()).getIdLong());
                    data.save();
                    event.reply("Your Minecraft account [" + data.getUsername() + "] has been linked successfully." )
                            .setEphemeral(true)
                            .queue();
                }
            }
        }
    }
}
