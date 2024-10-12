package com.mycompany.app.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

import com.mycompany.app.Global;

import java.io.*;

public class ModalListener extends ListenerAdapter {
    private static final String FILE_PATH = "src/main/java/com/mycompany/app/tagData.txt";
    public static final Map<String, String> tagsMap = new HashMap<>();
    @Override
    public void onModalInteraction(ModalInteractionEvent event){
        if(event.getModalId().equals("createtag")){
            EmbedBuilder baseEmbed = new EmbedBuilder();
            String title = event.getValue("tag-title").getAsString().toLowerCase();
            String data = event.getValue("tag-data").getAsString();
            tagsMap.put(title, data);
            saveTagData();
            Global.BuildSimpleDescriptionEmbed(
                Global.yukariYES + " Successfully added the **" + title + "** tag",
                Global.CUSTOMPURPLE,
                baseEmbed);
            event.replyEmbeds(baseEmbed.build()).queue();
        }
    }

    public static void saveTagData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, String> entry : tagsMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTagData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    tagsMap.put(parts[0], parts[1]);
                    System.out.println("Tag data loaded!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTagData(String tag) {
        tagsMap.remove(tag);
        saveTagData();
    }
}
