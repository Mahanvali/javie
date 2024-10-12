package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BanCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();

        User targetUser = event.getOption("user").getAsUser();
        Member targetMember = event.getOption("user").getAsMember();
        String reason = event.getOption("reason").getAsString();

        String userMention = targetUser.getAsMention();
        String userId = targetUser.getId();
        String userUsername = targetUser.getName();
        String moderator =  event.getUser().getAsMention();

        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        EmbedBuilder baseEmbed = new EmbedBuilder();
        EmbedBuilder LogEmbed = new EmbedBuilder();
        Global.BuildLogModEmbed("User Ban Event", userMention, userId, userUsername, moderator, reason, LogEmbed);

        // Don't allow the user to ban the bot
        if (targetUser.getId().equals(event.getJDA().getSelfUser().getId())) {
            Global.BuildSimpleDescriptionEmbed(
                Global.yukariPOLICE + " Hey! You can't bonk me.",
                Global.CUSTOMRED,
                baseEmbed);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        //  Don't allow the user to ban themselves
        if(targetUser.getId().equals(event.getUser().getId())){
            Global.BuildSimpleDescriptionEmbed(
                Global.yukariSMH + " Are you okay, why are you trying to ban yourself?",
                Global.CUSTOMRED,
                baseEmbed);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        //  Don't allow the user to ban moderators
        if(targetMember.getPermissions().contains(Permission.KICK_MEMBERS)){
            Global.BuildSimpleDescriptionEmbed(
                Global.yukariSMH + " Oops? I can't bonk a moderator.",
                Global.CUSTOMRED,
                baseEmbed);
            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            return;
        }

        //  Attempt to ban the user
        event.getGuild().ban(targetUser, 7, TimeUnit.DAYS).reason(reason).queue(
            (unused) -> {
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariBONK + " " + userMention + " no more cocktails for you.",
                    Global.CUSTOMPURPLE,
                    baseEmbed);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                logsChannel.sendMessageEmbeds(LogEmbed.build()).queue();
            },

            (error) -> {
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariSMH + " Failed to bonk " + userMention + " \n ```" + error.getMessage() + "```",
                    Global.CUSTOMPURPLE,
                    baseEmbed);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            }
        );
    }
}
