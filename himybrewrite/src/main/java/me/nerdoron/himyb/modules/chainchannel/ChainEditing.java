package me.nerdoron.himyb.modules.chainchannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChainEditing extends ListenerAdapter {
    static final Logger logger = LoggerFactory.getLogger(ChainChannelHandler.class);

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE))
            return;

        if (event.getChannel().getId().equals("880421327494873098")) {
            event.getMessage().delete().queue();
        }
    }

}