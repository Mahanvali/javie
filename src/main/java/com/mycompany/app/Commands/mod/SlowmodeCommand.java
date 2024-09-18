package com.mycompany.app.Commands.mod;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlowmodeCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        if(event.getSubcommandName().equals("set")){

            event.deferReply().queue();
            String slowmodeTimeString = event.getOption("time").getAsString();
            TextChannel currentChannel = event.getChannel().asTextChannel();
            EmbedBuilder baseEmbed = new EmbedBuilder();

            int slowmodeTime = 0;

            if (slowmodeTimeString.endsWith("h")) {
                    int hours = Integer.parseInt(slowmodeTimeString.substring(0, slowmodeTimeString.length() - 1));
                    slowmodeTime = hours * 3600;
                } else if (slowmodeTimeString.endsWith("m")) {
                    int minutes = Integer.parseInt(slowmodeTimeString.substring(0, slowmodeTimeString.length() - 1));
                    slowmodeTime = minutes * 60;
                } else if (slowmodeTimeString.endsWith("s")) {
                    int seconds = Integer.parseInt(slowmodeTimeString.substring(0, slowmodeTimeString.length() - 1));
                    slowmodeTime = seconds;
                } else {
                    Global.BuildSimpleDescriptionEmbed(
                        Global.yukariSMH + " Look buddy, enter a valid slowmode time, I've got things to do.\n> Example: /slowmode set 10s",
                        Global.CUSTOMPURPLE,
                        baseEmbed);
                    event.getHook().sendMessageEmbeds(baseEmbed.build()).setEphemeral(true).queue();
                    return;
                }
                
            currentChannel.getManager().setSlowmode(slowmodeTime).queue(
                (unused) -> {
                    baseEmbed.setDescription(Global.yukariNOTED + " Set slowmode to `" + slowmodeTimeString + "`");
                    baseEmbed.setColor(Global.CUSTOMPURPLE);
                    event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                },
    
                (error) -> {
                    baseEmbed.setDescription(Global.yukariSMH + " Failed to set slowmode\n```" + error.getMessage() + "```");
                    baseEmbed.setColor(Global.CUSTOMPURPLE);
                    event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                }
            );
        }

        if(event.getSubcommandName().equals("reset")){
            event.deferReply().queue();
            TextChannel currentChannel = event.getChannel().asTextChannel();
            EmbedBuilder baseEmbed = new EmbedBuilder();;

            currentChannel.getManager().setSlowmode(0).queue(
                (unused) -> {
                    baseEmbed.setDescription(Global.yukariNOTED + " Reset the channel's slowmode");
                    baseEmbed.setColor(Global.CUSTOMPURPLE);
                    event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                },
    
                (error) -> {
                    baseEmbed.setDescription(Global.yukariSMH + " Failed to set slowmode\n```" + error.getMessage() + "```");
                    baseEmbed.setColor(Global.CUSTOMPURPLE);
                    event.getHook().sendMessageEmbeds(baseEmbed.build()).queue();
                }
            );
        }
    }
}
