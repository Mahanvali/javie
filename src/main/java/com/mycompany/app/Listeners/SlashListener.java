package com.mycompany.app.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
//  JDA API IMPORTS
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//  JAVA IMPORTS
import java.util.HashMap;
import java.util.Map;


//  COMMAND IMPORTS
import com.mycompany.app.Commands.*;
import com.mycompany.app.Commands.level.*;
import com.mycompany.app.Commands.misc.*;
import com.mycompany.app.Commands.mod.*;
import com.mycompany.app.CommandImplementation;
import com.mycompany.app.Global;

public class SlashListener extends ListenerAdapter {

    //  Create a HashMap to store the commands
    private final Map<String, CommandImplementation> commands = new HashMap<String, CommandImplementation>();

    //  Constructor for the SlashListener class
    public SlashListener(){
        //  Store the commands in the hashmap
        commands.put("boo", new BooCommand());
        commands.put("poo", new PooCommand());
        commands.put("update", new UpdateCommand());

        commands.put("kick", new KickCommand());
        commands.put("ban", new BanCommand());
        commands.put("warn", new WarnCommand());
        commands.put("unban", new UnbanCommand());
        commands.put("slowmode", new SlowmodeCommand());
        commands.put("timeout", new TimeoutCommand());

        commands.put("level", new LevelCommand());
        commands.put("leaderboard", new LeaderboardCommand());
        commands.put("xp", new XPCommand());
        commands.put("set", new SetCommand());
        commands.put("currentconfigs", new CurrentConfigCommand());

        commands.put("links", new LinksCommand());
        commands.put("avatar", new AvatarCommand());
        commands.put("membercount", new MemberCountCommand());
        commands.put("tag", new TagCommand());
    }

    //  Override the onSlashCommandInteraction method from ListenerAdapter
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){

        // Get the command from the hashmap using the event name
        CommandImplementation command = commands.get(event.getName());
        EmbedBuilder errorEmbed = new EmbedBuilder();
        //  If the command isn't null, execute the command.
        if (command != null && event.isFromGuild()) {
            try{
                command.execute(event);
                System.out.println(event.getFullCommandName() + " ran by " + event.getUser().getName());
                System.out.println(event.getOptions());
                System.out.println("------------------------------------------------------------");
            }catch (Exception e){
                errorEmbed.setColor(Global.CUSTOMPURPLE);
                errorEmbed.setTitle("An unexpected error occured");
                errorEmbed.setDescription("```" + e.getMessage() + "```\n **This has been logged and will be fixed!**");
                // Send message to the channel
                event.getChannel().sendMessageEmbeds(errorEmbed.build()).queue();
                //  Add extra field for debugging
                errorEmbed.addField("Command:", event.getFullCommandName(), false);
                errorEmbed.addField("User:", event.getUser().getAsMention(), false);
                //  Send message to developer
                event.getJDA().getUserById(Global.botdeveloperUserId).openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(errorEmbed.build()))
                    .queue();
            }
        }
    }
}
