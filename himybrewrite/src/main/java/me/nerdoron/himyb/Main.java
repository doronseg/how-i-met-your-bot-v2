package me.nerdoron.himyb;

import javax.management.Query;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.examples.command.PingCommand;

import io.github.cdimascio.dotenv.DotEnvException;
import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.himyb.commands.BotCommandsHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Hi.");
        logger.info("Starting stage 1 - enviorment");
        try {
            setupEnviorment();
        } catch (DotEnvException ex) {
            logger.error("Exception occured whilst trying to setup the env file!", ex);
            ex.printStackTrace();
        }
    }

    private static void setupEnviorment() throws DotEnvException {
        Dotenv dotenv = Dotenv.load();
        logger.info("Stage 1 complete");
        logger.info("Stage 2: bot login");
        try {
            login(dotenv);
            logger.info("Stage 2 complete");
        } catch (LoginException ex) {
            logger.error("Exception occured whilst trying to login  !", ex);
            ex.printStackTrace();
        }
    }

    public static void login(Dotenv dotenv) throws LoginException {
        String token = dotenv.get("TOKEN");
        JDA api = JDABuilder.createLight(token)
                .enableIntents(GatewayIntent.GUILD_BANS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .build();
        logger.info("Logged in!");
        api.getPresence().setActivity(Activity.playing("Loading..."));
        registration(api);
    }

    public static void registration(JDA jda) {
        // command registration
        jda.addEventListener(new BotCommandsHandler());
        jda.updateCommands().addCommands(
                Commands.slash("ping", "Calculate te ping of the bot."),
                Commands.slash("uptime", "Show the bot's uptime."),
                Commands.slash("revive", "Send the chat revive message")
                        .setDefaultPermissions(Permission.MESSAGE_MANAGE))
                .queue();

    }
}
