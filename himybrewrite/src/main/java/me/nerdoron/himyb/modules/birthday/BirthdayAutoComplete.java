package me.nerdoron.himyb.modules.birthday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

public class BirthdayAutoComplete extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        // months
        String[] months = new String[] { "January", "Feburary", "March", "April", "May", "June", "July",
                "August",
                "September", "October", "November", "December" };
        if (event.getName().equals("birthday") && event.getFocusedOption().getName().equals("month")) {
            List<Command.Choice> options = Stream.of(months)
                    .filter(month -> month.startsWith(event.getFocusedOption().getValue()))
                    .map(month -> new Command.Choice(month, month))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }

        // days
        if (event.getName().equals("birthday") && event.getFocusedOption().getName().equals("day")) {
            List<String> days = new ArrayList<String>();
            for (int i = 0; i > 32; i++)
                days.add(i + "");

        }

        // days
        // if (event.getName().equals("birthday") &&
        // event.getFocusedOption().getName().equals("day")) {
        // List<Integer> days = new ArrayList<Integer>();
        // for (int i = 0; i > 32; i++)
        // days.add(i);

        // List<Command.Choice> options = Stream.of(days)
        // .filter(day ->
        // day.toString().startsWith(event.getFocusedOption().getValue()))
        // .map(day -> new Command.Choice(day, day))
        // .collect(Collectors.toList());
        // event.replyChoices(options).queue();

        // }
    }

}
