package me.nerdoron.himyb.modules.tickets.transcript;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class GenerateTranscript {

    public static String generate(TextChannel channel) {
        MessageHistoryRetriever retriever = new MessageHistoryRetriever();
        retriever.getHistory(channel);

        while (!retriever.isReady) {
            try {Thread.sleep(100);} catch (InterruptedException e) {}
        }

        ArrayList<Message> messages = retriever.output;
        StringBuilder builder = new StringBuilder();

        for (Message message : messages) {
            String tag = message.getAuthor().getAsTag();
            String content = message.getContentRaw();
            OffsetDateTime time = message.getTimeCreated();
            String parsed = parseTime(time);

            builder.append(parsed+tag+": "+content+"\n");
        }

        return builder.toString();
    }

    public static String numberHelper(long N){
        if (N < 10) {
            return "0"+N;
        } else {
            return ""+N;
        }
    }

    public static String parseTime(OffsetDateTime time) {
        return "["+ numberHelper(time.getHour())+":"+numberHelper(time.getMinute())+":"+numberHelper(time.getSecond())+"] ";
    }

}
