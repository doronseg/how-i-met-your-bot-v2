package me.nerdoron.himyb.modules;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

public class SuggestCommandAutoComplete extends ListenerAdapter {
    private String[] types = new String[] { "server", "video", "bot", "chain" };

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {

        if (event.getName().equals("suggest") && event.getFocusedOption().getName().equals("type")) {
            List<Command.Choice> options = Stream.of(types)
                    .filter(type -> type.startsWith(event.getFocusedOption().getValue()))
                    .map(type -> new Command.Choice(type, type))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }

}
