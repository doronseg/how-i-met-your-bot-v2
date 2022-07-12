package me.nerdoron.himyb.commands.funcommands.gambling;

import java.sql.SQLException;
import java.util.Random;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CoinFlip extends SlashCommand {

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

    private int chance() {
        Random random = new Random();
        int num = random.nextInt(2) + 1;
        return num;
    }

}
