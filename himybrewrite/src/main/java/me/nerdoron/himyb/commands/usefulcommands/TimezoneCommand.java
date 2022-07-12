package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.Database;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class TimezoneCommand extends SlashCommand {

    private ArrayList<Integer> possibleMinutes = new ArrayList<>(Arrays.asList(0, 15, 30, 45));


    @Override
    public void execute(SlashCommandInteractionEvent event) {

        String subcmd = event.getSubcommandName();
        if (subcmd.equals("remove")) {
            String statement = "delete from timezones where uid=?";
            Connection con = Database.connect();
            try {
                PreparedStatement ps = con.prepareStatement(statement);
                ps.setString(1, event.getUser().getId());
                ps.execute();
                ps.close();
                event.reply("Removed your timezone from my records.").setEphemeral(true).queue();
            } catch (SQLException ex) {
                event.deferReply().setEphemeral(true).setContent("Error! You did not have a timezone set in the bot!")
                        .queue();
            }
        }
        if (subcmd.equals("set")) {
            String time = event.getInteraction().getOption("time").getAsString();

            if (!time.matches("\\d{1,2}:\\d{2}")) {
                event.reply("Your time doesnt match the regex of XX:XX").queue();
                return;
            }

            String[] split = time.split(":");
            int hours = Integer.parseInt(split[0]);
            int minutes = Integer.parseInt(split[1]);

            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

            int diffH = 0;
            int diffM = 0;
            boolean found = false;
            for (diffH = -12; diffH < 12; diffH++) {
                for (Integer possibleMinute : possibleMinutes) {
                    diffM = possibleMinute;
                    ZonedDateTime mod = now.plusHours(diffH);
                    mod = mod.plusMinutes(possibleMinute);
                    ZonedDateTime minus = mod.minusMinutes(3);
                    ZonedDateTime plus  = mod.plusMinutes(3);
                    if (mod.getHour()==hours) {
                        if (minus.getMinute() <= minutes && plus.getMinute()>=minutes) {
                            found = true;
                        }
                    }
                    if (found) break;
                }
                if (found) break;
            }


            boolean negative = diffH<0;
            if (!found) {
                event.reply("I'm sorry no timezone was found matching your time. Make sure you typed your time correctly and your clock is not +3/-3 minutes off\n" +
                                "If you would like, you can specify your timezone using /timezone setmanual")
                        .setEphemeral(true).queue();
                return;
            } else {

            }


            Connection con = Database.connect();
            String statement = "insert into timezones (uid, timezone) values(?,?)";
            try {
                PreparedStatement ps = con.prepareStatement(statement);
                ps.setString(1, event.getUser().getId());
                ps.setString(2, diffH + ":" + diffM);
                ps.execute();
                ps.close();
                event.reply("Your time difference of **GMT " + (negative ? "" : "+") + diffH + ":" + diffM
                        + (diffM == 0 ? "0" : "") + "** has been set!").setEphemeral(true).queue();
            } catch (SQLException ex) {
                event.deferReply().setEphemeral(true).setContent("Error! You already have your timezone set!")
                        .queue();
            }
        }
        if (subcmd.equals("setmanual")) {
            int diffH = event.getOption("hours").getAsInt();
            int diffM = event.getOption("minutes").getAsInt();
            boolean negative = diffH<0;
            Connection con = Database.connect();
            String statement = "insert into timezones (uid, timezone) values(?,?)";
            try {
                PreparedStatement ps = con.prepareStatement(statement);
                ps.setString(1, event.getUser().getId());
                ps.setString(2, diffH + ":" + diffM);
                ps.execute();
                ps.close();
                event.reply("Your time difference of **GMT " + (negative ? "" : "+") + diffH + ":" + diffM
                        + (diffM == 0 ? "0" : "") + "** has been set!").setEphemeral(true).queue();
            } catch (SQLException ex) {
                event.deferReply().setEphemeral(true).setContent("Error! You already have your timezone set!")
                        .queue();
            }
        }

    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData timezone = Commands.slash("timezone", "Confused about time?");
        SubcommandData tz_remove = new SubcommandData("remove", "Removes your timezone from the database.");
        SubcommandData tz_set = new SubcommandData("set",
                "Set your timezone so you can share it with everyone!");
        tz_set.addOption(OptionType.STRING, "time",
                "Type what time it is for you in 24-hour format (14:24), and I will figure out your timezone.",
                true);
        SubcommandData tz_setmanual = new SubcommandData("setmanual",
                "Manually specify your timezone to the bot.");
        OptionData tz_setmanual_hours = new OptionData(OptionType.INTEGER, "hours",
                "GMT +/- this amound of hours", true);
        tz_setmanual_hours.setRequiredRange(-12, 12);
        OptionData tz_setmanual_minutes = new OptionData(OptionType.INTEGER, "minutes",
                "GMT +/- this amound of minutes", true);
        tz_setmanual_minutes.setRequiredRange(0, 59);
        tz_setmanual.addOptions(tz_setmanual_hours, tz_setmanual_minutes);
        timezone.addSubcommands(tz_set);
        timezone.addSubcommands(tz_remove);
        timezone.addSubcommands(tz_setmanual);

        return timezone;
    }
}
