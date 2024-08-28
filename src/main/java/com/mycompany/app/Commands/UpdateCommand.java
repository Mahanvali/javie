package com.mycompany.app.Commands;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.app.App;
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class UpdateCommand implements CommandImplementation {

    public static List<CommandData> globalCommandData = new ArrayList<>();

    @Override
    public void execute(SlashCommandInteractionEvent event){
        JDA jda = event.getJDA();
        event.deferReply().queue();
        EmbedBuilder embed = new EmbedBuilder();
        if(event.getUser().getId().equals(Global.botdeveloperUserId)){

            if(event.getSubcommandName().equals("dev")){
                App.registerDeveloperCommands(jda);
                Global.BuildSimpleDescriptionEmbed(
                    "Successfully updated developer commands! " + Global.yukariYES,
                    Global.CUSTOMPURPLE,
                    embed);
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
            if(event.getSubcommandName().equals("mod")){
                App.registerModCommands(jda);
                Global.BuildSimpleDescriptionEmbed(
                    "Successfully updated mod commands! " + Global.yukariYES,
                    Global.CUSTOMPURPLE,
                    embed);
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
            if(event.getSubcommandName().equals("level")){
                App.registerLevelCommands(jda);
                Global.BuildSimpleDescriptionEmbed(
                    "Successfully updated level commands! " + Global.yukariYES,
                    Global.CUSTOMPURPLE,
                    embed);
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
            if(event.getSubcommandName().equals("misc")){
                App.registerMiscCommands(jda);
                Global.BuildSimpleDescriptionEmbed(
                    "Successfully updated misc commands! " + Global.yukariYES,
                    Global.CUSTOMPURPLE,
                    embed);
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
            if(event.getSubcommandGroup().equals("activity")){
                jda.getPresence().setActivity(Activity.watching("Over " + Global.memberCount + " Members"));
                Global.BuildSimpleDescriptionEmbed(
                    "Successfully updated my activity! " + Global.yukariYES,
                    Global.CUSTOMPURPLE,
                    embed);
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        } else {
            Global.BuildInvalidPermissionsEmbed("BOT DEVELOPER", Global.CUSTOMRED, embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
