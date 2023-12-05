package com.yuziak;

import com.yuziak.market.MarketSearchItem;
import com.yuziak.market.QueueItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageCreator {

    private static final String garmothAssetsUrl = "https://assets.garmoth.com/items/";
    public static MessageEmbed createMessage(QueueItem queueItem) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(queueItem.getName(), null);

        eb.setColor(new Color(0xFFFFF000, true));

        String price = editPriceView(queueItem.getPrice());
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd HH:mm:ss");
        eb.setDescription("Price: " + price + "\n"
                + "Live at: " + sdfDate.format(new Date(queueItem.getLiveAt() * 1000)));

        eb.setFooter("Данєчка");

        eb.setThumbnail(garmothAssetsUrl + queueItem.getId() + ".png");

        return eb.build();
    }

    private static String editPriceView(BigInteger price) {
        StringBuilder sb = new StringBuilder();

        int rank = 1;
        while (price.compareTo(BigInteger.ONE) >= 0) {
            BigInteger lastChar = price.mod(BigInteger.valueOf(10));
            sb.insert(0, lastChar);
            if (rank % 3 == 0) {

                sb.insert(0, ',');
            }
            rank++;

            price = price.divide((BigInteger.valueOf(10)));
        }
        if (sb.toString().startsWith(",")) {
            return sb.substring(1);
        }
        return sb.toString();
    }

    public static MessageEmbed createMessage(MarketSearchItem searchItem) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(searchItem.getName(), null);

        eb.setColor(new Color(0xFFFFF000, true));


        eb.setDescription("Stock: " + searchItem.getCurrentStock());

        eb.setFooter("Данєчка");

        eb.setThumbnail(garmothAssetsUrl + searchItem.getId() + ".png");

        return eb.build();
    }
}
