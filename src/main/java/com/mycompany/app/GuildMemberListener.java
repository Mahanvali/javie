package com.mycompany.app;


//  JAVA IMPORTS
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    LocalDateTime time = LocalDateTime.now();   //  Get the current date
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");  //  Format the date
    String formattedTime = time.format(format); //  Apply the format

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        //  Get TextChannel using the channel id
        TextChannel welcomeChannel = event.getJDA().getTextChannelById(Global.welcomeChannelId);
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        if(welcomeChannel != null){
            //  Send a message in the welcome channel
            welcomeChannel.sendMessage("Welcome, " + event.getUser().getAsMention() + " to " + event.getGuild().getName()).queue();
        }

        if(logsChannel != null){
            //  Send an embed in the logs channel
            EmbedBuilder logsEmbed = new EmbedBuilder();
            logsEmbed.setTitle("Guild Member Join Event");
            logsEmbed.setColor(Global.CUSTOMGREEN);
            logsEmbed.addField("Guild Member:", event.getUser().getName(), false);
            logsEmbed.addField("Date:", formattedTime, false);
            logsChannel.sendMessageEmbeds(logsEmbed.build()).queue();
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event){
        //  Get TextChannel using the channel id
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
        
        if(logsChannel != null){
             //  Send an embed in the logs channel
            EmbedBuilder logsEmbed = new EmbedBuilder();
            logsEmbed.setTitle("Guild Member Remove Event");
            logsEmbed.setColor(Global.CUSTOMRED);
            logsEmbed.addField("Guild Member:", event.getUser().getName(), false);
            logsEmbed.addField("Date:", formattedTime, false);
            logsChannel.sendMessageEmbeds(logsEmbed.build()).queue();
        }
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event){
        List<Role> roles = event.getRoles();
        //  A for loop, for going through all the roles
        for(Role role : roles){

            TextChannel logChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
            TextChannel boosterChannel = event.getJDA().getTextChannelById(Global.boosterChannelId);

            if(logChannel != null){
                EmbedBuilder logsEmbed = new EmbedBuilder();
                logsEmbed.setTitle("Guild Member Role Add Event");
                logsEmbed.setColor(Global.CUSTOMGREEN);
                logsEmbed.addField("Guild Member:", event.getUser().getName(), false);
                logsEmbed.addField("Role:", role.getAsMention(), false);
                logsEmbed.addField("Date:", formattedTime, false);
                logChannel.sendMessageEmbeds(logsEmbed.build()).queue();
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

            TextChannel logChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

            if(logChannel != null){
                EmbedBuilder logsEmbed = new EmbedBuilder();
                logsEmbed.setTitle("Guild Member Role Remove Event");
                logsEmbed.setColor(Global.CUSTOMRED);
                logsEmbed.addField("Guild Member:", event.getUser().getName(), false);
                logsEmbed.addField("Role:", role.getAsMention(), false);
                logsEmbed.addField("Date:", formattedTime, false);
                logChannel.sendMessageEmbeds(logsEmbed.build()).queue();
            }
        }
    }
}
