package com.mycompany.app.Commands.level;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.LevelSystem;
//  JAVA IMPORTS
import java.time.format.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

//  Get all the cache data from a user
public class LevelCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        
        User targetUser = event.getOption("user").getAsUser();
        String targetUserId = targetUser.getId();
        int userLevel = LevelSystem.getLevel(targetUserId);
        int userXp = LevelSystem.levelInformation.getOrDefault(targetUserId, 0);
        int xpForNextLevel = LevelSystem.getXpForNextLevel(userLevel);
        String xpRequiredForNextLevelString =  Integer.toString(xpForNextLevel - userXp);
        String userLevelString = Integer.toString(userLevel);

        // Calculate the rank
        List<Map.Entry<String, Integer>> sortedUsers = LevelSystem.levelInformation.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<String, Integer> entry : sortedUsers) {
            if (entry.getKey().equals(targetUserId)) {
                break;
            }
            rank++;
        }
            
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(targetUser.getName() + "'s Information");
        embed.setColor(Global.CUSTOMPURPLE);
        embed.addField("Current Level: ", userLevelString, false);
        embed.addField("XP Required For Next Level: ", xpRequiredForNextLevelString, false);
        embed.addField("Current Rank: ", "#" + rank, false);
        embed.addField("Joined Server At: ", event.getOption("user").getAsMember().getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), false);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
