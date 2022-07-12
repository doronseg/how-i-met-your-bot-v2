package me.nerdoron.himyb.commands.staff;

import java.sql.SQLException;

import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SetCoinsCommand extends SlashCommand {
    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
    final Logger logger = LoggerFactory.getLogger(SetCoinsCommand.class);

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            event.reply("You don't have enough permissions to do that!").setEphemeral(true).queue();
            return;
        }

        int newAmount = event.getInteraction().getOption("amount").getAsInt();
        Member memberToModify = event.getInteraction().getOption("user").getAsMember();
        if (!broCoinsSQL.hasBrocoins(memberToModify)) {
            event.reply("That user does not have a BroBank account. Please tell them to create one.")
                    .setEphemeral(true).queue();
            return;
        }

        try {
            broCoinsSQL.setBrocoins(memberToModify, newAmount);
            event.reply(memberToModify.getAsMention() + " now has " + newAmount + " "
                    + Emoji.fromCustom(Global.broCoin).getAsMention()).queue();
            logger.info(member.getUser().getAsTag() + "(" + member.getId() + ") has set the amount of "
                    + memberToModify.getUser().getAsTag() + "(" + memberToModify.getId() + ") to (" + newAmount
                    + " Coins)");
            event.getJDA().getGuildById("850396197646106624").getTextChannelById("850447694673739816")
                    .sendMessage(member.getUser().getAsTag() + "(" + member.getId() + ") has set the amount of "
                            + memberToModify.getUser().getAsTag() + "(" + memberToModify.getId() + ") to (" + newAmount
                            + " Coins)")
                    .queue();
        } catch (SQLException e) {
            event.reply("Error!").queue();
            e.printStackTrace();
        }
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData setcoins = Commands.slash("setcoins", "Set the coins of a user.");
        setcoins.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        setcoins.addOption(OptionType.USER, "user", "The user you want to modify its coins", true);
        OptionData setAmount = new OptionData(OptionType.INTEGER, "amount",
                "The amount of coins you want to set", true);
        setAmount.setMinValue(1);
        setcoins.addOptions(setAmount);
        return setcoins;
    }

}
