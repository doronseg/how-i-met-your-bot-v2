package me.nerdoron.himyb.modules.jinx;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JinxHandler extends ListenerAdapter {
    private ArrayList<Message> messages = new ArrayList<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        //if (event.getMember().getPermissions().contains(Permission.MESSAGE_MANAGE)) return;

        boolean jinxHappend = false;
        Message pastMsg = null;
        Message presentMsg = null;

        messages.add(event.getMessage());
        ArrayList<Message> clone = new ArrayList<>(messages);
        Collections.reverse(clone);

        Message pastInArray = null;
        for (Message message : clone) {
            if (pastInArray == null) {
                pastInArray = message;
                continue;
            }

            if (pastInArray.getContentRaw().equalsIgnoreCase(message.getContentRaw()) && pastInArray.getChannel().getId().equals(message.getChannel().getId()) && !pastInArray.getAuthor().getId().equals(message.getAuthor().getId())) {
                // A jinx Happened
                jinxHappend = true;
                pastMsg = pastInArray;
                presentMsg = message;
                break;
            } else {
                pastInArray = message;
                if (messages.size() > 100) messages.remove(0);
                break;
            }
        }

        if (jinxHappend) {
            pastMsg.addReaction(Emoji.fromUnicode("⛓️")).queue();
            presentMsg.addReaction(Emoji.fromUnicode("⛓️")).queue();
        }

    }
}
