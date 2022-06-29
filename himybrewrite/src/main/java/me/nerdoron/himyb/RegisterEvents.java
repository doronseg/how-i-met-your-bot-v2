package me.nerdoron.himyb;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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
import me.nerdoron.himyb.modules.birthday.BirthdayFunction;
import me.nerdoron.himyb.modules.chainchannel.ChainChannelHandler;
import me.nerdoron.himyb.modules.chainchannel.ChainEditing;
import me.nerdoron.himyb.modules.counting.CountingChannelHandler;
import me.nerdoron.himyb.modules.counting.CountingEditing;
import me.nerdoron.himyb.modules.help.HelpButtonHandler;
import me.nerdoron.himyb.modules.selfpromo.SelfPromoHandler;
import me.nerdoron.himyb.modules.selfpromo.SubmitLinks;
import me.nerdoron.himyb.modules.tickets.CloseTicketButton;
import me.nerdoron.himyb.modules.tickets.transcript.TicketCreation;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.ArrayList;

public class RegisterEvents {

        public static void registration(JDA jda) {
                EventWaiter waiter = new EventWaiter();
                // command registration
                ArrayList<CommandData> slashCommands = new ArrayList<>();
                jda.addEventListener(new BotCommandsHandler());

                slashCommands.add(Commands.slash("ping", "Calculate te ping of the bot."));
                slashCommands.add(Commands.slash("uptime", "Show the bot's uptime."));
                slashCommands.add(Commands.slash("revive", "Send the chat revive message")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
                slashCommands.add(Commands.slash("suggest", "Send a suggestion")
                                .addOption(OptionType.STRING, "type", "Select the type of suggestion", true, true)
                                .addOption(OptionType.STRING, "suggestion", "Describe your suggestion", true));
                slashCommands.add(Commands.slash("afk", "Go AFK")
                                .addOption(OptionType.STRING, "reason", "Why are you away?", true));

                SlashCommandData say = Commands.slash("say", "Make the bot say something");
                OptionData channel = new OptionData(OptionType.CHANNEL, "channel",
                                "Which channel would you like to say it in?", true);
                channel.setChannelTypes(ChannelType.TEXT); // Limit this option to TextChannels
                say
                                .addOption(OptionType.CHANNEL, "channel", "What channel would you like to send it to?",
                                                true)
                                .addOption(OptionType.STRING, "message", "What would you like to say?", true)
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE));
                slashCommands.add(say);

                slashCommands.add(Commands.slash("reply", "Make the bot reply to a message")
                                .addOption(OptionType.STRING, "replyto",
                                                "What message would you like to reply to? (ID ONLY, MAKE SURE YOU ARE IN THE SAME CHANNEL)",
                                                true)
                                .addOption(OptionType.STRING, "message", "What would you like to say?", true)
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
                slashCommands.add(Commands.slash("help", "Displays the help menu."));
                slashCommands.add(Commands.slash("apply", "Apply for an open staff position.")
                                .addOption(OptionType.STRING, "position", "What position would you like to apply to?",
                                                true, true));

                SlashCommandData birthday = Commands.slash("birthday", "Sets your birthday.");
                birthday.addOption(OptionType.STRING, "month", "Select the month in which you were born.", true, true);
                OptionData day = new OptionData(OptionType.INTEGER, "day", "Select the day in which you were born.",
                                true);// Discord limits autocompletes to 25 options
                day.setMinValue(1);
                day.setMaxValue(31);
                birthday.addOptions(day);
                slashCommands.add(birthday);

                SlashCommandData timezone = Commands.slash("timezone", "Confused about time?");
                SubcommandData tz_remove = new SubcommandData("remove", "Removes your timezone from the database.");
                SubcommandData tz_set = new SubcommandData("set",
                                "Set your timezone so you can share it with everyone!");
                tz_set.addOption(OptionType.STRING, "time",
                                "Type what time it is for you in 24-hour format (14:24), and I will figure out your timezone.",
                                true);
                timezone.addSubcommands(tz_set);
                timezone.addSubcommands(tz_remove);
                slashCommands.add(timezone);

                SlashCommandData mytime = Commands.slash("mytime", "Show your time in the server");
                slashCommands.add(mytime);

                SlashCommandData whatTime = Commands.slash("whattime", "Show a selected user's time in the server")
                                .addOption(OptionType.USER, "member", "Who to get the time from", true);
                slashCommands.add(whatTime);

                slashCommands.add(Commands.slash("8ball", "Ask the magic 8ball a question..")
                                .addOption(OptionType.STRING, "question", "What's the question?",
                                                true));

                slashCommands.add(Commands.slash("pannels", "Send the ticket pannels")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));

                slashCommands.add(Commands.slash("selfpromo", "Submit a self promotion link"));

                slashCommands.add(Commands.slash("removebirthday", "Removes a birthday of a user")
                                .addOption(OptionType.MENTIONABLE, "member", "Who to remove the birthday from", true)
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS)));

                jda.updateCommands().addCommands(slashCommands).queue();

                // event registration
                jda.addEventListener(waiter);
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
                jda.addEventListener(new BirthdayFunction());
                jda.addEventListener(new SelfPromoHandler());
                jda.addEventListener(new TicketCreation());
                jda.addEventListener(new CloseTicketButton());
                jda.addEventListener(new SubmitLinks());
                // jda.addEventListener(new JinxHandler()); Postponed
                // new ZitchTimer(jda,waiter).execute(); Doron asked to comment it out

        }

}
