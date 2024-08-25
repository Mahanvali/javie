package com.mycompany.app.Commands.level;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SetCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();

        EmbedBuilder setEmbed = new EmbedBuilder();

        if(event.getSubcommandName().equals("xpgain")){
            Global.basicXPGain = event.getOption("amount").getAsInt();

            Global.BuildSimpleDescriptionEmbed(
                Global.yukariNOTED + " Sucessfully set the amount of xp gain per message to: `" + Global.basicXPGain + "`",
                Global.CUSTOMPURPLE,
                setEmbed);
            event.getHook().sendMessageEmbeds(setEmbed.build()).queue();
        }

        if(event.getSubcommandName().equals("cooldown")){
            OptionMapping messageOption = event.getOption("message");
            OptionMapping voiceOption = event.getOption("voice");

            if(messageOption != null){
                String messageCooldownString = event.getOption("message").getAsString();
                long messageCooldown = convertToMilliseconds(messageCooldownString);
                Global.messageCooldown = messageCooldown;

                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariNOTED + " Sucessfully set the message xp cooldown to: `" + messageCooldownString + "`!",
                    Global.CUSTOMPURPLE,
                    setEmbed);
                event.getHook().sendMessageEmbeds(setEmbed.build()).queue();
            }

            if(voiceOption != null){
                String voiceCooldownString = event.getOption("voice").getAsString();
                long voiceCooldown = convertToMilliseconds(voiceCooldownString);
                Global.voiceCooldown = voiceCooldown;

                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariNOTED + "Sucessfully set the voice xp cooldown to: `" + voiceCooldownString + "`!",
                    Global.CUSTOMPURPLE,
                    setEmbed);
                event.getHook().sendMessageEmbeds(setEmbed.build()).queue();
            }
        }
    }

    private long convertToMilliseconds(String timeString) {
        long milliseconds = 0;
        if (timeString.endsWith("s")) {
            int seconds = Integer.parseInt(timeString.substring(0, timeString.length() - 1));
            milliseconds = seconds * 1000;
        } else if (timeString.endsWith("m")) {
            int minutes = Integer.parseInt(timeString.substring(0, timeString.length() - 1));
            milliseconds = minutes * 60 * 1000;
        } else {
            // Default to seconds if no unit is specified
            int seconds = Integer.parseInt(timeString);
            milliseconds = seconds * 1000;
            timeString = timeString + "s";
        }
        return milliseconds;
    }
}
