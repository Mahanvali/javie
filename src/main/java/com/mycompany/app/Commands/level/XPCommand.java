package com.mycompany.app.Commands.level;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.LevelSystem;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class XPCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        int rangeInput = event.getOption("amount").getAsInt();
        Member targetUser = event.getOption("user").getAsMember();
        event.deferReply().queue();
        EmbedBuilder embed = new EmbedBuilder();
        if(event.getSubcommandName().equals("add")){
            LevelSystem.levelInformation.put(targetUser.getId(), LevelSystem.levelInformation.getOrDefault(targetUser.getId(), 0) + rangeInput);
            LevelSystem.saveLevelData();
            Global.BuildSimpleDescriptionEmbed(
                Global.yukariYES + " Added **" + rangeInput + "xp** to " + targetUser.getAsMention(), 
                Global.CUSTOMPURPLE,
                embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
        if(event.getSubcommandName().equals("remove")){
            int currentXP = LevelSystem.levelInformation.getOrDefault(targetUser.getId(), 0);
            int newXP = Math.max(currentXP - rangeInput, 0); // Ensure XP does not go below 0
            LevelSystem.levelInformation.put(targetUser.getId(), newXP);
            LevelSystem.saveLevelData();
        
            Global.BuildSimpleDescriptionEmbed(
                Global.yukariYES + " Removed **" + rangeInput + "xp** from " + targetUser.getAsMention(), 
                Global.CUSTOMPURPLE,
                embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
