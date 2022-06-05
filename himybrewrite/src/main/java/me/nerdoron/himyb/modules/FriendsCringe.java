package me.nerdoron.himyb.modules;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class FriendsCringe extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentDisplay().toLowerCase().contains("friends")) {
            if (event.getAuthor().isBot())
                return;
            if (event.getChannel().getId().equals("850628227835363358")
                    || (event.getChannel().getId().equals("850625575856504852")))
                return;
            event.getChannel().sendMessage("friends? cringe lol").queue();
        }
    }

}
