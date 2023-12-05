package com.yuziak.market;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import static com.yuziak.MessageCreator.createMessage;

@Getter
@Setter
public class WishListItem {


    private String[] names;
    private String channel;
    private Long grade;
    private static String aukRoleName = "Auction";

    public void checkNeededItems(QueueItem queueItem, Guild guild) {
        TextChannel channelToSend = guild.getTextChannelsByName(channel, true).get(0);
        Role auk = guild.getRolesByName(aukRoleName, false).get(0);

        if (isNeeded(queueItem, names, grade)) {
            send(channelToSend, queueItem, auk);
        }
    }

    private boolean isNeeded(QueueItem queueItem, String[] names, Long subId) {
        if (!queueItem.getSid().equals(subId)) {
            return false;
        }
        for (String name : names) {
            if (queueItem.getName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    private void send(TextChannel channel, QueueItem queueItem, Role aukRole) {
        channel.sendMessage("<@&" + aukRole.getId() + ">").submit();
        MessageEmbed itemMessage = createMessage(queueItem);
        channel.sendMessageEmbeds(itemMessage).submit();
    }

}

