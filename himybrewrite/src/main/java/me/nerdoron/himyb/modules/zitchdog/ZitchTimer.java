package me.nerdoron.himyb.modules.zitchdog;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.nerdoron.himyb.Global;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ZitchTimer {
    private final JDA jda;
    private final EventWaiter waiter;
    private final String guildID = "991446766169903165";
    private final String channelID = "991446767407218744";
    private int waitTime = generateNumber(Global.ms_1hour, Global.ms_1hour*3);

    public ZitchTimer(JDA jda, EventWaiter waiter) {
        this.jda = jda;
        this.waiter = waiter;
    }

    public void execute() {
        new Thread(() -> {
            try {Thread.sleep(waitTime);} catch (InterruptedException e) {}
            waitTime = generateNumber(Global.ms_1hour, Global.ms_1hour*3);
            //Testing guildID. Chance when on main
            Guild guild = jda.getGuildById(guildID);
            TextChannel channel = guild.getTextChannelById(channelID);
            channel.sendMessage("\uD83D\uDC36").queue(
                    this::eventHandler
            );
            execute();
        }).start();
    }

    private void eventHandler(Message message) {
        waiter.waitForEvent(
                MessageReceivedEvent.class, (event) -> {
                    User user = event.getAuthor();
                    return !user.isBot() && event.isFromGuild() && event.getChannel().getId().equals(message.getChannel().getId()) && event.getMessage().getContentRaw().equalsIgnoreCase("zitch dog");
                },
                (event) -> {

                    int brocoins = ZitchSQL.getBrocoin(event.getMember());
                    try {
                        ZitchSQL.setBrocoin(event.getMember(), brocoins+1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        message.editMessage("Uh oh. There has been a DB error").queue();
                        return;
                    }

                    message.editMessage("Congratulations "+event.getMember().getAsMention()+ "! You got 1 Brocoin! Now you have "+(brocoins+1)).queue();

                },
                5, TimeUnit.MINUTES,
                () -> {
                    message.editMessage("`:(` Timed out").queue();
                }
        );
    }

    private int generateNumber(int min, int max) {
        Random r = new Random();
        int low = min;
        int high = max;
        int result = r.nextInt(high-low) + low;
        return result;
    }

}
