package me.nerdoron.himyb.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

public class SuggestCommandAutoComplete extends ListenerAdapter {
    ArrayList<String> types = new ArrayList<>(List.of(new String[]{"server", "video", "bot", "chain"}));

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {

        if (event.getName().equals("suggest") && event.getFocusedOption().getName().equals("type")) {
            ArrayList<Command.Choice> options = new ArrayList<>();
            for (String type : types) {
                options.add(new Command.Choice(type, type));
            }

            event.replyChoices(options).queue();
        }

    }

}
