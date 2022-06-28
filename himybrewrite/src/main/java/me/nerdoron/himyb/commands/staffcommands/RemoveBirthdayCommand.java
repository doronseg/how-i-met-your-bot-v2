package me.nerdoron.himyb.commands.staffcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.Database;
import me.nerdoron.himyb.modules.birthday.BirthdayChecks;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RemoveBirthdayCommand extends SlashCommand {
    BirthdayChecks birthdayChecks = new BirthdayChecks();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getOption("member").getAsMember();

        if (!event.getMember().getPermissions().contains(Permission.KICK_MEMBERS)) {
            event.reply("I'm Sorry but only Moderators can execute this command").queue();
            return;
        }

        if (!birthdayChecks.hasBirthdaySet(member.getUser().getId()) == true) {
            event.deferReply().setEphemeral(true).setContent("Doest have a birthday set!").queue();
            return;
        }

        Connection con = Database.connect();
        String statement = "DELETE FROM birthday WHERE uid = ?";
        try {
            PreparedStatement ps = con.prepareStatement(statement);
            ps.setString(1, member.getId());
            ps.execute();
            event.reply("The birthday of "+member.getAsMention()+ "was deleted from the database").setEphemeral(true).queue();
        } catch (SQLException ex) {
            event.reply("There was an error in the DB").setEphemeral(true).queue();
            ex.printStackTrace();
        }
    }
}
