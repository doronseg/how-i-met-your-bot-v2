package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand extends Command {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MessageEmbed ping = new EmbedBuilder().setTitle("Ping")
                .setDescription("Pong! " + event.getJDA().getGatewayPing() + "ms.").setColor(Global.embedColor)
                .setFooter(Global.footertext, Global.footerpfp)
                .build();
        event.replyEmbeds(ping).queue();
    }

}
