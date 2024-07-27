package com.mycompany.app;

//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandImplementation {
    void execute(SlashCommandInteractionEvent event);
}
