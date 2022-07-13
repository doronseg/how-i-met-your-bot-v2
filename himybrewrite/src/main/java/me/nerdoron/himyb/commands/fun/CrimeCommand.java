package me.nerdoron.himyb.commands.fun;

import java.sql.SQLException;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules._bot.CooldownManager;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CrimeCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
        if (Global.COOLDOWN_MANAGER.hasCooldown(CooldownManager.commandID(event))) {
            String remaining = Global.COOLDOWN_MANAGER.parseCooldown(CooldownManager.commandID(event));
            event.reply("Don't commit to many crimes! You can attempt a new heist in " + remaining).setEphemeral(true)
                    .queue();
            return;
        }
        int chance = Global.generateNumber(1, 5);
        int reward = Global.generateNumber(10, 20);
        int fine = Global.generateNumber(10, 15);
        if (chance == 3) {
            try {
                Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), 120 * 60);
                event.reply(
                        "You attempted to " + getCrime() + ", and managed to get away with it. You got " + reward + " "
                                + Global.broCoin.getAsMention() + " as a reward.")
                        .queue();
                broCoinsSQL.updateBrocoins(event.getMember(), reward);
            } catch (SQLException e) {
                e.printStackTrace();
                event.reply("Error ;(").queue();
            }
        } else {
            try {
                Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), 360 * 60);
                if (fine > broCoinsSQL.getBrocoins(event.getMember())) {
                    event.reply("You attempted to " + getCrime()
                            + ", but you were caught! As a punishment, they tried to fine you  "
                            + fine + " " + Global.broCoin.getAsMention()
                            + ", but you're already way too broke, so they decided to just watch you for the next 6 hours.")
                            .queue();
                    return;
                }
                event.reply("You attempted to " + getCrime() + ", but you were caught! As a punishment, they fined you "
                        + fine + " " + Global.broCoin.getAsMention() + " and will watch you for the next 6 hours.")
                        .queue();
                broCoinsSQL.updateBrocoins(event.getMember(), -fine);
            } catch (SQLException e) {
                e.printStackTrace();
                event.reply("Error ;(").queue();
            }
        }
    }

    public String getCrime() {
        String[] crimes = new String[] {
                "Rob the Diamond Casino", "Steal a car", "Steal a bicycle", "Rob a store", "Sell weed", "Sell meth",
                "Pickpocket someone", "Help Kira's Gambling addiction", "Help illegal imigrants cross the border",
                "Pull a hit on a bounty", "Break into someone's house", "Hack in Hypixel",
                "Pirate how i met your mother", "Break the Geneva Convention", "Rob a bank",
                "Switch a fire alarm on because you couldn't help the idea of not meeting your soulmate.",
                "Steal Ted's Christmas decorations", "Steal a blue French Horn", "Take someone else's cab",
                "Leave a suitcase at the airport", "Steal a bottle of whiskey for your bro", "Move to San Francisco",
                "Talk to the North Koreans", "Talk to the South Koreans", "TP a Laser Tag place",
                "Hire a \"paralegal\""
        };
        return crimes[Global.generateNumber(0, crimes.length)];
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("crime", "Commit some criminal activities, and earn some good money.");
    }

}
