package me.nerdoron.himyb.modules.tickets;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class CloseTicketButton extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!(event.getChannel().getName().startsWith("ticket-"))
                && !(event.getChannel().getName().startsWith("admin-"))
                && !(event.getChannel().getName().startsWith("selfpromo"))) {
            event.reply("This isn't a ticket channel!").setEphemeral(true).queue();
            return;
        }
        event.getTextChannel().getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singleton(Permission.MESSAGE_SEND)).queue(
                (__) -> {
                    event.reply("Ticket closed!").queue();
                }
        );
    }
}
