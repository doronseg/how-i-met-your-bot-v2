package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.Database;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class TimezoneCommandV2 extends SlashCommand {

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
                event.deferReply().setEphemeral(true).setContent("Error! You already have your timezone set!")
                        .queue();
                ex.printStackTrace();
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
                event.reply("I'm sorry no timezone was found matching your time. Make sure you typed your time correctly and your clock is not +3/-3 minutes off")
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
                ex.printStackTrace();
            }
        }

    }
}
