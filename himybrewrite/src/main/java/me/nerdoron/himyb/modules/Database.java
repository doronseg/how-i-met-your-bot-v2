package me.nerdoron.himyb.modules;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class Database {

    public static Connection connect() {
        Connection con = null;
        Dotenv dotenv = Dotenv.load();
        final Logger logger = LoggerFactory.getLogger(Database.class);

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection((dotenv.get("DB")));
        } catch (Exception ex) {
            logger.error("An exception occurred while trying to connect to the database!", ex);
        }

        return con;
    }

}