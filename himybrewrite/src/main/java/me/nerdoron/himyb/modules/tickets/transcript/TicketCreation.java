package me.nerdoron.himyb.modules.tickets.transcript;

import me.nerdoron.himyb.modules.tickets.Panels;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;

public class TicketCreation extends ListenerAdapter {
        private final EnumSet<Permission> perms;
        private final String AdminStaffID = "850458148723621888";

        public TicketCreation() {
                this.perms = EnumSet.of(Permission.MESSAGE_HISTORY, Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
        }

        @Override
        public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
                if (!event.getComponent().getId().equals("ticketSelector"))
                        return;

                Member member = event.getMember();
                SelectOption selectOption = event.getSelectedOptions().get(0);
                String channelName = "";
                MessageEmbed greetingEmbed = null;

                switch (selectOption.getValue()) {
                        case "ticket_admin":
                                channelName = "admin-";
                                greetingEmbed = Panels.adminWelcome;
                                break;
                        case "ticket_staff":
                                channelName = "ticket-";
                                greetingEmbed = Panels.generalWelcome;
                                break;
                }

                channelName += member.getUser().getName().substring(0, 4) + "-" + member.getUser().getDiscriminator();

                TextChannel ticketChannel = event.getGuild().getCategoryById(AdminStaffID)
                                .createTextChannel(channelName)
                                .addPermissionOverride(event.getGuild().getPublicRole(),
                                                new ArrayList<>(),
                                                perms)
                                .addPermissionOverride(member, perms, new ArrayList<>()).complete();
                event.deferReply().setEphemeral(true)
                                .setContent("Created a ticket for you, " + ticketChannel.getAsMention())
                                .queue();

                MessageBuilder msb = new MessageBuilder();
                msb.setContent(member.getAsMention());
                msb.setEmbeds(greetingEmbed);
                msb.setActionRows(ActionRow.of(Button.danger("closeTicket", "Close this ticket")));
                ticketChannel.sendMessage(msb.build()).queue();

        }

}
