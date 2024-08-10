package com.mycompany.app.Listeners;

//  JDA API IMPORTS
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//  JAVA IMPORTS
import java.util.HashMap;
import java.util.Map;
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

    public static final Map<String, MessageData> messageCache = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getAuthor().isBot()){ //  Make sure the user isnt a bot
            //  Get the message
            Message receivedMessage = event.getMessage();
            //  Get the message author
            User author = receivedMessage.getAuthor();
            //  Store the message id, message content and the author
            messageCache.put(receivedMessage.getId(), new MessageData(receivedMessage.getContentDisplay(), author));
        }
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

            if(logsChannel != null){
                Global.SendDeletedMessageEmbed(
                    "Deleted Message Event",
                    messageData.author.getAsMention(),
                    messageData.content,
                    logsChannel);
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