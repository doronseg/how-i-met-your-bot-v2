package me.nerdoron.himyb.commands;

import me.nerdoron.himyb.commands.funcommands.ReplyCommand;
import me.nerdoron.himyb.commands.funcommands.SayCommand;
import me.nerdoron.himyb.commands.usefulcommands.AFKCommand;
import me.nerdoron.himyb.commands.usefulcommands.PingCommand;
import me.nerdoron.himyb.commands.usefulcommands.ReviveCommand;
import me.nerdoron.himyb.commands.usefulcommands.SuggestCommand;
import me.nerdoron.himyb.commands.usefulcommands.UptimeCommand;
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
        }
    }

}
