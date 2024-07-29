package com.mycompany.app;

//  JDA API IMPORTS
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
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
        // Disable parts of the cache
        .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
        //  Enable parts of the cache
        .enableCache(CacheFlag.ACTIVITY)
        //  Add event listeners
        .addEventListeners(new SlashListener(), new GuildMemberListener(), new GuildMessageListener(), new ReadyListener());
        //  Create JDA Instance
        JDA jda = jdaBotBuilder.build();

        CommandListUpdateAction commands = jda.updateCommands();

        //  Bot Developer commands
        commands.addCommands(
            Commands.slash("boo", "Check the amount of messages stored in cache"),
            Commands.slash("poo", "Clear the messages stored in cache")
        );

        // Send commands to discord using the API
        commands.queue();
    } 
}
