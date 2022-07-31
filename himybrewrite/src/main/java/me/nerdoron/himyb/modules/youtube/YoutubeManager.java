package me.nerdoron.himyb.modules.youtube;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.modules.WebRequest;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.NewsChannel;
import okhttp3.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class YoutubeManager {
    private static final Logger logger = LoggerFactory.getLogger(YoutubeManager.class);
    private static final String ytKey = Global.DOTENV.get("YTAPI");
    private static final String ytChannelID = "UCqj-7XzxhgL14RKORG1UIrw";
    private final int checkInterval_MS = Global.ms_1minute * 5;
    private final String guildID = "850396197646106624";
    private final String guildChannel = "850796695372955738";
    private static final int results = 1;
    private final JDA jda;

    public YoutubeManager(JDA jda) {
        this.jda = jda;
        // Let discord load b4 starting the loop
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
        }
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

    private void execute() {
        GuildChannel gchannel = null;
        youtubeVideo latestVideo = getLatestVideo();

        for (GuildChannel channel1 : jda.getGuildById(guildID).getChannels()) {
            if (channel1.getId().equals(guildChannel)) {
                gchannel = channel1;
            }
        }
        NewsChannel channel = ((NewsChannel) gchannel);

        channel.getHistory().retrievePast(5).queue(
                messages -> {
                    for (Message message : messages) {
                        if (message.getContentRaw().contains(latestVideo.getVideoID())) {
                            // The msg of the video was already sent.
                            return;
                        }
                    }
                    channel.sendMessage("@Mention\n" +
                            ":mega: __Oscar uploaded a new video!__ :mega:\n" +
                            "**"+latestVideo.getTitle()+"**\n"
                            +"https://www.youtube.com/watch?v="+latestVideo.getVideoID()).queue(
                            message -> {
                                message.crosspost().queue(
                                        (__) -> {}, (___) -> {
                                            ___.printStackTrace();
                                            logger.error("Unable to publish message. Please check my permissions");
                                        }
                                );
                            }
                    );
                }
        );
    }

    public static youtubeVideo getLatestVideo() {
        WebRequest webRequest = new WebRequest();
        webRequest.newRequest("https://www.googleapis.com/youtube/v3/search?key="+ytKey+"&channelId="+ytChannelID+"&part=snippet,id&order=date&maxResults="+results);
        webRequest.get();
        Response response = null;
        try {
            response = webRequest.sendRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject JSONresponse = null;
        try {
            JSONresponse = webRequest.getJSONObjectResponce();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.code() != 200) {
            logger.error(JSONresponse.toString(4));
            throw new RuntimeException("Youtube response code != 200");
        }

        JSONObject videoData = JSONresponse.getJSONArray("items").getJSONObject(0);
        String ytVideoID = videoData.getJSONObject("id").getString("videoId");
        videoData = videoData.getJSONObject("snippet");

        String ytVideoTitle = videoData.getString("title");
        String ytVideoDescription = videoData.getString("description");
        String ytVideoThumbnail = videoData.getJSONObject("thumbnails").getJSONObject("high").getString("url");

        return new youtubeVideo(ytVideoTitle,ytVideoDescription,ytVideoID,ytVideoThumbnail);
    }

}
