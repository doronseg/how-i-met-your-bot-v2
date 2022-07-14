package me.nerdoron.himyb.modules._bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.commands.currency.BankCommand;
import me.nerdoron.himyb.commands.currency.DailyCommand;
import me.nerdoron.himyb.commands.currency.MonthlyCommand;
import me.nerdoron.himyb.commands.fun.gambling.BlackjackCommand;
import me.nerdoron.himyb.commands.staff.*;
import me.nerdoron.himyb.commands.fun.*;
import me.nerdoron.himyb.commands.fun.gambling.CoinFlipCommand;
import me.nerdoron.himyb.commands.useful.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;

public class BotCommandsHandler extends ListenerAdapter {
    public ArrayList<SlashCommand> commands = new ArrayList<>();

    public BotCommandsHandler(EventWaiter waiter) {
        Global.COMMANDS_HANDLER = this;
        commands.add(new HelpCommand(this));
        commands.add(new PingCommand());
        commands.add(new UptimeCommand());
        commands.add(new ReviveCommand());
        commands.add(new SuggestCommand());
        commands.add(new AFKCommand());
        commands.add(new SayCommand());
        commands.add(new ReplyCommand());
        commands.add(new ApplyCommand());
        commands.add(new BirthdayCommand());
        commands.add(new TimezoneCommand());
        commands.add(new TimeCommand());
        commands.add(new EightBallCommand());
        commands.add(new SendPannelCommand());
        commands.add(new SelfPromoCommand());
        commands.add(new RemoveBirthdayCommand());
        commands.add(new BankCommand());
        commands.add(new CoinFlipCommand());
        commands.add(new SetCoinsCommand());
        commands.add(new WorkCommand());
        commands.add(new CrimeCommand());
        commands.add(new MonthlyCommand());
        commands.add(new DailyCommand());
        commands.add(new BlackjackCommand(waiter));
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

    public String getCategoryDetailedName(String category) {
        switch (category) {
            case "useful":
                return "🛠️ Useful Commands";
            case "fun":
                return "🦩 Fun Commands";
            case "currency":
                return "🪙 Currency Commands";
            default:
                return null;
        }
    }

}
