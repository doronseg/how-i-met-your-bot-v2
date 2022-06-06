package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.birthday.BirthdayChecks;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Calendar;

public class BirthdayCommand extends SlashCommand {
    BirthdayChecks birthdayChecks = new BirthdayChecks();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (birthdayChecks.hasBirthdaySet(event.getUser().getId()) == true) {
            event.deferReply().setEphemeral(true).setContent("You already have a birthday set!").queue();
            return;
        }

        int month = event.getOption("month").getAsInt();
        int day = event.getOption("day").getAsInt();

        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), month-1, day);

        event.reply("Your birthday has been set to the "+dayNumber(c.get(Calendar.DAY_OF_MONTH))+ " of "+c.get(Calendar.MONTH)).setEphemeral(true).queue();

        //Set to DB stuff
    }

    // Helper method to say the days in a cardinal way
    public String dayNumber(int day) {
        switch (day) {
            case 1: return "1st";
            case 2: return "2nd";
            case 3: return "3rd";
            case 21: return "21st";
            case 22: return "22nd";
            case 23: return "23rd";
            case 31: return "31st";
            default: return day+"th";
        }
    }

}
