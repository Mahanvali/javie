package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BanCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();

        User targetUser = event.getOption("user").getAsUser();
        String reason = event.getOption("reason").getAsString();

        String userMention = targetUser.getAsMention();
        String moderator =  event.getUser().getName();

        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        EmbedBuilder baseEmbed = new EmbedBuilder();
        EmbedBuilder LogEmbed = new EmbedBuilder();
        Global.BuildLogModEmbed("User Ban Event", userMention, moderator, reason, LogEmbed);

        // Don't allow the user to ban the bot
        if (targetUser.getId().equals(event.getJDA().getSelfUser().getId())) {
            baseEmbed.setDescription("Hey! You can't ban me. 🔴");
            baseEmbed.setColor(Global.CUSTOMRED);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        //  Don't allow the user to ban themselves
        if(targetUser.getId().equals(event.getUser().getId())){
            baseEmbed.setDescription("Hey! You can't ban yourself. 🔴");
            baseEmbed.setColor(Global.CUSTOMRED);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        //  Attempt to ban the user
        event.getGuild().ban(targetUser, 7, TimeUnit.DAYS).reason(reason).queue(
            (unused) -> {
                baseEmbed.setDescription("Successfully banned: " + userMention);
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();

                logsChannel.sendMessageEmbeds(LogEmbed.build()).queue();
            },

            (error) -> {
                baseEmbed.setDescription("Failed to ban " + userMention + " \n ```" + error.getMessage() + "```");
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            }
        );
    }
}
