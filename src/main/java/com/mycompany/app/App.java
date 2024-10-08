package com.mycompany.app;

//  JDA API IMPORTS
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
//  JAVA IMPORTS
import java.util.EnumSet;

import com.mycompany.app.Commands.UpdateCommand;
import com.mycompany.app.Listeners.ButtonListener;
import com.mycompany.app.Listeners.GuildMemberListener;
import com.mycompany.app.Listeners.GuildMessageListener;
import com.mycompany.app.Listeners.LevelSystem;
import com.mycompany.app.Listeners.ModalListener;
import com.mycompany.app.Listeners.ReadyListener;
import com.mycompany.app.Listeners.SlashListener;

public class App {
    public static void main(String[] args) throws Exception {


        //Start the JDA bot builder, letting you provide the token externally
        JDABuilder jdaBotBuilder = JDABuilder.createDefault(args[0], EnumSet.of(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.DIRECT_MESSAGE_REACTIONS,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_MEMBERS))
        .enableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE)    //  Required for activity caching
        .addEventListeners(new SlashListener(), new GuildMemberListener(), new GuildMessageListener(), new ReadyListener(), new LevelSystem(), new ModalListener(), new ButtonListener())
        .setMemberCachePolicy(MemberCachePolicy.ALL);   //  Required for nickname caching

        // Create JDA Instance
        JDA jda = jdaBotBuilder.build();

        UpdateCommand.globalCommandData.add(
            Commands.slash("update", "Update slash commands")
            .addSubcommands(
                new SubcommandData("dev", "Update dev commands!"),
                new SubcommandData("mod", "Update mod commands!"),
                new SubcommandData("level", "Update level commands!"),
                new SubcommandData("misc", "Update misc commands!"),
                new SubcommandData("activity", "Update my activity!")
            )
        );
        jda.updateCommands().addCommands(UpdateCommand.globalCommandData).queue();
    }

    public static void registerDeveloperCommands(JDA jda){
        UpdateCommand.globalCommandData.add(Commands.slash("boo", "Check the cache"));
        UpdateCommand.globalCommandData.add(Commands.slash("poo", "Clear the cache"));
        jda.updateCommands().addCommands(UpdateCommand.globalCommandData).queue();
    }

    public static void registerModCommands(JDA jda){

        UpdateCommand.globalCommandData.add(
            Commands.slash("kick", "Kick a user")
                .addOption(OptionType.USER, "user", "User to kick", true)
                .addOption(OptionType.STRING, "reason", "Reason for kicking the user", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS)));

        UpdateCommand.globalCommandData.add(
            Commands.slash("ban", "Ban a user")
                .addOption(OptionType.USER, "user", "User to ban", true)
                .addOption(OptionType.STRING, "reason", "Reason for banning the user", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)));
                
        UpdateCommand.globalCommandData.add(
            Commands.slash("unban", "Unban a user")
                .addOption(OptionType.USER, "user", "User to unban", true)
                .addOption(OptionType.STRING, "reason", "Reason for unbanning the user", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)));

        UpdateCommand.globalCommandData.add(
            Commands.slash("ticket", "hehehheee hi!!!! yiip[ee!!]")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));

        UpdateCommand.globalCommandData.add(
            Commands.slash("slowmode", "Slowmode command")
            .addSubcommands(
                new SubcommandData("set", "Set a slowmode for the current channel")
                    .addOption(OptionType.STRING, "time", "The slowmode to set to (s,m,h)", true),
                new SubcommandData("reset", "Reset the slowmode for the current channel"))
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL)));

                UpdateCommand.globalCommandData.add(
                    Commands.slash("warn", "Warn a user")
                    .addSubcommands(
                        new SubcommandData("add", "Warn a user")
                            .addOption(OptionType.USER, "user", "User to warn", true)
                            .addOption(OptionType.STRING, "reason", "Reason for warning the user", true),
                        new SubcommandData("remove", "Remove a warn from a user")
                            .addOption(OptionType.USER, "user", "Remove a warn from a user", true)
                            .addOption(OptionType.STRING, "reason", "Reason for removing the warn", true),
                        new SubcommandData("list", "Show the amount of warns a user has")
                            .addOption(OptionType.USER, "user", "User to show warnings of"))
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)));

        UpdateCommand.globalCommandData.add(
            Commands.slash("timeout", "Timeout a user for a duration")
            .addSubcommands(
                new SubcommandData("add", "Timeout a user")
                    .addOption(OptionType.USER, "user", "User to timeout", true)
                    .addOption(OptionType.STRING, "duration", "Amount of time to timeout the user (w,d,h,m,s)", true)
                    .addOption(OptionType.STRING, "reason", "Reason for timing out the user", true),
                new SubcommandData("remove", "Untimeout a user")
                    .addOption(OptionType.USER, "user", "User to remove untimeout", true)
                    .addOption(OptionType.STRING, "reason", "Reason for untiming out the user", true))
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)));
        jda.updateCommands().addCommands(UpdateCommand.globalCommandData).queue();
    }

    public static void registerLevelCommands(JDA jda){
        UpdateCommand.globalCommandData.add(
            Commands.slash("leaderboard", "Check the level leaderboards")
                .addOption(OptionType.INTEGER, "top", "The top x of people to show their levels", true));

        UpdateCommand.globalCommandData.add(
            Commands.slash("level", "Get a user's level")
                .addOption(OptionType.USER, "user", "User to get level from", true));

        UpdateCommand.globalCommandData.add(
            Commands.slash("set", "Set various configurations")
                .addSubcommands(
                    new SubcommandData("xpgain", "Change the amount of xp gain per message")
                        .addOption(OptionType.INTEGER, "amount", "Amount of xp to gain per message (Default: 5)", true),
                    new SubcommandData("cooldown", "Change the cooldown per message sent for gaining xp")
                        .addOption(OptionType.STRING, "message", "Message cooldown (Default: 3s) PICK ONLY 1")
                        .addOption(OptionType.STRING, "voice", "Voice channel cooldown (Default: 30m) PICK ONLY 1"))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)));

        UpdateCommand.globalCommandData.add(
            Commands.slash("currentconfigs", "Check the server's current configurations")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)));

        UpdateCommand.globalCommandData.add(
            Commands.slash("xp", "Add or remove xp from a user")
                .addSubcommands(
                    new SubcommandData("add", "Add a certain amount of xp to a user")
                        .addOption(OptionType.USER, "user", "User to add xp to", true)
                        .addOption(OptionType.INTEGER, "amount", "Amount of xp for user to gain", true),

                    new SubcommandData("remove", "Remove a certain amount of xp from a user")
                        .addOption(OptionType.USER, "user", "User to remove xp from", true)
                        .addOption(OptionType.INTEGER, "amount", "Amount of xp for user to lose", true))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
        jda.updateCommands().addCommands(UpdateCommand.globalCommandData).queue();
    }

    public static void registerMiscCommands(JDA jda){
        UpdateCommand.globalCommandData.add(
            Commands.slash("links", "Check all of yukari's links!"));

        UpdateCommand.globalCommandData.add(
            Commands.slash("avatar", "Check out another user's avatar")
            .addOption(OptionType.USER, "user", "User's avatar to look at", true));

        UpdateCommand.globalCommandData.add(
            Commands.slash("membercount", "Check out the server's member count"));

            UpdateCommand.globalCommandData.add(
                Commands.slash("tag", "Tags used for FAQs")
                    .addSubcommands(
                        new SubcommandData("add", "Add a tag (ADMINISTRATOR)"),
                        new SubcommandData("remove", "Remove a tag (ADMINISTRATOR)")
                            .addOption(OptionType.STRING, "tag", "The tag to remove", true),
                        new SubcommandData("show", "Search for a tag")
                            .addOption(OptionType.STRING, "tag", "The tag to show", true),
                        new SubcommandData("list", "Show the list of all tags")
                    )
                );
        jda.updateCommands().addCommands(UpdateCommand.globalCommandData).queue();
    }
}
