package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.timezones.TimezoneParse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class MytimeCommand extends SlashCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        TimezoneParse timezoneParse = new TimezoneParse();
        String timezone = null;
        try {
            timezone = timezoneParse.getTimezoneOf(event.getMember());
        } catch (Exception e) {
            event.reply("You didn't set your timezone in the bot!").setEphemeral(true).queue();
            return;
        }

        String[] tz = timezone.split(":");
        int hrs = Integer.parseInt(tz[0]);
        int mns = Integer.parseInt(tz[1]);

        ZonedDateTime time = ZonedDateTime.now(ZoneOffset.UTC).plusHours(hrs).plusMinutes(mns);
        event.reply("Hey, "+event.getUser().getName()+"'s time is **"+time.getHour()+":"+(time.getMinute()<10?"0":"")+time.getMinute()+"**").queue();
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData mytime = Commands.slash("mytime", "Show your time in the server");
        return mytime;
    }
}
