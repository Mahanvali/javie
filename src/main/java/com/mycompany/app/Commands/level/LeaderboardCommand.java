package com.mycompany.app.Commands.level;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;
import com.mycompany.app.Listeners.LevelSystem;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LeaderboardCommand implements CommandImplementation {
    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.deferReply().queue();
        EmbedBuilder leaderboardEmbed = new EmbedBuilder();
        leaderboardEmbed.setColor(Global.CUSTOMPURPLE);
        leaderboardEmbed.setTitle(event.getGuild().getName() + "'s Top 10 Degens <:yukariBASED:1270513258645819433> ");
        // Atomic integer to keep track of the ranking
        AtomicInteger rank = new AtomicInteger(1);
        // Sort the levelInformation map by XP in descending order and get the top 10 entries
        LevelSystem.levelInformation.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(10)
        .forEach(entry -> {
            int currentRank = rank.getAndIncrement();
            String rankSymbol = "";
            String userId = entry.getKey();
            int userLevel = LevelSystem.getLevel(userId);
            String userMention = event.getJDA().getUserById(userId).getAsMention();
            switch (currentRank) {
                case 1:
                    rankSymbol = "🥇"; // Gold medal
                    break;
                case 2:
                    rankSymbol = "🥈"; // Silver medal
                    break;
                case 3:
                    rankSymbol = "🥉"; // Bronze medal
                    break;
                default:
                    rankSymbol = "#" + currentRank; // Default rank
                    break;
            }
            leaderboardEmbed.appendDescription(rankSymbol + " | " + userMention + " • " + "Level: `"+ userLevel + "`\n");
        });
        event.getHook().sendMessageEmbeds(leaderboardEmbed.build()).queue();
    }
}
