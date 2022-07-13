package me.nerdoron.himyb;

import java.awt.Color;
import java.util.Random;

import me.nerdoron.himyb.modules._bot.BotCommandsHandler;
import me.nerdoron.himyb.modules._bot.CooldownManager;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class Global {
    public static Color embedColor = Color.decode("#2f3136");
    public static String footertext = "how i met your bot | Developed by nerdoron";
    public static String footerpfp = "https://media.discordapp.net/attachments/850432082738937896/901742492347691028/discord_bot_pfp.jpg";
    public static final int ms_1second = 1000;
    public static final int ms_1minute = 60000;
    public static final int ms_1hour = 60 * ms_1minute;
    public static CustomEmoji broCoin = Emoji.fromCustom("brocoin", 991661873126707210L, false);
    public static final CooldownManager COOLDOWN_MANAGER = new CooldownManager();
    public static BotCommandsHandler COMMANDS_HANDLER = null;

    public static int generateNumber(int min, int max) {
        Random r = new Random();
        int low = min;
        int high = max;
        int result = r.nextInt(high - low) + low;
        return result;
    }
}
