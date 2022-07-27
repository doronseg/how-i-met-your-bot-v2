package me.nerdoron.himyb.modules.zitchdog;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ZitchTimer {
    final Logger logger = LoggerFactory.getLogger(ZitchTimer.class);
    private final JDA jda;
    private final EventWaiter waiter;
    private final String guildID = "850396197646106624";
    private final String channelID = "850437485687078932";
    private int waitTime = generateNumber(Global.ms_1minute * 30, Global.ms_1hour * 3);

    public ZitchTimer(JDA jda, EventWaiter waiter) {
        this.jda = jda;
        this.waiter = waiter;
    }

    BroCoinsSQL brocoinsSQL = new BroCoinsSQL();

    public void execute() {
        new Thread(() -> {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
            }
            waitTime = generateNumber(Global.ms_1minute * 30, Global.ms_1hour * 3);
            Guild guild = jda.getGuildById(guildID);
            TextChannel channel = guild.getTextChannelById(channelID);
            channel.sendMessage("\uD83D\uDC36").queue(
                    this::eventHandler);
            new Thread(() -> {
                execute();
            }).start();
        }).start();
    }

    private void eventHandler(Message message) {
        waiter.waitForEvent(
                MessageReceivedEvent.class, (event) -> {
                    User user = event.getAuthor();
                    return !user.isBot() && event.isFromGuild()
                            && event.getChannel().getId().equals(message.getChannel().getId())
                            && event.getMessage().getContentRaw().equalsIgnoreCase("zitch dog");
                },
                (event) -> {
                    if (!brocoinsSQL.hasBrocoins(event.getMember())) {
                        message.getChannel()
                                .sendMessage(event.getMember().getAsMention() + " You don't have bank account!")
                                .queue();
                        eventHandler(message);
                        return;
                    }

                    int brocoins = brocoinsSQL.getBrocoins(event.getMember());
                    int reward = generateNumber(1, 5);

                    try {
                        brocoinsSQL.updateBrocoins(event.getMember(), reward);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        message.editMessage("Uh oh. There has been a DB error").queue();
                        return;
                    }

                    message.editMessage("Congratulations " + event.getMember().getAsMention()
                            + "! You got " + reward + " " + (reward == 1 ? "Brocoin" : "Brocoins") + "! Now you have "
                            + (brocoins + reward) + " " + Emoji.fromCustom(Global.broCoin).getAsMention()).queue();
                    int coinsNow = brocoinsSQL.getBrocoins(event.getMember());
                    logger.info(event.getMember().getUser().getAsTag() + "(" + event.getMember().getId() + ")"
                            + " won (" + reward + " Coins) in ZitchDog now they have (" + coinsNow
                            + ")");
                },
                5, TimeUnit.MINUTES,
                () -> {
                    message.delete().queue();
                });
    }

    private int generateNumber(int min, int max) {
        Random r = new Random();
        int low = min;
        int high = max;
        int result = r.nextInt(high - low) + low;
        return result;
    }

}
