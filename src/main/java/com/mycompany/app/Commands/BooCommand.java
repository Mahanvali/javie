package com.mycompany.app.Commands;

//  IMPORT COMMANDIMPLEMENTATION
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.GuildMessageListener;

import net.dv8tion.jda.api.EmbedBuilder;
//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BooCommand implements CommandImplementation {

    private static final long MEGABYTE = 1024L * 1024L;
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }    
    //  Override the BooCommand method from the CommandImplementation
    @Override
    public void execute(SlashCommandInteractionEvent event){
        // Defer the reply to allow for longer times to send the message
        event.deferReply().queue();
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        //  Get the number of messages cached
        String messageCacheSize = Integer.toString(GuildMessageListener.messageCache.size());
        //  Create new embed
        EmbedBuilder embed = new EmbedBuilder();

        if(event.getUser().getId().equals(Global.botdeveloperUserId)){  //  If the user is the bot developer
            embed.addField("Memory Usage:", "`" + Long.toString(bytesToMegabytes(memory)) + "`" + " megabytes", false);
            embed.addField("Cached:", "`" + messageCacheSize + "`" + " messages", false);
            embed.setColor(Global.CUSTOMPURPLE);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        } else {
            Global.BuildInvalidPermissionsEmbed("BOT DEVELOPER", Global.CUSTOMRED, embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }

    }
}
