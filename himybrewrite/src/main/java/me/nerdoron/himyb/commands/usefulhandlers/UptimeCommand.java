package me.nerdoron.himyb.commands.usefulhandlers;

import java.lang.management.ManagementFactory;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class UptimeCommand extends Command {
    public static String getUptime() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long uptimeSec = uptime / 1000;
        long hours = uptimeSec / (60 * 60);
        long minutes = (uptimeSec / 60) - (hours * 60);
        long seconds = uptimeSec % 60;

        String uptimeString = (Long.toString(hours) + " hours, " + Long.toString(minutes) + " minutes, "
                + Long.toString(seconds) + " seconds.");

        return uptimeString;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MessageEmbed uptime = new EmbedBuilder().setTitle("Bot Uptime")
                .setDescription("My uptime is " + getUptime()).setColor(Global.embedColor)
                .setFooter(Global.footertext, Global.footerpfp)
                .build();
        event.replyEmbeds(uptime).queue();
        ;
    }

}
