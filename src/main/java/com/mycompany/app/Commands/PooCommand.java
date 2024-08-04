package com.mycompany.app.Commands;

//  IMPORT COMMANDIMPLEMENTATION
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.GuildMessageListener;

import net.dv8tion.jda.api.EmbedBuilder;
//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PooCommand implements CommandImplementation {
    //  Override the PooCommand method from the CommandImplementation
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue(); // Defer the reply to allow for longer times to send the message

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Global.CUSTOMGREEN);

        if(event.getUser().getId().equals(Global.botdeveloperUserId)){  //  If the user is the bot developer

            String messageCacheSize = Integer.toString(GuildMessageListener.messageCache.size());
            event.getHook().sendMessage("Clearing `" + messageCacheSize + "` messages stored in cache.. 🟠").queue();
            GuildMessageListener.messageCache.clear(); //  Clear the message cache

            if(GuildMessageListener.messageCache.size() == 0){
                embed.setDescription("All messages in cache cleared! 🟢");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            } else {
                embed.setDescription("Something went wrong, couldn't clear the cache! 🔴");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        } else {
            Global.BuildInvalidPermissionsEmbed("BOT DEVELOPER", Global.CUSTOMRED, embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
