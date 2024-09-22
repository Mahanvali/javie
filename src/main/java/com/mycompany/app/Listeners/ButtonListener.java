package com.mycompany.app.Listeners;

import com.mycompany.app.Global;

import java.util.EnumSet;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ButtonListener extends ListenerAdapter {
    public static TextChannel createdChannel;
    public void onButtonInteraction(ButtonInteractionEvent event) {
        EmbedBuilder ticketEmbed = new EmbedBuilder();
        String userName = event.getUser().getEffectiveName();
        if(event.getComponentId().equals("create-ticket")){
            Category ticketCategory = event.getGuild().getCategoryById(Global.helpdeckCategoryId);
            EnumSet<Permission> allowed = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
            EnumSet<Permission> denied = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
            if(event.getGuild().getTextChannelsByName("channel-" + userName, false).isEmpty()){
                event.getGuild().createTextChannel("channel-" + userName)
                .addMemberPermissionOverride(event.getUser().getIdLong(), allowed, null)
                .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, denied)
                .setParent(ticketCategory).queue(
                    (unused) -> {
                        int createdChannelSize = event.getGuild().getTextChannelsByName("channel-" + userName, false).size();
                        createdChannel = event.getGuild().getTextChannelsByName("channel-" + userName, false).get(createdChannelSize - 1);
                        ticketEmbed.setDescription("Thank you for contacting support.\nPlease describe your issue and await a response.");
                        ticketEmbed.setTitle("Ticket Support");
                        ticketEmbed.setColor(Global.CUSTOMPURPLE);
                        ticketEmbed.setFooter("Powered by the GOAT (me)", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        createdChannel.sendMessageEmbeds(ticketEmbed.build()).addActionRow(
                            Button.danger("close-ticket", Emoji.fromFormatted(Global.yukariNANI) + " Close Ticket")
                        ).queue();
                    }
                );
            } else {
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariPOLICE + " You already have a ticket open!",
                    Global.CUSTOMRED,
                    ticketEmbed);
                ticketEmbed.setFooter("If you believe this is an issue, please report it.");
                ticketEmbed.setTitle("An error occured creating your ticket");
                event.getJDA().getUserById(event.getUser().getId()).openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(ticketEmbed.build()))
                .queue();
            }
        }
        if(event.getComponentId().equals("close-ticket")){
            event.getChannel().delete().queue();
            EmbedBuilder logEmbed = new EmbedBuilder();
            TextChannel logChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
            logEmbed.setTitle("Ticket Close Event");
            logEmbed.addField("User:", event.getMember().getAsMention(), false);
            logEmbed.addField("Ticket:", createdChannel.toString(), false);
            logEmbed.addField("Date:", Global.formattedDate, false);
            logChannel.sendMessageEmbeds(logEmbed.build()).queue();
        }
    }
}
