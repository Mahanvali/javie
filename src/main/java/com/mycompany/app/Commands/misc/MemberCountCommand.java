package com.mycompany.app.Commands.misc;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MemberCountCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        EmbedBuilder embed = new EmbedBuilder();
        Global.BuildSimpleDescriptionEmbed(
            "**" + event.getGuild().getName() + "** is currently at `" + Global.memberCount + "` members",
            Global.CUSTOMPURPLE,
            embed);
            
        if(event.getChannel().asTextChannel().getId().equals(Global.botCommandsChannelId)){
                event.replyEmbeds(embed.build()).queue();
            } else {
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
            }
    }
}
