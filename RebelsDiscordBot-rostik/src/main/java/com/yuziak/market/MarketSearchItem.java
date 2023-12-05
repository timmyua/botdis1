package com.yuziak.market;

import lombok.Getter;
import lombok.Setter;

//                   "name": "Lightstone of Fire: Aerial",
//                   "icon": "cdn.arsha.io/icons/758009.png",
//                   "id": 758009,
//                   "sid": 0,
//                   "minEnhance": "0",
//                   "maxEnhance": "0",
//                   "basePrice": 54000000,
//                   "currentStock": 696,
//                   "totalTrades": 1773,
//                   "priceMin": 50000000,
//                   "priceMax": 5000000000,
//                   "lastSoldPrice": 50000000,
//                   "lastSoldTime": 1670093168

@Getter
@Setter
public class MarketSearchItem {
   private String name;
   private String icon;
   private long id;
   private long sid;
   private String minEnhance;
   private String maxEnhance;
   private long basePrice;
   private long currentStock;
   private long totalTrades;
   private long priceMin;
   private long priceMax;
   private long lastSoldPrice;
   private long lastSoldTime;

   @Getter
   @Setter
   public static class MarketSearchItemRequest {
      private long id;
      private long grade;
   }
}


