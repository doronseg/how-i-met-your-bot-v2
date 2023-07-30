package me.nerdoron.himyb.commands.fun.gambling;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules._bot.CooldownManager;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class RussianRouletteCommand extends SlashCommand {
    final Logger logger = LoggerFactory.getLogger(Global.className(this.getClass()));
    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (Global.COOLDOWN_MANAGER.hasCooldown(CooldownManager.commandID(event))) {
            String time = Global.COOLDOWN_MANAGER.parseCooldown(CooldownManager.commandID(event));
            event.reply("You have already bet on Russian Roulette. Please try again in " + time).setEphemeral(true)
                    .queue();
            return;
        }
        if (!broCoinsSQL.hasBrocoins(event.getMember())) {
            event.reply("You don't have a BroCoins account!").setEphemeral(true).queue();
            return;
        }
        int bet = event.getOption("bet").getAsInt();
        int userCoins = broCoinsSQL.getBrocoins(event.getMember());
        if (userCoins < bet) {
            event.reply("I'm sorry but you are missing " + (bet - userCoins) + " " + Global.broCoin.getAsMention()
                    + " to play").queue();
            return;
        }

        int number = event.getOption("number").getAsInt();
        int rand = Global.generateNumber(1, 6);
        File video = getRandomRussianRoulette(rand);
        event.reply(
                "You bet " + bet + " " + Global.broCoin.getAsMention() + " on " + number + ". Let's see if you won...")
                .addFile(video)
                .queue();
        Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), 60 * 5);

        if (doesWin(rand, number)) {
            try {
                broCoinsSQL.updateBrocoins(event.getMember(), bet * 2);
                event.getHook().editOriginal("You bet " + bet + " " + Global.broCoin.getAsMention()
                        + " on `"
                        + number + "` and won " + bet * 2 + " " + Global.broCoin.getAsMention()
                        + ". You now have " + broCoinsSQL.getBrocoins(event.getMember()) + " "
                        + Global.broCoin.getAsMention() + ".").queueAfter(10, TimeUnit.SECONDS);
                logger.info(event.getUser().getAsTag() + "(" + event.getUser().getName() + ")" + " won ("
                        + bet * 2
                        + " Coins) in Russian Roulette now they have (" + broCoinsSQL.getBrocoins(event.getMember())
                        + ") coins");
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                event.reply("Error!").setEphemeral(true).queue();
                return;
            }
        } else {
            try {
                broCoinsSQL.updateBrocoins(event.getMember(), -(bet));
                event.getHook().editOriginal("You bet " + bet + " " + Global.broCoin.getAsMention()
                        + " on `"
                        + number + "` and lost your bet. You now have "
                        + broCoinsSQL.getBrocoins(event.getMember()) + " "
                        + Global.broCoin.getAsMention() + ".").queueAfter(10, TimeUnit.SECONDS);
                logger.info(event.getUser().getAsTag() + "(" + event.getUser().getName() + ")" + " lost ("
                        + bet
                        + " Coins) in Russian Roulette now they have (" + broCoinsSQL.getBrocoins(event.getMember())
                        + ") coins");
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                event.reply("Error!").setEphemeral(true).queue();
                return;
            }
        }

    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData rr = Commands.slash("russianroulette",
                "Bet on a game of Russian Roulette. High risk, high reward");
        OptionData bet = new OptionData(OptionType.INTEGER, "bet", "How much do you want to bet?", true);
        OptionData number = new OptionData(OptionType.INTEGER, "number",
                "Which number would you like to place your bet on?", true);
        bet.setMinValue(1);
        number.setMinValue(1);
        number.setMaxValue(6);
        rr.addOptions(bet, number);
        return rr;
    }

    private Boolean doesWin(int rand, int num) {
        if (rand == num)
            return true;
        else {
            return false;
        }

    }

    private File getRandomRussianRoulette(int num) {
        ArrayList<File> files = new ArrayList<>();
        files.add(new File("Russian1.mp4"));
        files.add(new File("Russian2.mp4"));
        files.add(new File("Russian3.mp4"));
        files.add(new File("Russian4.mp4"));
        files.add(new File("Russian5.mp4"));
        files.add(new File("Russian6.mp4"));
        File finalFile = files.get(num - 1);
        return finalFile;

    }

}
