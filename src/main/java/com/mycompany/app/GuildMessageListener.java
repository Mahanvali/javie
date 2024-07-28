package com.mycompany.app;

//  JDA API IMPORTS
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;



//  JAVA IMPORTS
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private final Map<String, MessageData> messageCache = new HashMap<>();

    LocalDateTime time = LocalDateTime.now();   //  Get the current date
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");  //  Format the date
    String formattedTime = time.format(format); //  Apply the format

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //  Get the message
        Message receivedMessage = event.getMessage();
        //  Get the message author
        User author = receivedMessage.getAuthor();
        //  Store the message id, message content and the author
        messageCache.put(receivedMessage.getId(), new MessageData(receivedMessage.getContentDisplay(), author));
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        //  Get the messageID of the deleted message
        String messageId = event.getMessageId();
        //  See if the deleted message id matches with the message id stored in the Hashmap
        MessageData messageData = messageCache.remove(messageId);
        //  If the message id exists in the Hashmap
        if (messageData != null) {
            TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
            
            EmbedBuilder messageDeletedEmbed = new EmbedBuilder();
            messageDeletedEmbed.setTitle("Deleted Message Event");
            messageDeletedEmbed.setColor(Global.CUSTOMRED);
            messageDeletedEmbed.addField("Author:", messageData.author.getName(), false);
            messageDeletedEmbed.addField("Deleted Message:", messageData.content, false);
            messageDeletedEmbed.addField("Date:", formattedTime, false);

            //  Send messageDeletedEmbed
            logsChannel.sendMessageEmbeds(messageDeletedEmbed.build()).queue();
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event){
        //  Get the messageID of the updated message
        String messageId = event.getMessageId();
        //  See if the updated message id matches with the message id stored in the hashmap
        MessageData messageData = messageCache.remove(messageId);
        //  If the message id exists in the Hashmap
        if(messageData != null ){
            TextChannel logsChannel = event.getJDA().getTextChannelById(Global.logsChannelId);
            
            EmbedBuilder messageUpdatedEmbed = new EmbedBuilder();
            messageUpdatedEmbed.setTitle("Updated Message Event");
            messageUpdatedEmbed.setColor(Global.CUSTOMRED);
            messageUpdatedEmbed.addField("Author:", messageData.author.getName(), false);
            messageUpdatedEmbed.addField("Old Message:", messageData.content, false);
            messageUpdatedEmbed.addField("New Message", event.getMessage().getContentDisplay(), false);
            messageUpdatedEmbed.addField("Date:", formattedTime, false);

            //  Send messageUpdatedEmbed
            logsChannel.sendMessageEmbeds(messageUpdatedEmbed.build()).queue();
        }
    }
}