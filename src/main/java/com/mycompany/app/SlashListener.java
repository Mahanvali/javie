package com.mycompany.app;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        switch (event.getName()) {
            case "boo":
                event.deferReply().queue(); // Defer the reply to allow for longer times to send the message
                event.getHook().sendMessage("poo").queue();
            break;

            default:
            return;
        }
    }
}
