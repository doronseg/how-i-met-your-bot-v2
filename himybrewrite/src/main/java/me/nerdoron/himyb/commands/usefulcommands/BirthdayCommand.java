package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.birthday.BirthdayChecks;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BirthdayCommand extends SlashCommand {
    BirthdayChecks birthdayChecks = new BirthdayChecks();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (birthdayChecks.hasBirthdaySet(event.getUser().getId()) == true) {
            event.deferReply().setEphemeral(true).setContent("You already have a birthday set!").queue();
            return;
        }

    }

}
