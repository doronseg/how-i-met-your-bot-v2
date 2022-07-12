package me.nerdoron.himyb.commands.funcommands.gambling;

import java.sql.SQLException;
import java.util.Random;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CoinFlipCommand extends SlashCommand {

    public BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
    private String result;

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String type = event.getOption("type").getAsString();
        int bet = event.getOption("bet").getAsInt();
        int rand = chance();

        if (rand == 1) {
            result = "Heads";
        } else {
            result = "Tails";
        }

        if (bet > broCoinsSQL.getBrocoins(event.getMember())) {
            event.reply("You dont have enough BroCoins!").setEphemeral(true).queue();
            return;
        }

        if (type.equals(result)) {
            // win
            try {
                broCoinsSQL.updateBrocoins(event.getMember(), bet * 2);
                event.reply(
                        event.getMember().getAsMention() + " bet " + bet + " " + Global.broCoin.getAsMention()
                                + " on a coinflip, won, and doubled his bet!")
                        .queue();
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
        OptionData heads_tails = new OptionData(OptionType.STRING, "type", "Heads or tails?", true);
        heads_tails.addChoice("heads", "Heads");
        heads_tails.addChoice("tails", "Tails");

        coinflip.addOptions(bet, heads_tails);
        return coinflip;
    }

    private int chance() {
        Random random = new Random();
        int num = random.nextInt(2) + 1;
        return num;
    }

}
