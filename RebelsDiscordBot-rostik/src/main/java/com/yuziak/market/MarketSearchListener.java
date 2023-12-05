package com.yuziak.market;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuziak.MessageCreator;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarketSearchListener extends ListenerAdapter {

   private final static String requestChannelName = "requests";
   private final static String requestURL = "https://api.arsha.io/v2/eu/GetWorldMarketSubList?id=";

   @Override
   public void onMessageReceived(MessageReceivedEvent event) {
      if (!requestChannelName.equals(event.getChannel().getName())) {
         return;
      }


      String messageText = event.getMessage().getContentDisplay();
      MarketSearchItem.MarketSearchItemRequest requst = mapToSearchItemRequest(messageText);

      //getting id and grade;
      long id = 0;
      long grade = 0;

      List<MarketSearchItem> marketSearchItems = mapToSearchItem(doSearch((id)));
      List<MessageEmbed> response = new ArrayList<>();
      marketSearchItems.forEach(item -> {
                 MessageEmbed tmpMessage = processItem(item, grade);
                 if (Objects.nonNull(tmpMessage)) {
                    response.add(tmpMessage);
                 }
              }
      );

      response.forEach(message -> {
         event.getChannel().sendMessageEmbeds(message).submit();
      });

   }

   private MessageEmbed processItem(MarketSearchItem searchItem, long grade) {
      if (!(searchItem.getSid() == grade) || (searchItem.getCurrentStock() == 0)) {
         return null;
      }
      return MessageCreator.createMessage(searchItem);
   }

   private String doSearch(long id) {
      try {
         Connection.Response response = Jsoup.connect(requestURL + id).ignoreContentType(true).method(Connection.Method.GET).execute();
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

   private List<MarketSearchItem> mapToSearchItem(String json) {
      List<MarketSearchItem> searchItems = new ArrayList<>();
      if (Objects.isNull(json)) {
         return searchItems;
      }

      ObjectMapper objectMapper = new ObjectMapper();
      try {
         searchItems = objectMapper.readValue(json, new TypeReference<List<MarketSearchItem>>() {
         });

      } catch (JsonProcessingException e) {
         e.printStackTrace();
         System.out.println("Error with mapping. Json: " + json);
      }
      return searchItems;
   }

   private MarketSearchItem.MarketSearchItemRequest mapToSearchItemRequest(String json) {
      MarketSearchItem.MarketSearchItemRequest searchItemRequest = null;
      if (Objects.isNull(json)) {
         return null;
      }

      if (!json.startsWith("{") && !json.endsWith("}")) {
         json = "{" + json + "}";
      }

      ObjectMapper objectMapper = new ObjectMapper();
      try {
         searchItemRequest = objectMapper.readValue(json, MarketSearchItem.MarketSearchItemRequest.class);
      } catch (JsonProcessingException e) {
         e.printStackTrace();
         System.out.println("Error with mapping. Json: " + json);
      }
      return searchItemRequest;
   }
}
