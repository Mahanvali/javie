package com.mycompany.app.Commands;


//  IMPORT COMMANDIMPLEMENTATION
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.GuildMessageListener;

//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BooCommand implements CommandImplementation {
    //  Override the BooCommand method from the CommandImplementation
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue(); // Defer the reply to allow for longer times to send the message
        if(event.getUser().getId().equals(Global.botdeveloperUserId)){  //  If the user is the bot developer
            String messageCacheSize = Integer.toString(GuildMessageListener.messageCache.size());
            event.getHook().sendMessage("`" + messageCacheSize + "`" + " messages stored in cache").queue();
        } else {
            event.getHook().sendMessage("Sorry, you can't run this command! 🔴").queue();
        }

    }
}
