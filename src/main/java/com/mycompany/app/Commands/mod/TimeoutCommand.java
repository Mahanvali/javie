package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import java.time.Duration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class TimeoutCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();

        User targetUser = event.getOption("user").getAsUser();
        Member targetMember = event.getOption("user").getAsMember();
        String reason = event.getOption("reason").getAsString();

        String userMention = targetUser.getAsMention();
        String moderator =  event.getUser().getAsMention();
        boolean isTimedOut = targetMember.isTimedOut();

        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        EmbedBuilder baseEmbed = new EmbedBuilder();
        EmbedBuilder LogEmbed = new EmbedBuilder();

        // Don't allow the user to timeout the bot
        if (targetUser.getId().equals(event.getJDA().getSelfUser().getId())) {
            baseEmbed.setDescription("Hey! You can't perform this action on me. 🔴");
            baseEmbed.setColor(Global.CUSTOMRED);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        //  Don't allow the user to timeout themselves
        if(targetUser.getId().equals(event.getUser().getId())){
            baseEmbed.setDescription("Hey! You can't perform this action on yourself. 🔴");
            baseEmbed.setColor(Global.CUSTOMRED);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        if(event.getSubcommandName().equals("remove")){
            if(isTimedOut){
                Global.BuildLogModEmbed("User Timeout Remove Event", userMention, moderator, reason, LogEmbed);
                event.getGuild().removeTimeout(targetUser).reason(reason).queue(
                    (unused) -> {
                        baseEmbed.setDescription("<:yukariYES:1270513445887934474> Successfully removed timeout for: " + userMention);
                        baseEmbed.setColor(Global.CUSTOMPURPLE);
                        event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();

                        logsChannel.sendMessageEmbeds(LogEmbed.build()).queue();
                    },

                    (error) -> {
                        baseEmbed.setDescription("Failed to remove timeout for " + userMention + " \n ```" + error.getMessage() + "```");
                        baseEmbed.setColor(Global.CUSTOMPURPLE);
                        event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                    }
                );
            } else {
                baseEmbed.setDescription("Failed to remove timeout for " + userMention + ": User is not timed out! <:yukariEHH:1270513090332459101>");
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            }
        }
        
        if(event.getSubcommandName().equals("add")){
            if(!isTimedOut){
                Duration timeoutDuration;
                String durationString = event.getOption("duration").getAsString();
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

                event.getGuild().timeoutFor(targetUser, timeoutDuration).reason(reason).queue(
                    (unused) -> {
                        baseEmbed.setDescription("<:yukariBONK:1270513141771407563> Successfully timed out: " + userMention);
                        baseEmbed.setColor(Global.CUSTOMPURPLE);
                        event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();

                        logsChannel.sendMessageEmbeds(LogEmbed.build()).queue();
                    },

                    (error) -> {
                        baseEmbed.setDescription("Failed to timeout " + userMention + " \n ```" + error.getMessage() + "```");
                        baseEmbed.setColor(Global.CUSTOMPURPLE);
                        event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                    }
                );
            } else {
                baseEmbed.setDescription("Failed to timeout " + userMention + ": User is already timed out! <:yukariEHH:1270513090332459101>");
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            }
        }  
    }
}
