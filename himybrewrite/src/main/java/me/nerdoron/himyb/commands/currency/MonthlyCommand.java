package me.nerdoron.himyb.commands.currency;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules._bot.CooldownManager;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class MonthlyCommand extends SlashCommand {

    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
    final Logger logger = LoggerFactory.getLogger(MonthlyCommand.class);

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int reward = Global.generateNumber(100, 300);
        if (Global.COOLDOWN_MANAGER.hasCooldown(CooldownManager.commandID(event))) {
            String remaining = Global.COOLDOWN_MANAGER.parseCooldown(CooldownManager.commandID(event));
            event.reply("A month hasn't passed since you last claimed your monthly batch. " + remaining + " remaining.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        if (broCoinsSQL.hasBrocoins(event.getMember())) {
            event.reply("You don't have a BroCoins account!").setEphemeral(true).queue();
            return;
        }

        try {
            broCoinsSQL.updateBrocoins(event.getMember(), reward);
            Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), Global.monthInSeconds);
            event.reply(
                    "You claimed your monthly batch of coins, and got " + reward + " " + Global.broCoin.getAsMention()
                            + ".")
                    .setEphemeral(true).queue();
            int coinsNow = broCoinsSQL.getBrocoins(event.getMember());
            logger.info(
                    event.getMember().getUser().getAsTag() + "(" + event.getMember().getId() + ")"
                            + " got the monthly batch of coins, (" + reward
                            + " Coins) now they have (" + coinsNow + ") coins");
        } catch (SQLException e) {
            event.reply("Error!").setEphemeral(true).queue();
            e.printStackTrace();
        }

    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("monthly", "Get a monthly batch of BroCoins.");
    }

}
