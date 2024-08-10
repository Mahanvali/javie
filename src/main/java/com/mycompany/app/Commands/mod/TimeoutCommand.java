package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import java.time.Duration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class TimeoutCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();

        User targetUser = event.getOption("user").getAsUser();
        String durationString = event.getOption("duration").getAsString();
        String reason = event.getOption("reason").getAsString();
        Duration timeoutDuration;

        String userMention = targetUser.getAsMention();
        String moderator =  event.getUser().getName();

        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        EmbedBuilder baseEmbed = new EmbedBuilder();
        EmbedBuilder LogEmbed = new EmbedBuilder();

        // Don't allow the user to ban the bot
        if (targetUser.getId().equals(event.getJDA().getSelfUser().getId())) {
            baseEmbed.setDescription("Hey! You can't time me out. 🔴");
            baseEmbed.setColor(Global.CUSTOMRED);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        //  Don't allow the user to ban themselves
        if(targetUser.getId().equals(event.getUser().getId())){
            baseEmbed.setDescription("Hey! You can't timeout yourself. 🔴");
            baseEmbed.setColor(Global.CUSTOMRED);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }
        

        // Determine the timeout duration
        if (durationString.endsWith("d")) {
            int days = Integer.parseInt(durationString.substring(0, durationString.length() - 1));
            timeoutDuration = Duration.ofDays(days);
        } else if (durationString.endsWith("w")) {
            int weeks = Integer.parseInt(durationString.substring(0, durationString.length() - 1));
            timeoutDuration = Duration.ofDays(weeks * 7);
        } else if (durationString.endsWith("h")) {
            int hours = Integer.parseInt(durationString.substring(0, durationString.length() - 1));
            timeoutDuration = Duration.ofHours(hours);
        } else if (durationString.endsWith("m")) {
            int minutes = Integer.parseInt(durationString.substring(0, durationString.length() - 1));
            timeoutDuration = Duration.ofMinutes(minutes);
        } else if (durationString.endsWith("s")) {
            int seconds = Integer.parseInt(durationString.substring(0, durationString.length() - 1));
            timeoutDuration = Duration.ofSeconds(seconds);
        } else {
            // Default to seconds if no unit is specified
            int seconds = Integer.parseInt(durationString);
            timeoutDuration = Duration.ofSeconds(seconds);
            durationString = seconds + "s"; //  Change durationString for logging purposes
        }

        // Ensure timeout duration does not exceed 28 days
        if (timeoutDuration.compareTo(Duration.ofDays(28)) > 0) {
            timeoutDuration = Duration.ofDays(28);
            durationString = "28d"; //  Change durationString for logging purposes
        }

        Global.BuildLogModEmbed("User Timeout Event", userMention, moderator, reason, LogEmbed);
        LogEmbed.addField("Duration:", durationString, false);

        //  Attempt to ban the user
        event.getGuild().timeoutFor(targetUser, timeoutDuration).reason(reason).queue(
            (unused) -> {
                baseEmbed.setDescription("<:yukariBONK:1270513141771407563> Successfully timed out: " + userMention);
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();

                logsChannel.sendMessageEmbeds(LogEmbed.build()).queue();
            },

            (error) -> {
                baseEmbed.setDescription("Failed to time out " + userMention + " \n ```" + error.getMessage() + "```");
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            }
        );
    }
}
