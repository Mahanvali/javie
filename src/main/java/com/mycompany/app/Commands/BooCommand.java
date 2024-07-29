package com.mycompany.app.Commands;


//  IMPORT COMMANDIMPLEMENTATION
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.GuildMessageListener;

//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BooCommand implements CommandImplementation {
    //  Override the BooCommand method from the CommandImplementation
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        String messageCacheSize = Integer.toString(GuildMessageListener.messageCache.size());
        event.getHook().sendMessage(messageCacheSize + " messages stored in cache").queue();
    }
}
