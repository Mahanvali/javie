package com.mycompany.app.Listeners;

import com.mycompany.app.Global;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        //  Get the member count
        Global.memberCount = event.getJDA().getGuilds().stream()
            .mapToInt(guild -> guild.getMemberCount())
            .sum();
        //  Update the presence
        event.getJDA().getPresence().setActivity(Activity.watching("Over " + Global.memberCount + " Members"));

        LevelSystem.loadData();
    }
}
