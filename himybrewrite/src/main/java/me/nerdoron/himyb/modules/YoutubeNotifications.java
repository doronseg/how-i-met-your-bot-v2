package me.nerdoron.himyb.modules;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class YoutubeNotifications extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals("850436948350992424")) {
            if (event.getAuthor().getId().equals("204255221017214977")) {
                event.getChannel().sendMessage("<@&959910598198579230> watch now! 🔫").queue();
            }
        }
    }

}
