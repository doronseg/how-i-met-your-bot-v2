package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.Database;
import me.nerdoron.himyb.modules.birthday.BirthdayChecks;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BirthdayCommand extends SlashCommand {
    BirthdayChecks birthdayChecks = new BirthdayChecks();
    ArrayList<String> months = new ArrayList<>(List.of("January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"));

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (birthdayChecks.hasBirthdaySet(event.getUser().getId()) == true) {
            event.deferReply().setEphemeral(true).setContent("You already have a birthday set!").queue();
            return;
        }

        int month = event.getOption("month").getAsInt();
        int day = event.getOption("day").getAsInt();

        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), month, day);

        event.reply("Your birthday has been set to the " + dayNumber(c.get(Calendar.DAY_OF_MONTH)) + " of "
                + months.get(c.get(Calendar.MONTH))).setEphemeral(true).queue();

        Connection con = Database.connect();
        String statement = "insert into birthday (uid, day, month) values(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(statement);
            ps.setString(1, event.getUser().getId());
            ps.setInt(2, c.get(Calendar.DAY_OF_MONTH));
            ps.setInt(3, (c.get(Calendar.MONTH)));
            ps.execute();
        } catch (SQLException ex) {
            event.deferReply().setEphemeral(true).setContent("Error!").queue();
            ex.printStackTrace();
        }
    }

    // Helper method to say the days in a cardinal way
    public String dayNumber(int day) {
        switch (day) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            case 21:
                return "21st";
            case 22:
                return "22nd";
            case 23:
                return "23rd";
            case 31:
                return "31st";
            default:
                return day + "th";
        }
    }

}
