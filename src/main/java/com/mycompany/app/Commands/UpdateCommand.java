package com.mycompany.app.Commands;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.app.App;
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class UpdateCommand implements CommandImplementation {

    public static List<CommandData> globalCommandData = new ArrayList<>();

    public void execute(SlashCommandInteractionEvent event){
        JDA jda = event.getJDA();
        event.deferReply().queue();
        EmbedBuilder embed = new EmbedBuilder();

        if(event.getSubcommandName().equals("dev")){
            App.registerDeveloperCommands(jda);
            Global.BuildSimpleDescriptionEmbed(
                "Successfully updated developer commands! <:yukariYES:1270513445887934474> ",
                Global.CUSTOMPURPLE,
                embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
        if(event.getSubcommandName().equals("mod")){
            App.registerModCommands(jda);
            Global.BuildSimpleDescriptionEmbed(
                "Successfully updated mod commands! <:yukariYES:1270513445887934474> ",
                Global.CUSTOMPURPLE,
                embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
        if(event.getSubcommandName().equals("level")){
            App.registerLevelCommands(jda);
            Global.BuildSimpleDescriptionEmbed(
                "Successfully updated level commands! <:yukariYES:1270513445887934474> ",
                Global.CUSTOMPURPLE,
                embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
