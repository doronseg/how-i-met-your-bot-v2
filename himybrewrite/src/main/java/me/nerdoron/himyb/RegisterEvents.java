package me.nerdoron.himyb;

import me.nerdoron.himyb.commands.BotCommandsHandler;
import me.nerdoron.himyb.commands.usefulcommands.SuggestCommandAutoComplete;
import me.nerdoron.himyb.modules.FriendsCringe;
import me.nerdoron.himyb.modules.LeaveJoin;
import me.nerdoron.himyb.modules.NotifyOfChange;
import me.nerdoron.himyb.modules.Sweden;
import me.nerdoron.himyb.modules.YoutubeNotifications;
import me.nerdoron.himyb.modules.afk.AFKMessageEvent;
import me.nerdoron.himyb.modules.chainchannel.ChainChannelHandler;
import me.nerdoron.himyb.modules.chainchannel.ChainEditing;
import me.nerdoron.himyb.modules.counting.CountingChannelHandler;
import me.nerdoron.himyb.modules.counting.CountingEditing;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class RegisterEvents {

        public static void registration(JDA jda) {
                // command registration
                jda.addEventListener(new BotCommandsHandler());
                jda.updateCommands().addCommands(
                                Commands.slash("ping", "Calculate te ping of the bot."),
                                Commands.slash("uptime", "Show the bot's uptime."),
                                Commands.slash("revive", "Send the chat revive message")
                                                .setDefaultPermissions(Permission.MESSAGE_MANAGE),
                                Commands.slash("suggest", "Send a suggestion")
                                                .addOption(OptionType.STRING, "type", "Select the type of suggestion",
                                                                true, true)
                                                .addOption(OptionType.STRING, "suggestion", "Describe your suggestion",
                                                                true),
                                Commands.slash("afk", "Go AFK")
                                                .addOption(OptionType.STRING, "reason", "Why are you away?", true),
                                Commands.slash("say", "Make the bot say something")
                                                .addOption(OptionType.CHANNEL, "channel",
                                                                "Which channel would you like to say it in?", true)
                                                .addOption(OptionType.STRING, "message", "What would you like to say?",
                                                                true)
                                                .setDefaultPermissions(Permission.MESSAGE_MANAGE),
                                Commands.slash("reply", "Make the bot reply to a message")
                                                .addOption(OptionType.STRING, "replyto",
                                                                "What message would you like to reply to? (ID ONLY, MAKE SURE YOU ARE IN THE SAME CHANNEL)",
                                                                true)
                                                .addOption(OptionType.STRING, "message", "What would you like to say?",
                                                                true)
                                                .setDefaultPermissions(Permission.MESSAGE_MANAGE)

                ).queue();

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

        }

}
