package me.nerdoron.himyb.commands.currency;

import java.sql.SQLException;
import java.util.Map;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import me.nerdoron.himyb.modules.brocoins.Sorter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankCommand extends SlashCommand {

    final Logger logger = LoggerFactory.getLogger(SlashCommand.class);
    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        String subcmd = event.getSubcommandName();
        if (subcmd.equals("create")) {
            if (broCoinsSQL.hasBrocoins(member) == true) {
                event.reply("You already have a bank account!").setEphemeral(true).queue();
                return;
            }
            try {
                broCoinsSQL.setBrocoins(member, 5);
                event.reply("Created a BroBank account! I gave you 5 " + Emoji.fromCustom(Global.broCoin).getAsMention()
                        + " to get started!").setEphemeral(true)
                        .queue();
                logger.info(member.getUser().getAsTag() + "("+member.getId()+") Created a bank account");
            } catch (SQLException e) {
                event.reply("Error!").queue();
                e.printStackTrace();
            }
        }

        // check
        if (subcmd.equals("check")) {
            if (broCoinsSQL.hasBrocoins(member) == false) {
                event.reply("You dont have a bank account!").setEphemeral(true).queue();
                return;
            }
            if (event.getInteraction().getOptions().isEmpty()) {
                int memberCoins = broCoinsSQL.getBrocoins(member);
                event.reply("You have " + memberCoins + " " + Emoji.fromCustom(Global.broCoin).getAsMention() + ".")
                        .setEphemeral(true).queue();
                return;
            }
            Member memberToCheck = event.getInteraction().getOption("user").getAsMember();
            int memberCoins = broCoinsSQL.getBrocoins(memberToCheck);
            if (broCoinsSQL.hasBrocoins(memberToCheck) == false) {
                event.reply(memberToCheck.getEffectiveName() + " doesn't have a bank account!").setEphemeral(true)
                        .queue();
                return;
            }
            event.reply(memberToCheck.getEffectiveName() + " has " + memberCoins + " "
                    + Emoji.fromCustom(Global.broCoin).getAsMention() + ".")
                    .queue();
        }

        // transfer
        if (subcmd.equals("transfer")) {
            if (!broCoinsSQL.hasBrocoins(member)) {
                event.reply("You dont have a bank account!").setEphemeral(true).queue();
                return;
            }
            int userCoins = broCoinsSQL.getBrocoins(member);
            int amountToTransfer = event.getInteraction().getOption("amount").getAsInt();

            if (userCoins < amountToTransfer) {
                event.reply("You dont have enough BroCoins to transfer!").setEphemeral(true).queue();
                return;
            }

            Member memberToTransferTo = event.getInteraction().getOption("user").getAsMember();
            if (!broCoinsSQL.hasBrocoins(memberToTransferTo)) {
                event.reply("That user does not have a BroBank account. Please tell them to create one.")
                        .setEphemeral(true).queue();
                return;
            }
            try {
                broCoinsSQL.updateBrocoins(memberToTransferTo, amountToTransfer);
                broCoinsSQL.updateBrocoins(member, -amountToTransfer);
                event.reply("Transferred " + amountToTransfer + " " + Emoji.fromCustom(Global.broCoin).getAsMention()
                        + " to "
                        + memberToTransferTo.getAsMention()).queue();

                int memberBrocoins = broCoinsSQL.getBrocoins(member);
                int targetBrocoins = broCoinsSQL.getBrocoins(memberToTransferTo);
                logger.info(member.getUser().getAsTag() + "("+member.getId()+") Transfered ("+amountToTransfer+" Coins) now they have ("+memberBrocoins+" Coins) and "+memberToTransferTo.getUser().getAsTag()+"("+memberToTransferTo.getId()+") has ("+targetBrocoins+" Coins)");
            } catch (SQLException e) {
                event.reply("Error!").queue();
                e.printStackTrace();
            }
        }

        // set
        if (subcmd.equals("set")) {
            if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                event.reply("You don't have enough permissions todo that!").setEphemeral(true).queue();
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
                event.reply(memberToModify.getAsMention()+ " now has "+newAmount+ " "+Emoji.fromCustom(Global.broCoin).getAsMention()).queue();
                logger.info(member.getUser().getAsTag() + "("+member.getId()+") has set the amount of "+ memberToModify.getUser().getAsTag()+"("+memberToModify.getId()+") to ("+newAmount+" Coins)");
            } catch (SQLException e) {
                event.reply("Error!").queue();
                e.printStackTrace();
            }
        }

        if (subcmd.equals("leaderboard")) {
            Map<String, Integer> brocoins = broCoinsSQL.getBrocoins();
            Map<String, Integer> sorted = new Sorter().sortMapMaxLow(brocoins,10);
            int longest = 0;
            for (Integer p : sorted.values()) {

                String a = p+"";
                if (a.length() > longest) {
                    longest = a.length();
                }
            }

            String users = "";
            for (String userID : sorted.keySet()) {
                int coins = sorted.get(userID);
                if (coins > 0) {
                    String textCoins = coins+"";
                    for (int i = textCoins.length(); i < longest; i++) {
                        textCoins=" "+textCoins;
                    }
                    users+="**`"+textCoins+"`** "+Global.broCoin.getAsMention()+" | "+"<@!"+userID+">"+"\n";
                }
            }

            EmbedBuilder emb = new EmbedBuilder();
            emb.setColor(Global.embedColor);
            emb.setTitle("Brocoins Leaderboard");
            emb.setDescription(users);

            event.replyEmbeds(emb.build()).setEphemeral(false).queue();

        }
    }
}
