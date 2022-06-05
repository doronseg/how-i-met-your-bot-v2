package me.nerdoron.himyb.modules.counting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CountingChannelHandler extends ListenerAdapter {
    static final Logger logger = LoggerFactory.getLogger(CountingChannelHandler.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMember() == null)
            return;
        if (!(event.getChannel().getId().equals("900090953233231964")))
            return;
        if (event.getMember().getUser().isBot()) {
            event.getMessage().delete().queue();
            return;
        }

        String uid = event.getMember().getId();
        String messageContent = event.getMessage().getContentDisplay();
        int numberContent;
        try {
            numberContent = Integer.parseInt(messageContent);
        } catch (NumberFormatException e) {
            event.getMessage().delete().queue();
            return;
        }

        event.getChannel().getHistory().retrievePast(2).map(messages -> messages.get(1)).queue(message -> {

            String oldMessageContent = message.getContentDisplay();
            int oldNumberContent;

            try {
                oldNumberContent = Integer.parseInt(oldMessageContent);
            } catch (NumberFormatException e) {
                event.getMessage().delete().queue();
                return;
            }

            if (!(numberContent == oldNumberContent + 1)) {
                event.getMessage().delete().queue();
                return;
            }

            if (message.getAuthor().getId().equals(uid)) {
                event.getMessage().delete().queue();
                return;
            }

            if (message.getStickers().size() > 0) {
                event.getMessage().delete().queue();
                return;
            }
        });

    }

}
