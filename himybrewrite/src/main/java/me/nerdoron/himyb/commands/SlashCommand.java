package me.nerdoron.himyb.commands;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public abstract class SlashCommand {

    public void executeGlobal(SlashCommandInteractionEvent event) {
        final Logger logger = LoggerFactory.getLogger(SlashCommand.class);
        try {
            execute(event);
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    public abstract void execute(SlashCommandInteractionEvent event);

}
