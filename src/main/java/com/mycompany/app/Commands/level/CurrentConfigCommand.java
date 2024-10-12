package com.mycompany.app.Commands.level;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CurrentConfigCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        EmbedBuilder currentsettingsEmbed = new EmbedBuilder();
        long voiceCooldown = (Global.voiceCooldown / 1000) / 60;
        long messageCooldown = (Global.messageCooldown / 1000);
        currentsettingsEmbed.setTitle(event.getGuild().getName() + "'s Configurations");
        currentsettingsEmbed.setColor(Global.CUSTOMPURPLE);
        currentsettingsEmbed.addField("XP Gains", 
            "Default Users: " + "**" + Global.basicXPGain + "xp**\n" + 
            "Boosters: " + "**" + Global.boosterXPGain + "xp**", false);
        currentsettingsEmbed.addField("Cooldowns", 
            "Voice: " + "**" + voiceCooldown + " minutes**\n" +
            "Message: " + "**" + messageCooldown + " seconds**", false);
        event.getHook().sendMessageEmbeds(currentsettingsEmbed.build()).queue();
    }
    
}
