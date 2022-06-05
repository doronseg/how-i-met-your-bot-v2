package me.nerdoron.himyb.modules.chainchannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChainChannelHandler extends ListenerAdapter {
    static final Logger logger = LoggerFactory.getLogger(ChainChannelHandler.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMember() == null)
            return;
        if (!(event.getChannel().getId().equals("880421327494873098")))
            return;
        if (event.getMember().getUser().isBot()) {
            event.getMessage().delete().queue();
            return;
        }
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE))
            return;

        String uid = event.getMember().getId();
        String messageContent = event.getMessage().getContentDisplay();

        event.getChannel().getHistory().retrievePast(2).map(messages -> messages.get(1)).queue(message -> {
            if (!(message.getContentDisplay().equals(messageContent))) {
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
