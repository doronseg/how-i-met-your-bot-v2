package me.nerdoron.himyb.modules._bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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
import me.nerdoron.himyb.modules.zitchdog.ZitchTimer;
import net.dv8tion.jda.api.JDA;

public class RegisterEvents {

        public static void registration(JDA jda) {
                EventWaiter waiter = new EventWaiter();
                BotCommandsHandler CmcHandler = new BotCommandsHandler(waiter);
                CmcHandler.updateCommandsOnDiscord(jda);
                jda.addEventListener(CmcHandler);

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
                new ZitchTimer(jda,waiter).execute();
                // jda.addEventListener(new JinxHandler()); Postponed

        }

}
