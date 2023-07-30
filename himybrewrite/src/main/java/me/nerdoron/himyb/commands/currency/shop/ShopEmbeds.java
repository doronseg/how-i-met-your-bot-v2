package me.nerdoron.himyb.commands.currency.shop;

import me.nerdoron.himyb.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ShopEmbeds {

    // main
    public MessageEmbed mainEmbed = new EmbedBuilder().setTitle(Global.broCoin.getAsMention() + " BroShop\u2122\uFE0F")
            .setDescription("Welcome to the BroShop\u2122\uFE0F. Please select a category.")
            .setColor(Global.embedColor)
            .setFooter(Global.footertext, Global.footerpfp)
            .build();

    // xp
    public MessageEmbed xpEmbed = new EmbedBuilder().setTitle("📈 XP Boosters")
            .setDescription(
                    "A list of all available XP Boosters you can buy. We know the prices are high, we're just encouraging gambling 😈\nTo buy an item, use `/buy [X1|X2|X3|X4|X5]`.")
            .setColor(Global.embedColor)
            .addField("`X1.` The Novice (24 Hours)",
                    "Grants an additional experience point for every message sent for 24 hours. \n2,000 "
                            + Global.broCoin.getAsMention(),
                    false)
            .addField("`X2.` The Novice (48 Hours)",
                    "Grants an additional experience point for every message sent for 48 hours. \n3,750 "
                            + Global.broCoin.getAsMention(),
                    false)
            .addField("`X3.` The Apprentice (24 Hours)",
                    "Grants two additional experience points for every message sent for 24 hours. \n5,000 "
                            + Global.broCoin.getAsMention(),
                    false)
            .addField("`X4.` The Apprentice (48 Hours)",
                    "Grants two additional experience points for every message sent for 48 hours. \n9,000 "
                            + Global.broCoin.getAsMention(),
                    false)
            .addField("`X5.` THE GRANDMASTER (1 HOUR)",
                    "Grants *FIVE* additional experience points for every message sent for **1 hour**. \n25,000 "
                            + Global.broCoin.getAsMention(),
                    false)
            .setFooter(Global.footertext, Global.footerpfp)
            .build();

    // roles
    public MessageEmbed roleEmbed = new EmbedBuilder().setTitle(Global.broCoin.getAsMention() + " BroShop\u2122\uFE0F")
            .setDescription("Welcome to the BroShop\u2122\uFE0F. Please select a category.")
            .setColor(Global.embedColor)
            .setDescription(
                    "A list of all available XP Boosters you can buy. We know the prices are high, we're just encouraging gambling 😈\nTo buy an item, use `/buy [R1|R2|R3|R4]`.")
            .addField("`R1.` Custom Role (1 Month)",
                    "Grants you a cosmetic custom role for a month. \n20,000 "
                            + Global.broCoin.getAsMention(),
                    false)
            .addField("`R2.` Custom Role (3 Months)",
                    "Grants you a cosmetic custom role for 3 months. \n50,000 "
                            + Global.broCoin.getAsMention(),
                    false)
            .addField("`R3.` Custom Role (6 Months)",
                    "Grants you a cosmetic custom role for 6 months. \n83,000 "
                            + Global.broCoin.getAsMention(),
                    false)
            .addField("`R4.` Display your custom role seperatly",
                    "Display your custom role seperatly, could be any role, whether given from the item shop, or from supporter rewards. \n10,000 "
                            + Global.broCoin.getAsMention(),
                    false)
            .setFooter(Global.footertext, Global.footerpfp)
            .build();
}
