package me.nerdoron.himyb.modules.applications;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

public class ApplyAutoComplete extends ListenerAdapter {
    String[] positions = new String[] {
            "Event Manager"
    };

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {

        if (event.getName().equals("apply") && event.getFocusedOption().getName().equals("position")) {
            List<Command.Choice> options = Stream.of(positions)
                    .filter(position -> position.startsWith(event.getFocusedOption().getValue()))
                    .map(position -> new Command.Choice(position, position))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }

}
