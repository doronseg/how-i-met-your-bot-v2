package me.nerdoron.himyb.commands.fun.gambling;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BlackjackCommand extends SlashCommand {
    private final EventWaiter waiter;
    private final BroCoinsSQL broCoinsSQL = new BroCoinsSQL();

    public BlackjackCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (Global.COOLDOWN_MANAGER.hasCooldown(event.getCommandId())) {
            String time = Global.COOLDOWN_MANAGER.parseCooldown(event.getCommandId());
            event.reply("You have already played a round of blackjack. Please try again in "+time).queue();
            return;
        }

        //TODO: Add check if the user has brocoins in this method and daily, monthly and probs other methods
        int bet = event.getOption("bet").getAsInt();
        int userCoins = broCoinsSQL.getBrocoins(event.getMember());
        if (userCoins < bet) {
            event.reply("I'm sorry but you are missing "+(bet-userCoins)+" "+Global.broCoin.getAsMention()+" to play").queue();
            return;
        }

        Global.COOLDOWN_MANAGER.addCooldown(event.getCommandId(), (60*5)+10);
        event.reply("Game started").setEphemeral(true).queue();
        final TextChannel channel = event.getTextChannel();
        EmbedBuilder emb = new EmbedBuilder();
        List<String> deck = new LinkedList<>(Collections.emptyList());
        List<String> bot = new LinkedList<>(Collections.emptyList());
        List<String> user = new LinkedList<>(Collections.emptyList());
        for (int i = 1; i < 13; i++) {
            deck.add(i + "");
            deck.add(i + "");
            deck.add(i + "");
            deck.add(i + "");
        }
        Collections.shuffle(deck);
        bot.add(drawCard(deck));
        user.add(drawCard(deck));
        user.add(drawCard(deck));
        bj(deck, bot, user, emb, channel, false, event, false, null, bet);
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData cmd = Commands.slash("blackjack", "Play a round of blackjack with the bot as the dealer");
        OptionData bet = new OptionData(OptionType.INTEGER, "bet", "How many BroCoins you wanna bet", true);
        bet.setMinValue(20);
        cmd.addOptions(bet);
        return cmd;
    }

    public void bj(List<String> deck, List<String> bot, List<String> user, EmbedBuilder emb, TextChannel channel, boolean check, SlashCommandInteractionEvent ctx, boolean edit, Message msg, int bet) {
        ActionRow b = ActionRow.of(Button.secondary("...", "Game complete").asDisabled());
        emb.clearFields();
        emb.setAuthor("Game of " + ctx.getUser().getAsTag());
        emb.setColor(Global.embedColor);
        emb.setTitle("Blackjack");
        String desc = "My hand: " + getAsString(bot) + " = " + getSum(bot) + "\n";
        desc += "Your hand: " + getAsString(user) + " = " + getSum(user);
        emb.addField("Status", "Select your choice", false);
        emb.setDescription(desc);
        if (getSum(bot) > 21) {
            emb.clearFields();
            emb.setColor(Color.green);
            emb.addField("Status", "I Busted!\n**You won**", true);
            emb.addField("Coins won", (bet*2)+" "+Global.broCoin.getAsMention(),true);
            processSQL(ctx.getMember(), bet*2, msg, emb);
            if (!edit) {
                channel.sendMessageEmbeds(emb.build()).setActionRows(b).queue();
            } else {
                msg.editMessageEmbeds(emb.build()).setActionRows(b).queue();
            }
            return;
        }
        if (getSum(bot) == 21) {
            emb.clearFields();
            emb.setColor(Color.red);
            emb.addField("Status", "**BLACKJACK**\nYou lost!", true);
            emb.addField("Coins lost", (bet)+" "+Global.broCoin.getAsMention(),true);
            processSQL(ctx.getMember(), -bet, msg, emb);
            if (!edit) {
                channel.sendMessageEmbeds(emb.build()).setActionRows(b).queue();
            } else {
                msg.editMessageEmbeds(emb.build()).setActionRows(b).queue();
            }
            return;
        }
        if (getSum(user) > 21) {
            emb.clearFields();
            emb.setColor(Color.red);
            emb.addField("Status", "You busted\n**I won**", true);
            emb.addField("Coins lost", (bet)+" "+Global.broCoin.getAsMention(),true);
            processSQL(ctx.getMember(), -bet, msg, emb);
            if (!edit) {
                channel.sendMessageEmbeds(emb.build()).setActionRows(b).queue();
            } else {
                msg.editMessageEmbeds(emb.build()).setActionRows(b).queue();
            }
            return;
        }
        if (getSum(user) == 21) {
            emb.clearFields();
            emb.setColor(Color.green);
            emb.addField("Status", "**BLACKJACK**\n**You won!**", true);
            emb.addField("Coins won", (bet*2)+" "+Global.broCoin.getAsMention(),true);
            processSQL(ctx.getMember(), bet*2, msg, emb);
            if (!edit) {
                channel.sendMessageEmbeds(emb.build()).setActionRows(b).queue();
            } else {
                msg.editMessageEmbeds(emb.build()).setActionRows(b).queue();
            }
            return;
        }


        if (check) {
            if (getSum(user) < getSum(bot)) {
                emb.clearFields();
                emb.setColor(Color.red);
                emb.addField("Status", "**You lost!**", true);
                emb.addField("Coins lost", (bet)+" "+Global.broCoin.getAsMention(),true);
                processSQL(ctx.getMember(), -bet, msg, emb);
                if (!edit) {
                    channel.sendMessageEmbeds(emb.build()).setActionRows(b).queue();
                } else {
                    msg.editMessageEmbeds(emb.build()).setActionRows(b).queue();
                }
                return;
            }
            if (getSum(user) > getSum(bot)) {
                emb.clearFields();
                emb.setColor(Color.green);
                emb.addField("Status", "**You won!**", true);
                emb.addField("Coins won", (bet*2)+" "+Global.broCoin.getAsMention(),true);
                processSQL(ctx.getMember(), bet*2, msg, emb);
                if (!edit) {
                    channel.sendMessageEmbeds(emb.build()).setActionRows(b).queue();
                } else {
                    msg.editMessageEmbeds(emb.build()).setActionRows(b).queue();
                }
                return;
            }
            if (getSum(user) == getSum(bot)) {
                emb.clearFields();
                emb.addField("Status", "**Its a draw!**", false);
                emb.addField("Coins returned", (bet)+" "+Global.broCoin.getAsMention(),true);
                processSQL(ctx.getMember(), bet, msg, emb);
                if (!edit) {
                    channel.sendMessageEmbeds(emb.build()).setActionRows(b).queue();
                } else {
                    msg.editMessageEmbeds(emb.build()).setActionRows(b).queue();
                }
                return;
            }
        }

        ArrayList<Button> actions = new ArrayList<>();
        actions.add(Button.success(ctx.getUser().getId()+"::hit", "Hit"));
        actions.add(Button.danger(ctx.getUser().getId()+"::stand", "Stand"));

        if (!edit) {
            channel.sendMessageEmbeds(emb.build()).setActionRows(ActionRow.of(actions)).queue(
                    message -> {
                        actions(ctx, channel, message, user, bot, deck, emb, bet);
                    }
            );
        } else {
            msg.editMessageEmbeds(emb.build()).setActionRows(ActionRow.of(actions)).queue(
                    message -> {
                        actions(ctx, channel, message, user, bot, deck, emb, bet);
                    }
            );
        }

    }

    public void actions(SlashCommandInteractionEvent ctx, TextChannel channel, Message msg, List<String> userC, List<String> botC, List<String> deck, EmbedBuilder emb, int bet) {
        waiter.waitForEvent(
                ButtonInteractionEvent.class, (event) -> {
                    User user = event.getUser();
                    return !user.isBot() && user.equals(ctx.getUser()) && event.getChannel().equals(channel);
                },
                (event) -> {

                    if (event.getButton().getId().equals(event.getUser().getId()+"::hit")) {
                        userC.add(drawCard(deck));
                        event.editMessageEmbeds(emb.build()).queue(
                                __ -> {bj(deck, botC, userC, emb, channel, false, ctx, true, msg, bet);}
                        );
                        return;
                    } else if (event.getButton().getId().equals(event.getUser().getId()+"::stand")) {
                        while (getSum(botC) < 17) {
                            botC.add(drawCard(deck));
                        }
                        event.editMessageEmbeds(emb.build()).queue(
                                __ -> {bj(deck, botC, userC, emb, channel, true, ctx, true, msg, bet);}
                        );
                        return;
                    }

                },
                5, TimeUnit.MINUTES,
                () -> {
                    try {
                        broCoinsSQL.updateBrocoins(ctx.getMember(), bet);
                        EmbedBuilder emb2 = new EmbedBuilder();
                        emb2.setColor(Color.RED);
                        emb2.setTitle(ctx.getUser().getName()+" didnt reply in time. Considered it as a loss!");
                        emb2.setDescription(ctx.getUser().getName()+" lost their bet of "+bet+" "+Global.broCoin.getAsMention());
                        ActionRow b = ActionRow.of(Button.secondary("...", "Game complete").asDisabled());
                        msg.editMessageEmbeds(emb2.build()).setActionRows(b).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                        EmbedBuilder emb2 = new EmbedBuilder();
                        emb2.setColor(Color.RED);
                        emb2.setTitle("There was an error while processing the timeout");
                        ActionRow b = ActionRow.of(Button.secondary("...", "Game completed with errors").asDisabled());
                        msg.editMessageEmbeds(emb2.build()).setActionRows(b).queue();
                    }
                }
        );

    }


    public String drawCard(List<String> deck) {
        String r = deck.get(0);
        deck.remove(0);
        return r;
    }

    public int getSum(List<String> deck) {
        int i = 0;
        for (String s : deck) {
            i += Integer.parseInt(s);
        }
        return i;
    }

    public static String getAsString(List<String> args) {
        return String.join(" ", args.subList(0, args.size()));
    }

    public void processSQL(Member member, int amountOfChange,Message msg, EmbedBuilder emb) {
        try {
            broCoinsSQL.updateBrocoins(member,amountOfChange);
        } catch (SQLException e) {
            ActionRow b = ActionRow.of(Button.secondary("...", "Game complete with errors").asDisabled());
            emb.clear();
            emb.setColor(Global.embedColor);
            emb.setDescription("There was an error on the SQL query");
            msg.editMessageEmbeds(emb.build()).setActionRows(b).queue();
            throw new RuntimeException(e);
        }
        //Todo: Logging here if they lost or won
    }
}
