package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.applications.EventManagerModal;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ApplyCommand extends SlashCommand {

    EventManagerModal eventManagerModal = new EventManagerModal();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String position = event.getOption("position").getAsString();
        if (!(event.isFromGuild())) {
            event.deferReply().setEphemeral(true).setContent("This command can only be executed in the server.")
                    .queue();
            return;
        }

        if (position.equals("Event Manager")) {
            event.replyModal(eventManagerModal.modal).queue();
        }
    }

}
