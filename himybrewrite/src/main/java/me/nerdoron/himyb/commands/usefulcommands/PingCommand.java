package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class PingCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MessageEmbed ping = new EmbedBuilder().setTitle("Ping")
                .setDescription("Pong! " + event.getJDA().getGatewayPing() + "ms.").setColor(Global.embedColor)
                .setFooter(Global.footertext, Global.footerpfp)
                .build();
        event.replyEmbeds(ping).queue();
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("ping", "Calculate te ping of the bot.");
    }

}
