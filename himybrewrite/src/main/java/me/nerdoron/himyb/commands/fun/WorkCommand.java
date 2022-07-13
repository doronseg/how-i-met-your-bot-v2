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
            Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), 60 * 60);
            event.reply("You worked as a " + getJob() + ", however, your employer scammed you, and didn't pay you.")
                    .queue();
            return;
        }
        try {
            if (chance == 1 || chance == 99)
                reward = reward * 3;
            broCoinsSQL.updateBrocoins(event.getMember(), reward);
            event.reply(
                    "You have worked as a " + getJob() + " and earned " + reward + " " + Global.broCoin.getAsMention())
                    .queue();
        } catch (SQLException e) {
            e.printStackTrace();
            event.reply("Error ;(").queue();
            return;
        }

        int coinsNow = broCoinsSQL.getBrocoins(event.getMember());
        Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), 60 * 60);
        logger.info(event.getMember().getUser().getAsTag() + "(" + event.getMember().getId() + ")" + " won (" + reward
                + " Coins) While working now they have (" + coinsNow + ") coins");
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("work", "Work hard and get some BroCoins as a reward");
    }

    public String getJob() {
        String[] jobs = new String[] {
                "Software Engineer", "Developer", "Story teller", "Dentist", "Veterinarian", "Teacher",
                "Financial Advisor", "Lawyer", "Accountant", "Architect",
                "P.L.E.A.S.E", "Actor", "Police Officer", "Video Editor", "Doctor", "Astronaut",
                "YouTuber", "Discord Admin", "Delivery Driver", "Cashier", "Meth Cooker", "Burger Flipper", "Cameraman",
                "Waiter", "Paramedic", "Electrician", "Chef", "Farmer", "Locksmith", "Mechanic", "Baker", "Butcher",
                "Pilot", "Sea-Captain", "Painter", "Musician", "Miner", "Dancer", "Bellhop", "Bookkeeper",
                "Carwash Cashier", "Kindergarten Teacher", "News Reporter", "Zookeeper", "Garbageman", "Babysitter"
        };

        return jobs[Global.generateNumber(0, jobs.length - 1)];
    }
}
