package com.yuziak;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BossSchedule {
    private static HashMap<String, String[]> schedule = new HashMap<>();
    static {
        schedule.put("Кзарка", new String[]{"Monday 00:00:00", "Tuesday 00:00:00", "Tuesday 14:00:00",
                "Wednesday 18:00:00", "Thursday 18:00:00", "Friday 01:00:00", "Friday 18:00:00",
                "Saturday 00:00:00", "Saturday 08:00:00", "Sunday 01:00:00", "Sunday 18:00:00"});
        schedule.put("Каранда", new String[]{"Monday 00:00:00", "Monday 16:00:00", "Monday 23:00:00",
                "Tuesday 01:00:00", "Tuesday 12:00:00", "Tuesday 18:00:00", "Wednesday 14:00:00",
                "Thursday 00:00:00", "Friday 12:00:00", "Saturday 16:00:00", "Sunday 08:00:00"});
        schedule.put("Нубер", new String[]{"Monday 01:00:00", "Monday 14:00:00", "Monday 23:00:00",
                "Wednesday 01:00:00", "Wednesday 16:00:00", "Thursday 12:00:00", "Thursday 16:00:00",
                "Friday 00:00:00", "Friday 16:00:00", "Saturday 00:00:00", "Saturday 10:00:00",
                "Sunday 18:00:00"});
        schedule.put("Кутум", new String[]{"Monday 12:00:00", "Monday 18:00:00", "Tuesday 16:00:00",
                "Wednesday 00:00:00", "Thursday 01:00:00", "Thursday 14:00:00", "Friday 14:00:00",
                "Saturday 01:00:00", "Saturday 12:00:00", "Saturday 18:00:00", "Sunday 10:00:00",
                "Sunday 14:00:00"});
        schedule.put("Камос", new String[]{"Thursday 23:00:00", "Sunday 00:00:00", "Sunday 12:00:00"});
        schedule.put("Велл", new String[]{"Wednesday 23:00:00", "Sunday 16:00:00"});
        schedule.put("Биба и Боба", new String[]{"Tuesday 23:00:00", "Saturday 14:00:00"});
        schedule.put("Офин", new String[]{"Wednesday 18:00:00", "Friday 23:00:00", "Sunday 23:00:00"});
    }


    private final static String guildName = "YummyDranik";
    private final static String bossChanelName = "напоминалка";
    private final static String bossRoleName = "Босс";
    private final static String sonilRoleName = "Сонил";

    public static void doSchedule(JDA bot) {
        TextChannel channelToRemind = bot.getTextChannelsByName(bossChanelName, true).get(0);
        Role boss = bot.getGuildsByName(guildName, false).get(0).getRolesByName(bossRoleName, false).get(0);
        Role sonil = bot.getGuildsByName(guildName, false).get(0).getRolesByName(sonilRoleName, false).get(0);

        while (true) {
            try {
                TimeUnit.MINUTES.sleep(1);

                String timePattern = "HH:mm";
                String time = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ofPattern(timePattern));
                if (time.equals("19:00") || time.equals("01:30")) {
                    channelToRemind.sendMessage("<@&" + sonil.getId() + "> Друг, забери своих сонилов").submit();
                }

                LocalDateTime curTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).plusMinutes(15);
                String checkBefore15 = curTime.format(DateTimeFormatter.ofPattern("EEEE HH:mm", Locale.ENGLISH));
                String check = curTime.minusMinutes(15).format(DateTimeFormatter.ofPattern("EEEE HH:mm", Locale.ENGLISH));
                BossSchedule.schedule.forEach((k, v) -> {
                    for (String date : v) {
                        if (checkBefore15.equals(date)) {
                            channelToRemind.sendMessage("<@&" + boss.getId() + "> " + k + " рес через 15 минут").submit();
                        } else if (check.equals(date)) {
                            channelToRemind.sendMessage("<@&" + boss.getId() + "> " + k + " реснулся").submit();
                        }
                    }
                });

            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            }
        }
    }
}
