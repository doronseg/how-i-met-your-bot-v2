package me.nerdoron.himyb.modules;

import java.util.Arrays;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NotifyOfChange extends ListenerAdapter {
    private String[] oldCommands = new String[] { "*help", "*uptime", "*ping", "*afk", "*ssuggest", "*vsuggest",
            "*psuggest", "*bsuggest" };

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (stringContainsItemFromList(event.getMessage().getContentDisplay(), oldCommands)) {
            event.getMessage().reply("Be advised, we moved to slash commands. Use /help for more information").queue();
        }
    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).anyMatch(inputStr::contains);
    }
}
