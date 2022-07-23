package me.nerdoron.himyb.commands.useful;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.timezones.TimezoneParse;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeCommand extends SlashCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping memberOption = event.getOption("member");

        if (memberOption == null) {
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

            MessageBuilder msb = new MessageBuilder();
            msb.setContent("Hey, "+event.getUser().getName()+"'s time is **"+time.getHour()+":"+(time.getMinute()<10?"0":"")+time.getMinute()+"**");
            msb.denyMentions(Message.MentionType.values());

            event.reply(msb.build()).queue();
        } else {
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
            event.reply("Hey, "+member.getUser().getName()+"'s time is **"+time.getHour()+":"+(time.getMinute()<10?"0":"")+time.getMinute()+"**").queue();
        }


    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData time = Commands.slash("time", "Show your time or someone's in the server");
        time.addOption(OptionType.USER, "member", "Who you want to view the time of?");
        return time;
    }
}
