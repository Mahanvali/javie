package com.mycompany.app.Commands;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Listeners.GuildMemberListener;
import com.mycompany.app.Listeners.GuildMemberListener.ActivityData;
import com.mycompany.app.Listeners.GuildMemberListener.NicknameData;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Map;

public class ShowAllCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        JDA jda = event.getJDA();   //  Get JDA instance

        // Iterate through the activity cache to find matching keys
        for (Map.Entry<String, ActivityData> entry : GuildMemberListener.activityCache.entrySet()) {
            String key = entry.getKey();    //  Get the activity name

            String userId = entry.getValue().getUserId();
            User user = jda.retrieveUserById(userId).complete();    //  Get the user from the user id
            String username = user.getName();   //  Get the user's name

            System.out.println("----------------------------");
            System.out.println("Activity: " + key + " User: " + username);
            System.out.println("----------------------------");
        }

        // Iterate through the spotify cache to find matching keys
        for (Map.Entry<RichPresence, ActivityData> entry : GuildMemberListener.spotifyCache.entrySet()) {
            String key = entry.getKey().getDetails();    //  Get the song name

            String userId = entry.getValue().getUserId();
            User user = jda.retrieveUserById(userId).complete();    //  Get the user from the user id
            String username = user.getName();   //  Get the user's name

            System.out.println("----------------------------");
            System.out.println("Song: " + key + " User: " + username);
            System.out.println("----------------------------");
        }

        // Iterate through the nickname cache to find matching keys
        for (Map.Entry<String, NicknameData> entry : GuildMemberListener.nicknameCache.entrySet()){
                String key = entry.getKey();    //  Get the nickname

                String userId = entry.getValue().getUserId();
                User user = jda.retrieveUserById(userId).complete();    //  Get the user from the user id
                String username = user.getName();   //  Get the user's name

                System.out.println("----------------------------");
                System.out.println("New Nickname: " + key + " User: " + username);
                System.out.println("----------------------------");
        }
        event.getHook().sendMessage("Check logs bucko").queue();
    }
}
