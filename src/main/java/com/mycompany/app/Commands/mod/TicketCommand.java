package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TicketCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        EmbedBuilder ticketEmbed = new EmbedBuilder();
        
        if(!event.getUser().getId().equals(Global.botdeveloperUserId)){
            Global.BuildInvalidPermissionsEmbed("BOT DEVELOPER", Global.CUSTOMRED, ticketEmbed);
            event.replyEmbeds(ticketEmbed.build()).setEphemeral(true).queue();
            return;

        }
        ticketEmbed.setDescription(Global.yukariNOTED + " Click the button below to make a ticket\n");
        ticketEmbed.setColor(Global.CUSTOMPURPLE);
        event.replyEmbeds(ticketEmbed.build()).setActionRow(
            Button.success("create-ticket", Emoji.fromFormatted(Global.yukariSEARCH) + " Open Ticket")
        ).queue();

    }
}
