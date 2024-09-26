package com.mycompany.app;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class Global {

    private static final LocalDateTime time = LocalDateTime.now();   //  Get the current date
    private static final DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd-MM-yyyy");  //  Format the date
    private static final DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("dd-HH-mm-ss");  // Format the time
    //  CHANNELS
    public static final String logsChannelId = "1270503164788998240";
    public static final String introChannelId = "1270503162893041714";
    public static final String appealChannelId = "1277259004795424778";
    public static final String welcomeChannelId = "1270503163258077244";
    public static final String boosterChannelId = "1270503163258077244";
    public static final String botCommandsChannelId = "1270503164289744940";
    public static final String staffCommandsChannelId = "1270503164788998239";
    public static final String verificationMessageId = "1270514127810330715";
    public static final String helpdeckCategoryId = "1270503162410700815";
    public static final List<String> noLevelMessageChannels = Arrays.asList("1270503164788998239", "1270503164289744940", "1270503162893041711", "1270503162893041712", "1270503163799015440", "1270533714983518319", "1270525556584349798");
    public static final List<String> noLevelVoiceChannels = Arrays.asList("1270503164289744937", "1270503164289744938");
    //  ROLES
    public static final String boosterRoleId = "1270511189620686850";
    public static final String verificationRoleId = "1270503160250630267";
    public static final String introducedRoleId = "1286984267502714890";
    public static final String level1RoleId = "1270503160225468541";
    public static final String level10RoleId = "1270503160225468542";
    public static final String level20RoleId = "1270503160225468543";
    public static final String level30RoleId = "1270503160225468544";
    public static final String level40RoleId = "1270503160225468545";
    public static final String level50RoleId = "1270503160225468546";
    //  USERS
    public static final String botdeveloperUserId = "764834445722386432";
    public static final String streamerUserId = "403268481338048514";
    //  MISC.
    public static final String formattedDate = time.format(dateformat);
    public static final String formattedTime = time.format(timeformat);
    public static int memberCount = 0;
    public static int basicXPGain = 5;
    public static int boosterXPGain = basicXPGain + 1;
    public static long messageCooldown = 3000;
    public static long voiceCooldown = 3000000;
    public static final Color CUSTOMRED = new Color(168, 52, 50);
    public static final Color CUSTOMGREEN = new Color(50, 168, 81);
    public static final Color CUSTOMPURPLE = new Color(148, 76, 176);
    public static final Color CUSTOMORANGE = new Color(245, 185, 66);
    // Emojis
    public static final String yukariNOTED = "<:yukariNOTED:1270513272780619799>";
    public static final String yukariHEART = "<:yukariHEART:1270513107621511240>";
    public static final String yukari4K = ":<:yukari4K:1270513158771183726>";
    public static final String yukariBONK = "<:yukariBONK:1270513141771407563>";
    public static final String yukariPOLICE = "<:yukariPOLICE:1270513511113424976>";
    public static final String yukariSEARCH = "<:yukariSEARCH:1270513409875640360>";
    public static final String yukariYES = "<:yukariYES:1270513445887934474> ";
    public static final String yukariSMH = "<:yukariSMH:1270513031129989140>";
    public static final String yukariWAVE = "<:yukariWAVE:1270512883834294292>";
    public static final String yukariNANI = "<:yukariNANI:1270513124281286696>";
    public static final String yukariOHOH = "<:yukariOHOHO:1270512933595643935>";
    public static final String yukariEVIL = "<:yukariEVIL:1270513475264843817>";
    public static final String yukariFU = "<:yukariFU:1270513376463949844>";
    //  EMBEDS
    public static void SendRoleLogEmbed(String embedTitle, Color embedColor, String guildMember, String roleMention, TextChannel channel){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(embedTitle);
        embed.setColor(embedColor);
        embed.addField("Guild Member:", guildMember, false);
        embed.addField("Role:", roleMention, false);
        embed.addField("Date:", Global.formattedDate, false);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static void BuildMemberLogEmbed(String embedTitle, Color embedColor, String guildMember, EmbedBuilder embed){
        embed.setTitle(embedTitle);
        embed.setColor(embedColor);
        embed.addField("Guild Member:", guildMember, false);
        embed.addField("Date:", Global.formattedDate, false);
    }

    public static void SendDeletedMessageEmbed(String embedTitle, String embedAuthor, String DeletedMessage, TextChannel channel){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(embedTitle);
        embed.setColor(Global.CUSTOMRED);
        embed.addField("Author:", embedAuthor, false);
        embed.addField("Deleted Message:", DeletedMessage, false);
        embed.addField("Date:", Global.formattedDate, false);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static void SendUpdatedMessageEmbed(String embedTitle, String embedAuthor, String OldMessage, String NewMessage, TextChannel channel){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Updated Message Event");
        embed.setColor(Global.CUSTOMORANGE);
        embed.addField("Author:", embedAuthor, false);
        embed.addField("Old Message:", OldMessage, false);
        embed.addField("New Message", NewMessage, false);
        embed.addField("Date:", Global.formattedDate, false);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static void BuildLogModEmbed(String embedTitle, String AffectedUser, String ResponsibleMod, String Reason, EmbedBuilder embed){
        embed.setTitle(embedTitle);
        embed.setColor(Global.CUSTOMRED);
        embed.addField("Affected User:", AffectedUser, false);
        embed.addField("Responsible Mod:", ResponsibleMod, false);
        embed.addField("Reason:", Reason, false);
    }

    public static void BuildSimpleDescriptionEmbed(String embedDescription, Color embedColor, EmbedBuilder embed){
        embed.setColor(embedColor);
        embed.setDescription(embedDescription);
    }

    public static void BuildInvalidPermissionsEmbed(String PermissionRequired, Color embedColor, EmbedBuilder embed){
        embed.setColor(embedColor);
        embed.setDescription(yukariPOLICE + " Sorry, you can't run this command!\n> Permissions Required: " + PermissionRequired);
    }

    public static void PrivateMessageDeveloper(JDA jda, EmbedBuilder embed){
        
        jda.getUserById(Global.botdeveloperUserId).openPrivateChannel()
            .flatMap(channel -> channel.sendMessageEmbeds(embed.build()))
            .queue();
    }
}
