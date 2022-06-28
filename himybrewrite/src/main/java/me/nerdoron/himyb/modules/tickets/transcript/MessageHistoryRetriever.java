package me.nerdoron.himyb.modules.tickets.transcript;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class MessageHistoryRetriever {
    Boolean isReady = false;
    ArrayList<Message> output = new ArrayList<>();

    public MessageHistoryRetriever() {
    }

    public void getHistory(TextChannel channel) {
        Guild guild = channel.getGuild();
        int msgs = 500;

        channel.getIterableHistory()
                .takeAsync(msgs)
                .thenApplyAsync((messages) -> {
                    ArrayList<Message> msgList = new ArrayList<>(messages);
                    for (Message message : msgList) {
                        if (!message.getAuthor().isBot()) {
                            output.add(message);
                        }
                    }
                    isReady = true;
                    return 0;
                })
                .exceptionally((thr) -> {
                    // In case it errors out
                    return 0;
                });

    }
}
