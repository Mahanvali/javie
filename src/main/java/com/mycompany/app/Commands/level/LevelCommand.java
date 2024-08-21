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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

//  Get all the cache data from a user
public class LevelCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        User targetUser = event.getOption("user").getAsUser();
        Member targetMember = event.getOption("user").getAsMember();
        String targetUserId = targetUser.getId();
        int userLevel = LevelSystem.getLevel(targetUserId);
        int userXp = LevelSystem.levelInformation.getOrDefault(targetUserId, 0);
        int xpForNextLevel = LevelSystem.getXpForNextLevel(userLevel);
        int xpRequiredForNextLevel = xpForNextLevel - userXp;
        String userLevelString = Integer.toString(userLevel);
        String userJoinDate = event.getOption("user").getAsMember().getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        Role level1Role = event.getJDA().getRoleById(Global.level1RoleId);
        Role level10Role = event.getJDA().getRoleById(Global.level10RoleId);
        Role level20Role = event.getJDA().getRoleById(Global.level20RoleId);
        Role level30Role = event.getJDA().getRoleById(Global.level30RoleId);
        Role level40Role = event.getJDA().getRoleById(Global.level40RoleId);
        Role level50Role = event.getJDA().getRoleById(Global.level50RoleId);

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

        embed.setColor(Global.CUSTOMPURPLE);
        embed.setAuthor(targetUser.getName(), null, targetUser.getAvatarUrl());
        embed.addField("Current Level: ", userLevelString, false);
        embed.addField("Current Rank: ", "#" + rank, false);
        embed.addField("Current XP: ", userXp + "xp", false);
        embed.addField("XP Required For Next Level: ", xpRequiredForNextLevel + "xp", false);
        embed.addField("Joined Server At: ", userJoinDate, false);
        if(targetMember.getRoles().contains(level1Role)){
            embed.addField("Level Perks (Newcomer): ", "- Voice Channel Access\n- Image Permissions", false);
        }
        if(targetMember.getRoles().contains(level10Role)){
            embed.addField("Level Perks (5th-Class Merc): ", "- Stream in VC\n- Change Nickname\n- Use Activities in VC ", false);
        }
        if(targetMember.getRoles().contains(level20Role)){
            embed.addField("Level Perks (4th-Class Merc): ", "- Soundboard Access\n- Change Voice Activity", false);
        }
        if(targetMember.getRoles().contains(level30Role)){
            embed.addField("Level Perks (3rd-Class Merc): ", "- Access to <#1270533714983518319>\n- GIF Permissions in <#1270503163258077244>", false);
        }
        if(targetMember.getRoles().contains(level40Role)){
            embed.addField("Level Perks (2nd-Class Merc): ", "- Create events", false);
        }
        if(targetMember.getRoles().contains(level50Role)){
            embed.addField("Level Perks (1st-Class Merc): ", "- Image Permissions in <#1270503163258077244>", false);
        }

        if(event.getChannel().asTextChannel().getId().equals(Global.botCommandsChannelId)){
            event.replyEmbeds(embed.build()).queue();
        } else {
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
