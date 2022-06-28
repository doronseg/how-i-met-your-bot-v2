package me.nerdoron.himyb.commands.staffcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.tickets.Panels;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

public class SendPannelCommand extends SlashCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Sending panels").queue();
        MessageBuilder msb = new MessageBuilder();
        msb.setEmbeds(Panels.AdminStaff);
        SelectMenu.Builder selectMenu = SelectMenu.create("ticketSelector");
        selectMenu.addOption("📇 Admin Ticket", "ticket_admin");
        selectMenu.addOption("📇 Staff Ticket", "ticket_staff");
        msb.setActionRows(ActionRow.of(selectMenu.build()));
        event.getChannel().sendMessage(msb.build()).queue();
    }
}
