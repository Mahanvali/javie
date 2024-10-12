package com.mycompany.app.Commands;

//  IMPORT COMMANDIMPLEMENTATION
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.GuildMemberListener;
import com.mycompany.app.Listeners.GuildMessageListener;

//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class BooCommand implements CommandImplementation {

    private static final long MEGABYTE = 1024L * 1024L;
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }    
    //  Override the BooCommand method from the CommandImplementation
    @Override
    public void execute(SlashCommandInteractionEvent event){
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        //  Get the number of messages cached
        String messageCacheSize = Integer.toString(GuildMessageListener.messageCache.size());
        String nicknameCacheSize = Integer.toString(GuildMemberListener.nicknameCache.size());
        
        //  Create new embed
        EmbedBuilder embed = new EmbedBuilder();

        if(event.getUser().getId().equals(Global.botdeveloperUserId)){  //  If the user is the bot developer
            embed.addField("Memory Usage:", "**" + bytesToMegabytes(memory) + "**" + " megabytes", false);
            embed.addField("Ping:",  "**" + event.getJDA().getGatewayPing() + "ms**", false);
            embed.addField("Cached:", "**" + messageCacheSize + "**" + " messages\n" + "**" + nicknameCacheSize + "**" + " nicknames", false);
            embed.setColor(Global.CUSTOMPURPLE);
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        } else {
            Global.BuildInvalidPermissionsEmbed("BOT DEVELOPER", Global.CUSTOMRED, embed);
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }

    }
}
