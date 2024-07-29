package com.mycompany.app;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class Global {

    private static final LocalDateTime time = LocalDateTime.now();   //  Get the current date
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");  //  Format the date

    //  CHANNELS
    public static final String logsChannelId = "1113468093134557220";
    public static final String welcomeChannelId = "1113468093134557219";
    public static final String boosterChannelId = "1113468093134557219";
    //  ROLES
    public static final String boosterRoleId = "1173142323597672478";
    //  USERS
    public static final String botdeveloperUserId = "764834445722386432";
    public static final String streamerUserId = "403268481338048514";
    //  MISC.
    public static final String formattedTime = time.format(format);
    public static final Color CUSTOMRED = new Color(168, 52, 50);
    public static final Color CUSTOMGREEN = new Color(50, 168, 81);
    public static final Color CUSTOMPURPLE = new Color(148, 76, 176);
    public static final Color CUSTOMORANGE = new Color(148, 76, 176);

    //  EMBEDS
    public static void SendRoleLogEmbed(String embedTitle, Color embedColor, String guildMember, String roleMention, TextChannel channel){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(embedTitle);
        embed.setColor(embedColor);
        embed.addField("Guild Member:", guildMember, false);
        embed.addField("Role:", roleMention, false);
        embed.addField("Date:", Global.formattedTime, false);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static void SendMemberLogEmbed(String embedTitle, Color embedColor, String guildMember, TextChannel channel){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(embedTitle);
        embed.setColor(embedColor);
        embed.addField("Guild Member:", guildMember, false);
        embed.addField("Date:", Global.formattedTime, false);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static void SimpleDescriptionEmbed(String embedDescription, Color embedColor, TextChannel channel ){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(embedColor);
        embed.setDescription(embedDescription);
        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
