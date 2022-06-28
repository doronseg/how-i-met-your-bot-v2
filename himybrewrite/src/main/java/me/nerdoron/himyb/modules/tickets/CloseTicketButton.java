package me.nerdoron.himyb.modules.tickets;

import me.nerdoron.himyb.modules.tickets.transcript.GenerateTranscript;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;

public class CloseTicketButton extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("closeTicket"))
            return;
        if (!(event.getChannel().getName().startsWith("ticket-"))
                && !(event.getChannel().getName().startsWith("admin-"))
                && !(event.getChannel().getName().startsWith("selfpromo"))) {
            event.reply("This isn't a ticket channel!").setEphemeral(true).queue();
            return;
        }
        event.getTextChannel().getManager().clearOverridesAdded().queue();
        event.getTextChannel().getManager()
                .putPermissionOverride(event.getGuild().getPublicRole(), null,
                        Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                .queue(
                        (__) -> {
                            event.reply("Closing ticket.").queue();
                        });
        sendTranscript(event);

    }

    public static void sendTranscript(@NotNull ButtonInteractionEvent event) {
        new Thread(() -> {
            String transcript = GenerateTranscript.generate(event.getTextChannel());
            File f = new File(event.getTextChannel().getName() + ".txt");

            event.getTextChannel().getHistoryFromBeginning(1).queue(
                    messageHistory -> {
                        Message message = messageHistory.getRetrievedHistory().get(0);
                        User ticketAuthor = message.getMentions().getUsers().get(0);
                        try {
                            writeToFile(f, transcript.split("\n"), true);
                            TextChannel transcriptChannel = null;
                            if (event.getChannel().getName().startsWith("ticket"))
                                transcriptChannel = event.getGuild().getTextChannelById("991294991517360200");
                            if (event.getChannel().getName().startsWith("admin"))
                                transcriptChannel = event.getGuild().getTextChannelById("991376488010109008");

                            EmbedBuilder emb = new EmbedBuilder();
                            emb.setColor(Color.decode("#2f3136"));
                            emb.setDescription(
                                    "**Transcript from " + ticketAuthor.getAsMention() + "'s ticket**" + "\n" +
                                            "TicketID: " + event.getTextChannel().getName() + "\n" +
                                            "Closed at: " + getAsTimeThing(event.getTimeCreated(), "f"));
                            transcriptChannel.sendMessageEmbeds(emb.build()).addFile(f).queue();
                            event.getTextChannel().delete().queue();
                            f.delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        }).start();
    }

    public static void writeToFile(File file, String[] lines, boolean newLine) throws IOException {
        FileWriter writer = new FileWriter(file);
        for (String line : lines) {
            writer.write(line);
            if (newLine) {
                writer.write("\n");
            }
        }
        writer.close();
    }

    public static String getAsTimeThing(OffsetDateTime time, String letterAtTheEnd) {
        return "<t:" + time.toEpochSecond() + ":" + letterAtTheEnd + ">";
    }
}
