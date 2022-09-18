package dev.elrol.serverutilities.discord.init;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
        if(guild == null) {
            Main.getLogger().error("Discord Server was NULL");
            return;
        }
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
        Member member = event.getMember();
        if(member == null) {
            event.reply("Error: Member was Null").setEphemeral(true).queue();
            return;
        }
        Logger.log("Discord Command: " + cmd);
        if(cmd.equalsIgnoreCase("run")) {
            if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("Error: You don't have permission to use this command.").setEphemeral(true).queue();
                return;
            }
            OptionMapping option = event.getOption("command");
            String command = option == null ? "" : option.getAsString();
            CommandSourceStack console = Main.mcServer.createCommandSourceStack();
            Main.mcServer.getCommands().performPrefixedCommand(console, command);
            event.reply("Command ran successfully")
                    .setEphemeral(true)
                    .queue();
        } else if(cmd.equalsIgnoreCase("link")) {
            OptionMapping option = event.getOption("verification");
            String code = option == null ? "" : option.getAsString();
            if(code.isEmpty()) {
                code = Main.textUtils.generateString();
                Main.serverData.discordVerifications.put(code, member.getIdLong());
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
