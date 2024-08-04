package com.mycompany.app.Listeners;

//  JAVA IMPORTS
import java.util.List;

import com.mycompany.app.Global;

//  JDA API IMPORTS
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        //  Update the total member count (Look at ReadyListener)
        Global.memberCount++;
        //  Update the presence
        event.getJDA().getPresence().setActivity(Activity.watching("Over " + Global.memberCount + " Members"));

        TextChannel welcomeChannel = event.getJDA().getTextChannelById(Global.welcomeChannelId);
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
        EmbedBuilder embed = new EmbedBuilder();

        if(welcomeChannel != null){
            //  Send a message in the welcome channel
            Global.BuildSimpleDescriptionEmbed(
                "Welcome, " + event.getUser().getAsMention() + " to " + event.getGuild().getName() + "!",
                Global.CUSTOMPURPLE,
                embed);
            welcomeChannel.sendMessageEmbeds(embed.build());
        }

        if(logsChannel != null){

            //  Send an embed in the logs channel
            Global.SendMemberLogEmbed(
                "Guild Member Join Event",
                Global.CUSTOMGREEN,
                event.getUser().getName(), 
                logsChannel);
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event){
        //  Update the total member count (Look at ReadyListener)
        Global.memberCount--;
        //  Update the presence
        event.getJDA().getPresence().setActivity(Activity.watching("Over " + Global.memberCount + " Members"));

        //  Get TextChannel using the channel id
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        if(logsChannel != null){

            //  Send an embed in the logs channel
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
            TextChannel boosterChannel = event.getJDA().getTextChannelById(Global.boosterChannelId);

           
            if(logsChannel != null){
                //  Send an embed in the logs channel
                Global.SendRoleLogEmbed(
                    "Guild Member Role Remove Event",
                    Global.CUSTOMRED,
                    event.getUser().getName(), 
                    role.getAsMention(),
                    logsChannel);
            }

            if(role.getId().equals(Global.boosterRoleId)){
                if(boosterChannel != null){
                    boosterChannel.sendMessage("BOO! RIDICULE THIS MAN FOR NOT BOOSTING").queue();
                }
            }
        }
    }

    @Override
    public void onUserUpdateActivities(UserUpdateActivitiesEvent event) {
        //  A for loop, for going through all the activies. 
        //  Just like the role events
        for(Activity activity : event.getMember().getActivities()){
            //  If the activity updated is STREAMING and the userID is the streamer
            if(activity.getType() == Activity.ActivityType.STREAMING && event.getMember().getId().equals(Global.streamerUserId)){
                System.out.println("STREAMING");
            } else {
                System.out.println("NOT STREAMING");
            }
        }
    }
}
