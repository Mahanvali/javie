package com.mycompany.app.Listeners;

//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//  JAVA IMPORTS
import java.util.HashMap;
import java.util.Map;


//  COMMAND IMPORTS
import com.mycompany.app.Commands.BooCommand;
import com.mycompany.app.Commands.PooCommand;
import com.mycompany.app.Commands.mod.KickCommand;
import com.mycompany.app.CommandImplementation;

public class SlashListener extends ListenerAdapter {

    //  Create a HashMap to store the commands
    private final Map<String, CommandImplementation> commands = new HashMap<String, CommandImplementation>();

    //  Constructor for the SlashListener class
    public SlashListener(){
        //  Store the commands in the hashmap
        commands.put("boo", new BooCommand());
        commands.put("poo", new PooCommand());
        commands.put("kick", new KickCommand());
    }

    //  Override the onSlashCommandInteraction method from ListenerAdapter
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){

        // Get the command from the hashmap using the event name
        CommandImplementation command = commands.get(event.getName());

        //  If the command isn't null, execute the command.
        if (command != null) {
            command.execute(event);
        }
    }
}
