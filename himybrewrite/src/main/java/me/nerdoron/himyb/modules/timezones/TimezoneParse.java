package me.nerdoron.himyb.modules.timezones;

import me.nerdoron.himyb.modules.Database;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TimezoneParse {

    Connection con = Database.connect();
    Logger logger = LoggerFactory.getLogger(TimezoneParse.class);

    public String getTimezoneOf(Member member) {
        String timezone = "";
        try {
            System.out.println("1");
            String SQL = "select timezone FROM timezones WHERE uid=?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, member.getId());
            ResultSet rs = ps.executeQuery();
            timezone = rs.getString(1);
        } catch (SQLException e) {
            logger.error("Error while trying to find someone's timezone in the database!", e.getCause().getMessage());
            e.printStackTrace();
            return null;
        }
        return timezone;
    }
}
