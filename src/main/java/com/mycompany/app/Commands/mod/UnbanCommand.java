package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class UnbanCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();

        User targetUser = event.getOption("user").getAsUser();
        String reason = event.getOption("reason").getAsString();

        String userMention = targetUser.getAsMention();
        String moderator =  event.getUser().getAsMention();

        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        EmbedBuilder baseEmbed = new EmbedBuilder();
        EmbedBuilder LogEmbed = new EmbedBuilder();
        Global.BuildLogModEmbed("User Unban Event", userMention, moderator, reason, LogEmbed);

        // Check if the user is in the ban list
        event.getGuild().retrieveBanList().queue(bans -> {
            boolean isBanned = bans.stream().anyMatch(ban -> ban.getUser().equals(targetUser));

            if (isBanned) {
                // Attempt to unban the user
                event.getGuild().unban(targetUser).queue(
                    (unused) -> {
                        baseEmbed.setDescription("<:yukariRAVE:1270512974465073164> unbanned " + userMention);
                        baseEmbed.setColor(Global.CUSTOMPURPLE);
                        event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                        logsChannel.sendMessageEmbeds(LogEmbed.build()).queue();
                    },
                    (error) -> {
                        baseEmbed.setDescription("Failed to unban " + userMention + " \n ```" + error.getMessage() + "```");
                        baseEmbed.setColor(Global.CUSTOMPURPLE);
                        event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                    }
                );
                
            } else {
                baseEmbed.setDescription("Failed to unban " + userMention + ": User is not banned <:yukariEHH:1270513090332459101>");
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            }
        }, error -> {
            baseEmbed.setDescription("Failed to retrieve ban list" + " \n ```" + error.getMessage() + "```");
            baseEmbed.setColor(Global.CUSTOMPURPLE);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
        });
    }
}
