package me.nerdoron.himyb.modules.brocoins;

import me.nerdoron.himyb.modules.Database;
import me.nerdoron.himyb.modules.afk.AFKChecks;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BroCoinsSQL {
    static Connection con = Database.connect();
    static PreparedStatement ps = null;
    static final Logger logger = LoggerFactory.getLogger(AFKChecks.class);

    public boolean hasBrocoins(Member member) {
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

    public boolean hasBrocoins(User member) {
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

    public int getBrocoins(Member member) {
        if (!hasBrocoins(member))
            return 0;
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

    public Map<String, Integer> getBrocoins() {
        Map<String, Integer> result = new HashMap<>();
        try {
            String SQL = "SELECT * from brocoins";
            PreparedStatement ps = con.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userID = rs.getString(1);
                int userBroCoins = rs.getInt(2);
                result.put(userID,userBroCoins);
            }
            ps.close();
        } catch (SQLException e) {
            logger.error("Error while getting some's brocoins in the DB", e.getCause().getMessage());
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public void setBrocoins(Member member, int newAmount) throws SQLException {
        if (hasBrocoins(member)) {
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

    public void updateBrocoins(Member member, int amountToChange) throws SQLException {
        int memberCoins = this.getBrocoins(member);
        memberCoins+=amountToChange;
        this.setBrocoins(member,memberCoins);
    }
}
