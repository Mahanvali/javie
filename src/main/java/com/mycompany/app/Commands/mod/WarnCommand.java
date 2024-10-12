package com.mycompany.app.Commands.mod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class WarnCommand implements CommandImplementation {
    public static final Map<String, Integer> warnInformation = new HashMap<>();
    private static final String FILE_PATH = "src/main/java/com/mycompany/app/warnData.txt";
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        EmbedBuilder baseEmbed = new EmbedBuilder();
        EmbedBuilder privateEmbed = new EmbedBuilder();
        EmbedBuilder logEmbed = new EmbedBuilder();
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

        User targetUser = event.getOption("user").getAsUser();
        Member targetMember = event.getOption("user").getAsMember();
        String reason = event.getOption("reason").getAsString();
        String targetUserId = targetUser.getId();
        String targetUserMention = targetUser.getAsMention();
        String targetUserName = targetUser.getName();
        String moderatorMention = event.getUser().getAsMention();
        Duration timeoutDuration;

        if(event.getSubcommandName().equals("add")){

            // Don't allow the user to warn the bot
            if (targetUser.getId().equals(event.getJDA().getSelfUser().getId())) {
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariSMH + " Why? Just why?",
                    Global.CUSTOMRED,
                    baseEmbed);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                return;
            }

            //  Don't allow the user to warn themselves
            if(targetUser.getId().equals(event.getUser().getId())){
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariSMH + " Heh, dumbass.",
                    Global.CUSTOMRED,
                    baseEmbed);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                return;
            }

            //  Don't allow the user to warn moderators
            if(targetMember.getPermissions().contains(Permission.VOICE_MUTE_OTHERS)){
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariPOLICE + " Sorry! I can't warn a moderator.",
                    Global.CUSTOMRED,
                    baseEmbed);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                return;
            }

            warnInformation.put(targetUserId, warnInformation.getOrDefault(targetUser.getId(), 0) + 1);
            saveWarnData();

            int warns = warnInformation.getOrDefault(targetUserId, 0);
            Global.BuildSimpleDescriptionEmbed(Global.yukariOHOH + " Warned " + targetUserMention + "\nuser now has **" + warns + "** warns.",
             Global.CUSTOMPURPLE, baseEmbed);
            Global.BuildLogModEmbed("User Warn Add Event", targetUserMention, targetUserId, targetUserName, moderatorMention, reason, logEmbed);

            String warningMessage = "You've been warned in " + event.getGuild().getName();
            String warningFooter = "If you believe this is a mistake, please create a ticket.";
            String warningInputs = "> **Moderator:** " + moderatorMention + "\n> **Reason:** " + reason + "\n";
            privateEmbed.setTitle(warningMessage);
            privateEmbed.setFooter(warningFooter);
            privateEmbed.setColor(Global.CUSTOMPURPLE);

            if(warns == 1 || warns == 2){
                String warningExtra = warningInputs + "\nPlease take this as a caution and behave better next time " + Global.yukariNOTED;
                privateEmbed.setDescription(warningExtra);
                targetUser.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(privateEmbed.build())).queue();
            }

            if(warns == 3){
                timeoutDuration = Duration.ofDays(1);
                event.getGuild().timeoutFor(targetUser, timeoutDuration).reason(reason).queue();
                String warningExtra =  warningInputs + "\nDue to this being your 3rd warning, we've gone ahead and temporarily timed you out. Please behave better " + Global.yukariSMH;
                privateEmbed.setDescription(warningExtra);
                targetUser.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(privateEmbed.build())).queue();
            }

            if(warns == 4){
                timeoutDuration = Duration.ofDays(14);
                event.getGuild().timeoutFor(targetUser, timeoutDuration).reason(reason).queue();
                String warningExtra = warningInputs + "\nDue to this being your 4th warning, we've gone ahead and temporarily timed you out. Please behave better, one more warn and it's a ban. " + Global.yukariSMH;
                privateEmbed.setDescription(warningExtra);
                targetUser.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(privateEmbed.build())).queue();
            }

            if(warns >= 5){
                String warningExtra = warningInputs + "\nYou've been warned 5 times, and have been banned from **" + event.getGuild().getName() + "**";
                targetUser.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage(warningExtra)).queue();
                event.getGuild().ban(targetUser, 7, TimeUnit.DAYS).reason(reason).queue();
            }

            event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            logsChannel.sendMessageEmbeds(logEmbed.build()).queue();
        }

        if(event.getSubcommandName().equals("remove")){
            warnInformation.put(targetUserId, warnInformation.getOrDefault(targetUser.getId(), 0) - 1);
            saveWarnData();
            int warns = warnInformation.getOrDefault(targetUserId, 0);

            Global.BuildSimpleDescriptionEmbed(Global.yukariYES + " Removed a warning from " + targetUserMention + ", user now has **" + warns + "** warns.",
            Global.CUSTOMPURPLE, baseEmbed);
           Global.BuildLogModEmbed("User Warn Remove Event", targetUserMention, targetUserId, targetUserName, moderatorMention, reason, logEmbed);
           event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
        }

        if(event.getSubcommandName().equals("list")){
            if(warnInformation.containsKey(targetUserId)){
                int warns = warnInformation.getOrDefault(targetUserId, 0);
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariNOTED + " " + targetUserMention + "has **" + warns + "** warns", 
                    Global.CUSTOMPURPLE, baseEmbed);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            } else {
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariNOTED + " " + targetUserMention + "has **0** warns", 
                    Global.CUSTOMPURPLE, baseEmbed);
                event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
            }
        }
    }

    public static void loadWarnData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    warnInformation.put(parts[0], Integer.parseInt(parts[1]));
                    System.out.println("Warning data loaded!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveWarnData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, Integer> entry : warnInformation.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
