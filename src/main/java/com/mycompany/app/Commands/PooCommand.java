package com.mycompany.app.Commands;

//  IMPORT COMMANDIMPLEMENTATION
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.GuildMemberListener;
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

        if(event.getUser().getId().equals(Global.botdeveloperUserId)){  //  If the user is the bot developer

            int messageCacheSize    =   GuildMessageListener.messageCache.size();
            int nicknameCacheSize   =   GuildMemberListener.nicknameCache.size();   
            String totalCacheSize   =   Integer.toString(messageCacheSize + nicknameCacheSize);   
            
            event.getHook().sendMessage("Clearing `" + totalCacheSize + "` items stored in cache.. 🟠").queue();
            GuildMessageListener.messageCache.clear();  //  Clear messages
            GuildMemberListener.nicknameCache.clear();  //  Clear nicknames

            if(GuildMessageListener.messageCache.size() == 0 &&
            GuildMemberListener.nicknameCache.size() == 0){
                embed.setDescription("All of cache cleared! 🟢");
                embed.setColor(Global.CUSTOMGREEN);
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            } else {
                embed.setDescription("Something went wrong, couldn't clear the cache! 🔴");
                embed.setColor(Global.CUSTOMRED);
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        } else {
            Global.BuildInvalidPermissionsEmbed("BOT DEVELOPER", Global.CUSTOMRED, embed);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
