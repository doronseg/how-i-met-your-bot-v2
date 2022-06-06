package me.nerdoron.himyb;

import me.nerdoron.himyb.commands.BotCommandsHandler;
import me.nerdoron.himyb.modules.FriendsCringe;
import me.nerdoron.himyb.modules.LeaveJoin;
import me.nerdoron.himyb.modules.NotifyOfChange;
import me.nerdoron.himyb.modules.SuggestCommandAutoComplete;
import me.nerdoron.himyb.modules.Sweden;
import me.nerdoron.himyb.modules.YoutubeNotifications;
import me.nerdoron.himyb.modules.afk.AFKMessageEvent;
import me.nerdoron.himyb.modules.applications.ApplyAutoComplete;
import me.nerdoron.himyb.modules.applications.EventManagerApplicationHandler;
import me.nerdoron.himyb.modules.birthday.BirthdayAutoComplete;
import me.nerdoron.himyb.modules.chainchannel.ChainChannelHandler;
import me.nerdoron.himyb.modules.chainchannel.ChainEditing;
import me.nerdoron.himyb.modules.counting.CountingChannelHandler;
import me.nerdoron.himyb.modules.counting.CountingEditing;
import me.nerdoron.himyb.modules.help.HelpButtonHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;

public class RegisterEvents {

        public static void registration(JDA jda) {
                // command registration
                ArrayList<CommandData> slashCommands = new ArrayList<>();
                jda.addEventListener(new BotCommandsHandler());

                slashCommands.add(Commands.slash("ping", "Calculate te ping of the bot."));
                slashCommands.add(Commands.slash("uptime", "Show the bot's uptime."));
                slashCommands.add(Commands.slash("revive", "Send the chat revive message")
                        .setDefaultPermissions(Permission.MESSAGE_MANAGE));
                slashCommands.add(Commands.slash("suggest", "Send a suggestion")
                        .addOption(OptionType.STRING, "type", "Select the type of suggestion",
                                true, true)
                        .addOption(OptionType.STRING, "suggestion", "Describe your suggestion",
                                true));
                slashCommands.add(Commands.slash("afk", "Go AFK")
                        .addOption(OptionType.STRING, "reason", "Why are you away?", true));
                slashCommands.add(Commands.slash("say", "Make the bot say something")
                        .addOption(OptionType.CHANNEL, "channel",
                                "Which channel would you like to say it in?", true)
                        .addOption(OptionType.STRING, "message", "What would you like to say?",
                                true)
                        .setDefaultPermissions(Permission.MESSAGE_MANAGE));
                slashCommands.add(Commands.slash("reply", "Make the bot reply to a message")
                        .addOption(OptionType.STRING, "replyto",
                                "What message would you like to reply to? (ID ONLY, MAKE SURE YOU ARE IN THE SAME CHANNEL)",
                                true)
                        .addOption(OptionType.STRING, "message", "What would you like to say?",
                                true)
                        .setDefaultPermissions(Permission.MESSAGE_MANAGE));
                slashCommands.add(Commands.slash("help", "Displays the help menu."));
                slashCommands.add(Commands.slash("apply", "Apply for an open staff position.")
                        .addOption(OptionType.STRING, "position",
                                "What position would you like to apply to?", true,
                                true));
                slashCommands.add(Commands.slash("birthday", "Sets your birthday.")
                        .addOption(OptionType.STRING, "month",
                                "Select the month in which you were born.", true, true)
                        .addOption(OptionType.STRING, "day",
                                "Select the day in which you were born.", true, false)); // Discord limits autocompletes to 25 options

                jda.updateCommands().addCommands(slashCommands).queue();

                // event registration
                jda.addEventListener(new FriendsCringe());
                jda.addEventListener(new LeaveJoin());
                jda.addEventListener(new Sweden());
                jda.addEventListener(new YoutubeNotifications());
                jda.addEventListener(new CountingChannelHandler());
                jda.addEventListener(new CountingEditing());
                jda.addEventListener(new ChainChannelHandler());
                jda.addEventListener(new ChainEditing());
                jda.addEventListener(new SuggestCommandAutoComplete());
                jda.addEventListener(new NotifyOfChange());
                jda.addEventListener(new AFKMessageEvent());
                jda.addEventListener(new HelpButtonHandler());
                jda.addEventListener(new ApplyAutoComplete());
                jda.addEventListener(new EventManagerApplicationHandler());
                jda.addEventListener(new BirthdayAutoComplete());

        }

}
