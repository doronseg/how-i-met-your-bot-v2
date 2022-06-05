package me.nerdoron.himyb.modules.counting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CountingEditing extends ListenerAdapter {
    static final Logger logger = LoggerFactory.getLogger(CountingEditing.class);

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE))
            return;

        if (event.getChannel().getId().equals("900090953233231964")) {
            event.getMessage().delete().queue();
        }
    }

}