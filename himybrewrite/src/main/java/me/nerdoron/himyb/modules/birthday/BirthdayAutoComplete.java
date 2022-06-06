package me.nerdoron.himyb.modules.birthday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class BirthdayAutoComplete extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        // months
        ArrayList<String> months = new ArrayList<>(List.of("January", "February", "March", "April", "May", "June", "July",
                "August",
                "September", "October", "November", "December"));

        if (event.getName().equals("birthday") && event.getFocusedOption().getName().equals("month")) {
            ArrayList<Command.Choice> choices = new ArrayList<>();
            for (String month : months) {
                choices.add(new Command.Choice(month, months.indexOf(month)));
            }
            event.replyChoices(choices).queue();
        }

        // days
        if (event.getName().equals("birthday") && event.getFocusedOption().getName().equals("day")) {
            ArrayList<Command.Choice> days = new ArrayList<Command.Choice>();
            for (int i = 0; i > 32; i++) {
                days.add(new Command.Choice(days+"", days+""));
            }
            event.replyChoices(days).queue();
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
