package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.GuildMemberListener;

//  JAVA IMPORTS
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

//  Get all the cache data from a user
public class HistoryCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        
        User targetUser = event.getOption("user").getAsUser();  //  Get the user
        StringBuilder foundKeys = new StringBuilder(); // Initialize a StringBuilder
        EmbedBuilder embed = new EmbedBuilder();

        // Iterate through the activity cache to find matching keys
        for (Map.Entry<String, String> entry : GuildMemberListener.activityCache.entrySet()) {
            if (entry.getValue().equals(targetUser.getId())) {
                String key = entry.getKey();
                foundKeys.append("`" + key + "`");  //  Add the keys to the StringBuilder
                foundKeys.append("\n"); //  Add some formatting
            }
        }

        if(GuildMemberListener.activityCache.containsValue(targetUser.getId())){
            embed.setTitle("User History");
            embed.setColor(Global.CUSTOMGREEN);
            embed.addField("Activites:", foundKeys.toString(), false);
            //  More fields later!!
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        } else {
            event.getHook().sendMessage("`" + targetUser.getName() + "`'s activities has not been cached!").queue();
        }
    }
}
