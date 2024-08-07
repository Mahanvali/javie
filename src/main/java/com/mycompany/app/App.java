package com.mycompany.app;

//  JDA API IMPORTS
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

//  JAVA IMPORTS
import java.util.EnumSet;

import com.mycompany.app.Listeners.GuildMemberListener;
import com.mycompany.app.Listeners.GuildMessageListener;
import com.mycompany.app.Listeners.ReadyListener;
import com.mycompany.app.Listeners.SlashListener;

public class App {
    public static void main(String[] args) throws Exception {
        //Start the JDA bot builder, letting you provide the token externally
        JDABuilder jdaBotBuilder = JDABuilder.createDefault(args[0], EnumSet.of(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_MEMBERS))
        .enableCache(CacheFlag.ACTIVITY)    //  Required for activity caching
        .addEventListeners(new SlashListener(), new GuildMemberListener(), new GuildMessageListener(), new ReadyListener())
        .setMemberCachePolicy(MemberCachePolicy.ALL);   //  Required for nickname caching
        //  Create JDA Instance
        JDA jda = jdaBotBuilder.build();

        CommandListUpdateAction commands = jda.updateCommands();

        //  Bot Developer commands
        commands.addCommands(
            Commands.slash("boo", "Check the cache"),
            Commands.slash("poo", "Clear the cache"),
            Commands.slash("history", "Get a user's history")
                .addOption(OptionType.MENTIONABLE, "user", "User to get history from", true),
            Commands.slash("showall", "secret ahh command")
        ).queue();

        //  Mod commands
        commands.addCommands(
            Commands.slash("kick", "Kick a user")
                .addOption(OptionType.MENTIONABLE, "user", "The user to kick", true)
                .addOption(OptionType.STRING, "reason", "Reason for kicking the user", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS)),

            Commands.slash("ban", "Ban a user")
                .addOption(OptionType.MENTIONABLE, "user", "The user to ban.", true)
                .addOption(OptionType.STRING, "reason", "Reason for banning the user.", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),

            Commands.slash("unban", "Unban a user")
                .addOption(OptionType.MENTIONABLE, "user", "The user to unban.", true)
                .addOption(OptionType.STRING, "reason", "Reason for unbanning the user.", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
            
        ).queue();
   
    } 
}
