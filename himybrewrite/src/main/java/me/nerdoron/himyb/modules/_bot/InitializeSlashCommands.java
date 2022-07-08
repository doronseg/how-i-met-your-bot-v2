package me.nerdoron.himyb.modules._bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.ArrayList;

public class InitializeSlashCommands {
        public InitializeSlashCommands(JDA jda) {
                ArrayList<CommandData> slashCommands = new ArrayList<>();

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
                SubcommandData tz_setmanual = new SubcommandData("setmanual",
                                "Manually specify your timezone to the bot.");
                OptionData tz_setmanual_hours = new OptionData(OptionType.INTEGER, "hours",
                                "GMT +/- this amound of hours", true);
                tz_setmanual_hours.setRequiredRange(-12, 12);
                OptionData tz_setmanual_minutes = new OptionData(OptionType.INTEGER, "minutes",
                                "GMT +/- this amound of minutes", true);
                tz_setmanual_minutes.setRequiredRange(0, 59);
                tz_setmanual.addOptions(tz_setmanual_hours, tz_setmanual_minutes);
                timezone.addSubcommands(tz_set);
                timezone.addSubcommands(tz_remove);
                timezone.addSubcommands(tz_setmanual);
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

                SlashCommandData bank = Commands.slash("bank", "Control your BroBank account");
                SubcommandData b_create = new SubcommandData("create", "Create a BroBank account");
                SubcommandData b_transfer = new SubcommandData("transfer",
                                "Transfers a set amount of coins from your bank account to another person's bank account");
                b_transfer.addOption(OptionType.USER, "user", "The user you want to transfer to.", true);
                OptionData amount = new OptionData(OptionType.INTEGER, "amount",
                                "The amount of coins you want to transfer", true);
                amount.setMinValue(1);
                b_transfer.addOptions(amount);

                bank.addSubcommands(b_transfer);
                bank.addSubcommands(b_create);
                slashCommands.add(bank);

                jda.updateCommands().addCommands(slashCommands).queue();
        }
}
