package com.yuziak.market;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yuziak.MessageCreator.createMessage;

public class QueueChecker {

    private final String urlPath = "https://api.arsha.io/v2/eu/GetWorldMarketWaitList";
    private List<QueueItem> currentRegistrationQueue = new ArrayList<>();

    private List<WishListItem> wishListItems = new ArrayList<>();
    final String guildName = "YummyDranik";

    final String CURRENT_QUEUE_CHANEL = "current-lots";
    final String JSON_CHANEL = "json-for-search";

    TextChannel channelCurQueue;

    public void start(JDA bot) throws InterruptedException {
        Guild guild = bot.getGuildsByName(guildName, false).get(0);

        channelCurQueue = guild.getTextChannelsByName(CURRENT_QUEUE_CHANEL, true).get(0);

        updateWishListItems(guild);

        int flag = 0;

        while (true) {
            long currentTime = new Date().getTime();
            currentRegistrationQueue.removeIf(queueItem -> queueItem.getLiveAt() * 1000 < currentTime);

            String queueJSON = check();
            List<QueueItem> newQueueItems = mapToQueueItem(queueJSON);

            for (QueueItem newQueueItem : newQueueItems) {
                boolean isNew = true;
                for (QueueItem currentQueueItem : currentRegistrationQueue) {
                    if (!currentQueueItem.isAnother(newQueueItem)) {
                        isNew = false;
                    }
                }
                if (isNew) {
                    wishListItems.forEach(wishListItem -> {
                        wishListItem.checkNeededItems(newQueueItem, guild);
                    });
                    currentRegistrationQueue.add(newQueueItem);
                }
            }

            MessageHistory history = MessageHistory.getHistoryFromBeginning(channelCurQueue).complete();
            List<Message> mess = history.getRetrievedHistory();
            for (Message m : mess) {
                m.delete().queue();
            }

            for (QueueItem queueItem : currentRegistrationQueue) {
                MessageEmbed curItem = createMessage(queueItem);
                channelCurQueue.sendMessageEmbeds(curItem).submit();
            }
            channelCurQueue.sendMessage("Current wish list size: " + wishListItems.size()).submit();

            if (flag % 5 == 0) {
                updateWishListItems(guild);
            }
            flag++;
            TimeUnit.MINUTES.sleep(1);
        }
    }

    private void updateWishListItems(Guild guild) {
        TextChannel wishListChannel = guild.getTextChannelsByName(JSON_CHANEL, true).get(0);
        MessageHistory historyWish = MessageHistory.getHistoryFromBeginning(wishListChannel).complete();
        List<Message> messages = historyWish.getRetrievedHistory();
        String wishJSON = "[";
        for (Message m : messages) {
            wishJSON += m.getContentDisplay() + ",";
        }
        wishJSON = wishJSON.substring(0, wishJSON.length() - 1) + "]";
        List<WishListItem>  newWishList = mapToWishList(wishJSON);

        wishListItems =newWishList;
    }

    private String check() {
        try {
            Connection.Response response = Jsoup.connect(urlPath).ignoreContentType(true).method(Connection.Method.GET).execute();
            String json = response.body();
            if (!json.startsWith("[") && !json.endsWith("]")) {
                json = "[" + json + "]";
            }
            return json;

        } catch (IOException e) {
            if (!e.getMessage().contains("515")) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }

    private List<QueueItem> mapToQueueItem(String json) {
        List<QueueItem> queueItems = new ArrayList<>();
        if (Objects.isNull(json)) {
            return queueItems;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            queueItems = objectMapper.readValue(json, new TypeReference<List<QueueItem>>() {
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error with mapping. Json: " + json);
        }
        return queueItems;
    }

    private List<WishListItem> mapToWishList(String json) {
        List<WishListItem> wishListItems = new ArrayList<>();
        if (Objects.isNull(json)) {
            return wishListItems;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            wishListItems = objectMapper.readValue(json, new TypeReference<List<WishListItem>>() {
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error with mapping. Json: " + json);
        }
        return wishListItems;
    }


}
