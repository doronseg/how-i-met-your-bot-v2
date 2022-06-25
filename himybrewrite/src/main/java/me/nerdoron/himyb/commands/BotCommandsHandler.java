package me.nerdoron.himyb.commands;

import me.nerdoron.himyb.commands.funcommands.EightBall;
import me.nerdoron.himyb.commands.funcommands.ReplyCommand;
import me.nerdoron.himyb.commands.funcommands.SayCommand;
import me.nerdoron.himyb.commands.usefulcommands.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommandsHandler extends ListenerAdapter {
    PingCommand pingCommand = new PingCommand();
    UptimeCommand uptimeCommand = new UptimeCommand();
    ReviveCommand reviveCommand = new ReviveCommand();
    SuggestCommand suggestCommand = new SuggestCommand();
    AFKCommand afkCommand = new AFKCommand();
    SayCommand sayCommand = new SayCommand();
    ReplyCommand replyCommand = new ReplyCommand();
    HelpCommand helpCommand = new HelpCommand();
    ApplyCommand applyCommand = new ApplyCommand();
    BirthdayCommand birthdayCommand = new BirthdayCommand();
    TimezoneCommand timezoneCommand = new TimezoneCommand();
    MytimeCommand mytimeCommand = new MytimeCommand();
    WhatTimeCommand whatTimeCommand = new WhatTimeCommand();
    EightBall eightBall = new EightBall();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping":
                pingCommand.execute(event);
                break;
            case "uptime":
                uptimeCommand.execute(event);
                break;
            case "revive":
                reviveCommand.execute(event);
                break;
            case "suggest":
                suggestCommand.execute(event);
                break;
            case "afk":
                afkCommand.execute(event);
                break;
            case "say":
                sayCommand.execute(event);
                break;
            case "reply":
                replyCommand.execute(event);
                break;
            case "help":
                helpCommand.execute(event);
                break;
            case "birthday":
                birthdayCommand.execute(event);
                break;
            case "apply":
                applyCommand.execute(event);
                break;
            case "timezone":
                timezoneCommand.execute(event);
                break;
            case "mytime":
                mytimeCommand.execute(event);
                break;
            case "whattime":
                whatTimeCommand.execute(event);
                break;
            case "8ball":
                eightBall.execute(event);
                break;
        }
    }

}
