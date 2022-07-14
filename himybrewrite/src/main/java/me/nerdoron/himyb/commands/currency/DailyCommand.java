package me.nerdoron.himyb.commands.currency;

import java.sql.SQLException;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules._bot.CooldownManager;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class DailyCommand extends SlashCommand {

    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int reward = Global.generateNumber(5, 15);
        if (Global.COOLDOWN_MANAGER.hasCooldown(CooldownManager.commandID(event))) {
            String remaining = Global.COOLDOWN_MANAGER.parseCooldown(CooldownManager.commandID(event));
            event.reply("A day hasn't passed since you last claimed your daily batch. " + remaining + " remaining.")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        try {
            broCoinsSQL.updateBrocoins(event.getMember(), reward);
            Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), Global.dayinSeconds);
            event.reply("You claimed your daily batch of coins, and got " + reward + " " + Global.broCoin.getAsMention()
                    + ".").setEphemeral(true).queue();
        } catch (SQLException e) {
            event.reply("Error!").setEphemeral(true).queue();
            e.printStackTrace();
        }

    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("daily", "Get a daily batch of BroCoins.");
    }

}
