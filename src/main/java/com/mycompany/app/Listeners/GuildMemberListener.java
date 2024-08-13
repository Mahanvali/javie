package com.mycompany.app.Listeners;

//  JAVA IMPORTS
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.mycompany.app.Global;

//  JDA API IMPORTS
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.EmbedBuilder;
// import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
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
        String userMention = event.getMember().getAsMention();

        if(welcomeChannel != null){
            //  Send a message in the welcome channel
            // Global.BuildSimpleDescriptionEmbed(
            //     "Welcome, " + event.getUser().getAsMention() + " to " + event.getGuild().getName() + "! <:yukariWAVE:1270512883834294292>",
            //     Global.CUSTOMPURPLE,
            //     embed);
            welcomeChannel.sendMessage("Welcome, " + userMention + " to " + event.getGuild().getName() + "! <:yukariWAVE:1270512883834294292>").queue();
        }

        if(logsChannel != null){

            Global.BuildMemberLogEmbed(
                "Guild Member Join Event",
                Global.CUSTOMGREEN,
                userMention,
                embed);
            logsChannel.sendMessageEmbeds(embed.build()).queue();
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
        EmbedBuilder embed = new EmbedBuilder();
        String userId = event.getMember().getId();
        String userMention = event.getMember().getAsMention();

        if(logsChannel != null){
            //  Send an embed in the logs channel
            Global.BuildMemberLogEmbed(
                "Guild Member Leave/Remove Event",
                Global.CUSTOMRED,
                userMention,
                embed);
                embed.addField("User XP Removed:", LevelSystem.levelInformation.getOrDefault(userId, 0) + "", false);
                logsChannel.sendMessageEmbeds(embed.build()).queue();
        }
        LevelSystem.deleteData(userId);
    }


    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event){
        List<Role> roles = event.getRoles();
        //  A for loop, for going through all the roles
        for(Role role : roles){
            TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
            TextChannel boosterChannel = event.getJDA().getTextChannelById(Global.boosterChannelId);
            // EmbedBuilder boostEmbed = new EmbedBuilder();

            // Global.BuildSimpleDescriptionEmbed(
            //     event.getUser().getAsMention() + " Thank you for boosting! <:yukariBASED:1270513258645819433> ", 
            //     Global.CUSTOMPURPLE, boostEmbed
            // );

            String roleMention = role.getAsMention();
            String userMention = event.getMember().getAsMention();

            if(logsChannel != null){
                //  Send an embed in the logs channel
                Global.SendRoleLogEmbed(
                    "Guild Member Role Add Event",
                    Global.CUSTOMGREEN,
                    userMention, 
                    roleMention,
                    logsChannel);
            }

            if(role.getId().equals(Global.boosterRoleId)){
                if(boosterChannel != null){
                    boosterChannel.sendMessage(userMention + " Thank you for boosting! <:yukariBASED:1270513258645819433>").queue();
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

            String roleMention = role.getAsMention();
            String userMention = event.getMember().getAsMention();

           
            if(logsChannel != null){
                //  Send an embed in the logs channel
                Global.SendRoleLogEmbed(
                    "Guild Member Role Remove Event",
                    Global.CUSTOMRED,
                    userMention, 
                    roleMention,
                    logsChannel);
            }

            if(role.getId().equals(Global.boosterRoleId)){
                if(boosterChannel != null){
                    boosterChannel.sendMessage("BOO! RIDICULE THIS PERSON FOR NO LONGER BOOSTING " + event.getUser().getAsMention()).queue();
                }
            }
        }
    }

    public static class NicknameData {
        String userid;
        String date;
        String oldnickname;

        public NicknameData(String userid, String date, String oldnickname) {
            this.userid = userid;
            this.date = date;
            this.oldnickname = oldnickname;
        }

        //  Public getters
        public String getUserId(){
            return userid;
        }
        public String getDate(){
            return date;
        }
        public String getOldNickname(){
            return oldnickname;
        }
    }

    public static final Map<String, NicknameData> nicknameCache = new HashMap<>();

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event){
        String userID = event.getUser().getId();
        String date = Global.formattedDate;
        String oldNickname = event.getOldNickname();
        String newNickname = event.getNewNickname();
        nicknameCache.put(newNickname, new NicknameData(userID, date, oldNickname));
    }
    
    @Override
    public void onUserActivityStart(UserActivityStartEvent event){
        for(Activity activity : event.getMember().getActivities()){
            if(activity.getType() == ActivityType.STREAMING && event.getMember().getId().equals(Global.streamerUserId)){
                System.out.println("STREAMING" + activity.asRichPresence().getDetails() + activity.getUrl());
            }
        }
    }
}
