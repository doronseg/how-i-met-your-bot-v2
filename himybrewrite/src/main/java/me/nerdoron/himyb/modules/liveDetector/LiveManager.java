package me.nerdoron.himyb.modules.liveDetector;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.modules.WebRequest;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.OffsetDateTime;

public class LiveManager extends ListenerAdapter {
    private JDA jda;
    private String guildID = "850396197646106624";
    private String guildChannel = "850796695372955738";
    private final int timeBetweenMSGS_Sec = Global.hourinSeconds * 3;
    private final int checkInterval_MS = Global.ms_1minute;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public LiveManager(JDA jda) {
        this.jda = jda;
        // Let discord load b4 starting the loop
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
        }
        ;
        loop();
    }

    public void loop() {
        new Thread(() -> {
            execute();
            try {
                Thread.sleep(checkInterval_MS);
            } catch (InterruptedException e) {
            }
            loop();
        }).start();
    }

    public void execute() {
        TextChannel channel = null;
        String latestMessageId = null;
        try {
            channel = jda.getGuildById(guildID).getTextChannelById(guildChannel);
            latestMessageId = channel.getLatestMessageId();
        } catch (Exception e) {
            logger.error("LiveManager attempted to fetch data from the discord channel but failed");
            return;
        }

        boolean live = false;

        try {
            live = isLive();
        } catch (IOException e) {
            logger.error("An error was thrown while checking if Oscar was live");
            throw new RuntimeException(e);
        }

        TextChannel finalChannel = channel;
        boolean finalLive = live;

        if (!live) {
            return;
        }
        logger.info("Oscar is live!");

        channel.retrieveMessageById(latestMessageId).queue(
                message -> {
                    OffsetDateTime timeCreated = message.getTimeCreated();
                    OffsetDateTime now = OffsetDateTime.now();
                    boolean canSendMessage = (timeCreated.toEpochSecond() + timeBetweenMSGS_Sec) < now.toEpochSecond();
                    if (canSendMessage) {
                        if (finalLive) {
                            sendLiveMessage(finalChannel);
                        }
                    }
                },
                (__) -> {
                    // No msg found in channel (rare case)
                    if (finalLive) {
                        sendLiveMessage(finalChannel);
                    }
                });
    }

    public void sendLiveMessage(TextChannel channel) {
        logger.info("Sending message.");
        // Note: Using the API I can also get the stream title that oscar have set
        String send = "<@&868751837405265950>\n";
        send += " :red_circle: __**Oscar is live on YouNow!**__ :red_circle:\n";
        send += " https://www.younow.com/OscarStinson";
        channel.sendMessage(send).queue();
    }

    public boolean isLive() throws IOException {
        WebRequest request = new WebRequest();
        request.newRequest("https://api.younow.com/php/api/broadcast/info/curId=0/lang=en/user=OscarStinson");
        request.get();
        request.sendRequest();
        JSONObject responce = request.getJSONObjectResponce();

        int errorCode = responce.getInt("errorCode");

        return (errorCode == 0) && (!responce.getString("stateCopy").equals("OscarStinson is about to go live"));
    }
}
