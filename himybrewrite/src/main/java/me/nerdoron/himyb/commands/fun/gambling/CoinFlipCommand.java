package me.nerdoron.himyb.commands.fun.gambling;

import java.sql.SQLException;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinFlipCommand extends SlashCommand {

    final Logger logger = LoggerFactory.getLogger(CoinFlipCommand.class);
    public BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
    private String result;

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String type = event.getOption("type").getAsString();
        int bet = event.getOption("bet").getAsInt();
        int rand = Global.generateNumber(1, 2);

        if (rand == 1) {
            result = "Heads";
        } else {
            result = "Tails";
        }

        if (!broCoinsSQL.hasBrocoins(event.getMember())) {
            event.reply("You don't have a BroCoins account!").setEphemeral(true).queue();
            return;
        }

        if (bet > broCoinsSQL.getBrocoins(event.getMember())) {
            event.reply("You dont have enough BroCoins!").setEphemeral(true).queue();
            return;
        }

        if (type.equals(result)) {
            // win
            try {
                broCoinsSQL.updateBrocoins(event.getMember(), bet);
                event.reply(
                        event.getMember().getAsMention() + " bet " + bet + " " + Global.broCoin.getAsMention()
                                + " on a coinflip, won, and doubled his bet!")
                        .queue();
                int coins = broCoinsSQL.getBrocoins(event.getMember());
                logger.info(event.getUser().getAsTag() + "(" + event.getMember().getId()
                        + ") Won a coin flip while betting " + bet + " now they have " + coins + " coins");
            } catch (SQLException e) {
                e.printStackTrace();
                event.reply("Error!").setEphemeral(true).queue();
            }
        } else {
            try {
                broCoinsSQL.updateBrocoins(event.getMember(), -(bet));
                event.reply(
                        event.getMember().getAsMention() + " bet " + bet + " " + Global.broCoin.getAsMention()
                                + " on a coinflip, lost, and lost his bet.")
                        .queue();
                int coins = broCoinsSQL.getBrocoins(event.getMember());
                logger.info(event.getUser().getAsTag() + "(" + event.getMember().getId()
                        + ") Lost a coin flip while betting " + bet + " now they have " + coins + " coins");
            } catch (SQLException e) {
                e.printStackTrace();
                event.reply("Error!").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData coinflip = Commands.slash("coinflip", "Bet on a coin flip.");
        OptionData bet = new OptionData(OptionType.INTEGER, "bet", "How much do you want to bet?", true);
        bet.setMinValue(1);
        bet.setMaxValue(50);
        OptionData heads_tails = new OptionData(OptionType.STRING, "type", "Heads or tails?", true);
        heads_tails.addChoice("heads", "Heads");
        heads_tails.addChoice("tails", "Tails");

        coinflip.addOptions(bet, heads_tails);
        return coinflip;
    }
}
