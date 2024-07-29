package com.mycompany.app.Commands;

//  IMPORT COMMANDIMPLEMENTATION
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.GuildMessageListener;

//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PooCommand implements CommandImplementation {
    //  Override the PooCommand method from the CommandImplementation
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue(); // Defer the reply to allow for longer times to send the message
        String messageCacheSize = Integer.toString(GuildMessageListener.messageCache.size());
        event.getHook().sendMessage("Clearing " + messageCacheSize + " messages stored in cache.. 🟠").queue();
        GuildMessageListener.messageCache.clear(); //  Clear the message cache

        if(GuildMessageListener.messageCache.size() == 0){
            event.getHook().sendMessage("All messages in cache cleared! 🟢").queue();
        } else {
            event.getHook().sendMessage("Something went wrong, couldn't clear the cache! 🔴").queue();
        }
    }
}
