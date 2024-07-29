package com.mycompany.app;

//  TODO: WHEN A STREAMER'S ACTIVITY IS "STREAMING", SEND A MESSAGE TO THAT STREAM
// https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/entities/Activity.html

//  JAVA IMPORTS
import java.util.List;

//  JDA API IMPORTS
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        //  Get TextChannel using the channel id
        TextChannel welcomeChannel = event.getJDA().getTextChannelById(Global.welcomeChannelId);
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        if(welcomeChannel != null){
            //  Send a message in the welcome channel
            EmbedBuilder welcomeEmbed = new EmbedBuilder();
            welcomeEmbed.setDescription("Welcome, " + event.getUser().getAsMention() + " to " + event.getGuild().getName() + "!");
            welcomeEmbed.setColor(Global.CUSTOMPURPLE);
            welcomeChannel.sendMessageEmbeds(welcomeEmbed.build()).queue();
        }

        if(logsChannel != null){

            //  Send an embed in the logs channel
            Global.SendMemberLogEmbed(
                "Guild Member Join Event",
                Global.CUSTOMRED,
                event.getUser().getName(), 
                logsChannel);
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event){

        //  Get TextChannel using the channel id
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        //  Send an embed in the logs channel
        if(logsChannel != null){
            Global.SendMemberLogEmbed(
                "Guild Member Remove Event",
                Global.CUSTOMRED,
                event.getUser().getName(), 
                logsChannel);
        }
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event){
        List<Role> roles = event.getRoles();
        //  A for loop, for going through all the roles
        for(Role role : roles){

            TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
            TextChannel boosterChannel = event.getJDA().getTextChannelById(Global.boosterChannelId);

            if(logsChannel != null){
                //  Send an embed in the logs channel
                Global.SendRoleLogEmbed(
                    "Guild Member Role Add Event",
                    Global.CUSTOMGREEN,
                    event.getUser().getName(), 
                    role.getAsMention(),
                    logsChannel);
            }

            if(role.getId().equals(Global.boosterRoleId)){
                if(boosterChannel != null){
                    boosterChannel.sendMessage("INSERT BOOSTER MESSAGE HERE").queue();
                }
            }
        }
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event){
        List<Role> roles = event.getRoles();
        //  A for loop, for going through all the roles
        for(Role role : roles){

            TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

            //  Send an embed in the logs channel
            if(logsChannel != null){
                Global.SendRoleLogEmbed(
                    "Guild Member Role Remove Event",
                    Global.CUSTOMRED,
                    event.getUser().getName(), 
                    role.getAsMention(),
                    logsChannel);
            }
        }
    }
}
