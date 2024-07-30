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
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
        EmbedBuilder baseEmbed = new EmbedBuilder();
        EmbedBuilder LogEmbed = new EmbedBuilder();
        Global.BuildLogModEmbed("User Kicked Event", targetUser.getName(), event.getUser().getName(), reason, LogEmbed);

        // Check if target user is the bot itself (avoid self-kicking)
        if (targetUser.getId().equals(event.getJDA().getSelfUser().getId())) {
            baseEmbed.setDescription("Hey! You can't kick me. 🔴");
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        if(targetUser.getId().equals(event.getUser().getId())){
            baseEmbed.setDescription("Hey! You can't kick yourself. 🔴");
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        baseEmbed.setDescription("Successfully kicked" + targetUser.getName());

        // Attempt to kick the user
        event.getGuild().kick(targetUser).queue(
            (unused) -> event.getHook().sendMessageEmbeds(baseEmbed.build()).queue(),
            (error) -> event.getHook().sendMessage("Failed to kick user: " + error.getMessage()).queue()
        );

        logsChannel.sendMessageEmbeds(LogEmbed.build()).queue();
    }
}
