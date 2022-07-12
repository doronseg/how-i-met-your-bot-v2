package me.nerdoron.himyb.modules.help;

import me.nerdoron.himyb.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpEmbeds {
        public MessageEmbed mainMenu = new EmbedBuilder()
                        .setTitle("🔮 Help Menu")
                        .setDescription(
                                        "Hello! I'm how i met your bot. You can use the buttons below to view all of my functions, but for now, here is some information about me.")
                        .addField("Library",
                                        "[JDA 5.0.0-alpha.13](https://github.com/DV8FromTheWorld/JDA/releases/tag/v5.0.0-alpha.13)",
                                        true)
                        .addField("Prefix", "`/`", true)
                        .addField("Github", "[Click me](https://github.com/nerdoron/how-i-met-your-bot)", true)
                        .setColor(Global.embedColor)
                        .setFooter(Global.footertext, Global.footerpfp)
                        .build();

        public MessageEmbed usefulMenu = new EmbedBuilder()
                        .setTitle("🛠️ Useful Commands")
                        .setDescription("A list of all commands under the `useful` category:" +
                                        "\n\n`/help` - Shows this menu." +
                                        "\n\n`/uptime` - Shows the bot's uptime." +
                                        "\n\n`/ping` - Shows the bot's ping." +
                                        "\n\n`/afk [reason]` - Sets you as Away from Keyboard." +
                                        "\n\n`/apply [type]` - Apply for an open position." +
                                        "\n\n`/birthday [month] [day]` - Set your birthday." +
                                        "\n\n`/timezone set [time]` - Set your timezone. " +
                                        "\n\n`/mytime` - Shows what time it is for you." +
                                        "\n\n`/whattime` [user] - Shows what time it is for the selected user." +
                                        "\n\n`/suggest [type] [suggestions]` - Sends a suggestion.\nSuggestion types: `server`, `bot`, `video` & `chain`."
                                        + "\n\n`/selfpromo` - Send a self promotion link (requires level 10)")

                        .setColor(Global.embedColor)
                        .setFooter(Global.footertext, Global.footerpfp)
                        .build();

        public MessageEmbed funMenu = new EmbedBuilder().setColor(Global.embedColor)
                        .setTitle("🦩 Fun Commands")
                        .setDescription("A list of all commands under the `fun` category:\n\n" +
                                        "`/8ball` - Ask the magic 8ball a question.")

                        .setFooter(Global.footertext, Global.footerpfp).build();

        public MessageEmbed currencyMenu = new EmbedBuilder().setColor(Global.embedColor)
                        .setTitle(Global.broCoin.getAsMention() + " Currency Commands")
                        .setDescription("A list of all commands under the `currency` category:\n\n" +
                                        "`/bank create` - Create a BroBank account.\n\n" +
                                        "`/bank check [user]` - Check a bank account. `[user]` is optional.\n\n" +
                                        "`/bank transfer [user] [amount]` - Transfer BroCoins to another user.")
                        .setFooter(Global.footertext, Global.footerpfp).build();
}
