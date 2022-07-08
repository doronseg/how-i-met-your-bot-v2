package me.nerdoron.himyb.commands.currency;

import java.sql.SQLException;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BankCommand extends SlashCommand {

    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        String subcmd = event.getSubcommandName();
        if (subcmd.equals("create")) {
            if (broCoinsSQL.hasBrocoin(member) == true) {
                event.reply("You already have a bank account!").setEphemeral(true).queue();
                return;
            }
            try {
                broCoinsSQL.setBrocoin(member, 5);
                event.reply("Created a BroBank account! I gave you 5 " + Emoji.fromCustom(Global.broCoin).getAsMention()
                        + " to get started!").setEphemeral(true)
                        .queue();
            } catch (SQLException e) {
                event.reply("Error!").queue();
                e.printStackTrace();
            }
        }

        // transfer
        if (subcmd.equals("transfer")) {
            if (broCoinsSQL.hasBrocoin(member) == false) {
                event.reply("You dont have a bank account!").setEphemeral(true).queue();
                return;
            }
            int userCoins = broCoinsSQL.getBrocoin(member);
            int amountToTransfer = event.getInteraction().getOption("amount").getAsInt();
            if (userCoins < amountToTransfer) {
                event.reply("You dont have enough BroCoins to transfer!").setEphemeral(true).queue();
                return;
            }

            Member memberToTransferTo = event.getInteraction().getOption("user").getAsMember();
            if (broCoinsSQL.hasBrocoin(memberToTransferTo) == false) {
                event.reply("That user does not have a BroBank account. Please tell them to create one.")
                        .setEphemeral(true).queue();
                return;
            }
            try {
                int memberToTransferCoins = broCoinsSQL.getBrocoin(memberToTransferTo);
                int memberCoins = broCoinsSQL.getBrocoin(member);
                int finalAmount = memberToTransferCoins + amountToTransfer;
                broCoinsSQL.setBrocoin(memberToTransferTo, finalAmount);
                broCoinsSQL.setBrocoin(member, memberCoins - amountToTransfer);
                event.reply("Transferred " + amountToTransfer + " " + Emoji.fromCustom(Global.broCoin).getAsMention()
                        + " to "
                        + memberToTransferTo.getAsMention()).queue();
            } catch (SQLException e) {
                event.reply("Error!").queue();
                e.printStackTrace();
            }
        }
    }
}
