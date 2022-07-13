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
}
