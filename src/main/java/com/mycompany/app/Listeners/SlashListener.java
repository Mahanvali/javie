package com.mycompany.app.Listeners;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//  JAVA IMPORTS
import java.util.HashMap;
import java.util.Map;


//  COMMAND IMPORTS
import com.mycompany.app.Commands.*;
import com.mycompany.app.Commands.mod.*;
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

public class SlashListener extends ListenerAdapter {

    //  Create a HashMap to store the commands
    private final Map<String, CommandImplementation> commands = new HashMap<String, CommandImplementation>();

    //  Constructor for the SlashListener class
    public SlashListener(){
        //  Store the commands in the hashmap
        commands.put("boo", new BooCommand());
        commands.put("poo", new PooCommand());

        commands.put("kick", new KickCommand());
        commands.put("ban", new BanCommand());
        commands.put("unban", new UnbanCommand());

        commands.put("history", new HistoryCommand());
        commands.put("showall", new ShowAllCommand());
    }

    //  Override the onSlashCommandInteraction method from ListenerAdapter
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){

        // Get the command from the hashmap using the event name
        CommandImplementation command = commands.get(event.getName());
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
        //  If the command isn't null, execute the command.
        if (command != null) {
            try{
                command.execute(event);
            }catch (Exception e){
                logsChannel.sendMessage(event.getUser().getAsMention() + " An unexpected error occurred: \n ```" + e.getMessage() + "```").queue();
            }
        }
    }
}
