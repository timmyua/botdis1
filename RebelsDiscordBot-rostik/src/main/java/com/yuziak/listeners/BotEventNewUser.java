package com.yuziak.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotEventNewUser extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        System.out.println("new member");
        String joindUserRoleName = "Пропущенный";
        String chanellName = "кто-залетел";
        Role joined = event.getGuild().getRolesByName(joindUserRoleName, false).get(0);
        event.getGuild().addRoleToMember(event.getMember(), joined).submit();
        event.getGuild().getTextChannelsByName(chanellName, false).get(0).sendMessage(
                event.getUser().getAsTag() + " залетел к нам на сервер").submit();
    }


    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        System.out.println("member leave");
        String chanellName = "кто-залетел";
        event.getGuild().getTextChannelsByName(chanellName, false).get(0).sendMessage(
                event.getUser().getAsTag() + " ушел от нас").submit();
    }

}
