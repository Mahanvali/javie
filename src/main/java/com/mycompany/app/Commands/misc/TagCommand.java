package com.mycompany.app.Commands.misc;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.ModalListener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.Map;

public class TagCommand implements CommandImplementation  {

    @Override
    public void execute(SlashCommandInteractionEvent event){
        EmbedBuilder baseEmbed = new EmbedBuilder();
        if(event.getSubcommandName().equals("add")){

            if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)){
                Global.BuildInvalidPermissionsEmbed(
                    "ADMINISTRATOR",
                    Global.CUSTOMPURPLE,
                    baseEmbed);

                event.replyEmbeds(baseEmbed.build()).queue();
                return;
            }

            TextInput title = TextInput.create("tag-title", "Title", TextInputStyle.SHORT)
                .setPlaceholder("Title for this tag")
                .setMinLength(3)
                .setMaxLength(50)
                .build();

            TextInput data = TextInput.create("tag-data", "Data", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Data for this tag")
                .setMinLength(30)
                .setMaxLength(1500)
                .build();

            Modal modal = Modal.create("createtag", "Create a tag")
                .addComponents(ActionRow.of(title), ActionRow.of(data))
                .build();

            event.replyModal(modal).queue();
        }

        if(event.getSubcommandName().equals("remove")){
            if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)){
                Global.BuildInvalidPermissionsEmbed(
                    "ADMINISTRATOR",
                    Global.CUSTOMPURPLE,
                    baseEmbed);

                event.replyEmbeds(baseEmbed.build()).queue();
                return;
            }
            String tag = event.getOption("tag").getAsString().toLowerCase();
            if(ModalListener.tagsMap.containsKey(tag)){
                ModalListener.deleteTagData(tag);
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariYES + " Successfully removed the **" + tag + "** tag",
                    Global.CUSTOMPURPLE,
                    baseEmbed);
                    event.replyEmbeds(baseEmbed.build()).queue();
            } else {
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariSMH + " The tag **" + tag + "** does not exist",
                    Global.CUSTOMRED,
                    baseEmbed);
                    event.replyEmbeds(baseEmbed.build()).queue();
            }
        }

        if(event.getSubcommandName().equals("show")){
            String tag = event.getOption("tag").getAsString().toLowerCase();
            if(ModalListener.tagsMap.containsKey(tag)){
                Global.BuildSimpleDescriptionEmbed(
                    ModalListener.tagsMap.get(tag),
                    Global.CUSTOMPURPLE,
                    baseEmbed);
                baseEmbed.setTitle(tag);
                event.replyEmbeds(baseEmbed.build()).queue();
            } else {
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariSMH + " The tag **" + tag + "** does not exist",
                    Global.CUSTOMRED,
                    baseEmbed);
                    event.replyEmbeds(baseEmbed.build()).queue();
            }
        }

        if(event.getSubcommandName().equals("list")){
            String taglistString = "";

            //  Get all the tags (keys), and seperate them by a comma
            for (Map.Entry<String, String> entry : ModalListener.tagsMap.entrySet()) {
                taglistString += entry.getKey() + ", ";
            }

            // Remove the trailing comma
            taglistString = taglistString.substring(0, taglistString.length() - 2);

            Global.BuildSimpleDescriptionEmbed(
                taglistString,
                Global.CUSTOMPURPLE,
                baseEmbed);
            baseEmbed.setTitle("All current available tags");
            event.replyEmbeds(baseEmbed.build()).queue();
        }
    }
}
