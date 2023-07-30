package me.nerdoron.himyb.commands.currency.shop;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ShopCommand extends SlashCommand {

    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
    final Logger logger = LoggerFactory.getLogger(Global.className(this.getClass()));
    ShopEmbeds shopEmbeds = new ShopEmbeds();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String uid = event.getUser().getId();
        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("nuh uh 🤓").queue();
            return;
        }
        // handle buttons
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(Button.secondary("SHOP:" + uid + ":main", "🔮 Main Menu"));
        buttons.add(Button.secondary("SHOP:" + uid + ":xp", "📈 XP Boosters"));
        buttons.add(Button.secondary("SHOP:" + uid + ":roles", "🔐 Roles"));

        event.replyEmbeds(shopEmbeds.mainEmbed)
                .addActionRow(buttons).queue();
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("shop", "Opens the BroShop\u2122\uFE0F");
    }

}
