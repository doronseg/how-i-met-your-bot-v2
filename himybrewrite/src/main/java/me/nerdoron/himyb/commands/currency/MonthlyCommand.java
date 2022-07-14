package me.nerdoron.himyb.commands.currency;

import java.sql.SQLException;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules._bot.CooldownManager;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class MonthlyCommand extends SlashCommand {

    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();

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
        try {
            broCoinsSQL.updateBrocoins(event.getMember(), reward);
            Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), Global.monthInSeconds);
            event.reply(
                    "You claimed your monthly batch of coins, and got " + reward + " " + Global.broCoin.getAsMention()
                            + ".")
                    .setEphemeral(true).queue();
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
