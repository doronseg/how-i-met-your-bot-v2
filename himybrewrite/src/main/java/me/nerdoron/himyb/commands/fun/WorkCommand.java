package me.nerdoron.himyb.commands.fun;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules._bot.CooldownManager;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class WorkCommand extends SlashCommand {
    final Logger logger = LoggerFactory.getLogger(WorkCommand.class);

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
        if (Global.COOLDOWN_MANAGER.hasCooldown(CooldownManager.commandID(event))) {
            String remaining = Global.COOLDOWN_MANAGER.parseCooldown(CooldownManager.commandID(event));
            event.reply("Don't work too hard! You can work again in " + remaining).setEphemeral(true).queue();
            return;
        }

        if (!broCoinsSQL.hasBrocoins(event.getMember())) {
            event.reply("You don't have a BroCoins account!").setEphemeral(true).queue();
            return;
        }

        int reward = Global.generateNumber(1, 10);
        int chance = Global.generateNumber(1, 100);
        if (chance == 50) {
            Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), Global.dayinSeconds);
            event.reply("You worked " + getJob() + ", however, your employer scammed you, and didn't pay you.")
                    .queue();
            return;
        }
        try {
            if (chance == 1 || chance == 99)
                reward = reward * 3;
            broCoinsSQL.updateBrocoins(event.getMember(), reward);
            event.reply(
                    "You have worked " + getJob() + " and earned " + reward + " " + Global.broCoin.getAsMention())
                    .queue();
        } catch (SQLException e) {
            e.printStackTrace();
            event.reply("Error ;(").queue();
            return;
        }

        int coinsNow = broCoinsSQL.getBrocoins(event.getMember());
        Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), Global.hourinSeconds);
        logger.info(event.getMember().getUser().getAsTag() + "(" + event.getMember().getId() + ")" + " won (" + reward
                + " Coins) While working now they have (" + coinsNow + ") coins");
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("work", "Work hard and get some BroCoins as a reward");
    }

    public String getJob() {
        String[] jobs = new String[] {
                "as a Software Engineer", "as a Developer", "as a Story teller", "as a Dentist", "as a Veterinarian",
                "as a Teacher",
                "as a Financial Advisor", "as a Lawyer", "as a Accountant", "as a Architect",
                "as a P.L.E.A.S.E", "as an Actor", "as a Police Officer", "as a Video Editor", "as a Doctor",
                "as an Astronaut",
                "as a YouTuber", "as a Discord Admin", "as a Delivery Driver", "as a Cashier", "as a Meth Cooker",
                "as a Burger Flipper", "as a Cameraman",
                "as a waiter", "as a Paramedic", "as a Electrician", "as a Chef", "as a Farmer", "as a Locksmith",
                "as a Mechanic", "as a Baker", "as a Butcher",
                "as a Pilot", "as a Sea-Captain", "as a Painter", "as a Musician", "as a Miner", "as a Dancer",
                "as a Bellhop", "as a Bookkeeper",
                "as a Carwash Cashier", "as a Kindergarten Teacher", "as a News Reporter", "as a Zookeeper",
                "as a Garbageman", "as a Babysitter", "for GNB", "for the NRDC"
        };

        return jobs[Global.generateNumber(0, jobs.length - 1)];
    }
}
