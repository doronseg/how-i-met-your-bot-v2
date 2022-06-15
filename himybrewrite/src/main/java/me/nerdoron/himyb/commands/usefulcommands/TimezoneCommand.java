package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.Database;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class TimezoneCommand extends SlashCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {

        String subcmd = event.getSubcommandName();
        if (subcmd.equals("set")) {
            String time = event.getInteraction().getOption("time").getAsString();

            if (!time.matches("\\d{1,2}:\\d{2}")) {
                event.reply("Your time doesnt match the regex of XX:XX").queue();
                return;
            }

            String[] split = time.split(":");
            int hours   = Integer.parseInt(split[0]);
            int minutes = Integer.parseInt(split[1]);

            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            int nowHour = now.getHour();
            int nowMinute = now.getMinute();

            int diffH = Math.abs(nowHour-hours);
            int diffM = Math.abs(nowMinute-minutes);

            boolean negative = hours < nowHour;
            if (negative) {
                diffH*=-1;
            }

            if (diffM > 13 && diffM < 17) {
                diffM = 15;
            } else if (diffM > 28 && diffM < 32) {
                diffM = 30;
            }else if (diffM > 43 && diffM < 47) {
                diffM = 45;
            } else {
                diffM = 0;
            }

            Connection con = Database.connect();
            String statement = "insert into timezones (uid, timezone) values(?,?)";
            try {
                PreparedStatement ps = con.prepareStatement(statement);
                ps.setString(1, event.getUser().getId());
                ps.setString(2, diffH+":"+diffM);
                ps.execute();
                event.reply("Your time difference of **GMT"+(negative? "":"+")+diffH+":"+diffM+(diffM==0 ? "0":"")+"** has been set!").setEphemeral(true).queue();
            } catch (SQLException ex) {
                event.deferReply().setEphemeral(true).setContent("Error! You already have your timezone set!").queue();
                ex.printStackTrace();
            }
        }


    }
}
