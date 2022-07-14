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
            if (Global.COOLDOWN_MANAGER.hasTag(CooldownManager.commandID(event), "Caught")) {
                event.reply("The cops are watching you :eyes: Try again in " + remaining).setEphemeral(true)
                        .queue();
            } else {
                event.reply("Don't commit to many crimes! You can attempt a new heist in " + remaining)
                        .setEphemeral(true)
                        .queue();
            }
            return;
        }
        int chance = Global.generateNumber(1, 6);
        int reward = Global.generateNumber(10, 20);
        int fine = Global.generateNumber(10, 15);
        if (chance == 3) {
            try {
                Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), Global.hourinSeconds * 2);
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
                Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), "Caught",
                        Global.hourinSeconds * 6);
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
                "rob the Diamond Casino", "steal a car", "steal a bicycle", "rob a store", "sell weed", "sell meth",
                "pickpocket someone", "help Kira's Gambling addiction", "help illegal imigrants cross the border",
                "pull a hit on a bounty", "break into someone's house", "hack in Hypixel",
                "pirate how i met your mother", "break the Geneva Convention", "rob a bank",
                "switch a fire alarm on because you couldn't help the idea of not meeting your soul-mate",
                "steal Ted's Christmas decorations", "steal a Blue French Horn", "take someone else's cab",
                "leave a suitcase at the airport", "steal a bottle of whiskey for your bro", "move to San Francisco",
                "talk to the North Koreans", "talk to the South Koreans", "TP a Laser Tag place",
                "hire a \"paralegal\""
        };
        return crimes[Global.generateNumber(0, crimes.length - 1)];
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("crime", "Commit some criminal activities, and earn some good money.");
    }

}
