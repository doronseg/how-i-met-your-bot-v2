package me.nerdoron.himyb.commands;

import java.awt.Color;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class Command {

    public void executeGlobal(SlashCommandInteractionEvent event) {
        final Logger logger = LoggerFactory.getLogger(Command.class);
        try {
            execute(event);
        } catch (Exception ex) {
            event.deferReply().setEphemeral(true).setContent(
                    "There has been an error while executing this command. I have already informed the developer of it.")
                    .queue();
            TextChannel errors = event.getJDA().getGuildById("850396197646106624")
                    .getTextChannelById("850396197646106624");
            MessageEmbed error = new EmbedBuilder().setTitle("Error!")
                    .setDescription("There has been an error while executing a command.")
                    .addField("Error Cause", ex.getCause().toString(), true)
                    .addField("Stack trace", ex.getStackTrace().toString(), false)
                    .setColor(Color.red)
                    .build();
            errors.sendMessageEmbeds(error).queue();
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    public abstract void execute(SlashCommandInteractionEvent event);

}
