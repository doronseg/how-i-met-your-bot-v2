package me.nerdoron.himyb.modules.youtube;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.modules.Database;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class YoutubeManager {
    private static final Logger logger = LoggerFactory.getLogger(YoutubeManager.class);
    private static final Connection con = Database.connect();
    private static final String ytKey = Global.DOTENV.get("YTAPI");
    private static final String ytChannelID_Oscar = "UCqj-7XzxhgL14RKORG1UIrw";
    private static final String ytChannelID_OscarPlus = "UC3dr7tOM3v0ulzz-DSfJTIg";
    private final int checkInterval_MS = Global.ms_1minute * 5;
    private final String guildID = "991446766169903165";
    private final String guildChannel = "1002977864502759424";
    private static final int results = 1;
    private final JDA jda;

    public YoutubeManager(JDA jda) {
        this.jda = jda;
        // Let discord load b4 starting the loop
        new Thread(() -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
            }
            loop();
        }).start();
    }

    public void loop() {
        new Thread(() -> {
            execute(ytChannelID_Oscar,"Oscar");
            execute(ytChannelID_OscarPlus,"Oscar+");
            try {
                Thread.sleep(checkInterval_MS);
            } catch (InterruptedException e) {
            }
            loop();
        }).start();
    }

    private void execute(String channelID, String channelName) {
        GuildChannel gchannel = null;
        youtubeVideo latestVideo = getLatestVideoYT(channelID);
        String latestVideoDB = getLatestVideoDB(channelID);

        for (GuildChannel channel1 : jda.getGuildById(guildID).getChannels()) {
            if (channel1.getId().equals(guildChannel)) {
                gchannel = channel1;
            }
        }
        NewsChannel channel = ((NewsChannel) gchannel);

        if (!latestVideoDB.equals(latestVideo.getVideoID())) {
            updateDB(channelID,latestVideo.getVideoID());
            channel.sendMessage("@Mention\n" +
                    ":mega: __**"+channelName+" uploaded a new video!**__ :mega:\n" +
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
    }

    public static youtubeVideo getLatestVideoYT(String ytChannelID) {
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
    public static String getLatestVideoDB(String channelID) {
        String r = "";
        try {
            String SQL = "SELECT latestvideo FROM youtube WHERE channeluid=?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, channelID);
            ResultSet rs = ps.executeQuery();
            r = rs.getString(1);
            ps.close();
        } catch (SQLException e) {
            logger.error("Error while trying to fetch the latest videoID from the database", e.getCause().getMessage());
            throw new RuntimeException(e);
        }
        return r;
    }

    public static void updateDB(String channelID, String videoID) {
        try {
            String SQL = "UPDATE youtube SET latestvideo = ? WHERE channeluid = ?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, videoID);
            ps.setString(2, channelID);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            logger.error("Error while trying updating the DB with the latest video of "+channelID, e.getCause().getMessage());
            throw new RuntimeException(e);
        }
    }

}
