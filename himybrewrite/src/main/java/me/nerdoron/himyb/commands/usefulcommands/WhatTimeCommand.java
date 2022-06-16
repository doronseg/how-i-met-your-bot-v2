package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.timezones.TimezoneParse;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class WhatTimeCommand extends SlashCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        TimezoneParse timezoneParse = new TimezoneParse();
        Member member = event.getOption("member").getAsMember();
        String timezone = null;
        try {
            timezone = timezoneParse.getTimezoneOf(member);
        } catch (Exception e) {
            event.reply(member.getAsMention()+" didn't set their timezone in the bot!").setEphemeral(true).queue();
            return;
        }

        String[] tz = timezone.split(":");
        int hrs = Integer.parseInt(tz[0]);
        int mns = Integer.parseInt(tz[1]);

        ZonedDateTime time = ZonedDateTime.now(ZoneOffset.UTC).plusHours(hrs).plusMinutes(mns);
        event.reply("Hey, "+member.getUser().getName()+"'s time is **"+time.getHour()+":"+time.getMinute()+(time.getMinute()<10?"0":"")+"**").queue();
    }
}
