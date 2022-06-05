package me.nerdoron.himyb.modules.afk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.himyb.modules.Database;

public class AFKChecks {

    static Connection con = Database.connect();
    static PreparedStatement ps = null;
    static final Logger logger = LoggerFactory.getLogger(AFKChecks.class);

    public static boolean CheckAFK(String UID) {
        String SQL = "SELECT REASON FROM afk WHERE UID=?";

        try {
            ps = con.prepareStatement(SQL);
            ps.setString(1, UID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                return true;
            } else {
                ps.close();
                return false;
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
        }

        return false;
    }

    public static String afkReason(String UID) {
        String SQL = "SELECT REASON FROM afk WHERE UID=?";

        try {
            ps = con.prepareStatement(SQL);
            ps.setString(1, UID);
            ResultSet rs = ps.executeQuery();
            String reason = rs.getString(1);
            ps.close();
            return reason;
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return null;
    }
}
