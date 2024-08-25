package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class KickCommand implements CommandImplementation {
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
        Global.BuildLogModEmbed("User Kick Event", userMention, moderator, reason, LogEmbed);

        // Don't allow the user to ban the bot
        if (targetUser.getId().equals(event.getJDA().getSelfUser().getId())) {
            baseEmbed.setDescription("Hey! You can't kick me. 🔴");
            baseEmbed.setColor(Global.CUSTOMPURPLE);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        //  Don't allow the user to ban themselves
        if(targetUser.getId().equals(event.getUser().getId())){
            baseEmbed.setDescription("Hey! You can't kick yourself. 🔴");
            baseEmbed.setColor(Global.CUSTOMPURPLE);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        // Attempt to kick the user
        event.getGuild().kick(targetUser).queue(
            (unused) -> {
                baseEmbed.setDescription(Global.yukariBONK + " Get out " + userMention + "!");
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                logsChannel.sendMessageEmbeds(LogEmbed.build()).queue();
            },

            (error) -> {
                baseEmbed.setDescription("Failed to kick " + userMention + " \n ```" + error.getMessage() + "```");
                baseEmbed.setColor(Global.CUSTOMPURPLE);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            }
        );
    }
}
