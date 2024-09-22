package com.mycompany.app.Listeners;

import com.mycompany.app.Global;
import com.mycompany.app.Commands.mod.WarnCommand;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        //  Get the member count
        Global.memberCount = (int) event.getJDA().getGuilds().stream()
            .mapToLong(guild -> guild.getMembers().stream()
                .filter(member -> !member.getUser().isBot())
                .count())
            .sum();
        //  Update the presence
        event.getJDA().getPresence().setActivity(Activity.watching("Over " + Global.memberCount + " Members"));

        LevelSystem.loadLevelData();
        WarnCommand.loadWarnData();
        ModalListener.loadTagData();
    }
}
