package dev.elrol.serverutilities.config;

import dev.elrol.serverutilities.libs.Logger;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber
public class FeatureConfig {
    public static ForgeConfigSpec.ConfigValue<String> tag;

    public static ForgeConfigSpec.BooleanValue color_chat_enable;
    public static ForgeConfigSpec.BooleanValue translation_enable;
    public static ForgeConfigSpec.BooleanValue discord_bot_enable;
    public static ForgeConfigSpec.ConfigValue<String> color_chat_perm;
    public static ForgeConfigSpec.ConfigValue<String> link_chat_perm;
    public static ForgeConfigSpec.BooleanValue rainbow_code_enable;
    public static ForgeConfigSpec.BooleanValue holiday_code_enable;
    public static ForgeConfigSpec.BooleanValue enable_global_perms;
    public static ForgeConfigSpec.BooleanValue enable_economy;

    public static ForgeConfigSpec.ConfigValue<String> currency_singular;
    public static ForgeConfigSpec.ConfigValue<String> currency_plural;
    public static ForgeConfigSpec.ConfigValue<String> currency_symbol;

    public static ForgeConfigSpec.BooleanValue sign_shops_enabled;
    public static ForgeConfigSpec.BooleanValue chest_shops_enabled;
    public static ForgeConfigSpec.BooleanValue chest_buy_shops_enabled;
    public static ForgeConfigSpec.BooleanValue chest_sell_shops_enabled;
    public static ForgeConfigSpec.BooleanValue chest_admin_buy_shops_enabled;
    public static ForgeConfigSpec.BooleanValue chest_admin_sell_shops_enabled;

    public static ForgeConfigSpec.ConfigValue<String> jan_colors;
    public static ForgeConfigSpec.ConfigValue<String> feb_colors;
    public static ForgeConfigSpec.ConfigValue<String> mar_colors;
    public static ForgeConfigSpec.ConfigValue<String> apr_colors;
    public static ForgeConfigSpec.ConfigValue<String> may_colors;
    public static ForgeConfigSpec.ConfigValue<String> jun_colors;
    public static ForgeConfigSpec.ConfigValue<String> jul_colors;
    public static ForgeConfigSpec.ConfigValue<String> aug_colors;
    public static ForgeConfigSpec.ConfigValue<String> sep_colors;
    public static ForgeConfigSpec.ConfigValue<String> oct_colors;
    public static ForgeConfigSpec.ConfigValue<String> nov_colors;
    public static ForgeConfigSpec.ConfigValue<String> dec_colors;

    public static ForgeConfigSpec.IntValue clearlag_frequency;
    public static ForgeConfigSpec.BooleanValue clearlag_items;
    public static ForgeConfigSpec.BooleanValue clearlag_hostile;
    public static ForgeConfigSpec.BooleanValue clearlag_passive;
    public static ForgeConfigSpec.BooleanValue auto_clearlag_enabled;

    public static ForgeConfigSpec.ConfigValue<String> command_help_spacer;
    public static ForgeConfigSpec.ConfigValue<String> command_help_info;
    public static ForgeConfigSpec.ConfigValue<String> command_help_entry;

    public static ForgeConfigSpec.BooleanValue welcome_msg_enable;
    public static ForgeConfigSpec.ConfigValue<String> welcome_msg_text;

    public static ForgeConfigSpec.BooleanValue goodbye_msg_enable;
    public static ForgeConfigSpec.ConfigValue<String> goodbye_msg_text;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> jailCommands;
    public static ForgeConfigSpec.BooleanValue jailCommandsWhitelist;
    public static ForgeConfigSpec.IntValue jailMaxDistance;
    public static ForgeConfigSpec.IntValue jailProtection;

    public static ForgeConfigSpec.ConfigValue<String> sc_tag;
    public static ForgeConfigSpec.ConfigValue<String> sc_format;
    public static ForgeConfigSpec.ConfigValue<String> sc_jail_tag;
    public static ForgeConfigSpec.ConfigValue<String> sc_jail_format;

    public static ForgeConfigSpec.BooleanValue votingEnabled;

    public static void init(ForgeConfigSpec.Builder server){
        server.comment("The tag used by the mod, leave empty for no tag.");
        tag = server.define("tag", "&8[&9S&aU&8]");
        server.push("Welcome Message");
            welcome_msg_enable = server.define("enabled", true);
            server.comment("{player} will be replaced with the player name.");
            welcome_msg_text = server.define("text", "&hWelcome {player}&h to the server!");
        server.pop();
        server.push("Goodbye Message");
            goodbye_msg_enable = server.define("enabled", true);
            server.comment("{player} will be replaced with the player name.");
            goodbye_msg_text = server.define("text", "&hGoodbye {player}");
        server.pop();
        server.comment("enabling global permissions will attempt to change all commands to use permissions generated dynamically by ServerUtils. If disabled, only the commands from ServerUtils will have the permissions.");
        enable_global_perms = server.define("global perms", true);

        server.comment("disabling translations will force all messages to be in english.");
        translation_enable = server.define("translations", true);

        server.comment("enabling this will allow you to run a discord bot that will bridge the server with discord. be sure to edit the discord config.");
        discord_bot_enable = server.define("discord bot", false);

        server.comment("enabling the economy will apply the cost for commands, and other money related features.");
        server.push("Economy");
            enable_economy = server.define("economy", false);
            server.push("Currency");
                currency_singular = server.define("singular", "Dollar");
                currency_plural = server.define("plural", "Dollars");
                currency_symbol = server.define("symbol", "$");
            server.pop();
        server.pop();
        server.push("Voting");
            votingEnabled = server.define("enable voting", false);
        server.pop();
        server.push("Auto Clearlag");
            auto_clearlag_enabled = server.define("enabled", false);
            server.comment("Clearlag Frequency requires a restart to apply");
            clearlag_frequency = server.defineInRange("frequency", 5, 0, Integer.MAX_VALUE);
            server.push("Entities");
                clearlag_items = server.define("item", true);
                clearlag_passive = server.define("passive", true);
                clearlag_hostile = server.define("hostile", true);
            server.pop();
        server.pop();
        server.push("Shops");
            sign_shops_enabled = server.define("Enabled", true);
            server.push("Chest Shops");
                chest_shops_enabled = server.define("Enabled", true);
                chest_buy_shops_enabled = server.define("Buy Enabled", true);
                chest_sell_shops_enabled = server.define("Sell Enabled", true);
                chest_admin_buy_shops_enabled = server.define("Admin Buy Enabled", true);
                chest_admin_sell_shops_enabled = server.define("Admin Sell Enabled", true);
            server.pop();
        server.pop();
        server.push("Color Chat");
            color_chat_perm = server.define("perm", "serverutils.colorchat");
            link_chat_perm = server.define("link-perm", "serverutils.linkchat");
            color_chat_enable = server.define("enabled", true);
            server.push("Command Help");
                server.comment("KEY will be replaced with the sub command, and VALUE will be replaced with the info about the sub command.");
                command_help_entry = server.define("entry-format", "&8[&9KEY&8]&7:&aVALUE");
                server.comment("INFO will be replaced with the info about a command.");
                command_help_info = server.define("info-format", "&bINFO");
                command_help_spacer = server.define("spacer-format", "&6====================");
            server.pop();
            server.push("Color Codes");
                server.comment("This will enable the &g color code, which will change text to rainbow.");
                rainbow_code_enable = server.define("rainbow enabled", true);

                server.comment("This will enable the &h color code, which will change text based on the month it is.");
                holiday_code_enable = server.define("holiday enabled", true);

                server.push("Holiday Colors");
                    jan_colors = server.define("January", "876f");
                    feb_colors = server.define("February", "fd5c");
                    mar_colors = server.define("March", "2af6");
                    apr_colors = server.define("April", "ba9f");
                    may_colors = server.define("May", "aeb9");
                    jun_colors = server.define("June", "7fc");
                    jul_colors = server.define("July", "cf9");
                    aug_colors = server.define("August", "6ca");
                    sep_colors = server.define("September", "8ce");
                    oct_colors = server.define("October", "86f");
                    nov_colors = server.define("November", "c6e");
                    dec_colors = server.define("December", "afc");
                server.pop();
            server.pop();

            server.push("Staff Chat");
                server.comment("Formatting for the staff chat. Uses just the formatting codes. Don't include the &");
                sc_tag = server.define("staff tag", "&3&l[STAFF]&r");

                server.comment("Formatting for the staff chat. Uses just the formatting codes. Don't include the &");
                sc_format = server.define("formatting", "bl");

                server.comment("Formatting for the staff chat. Uses just the formatting codes. Don't include the &");
                sc_jail_tag = server.define("jail tag", "&8&o[JAIL]");

                server.comment("Jail Formatting for the staff chat. Uses just the formatting codes. Don't include the &");
                sc_jail_format = server.define("jail formatting", "7o");
            server.pop();
        server.pop();

        server.comment("Jail Configs");
        server.push("Jails");
            server.comment("A list of commands that can/can't be used while jailed.");
            jailCommands = server.defineList("commands", Collections.singletonList(""), o -> o instanceof String);

            server.comment("If true, the commands listed will be allowed, if false then they will not be allowed.");
            jailCommandsWhitelist = server.define("whitelist", false);

            server.comment("This is the max distance (in blocks) that the jailed player can travel before being teleported back to the jail.");
            jailMaxDistance = server.defineInRange("max distance", 10, 0, Integer.MAX_VALUE);

            server.comment("Sets how jailed players are damaged. 0 will leave it normal, 1 will make the damage they take be 0, 2 will prevent them from taking any damage");
            jailProtection = server.defineInRange("player protection", 1, 0, 2);
        server.pop();
        Logger.log("Config has been initialized");
    }
}