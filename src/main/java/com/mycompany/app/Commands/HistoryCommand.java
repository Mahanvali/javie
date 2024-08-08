package com.mycompany.app.Commands;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.LevelSystem;
//  JAVA IMPORTS
import java.time.format.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

//  Get all the cache data from a user
public class HistoryCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        
        User targetUser = event.getOption("user").getAsUser();  //  Get the user
        String targetUserId = targetUser.getId();   //  Get the user iD
        int userLevel = LevelSystem.getLevel(targetUserId);
        int userXp = LevelSystem.levelInformation.getOrDefault(targetUserId, 0);
        int xpForNextLevel = LevelSystem.getXpForNextLevel(userLevel);
        String xpRequiredForNextLevelString =  Integer.toString(xpForNextLevel - userXp);
        String userLevelString = Integer.toString(userLevel);
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(targetUser.getName() + "'s History");
        embed.setColor(Global.CUSTOMPURPLE);
        embed.addField("Current Level: ", userLevelString, false);
        embed.addField("XP Required For Next Level: ", xpRequiredForNextLevelString, false);
        embed.addField("Joined Server At:", event.getOption("user").getAsMember().getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), false);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
