package me.nerdoron.himyb.modules.birthday;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.himyb.modules.Database;

public class BirthdayChecks {

    Connection con = Database.connect();
    Logger logger = LoggerFactory.getLogger(BirthdayChecks.class);

    public boolean hasBirthdaySet(String uid) {
        try {
            String SQL = "select month from birthday where uid=?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, uid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                return true;
            } else {
                ps.close();
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error while trying to find someone in the Birthday database!", e.getCause().getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
