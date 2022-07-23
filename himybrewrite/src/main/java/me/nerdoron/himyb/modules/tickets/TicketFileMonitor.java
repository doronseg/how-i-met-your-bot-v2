package me.nerdoron.himyb.modules.tickets;

import me.nerdoron.himyb.Global;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TicketFileMonitor extends ListenerAdapter {

    public Map<String, ArrayList<Message>> linker = new HashMap<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        TextChannel monitorChannel = event.getGuild().getTextChannelById(Global.ticketFileMonitorChannel);
        if (!event.isFromGuild()) return;
        Message message = event.getMessage();
        if (message.getAttachments().isEmpty()) return;
        if (!event.getChannel().getName().startsWith("admin-") && !event.getChannel().getName().startsWith("ticket-")) return;

        for (Message.Attachment attachment : message.getAttachments()) {
            attachment.getProxy().downloadToFile(new File("./" + attachment.getFileName())).whenComplete(
                    (__, err) -> {
                        if (__ == null) {
                            err.printStackTrace();
                            return;
                        }

                        MessageBuilder msb = new MessageBuilder();
                        msb.setContent("From: "+message.getMember().getAsMention()+"\nIn channel: "+message.getChannel().getName());
                        msb.denyMentions(Message.MentionType.values());

                        monitorChannel.sendMessage(msb.build()).addFile(__).queue(
                                (msgInMonitor) -> {
                                    if (linker.containsKey(message.getId())) {
                                        linker.get(message.getId()).add(msgInMonitor);
                                    } else {
                                        linker.put(message.getId(), new ArrayList<>(Arrays.asList(msgInMonitor)));
                                    }

                                    __.delete();
                                },
                                (error) -> {
                                    error.printStackTrace();
                                    __.delete();
                                }
                        );
                    }
            );
        }
    }
}
