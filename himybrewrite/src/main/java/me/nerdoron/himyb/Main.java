package me.nerdoron.himyb;

import javax.security.auth.login.LoginException;

import me.nerdoron.himyb.modules._bot.RegisterEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.DotEnvException;
import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.himyb.modules.StatusTimer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Global.className(Main.class));

    public static void main(String[] args) {
        String version = "Shop Update (1/8/23)";
        logger.info("Hi - You're running " + version);
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
        Global.DOTENV = dotenv;
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
        StatusTimer.changeActivity(api);
        RegisterEvents.registration(api);
    }

}
