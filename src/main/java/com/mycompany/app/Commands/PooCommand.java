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

        EmbedBuilder embed = new EmbedBuilder();

        if(event.getUser().getId().equals(Global.botdeveloperUserId)){  //  If the user is the bot developer
            GuildMessageListener.messageCache.clear();
            GuildMemberListener.nicknameCache.size();
            
            int messageCacheSize    =   GuildMessageListener.messageCache.size();
            int nicknameCacheSize   =   GuildMemberListener.nicknameCache.size();  

            if(nicknameCacheSize == 0 &&
            messageCacheSize == 0){
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariNOTED + " Successfully cleared the cache!",
                    Global.CUSTOMPURPLE,
                    embed);
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
            } else {
                Global.BuildSimpleDescriptionEmbed(
                    Global.yukariSMH + " There was a problem clearing the cache.",
                    Global.CUSTOMRED,
                    embed);
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
            }
        } else {
            Global.BuildInvalidPermissionsEmbed("BOT DEVELOPER", Global.CUSTOMRED, embed);
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
    }
}
