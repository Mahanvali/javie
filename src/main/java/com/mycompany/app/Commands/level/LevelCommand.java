package com.mycompany.app.Commands.level;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.LevelSystem;
//  JAVA IMPORTS
import java.time.format.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

//  Get all the cache data from a user
public class LevelCommand implements CommandImplementation {
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
        AtomicInteger rank = new AtomicInteger();
        LevelSystem.levelInformation.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> {
                if(entry.getKey().equals(targetUserId)){    //  If the key is the user, stop incrementing
                    return;
                }
                rank.incrementAndGet(); //  If it isn't, continue incrementing
            });
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(targetUser.getName() + "'s Information");
        embed.setColor(Global.CUSTOMPURPLE);
        embed.addField("Current Level: ", userLevelString, false);
        embed.addField("XP Required For Next Level: ", xpRequiredForNextLevelString, false);
        embed.addField("Current Rank: ", "#" + rank.get(), false);
        embed.addField("Joined Server At: ", event.getOption("user").getAsMember().getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), false);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
