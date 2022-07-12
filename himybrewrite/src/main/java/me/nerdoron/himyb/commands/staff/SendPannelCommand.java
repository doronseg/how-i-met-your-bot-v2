package me.nerdoron.himyb.commands.staff;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.tickets.Panels;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
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

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData cmd = Commands.slash("pannels", "Send the ticket pannels")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        return cmd;
    }
}
