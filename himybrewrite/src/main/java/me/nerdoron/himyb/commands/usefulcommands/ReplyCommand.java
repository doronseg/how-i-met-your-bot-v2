package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ReplyCommand extends Command {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)))
            return;
        String messageId = event.getOption("replyto").getAsString();
        Message messageToReply = event.getTextChannel().retrieveMessageById(messageId).complete();
        String message = event.getOption("message").getAsString();
        messageToReply.reply(message).queue();
        event.deferReply().setEphemeral(true).setContent("Sent your message.").queue();

    }

}
