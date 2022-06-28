package me.nerdoron.himyb.modules.zitchdog;

import me.nerdoron.himyb.modules.Database;
import me.nerdoron.himyb.modules.afk.AFKChecks;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZitchSQL {
    static Connection con = Database.connect();
    static PreparedStatement ps = null;
    static final Logger logger = LoggerFactory.getLogger(AFKChecks.class);

    public static boolean hasBrocoin(Member member) {
        String SQL = "SELECT uid FROM brocoins WHERE UID=?";
        try {
            ps = con.prepareStatement(SQL);
            ps.setString(1, member.getId());
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

    public static int getBrocoin(Member member) {
        if (!hasBrocoin(member)) return 0;
        int brocoins = 0;
        try {
            String SQL = "select amount FROM brocoins WHERE uid=?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, member.getId());
            ResultSet rs = ps.executeQuery();
            brocoins = rs.getInt(1);
            ps.close();
        } catch (SQLException e) {
            logger.error("Error while getting some's brocoins in the DB", e.getCause().getMessage());
            e.printStackTrace();
            return 0;
        }
        return brocoins;
    }

    public static void setBrocoin(Member member, int newAmount) throws SQLException {
        if (hasBrocoin(member)) {
            String SQL = "UPDATE brocoins SET amount = ? WHERE uid = ?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setInt(1, newAmount);
            ps.setString(2, member.getId());
            ps.execute();
            ps.close();
        } else {
            String SQL = "INSERT into brocoins (UID, AMOUNT) values(?,?)";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, member.getId());
            ps.setInt(2, newAmount);
            ps.execute();
            ps.close();
        }
    }
}
