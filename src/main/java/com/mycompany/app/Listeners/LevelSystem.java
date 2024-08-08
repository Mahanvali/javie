package com.mycompany.app.Listeners;

import java.util.HashMap;
import java.util.Map;

import com.mycompany.app.Global;

import java.io.*;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LevelSystem extends ListenerAdapter {
    public static final Map<String, Integer> levelInformation = new HashMap<>();
    private static final String FILE_PATH = "src/main/java/com/mycompany/app/levelData.txt";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        Role level10Role = event.getJDA().getRoleById(Global.level10RoleId);
        Role level20Role = event.getJDA().getRoleById(Global.level20RoleId);
        Role level30Role = event.getJDA().getRoleById(Global.level30RoleId);
        Role level40Role = event.getJDA().getRoleById(Global.level40RoleId);
        Role level50Role = event.getJDA().getRoleById(Global.level50RoleId);
        Role boosterRole = event.getJDA().getRoleById(Global.boosterRoleId);

        if (!event.getAuthor().isBot() && !event.getChannel().getId().equals(Global.botCommandsChannelId)) {
            if(event.getMember().getRoles().contains(boosterRole)){
                levelInformation.put(userId, levelInformation.getOrDefault(userId, 0) + 11);    //  Increment XP by 11 for each message (Increase for boosters)
            } else {
                levelInformation.put(userId, levelInformation.getOrDefault(userId, 0) + 10);    //  Increment XP by 10 for each message
            }
            saveData();
        }

        if(getLevel(userId) == 1 && !event.getMember().getRoles().contains(level10Role)){
            event.getGuild().addRoleToMember(event.getMember(), level10Role);
        }

        if(getLevel(userId) == 20 && !event.getMember().getRoles().contains(level20Role)){
            event.getGuild().addRoleToMember(event.getMember(), level20Role);
            event.getGuild().removeRoleFromMember(event.getMember(), level10Role);
        }

        if(getLevel(userId) == 30 && !event.getMember().getRoles().contains(level30Role)){
            event.getGuild().addRoleToMember(event.getMember(), level30Role);
            event.getGuild().removeRoleFromMember(event.getMember(), level20Role);
        }

        if(getLevel(userId) == 40 && !event.getMember().getRoles().contains(level40Role)){
            event.getGuild().addRoleToMember(event.getMember(), level40Role);
            event.getGuild().removeRoleFromMember(event.getMember(), level30Role);  
        }

        if(getLevel(userId) == 50 && !event.getMember().getRoles().contains(level50Role)){
            event.getGuild().addRoleToMember(event.getMember(), level50Role);
            event.getGuild().removeRoleFromMember(event.getMember(), level40Role);  
        }
    }

    static public int getLevel(String userId) {
        int xp = levelInformation.getOrDefault(userId, 0);
        return (int) Math.floor((Math.sqrt(1 + 8 * xp / 100) - 1) / 2); //  Increase the number of XP required for each level by 100
    }

    static public int getXpForNextLevel(int currentLevel) {
        return 100 * (currentLevel + 1) * (currentLevel + 2) / 2;   //  Get the amount of xp the user requires for the next level
    }

    public static void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, Integer> entry : levelInformation.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    levelInformation.put(parts[0], Integer.parseInt(parts[1]));
                    System.out.println("Level data loaded!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
