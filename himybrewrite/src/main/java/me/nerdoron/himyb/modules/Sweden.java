package me.nerdoron.himyb.modules;

import java.io.File;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Sweden extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if ((event.getMessage().getContentDisplay().toLowerCase().contains("swedish"))
                || (event.getMessage().getContentDisplay().toLowerCase().contains("sweden"))) {
            if (event.getAuthor().isBot())
                return;
            if (event.getChannel().getId().equals("850628227835363358")
                    || (event.getChannel().getId().equals("850625575856504852")))
                return;
            event.getChannel().sendMessage("🇸🇪").addFile(new File("sweden.mp4")).queue();
        }
    }

}
