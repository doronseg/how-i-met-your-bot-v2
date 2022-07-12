package me.nerdoron.himyb.commands.usefulcommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class AFKCommand extends SlashCommand {

    final Logger logger = LoggerFactory.getLogger(SlashCommand.class);

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String reason = " ";
        reason = event.getOption("reason").getAsString();
        String uid = event.getMember().getId();

        try {
            addAfk(uid, reason);
            MessageEmbed afk = new EmbedBuilder().setTitle(":wave: Goodbye, " + event.getUser().getAsTag())
                    .setDescription("I've sucessfully set your AFK status!").addField("Reason", reason, false)
                    .setColor(Global.embedColor).setFooter(Global.footertext, Global.footerpfp)
                    .build();
            event.replyEmbeds(afk).queue();

        } catch (SQLException ex) {
            event.deferReply().setEphemeral(true).setContent(
                    "There has been an error while executing this command.")
                    .queue();
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    public void addAfk(String uid, String reason) throws SQLException {
        Connection con = Database.connect();
        String statement = "insert into afk (UID, REASON) values(?,?)";
        PreparedStatement ps = con.prepareStatement(statement);
        ps.setString(1, uid);
        ps.setString(2, reason);
        ps.execute();
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData cmd = Commands.slash("afk", "Go AFK")
                .addOption(OptionType.STRING, "reason", "Why are you away?", true);

        return cmd;
    }

}
