package me.nerdoron.himyb.modules._bot;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.commands.currencycommands.BankCommand;
import me.nerdoron.himyb.commands.currencycommands.SetCoinsCommand;
import me.nerdoron.himyb.commands.funcommands.EightBallCommand;
import me.nerdoron.himyb.commands.funcommands.ReplyCommand;
import me.nerdoron.himyb.commands.funcommands.SayCommand;
import me.nerdoron.himyb.commands.funcommands.gambling.CoinFlipCommand;
import me.nerdoron.himyb.commands.staffcommands.RemoveBirthdayCommand;
import me.nerdoron.himyb.commands.staffcommands.SendPannelCommand;
import me.nerdoron.himyb.commands.usefulcommands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;

public class BotCommandsHandler extends ListenerAdapter {
    ArrayList<SlashCommand> commands = new ArrayList<>();

    public BotCommandsHandler() {
        commands.add(new PingCommand());
        commands.add(new UptimeCommand());
        commands.add(new ReviveCommand());
        commands.add(new SuggestCommand());
        commands.add(new AFKCommand());
        commands.add(new SayCommand());
        commands.add(new ReplyCommand());
        commands.add(new HelpCommand());
        commands.add(new ApplyCommand());
        commands.add(new BirthdayCommand());
        commands.add(new TimezoneCommand());
        commands.add(new MytimeCommand());
        commands.add(new WhatTimeCommand());
        commands.add(new EightBallCommand());
        commands.add(new SendPannelCommand());
        commands.add(new SelfPromoCommand());
        commands.add(new RemoveBirthdayCommand());
        commands.add(new BankCommand());
        commands.add(new CoinFlipCommand());
        commands.add(new SetCoinsCommand());
    }

    public void updateCommandsOnDiscord(JDA jda) {
        ArrayList<CommandData> slashes = new ArrayList<>();
        for (SlashCommand command : commands) {
            slashes.add(command.getSlash());
        }
        jda.updateCommands().addCommands(slashes).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        for (SlashCommand command : commands) {
            if (command.getSlash().getName().equals(commandName)) {
                command.execute(event);
                break;
            }
        }
    }

}
