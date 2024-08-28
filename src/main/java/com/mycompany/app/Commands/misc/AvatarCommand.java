package com.mycompany.app.Commands.misc;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class AvatarCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        Member targetUser = event.getOption("user").getAsMember();
        String avatarUrl = targetUser.getUser().getAvatarUrl() + "?size=4096";
        EmbedBuilder embed = new EmbedBuilder();
        if(targetUser.getId().equals(event.getJDA().getSelfUser().getId())){
            embed.setDescription("Why are you trying to look at my avatar, weirdo" + Global.yukari4K);
            embed.setColor(Global.CUSTOMPURPLE);
            event.replyEmbeds(embed.build()).queue();
            return;
        }
        embed.setTitle(targetUser.getUser().getName() + "'s Avatar");
        embed.setAuthor(event.getUser().getName(), null, event.getUser().getAvatarUrl());
        embed.setImage(avatarUrl);
        embed.setColor(Global.CUSTOMPURPLE);
        embed.setFooter("Why are you looking at other people's avatar, weirdo.");
        
        if(event.getChannel().asTextChannel().getId().equals(Global.botCommandsChannelId)){
            event.replyEmbeds(embed.build()).queue();
        } else {
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
