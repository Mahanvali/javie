package com.mycompany.app.Listeners;

//  JDA API IMPORTS
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
//  JAVA IMPORTS
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mycompany.app.Global;


public class GuildMessageListener extends ListenerAdapter {

    //  Custom data structure to store message content and author
    //  private and static, so it’s only accessible within the outer class and doesn’t require an instance of the outer class to be instantiated.
    private static class MessageData {
        String content;
        User author;

        public MessageData(String content, User author) {
            this.content = content;
            this.author = author;
        }
    }
    // just giving some personality to the bot
    private String[] deletedMessagesResponses = {
        "I saw what you deleted.",
        "I watch every message you delete.",
        "I watch every message you delete, you're not slick.",
        "You're not slick buddy, I saw that.",
        "I saw the message you erased.",
        "You're not slick for deleting that message bucko.",
        "Wowww, deleting messages now are we?",
        "I may or may not have seen what you deleted.",
        "It's too late to delete your messages now",
        "It's already over for you bro",
        "What are you hiding?",
        "I'm looking at chat all the time.",
        "I have nothing better to do than just to stare at chat, I saw what you deleted.",
        "what'd you delete buddy?",
        "I saw what you deleted bucko.",
        "I saw that message you deleted.",
        "I saw that, extremely clearly.",
        "I saw that",
        "I saw the message, you can't hide it.",
        "Do you really think you're slick for deleting that message?",
    };

    private List<String> hatefultriggerString = Arrays.asList(
        "FUCK YOU BLAKE",
        "SHUT UP BLAKE",
        "KILL YOURSELF BLAKE",
        "SHUT THE FUCK UP BLAKE",
        "LOSER BLAKE",
        "KYS BLAKE",
        "KYS BOT",
        "BLAKE SUCKS",
        "BLAKE FUCK YOU",
        "DUMBASS BOT",
        "LOSER BOT",
        "STINKY BOT",
        "IDIOT BOT",
        "IDIOT BLAKE",
        "STINKY BLAKE");

    private static String[] cheekyEmojis = {Global.yukariEVIL, Global.yukariSEARCH, Global.yukari4K, Global.yukariOHOH};

    public static final Map<String, MessageData> messageCache = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getAuthor().isBot() && event.isFromGuild()){
            //  Get the message
            Message receivedMessage = event.getMessage();
            //  Get the message author
            User author = receivedMessage.getAuthor();
            //  Store the message id, message content and the author
            messageCache.put(receivedMessage.getId(), new MessageData(receivedMessage.getContentDisplay(), author));
        }

        // if(!event.isFromGuild() && !event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())){
        //     TextChannel appealChannel = event.getJDA().getTextChannelById(Global.appealChannelId);
        //     EmbedBuilder appealEmbed = new EmbedBuilder();
        //     appealEmbed.setDescription(event.getMessage().getContentDisplay());
        //     appealEmbed.setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
        //     appealEmbed.setColor(Global.CUSTOMPURPLE);
        //     appealChannel.sendMessageEmbeds(appealEmbed.build()).queue();
        // }
        Role introducedRole = event.getJDA().getRoleById(Global.introducedRoleId);
        Emoji yukariWave = event.getJDA().getEmojiById("1270512883834294292");
        if(event.getChannel().getId().equals(Global.introChannelId) && !event.getMember().getRoles().contains(introducedRole)){
            event.getGuild().addRoleToMember(event.getAuthor(), introducedRole).queue();
            event.getMessage().createThreadChannel(event.getAuthor().getEffectiveName() + ", welcome to the family!").queue();
            event.getMessage().addReaction(yukariWave).queue();
        }

        if(hatefultriggerString.contains(event.getMessage().getContentRaw().toUpperCase())){
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + Global.yukariFU).queue();
        }
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        //  Get the messageID of the deleted message
        String messageId = event.getMessageId();
        //  See if the deleted message id matches with the message id stored in the Hashmap
        MessageData messageData = messageCache.remove(messageId);
        //  Channels
        TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
        TextChannel sentMessagetTextChannel = event.getChannel().asTextChannel();
        //  If the message id exists in the Hashmap
        if (messageData != null) {
            if(logsChannel != null){
                Global.SendDeletedMessageEmbed(
                    "Deleted Message Event",
                    messageData.author.getAsMention(),
                    messageData.content,
                    logsChannel);
            }
            //  20 percent chance of sending the message
            Random random = new Random();
            String chosenResponse = deletedMessagesResponses[random.nextInt(deletedMessagesResponses.length)];
            String chosenEmoji = cheekyEmojis[random.nextInt(cheekyEmojis.length)];

            if(messageData.author.getId().equals("307892737729101825")){    //   Aaron's ID
                if(messageData.content.contains("@")){
                    sentMessagetTextChannel.sendMessage("Aaron, go sleep, enough ghost pinging.").queue();
                    return;
                }
            }
            
            if (random.nextInt(100) < 20 && !event.getChannel().getId().equals(Global.introChannelId)) {
                sentMessagetTextChannel.sendMessage(messageData.author.getAsMention() + ", " + chosenResponse + " " + chosenEmoji).queue();
            }
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event){
        //  Get the messageID of the updated message
        String messageId = event.getMessageId();
        //  See if the updated message id matches with the message id stored in the hashmap
        MessageData messageData = messageCache.get(messageId);
        //  If the message id exists in the Hashmap
        if(messageData != null){
            TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);

            if(logsChannel != null){
                Global.SendUpdatedMessageEmbed(
                "Updated Message Event",
                messageData.author.getAsMention(),
                messageData.content,
                event.getMessage().getContentDisplay(),
                logsChannel);
            }

            //  Update the message content in the cache
            messageData.content = event.getMessage().getContentDisplay();

        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        if(event.getMessageId().equals(Global.verificationMessageId)){
            //  Get the verifcation role from the role id
            Role verificationRole = event.getJDA().getRoleById(Global.verificationRoleId);

            //  Give the role to the user
            event.getGuild().addRoleToMember(event.getUser(), verificationRole).queue();
        }
    }
}