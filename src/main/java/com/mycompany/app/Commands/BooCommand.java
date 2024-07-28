package com.mycompany.app.Commands;


//  IMPORT COMMANDIMPLEMENTATION
import com.mycompany.app.CommandImplementation;

//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BooCommand implements CommandImplementation {
    //  Override the BooCommand method from the CommandImplementation
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue(); // Defer the reply to allow for longer times to send the message
        event.getHook().sendMessage("poo").queue(); //  Edit the message to "poo"
    }
}
