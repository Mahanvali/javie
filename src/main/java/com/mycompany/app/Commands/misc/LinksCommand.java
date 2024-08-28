package com.mycompany.app.Commands.misc;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LinksCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Check out all of these links!");
        embed.setColor(Global.CUSTOMPURPLE);
        embed.setThumbnail("https://i.postimg.cc/ht9NDWDk/Screen-Shot-2024-08-27-at-12-22-38-PM.png");
        embed.setDescription(
            Global.yukariHEART + " **[Twitter](https://x.com/yukarivt)**\n"
            + Global.yukari4K + " **[Twitch](https://twitch.tv/yukarivt)**\n"
            + Global.yukariBONK + " **[Discord](https://discord.gg/yukarii)**\n"
            + Global.yukariSEARCH + " **[Youtube](https://www.youtube.com/@yukariVODs)**\n"
            + Global.yukariOHOH + " **[Ko-Fi](https://ko-fi.com/yukarivt)**\n"
            + Global.yukariYES + " **[TikTok](https://www.tiktok.com/@yukarivt)**\n"
            + Global.yukariEVIL + " **[Donations](https://streamelements.com/yukarivt/tip)**");
            
        if(event.getChannel().asTextChannel().getId().equals(Global.botCommandsChannelId)){
                event.replyEmbeds(embed.build()).queue();
            } else {
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
            }
    }
}
