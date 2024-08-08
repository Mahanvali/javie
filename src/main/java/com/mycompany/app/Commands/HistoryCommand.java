package com.mycompany.app.Commands;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.GuildMemberListener;
import com.mycompany.app.Listeners.LevelSystem;
import com.mycompany.app.Listeners.GuildMemberListener.ActivityData;
import com.mycompany.app.Listeners.GuildMemberListener.NicknameData;

import kotlin.RequiresOptIn.Level;

//  JAVA IMPORTS
import java.util.Map;
import java.time.format.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

//  Get all the cache data from a user
public class HistoryCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        
        User targetUser = event.getOption("user").getAsUser();  //  Get the user
        String targetUserId = targetUser.getId();
        int userLevel = LevelSystem.getLevel(targetUserId);
        int userXp = LevelSystem.levelInformation.getOrDefault(targetUserId, 0);
        String xpRequiredForNextLevelString =  Integer.toString(LevelSystem.getXpForNextLevel(userLevel) - userXp);
        String userLevelString = Integer.toString(userLevel);
        // StringBuilder foundActivityKeys = new StringBuilder();  // Initialize a StringBuilder
        // StringBuilder foundNicknameKeys = new StringBuilder();  // Initialize a StringBuilder
        // StringBuilder foundSpotifyKeys = new StringBuilder();   // Initialize a StringBuilder
        EmbedBuilder embed = new EmbedBuilder();

        // // Iterate through the activity cache to find matching keys
        // for (Map.Entry<String, ActivityData> entry : GuildMemberListener.activityCache.entrySet()) {
        //     if (entry.getValue().getUserId().equals(targetUserId)) {
        //         String key = entry.getKey();    //  Get the activity name
        //         foundActivityKeys.append("```" + key + " @ " + entry.getValue().getDate() + "```");  //  Add the keys to the StringBuilder
        //     }
        // }

        // // Iterate through the spotify cache to find matching keys
        // for (Map.Entry<RichPresence, ActivityData> entry : GuildMemberListener.spotifyCache.entrySet()) {
        //     if (entry.getValue().getUserId().equals(targetUserId)) {
        //         RichPresence key = entry.getKey();    //  Get the activity name
        //         foundSpotifyKeys.append("```" + key.getDetails() + " by " + key.getState() + "@" + "```");  //  Add the keys to the StringBuilder
        //     }
        // }

        // // Iterate through the nickname cache to find matching keys
        // for (Map.Entry<String, NicknameData> entry : GuildMemberListener.nicknameCache.entrySet()){
        //     if(entry.getValue().getUserId().equals(targetUserId)){
        //         String key = entry.getKey();    //  Get the activity name
        //         foundNicknameKeys.append("```" + entry.getValue().getOldNickname() + " -> " + key + " @ " + entry.getValue().getDate() + "```");  //  Add the keys to the StringBuilder
        //     }
        // }

        embed.setTitle(targetUser.getName() + "'s History");
        embed.setColor(Global.CUSTOMPURPLE);
        // embed.addField("Recent Activity:", foundActivityKeys.toString(), false);
        // embed.addField("Recent Spotify Activity: ", foundSpotifyKeys.toString(), false);
        // embed.addField("Recent Nicknames:", foundNicknameKeys.toString(), false);
        embed.addField("Current Level: ", userLevelString, false);
        embed.addField("XP Required For Next Level: ", xpRequiredForNextLevelString, false);
        embed.addField("Joined Server At:", event.getOption("user").getAsMember().getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), false);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
