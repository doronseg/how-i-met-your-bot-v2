package me.nerdoron.himyb.commands.fun.gambling;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules._bot.CooldownManager;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotmachineCommand extends SlashCommand {
    BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
    String[] figures = new String[]{"⬜","\uD83D\uDFE7","\uD83D\uDFE6", "\uD83D\uDFE5", "\uD83D\uDFE9"};
    int baseOdd = 5; //Paying the minimum amount you have 1/this chances of winning
    int minBet = 10; //Set the same as the slash cmd option

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (Global.COOLDOWN_MANAGER.hasCooldown(CooldownManager.commandID(event))) {
            String time = Global.COOLDOWN_MANAGER.parseCooldown(CooldownManager.commandID(event));
            event.reply("You have already betted on the slot machine. Please try again in " + time).setEphemeral(true).queue();
            return;
        }

        if (!broCoinsSQL.hasBrocoins(event.getMember())) {
            event.reply("You don't have a BroCoins account!").setEphemeral(true).queue();
            return;
        }

        int bet = event.getOption("bet").getAsInt();
        int userCoins = broCoinsSQL.getBrocoins(event.getMember());
        if (userCoins < bet) {
            event.reply("I'm sorry but you are missing " + (bet - userCoins) + " " + Global.broCoin.getAsMention()
                    + " to play").queue();
            return;
        }

        //TODO: Change cooldown to something longer or leave it like this, idk u decide doron
        Global.COOLDOWN_MANAGER.addCooldown(CooldownManager.commandID(event), 10);



        ArrayList<String> position1 = new ArrayList<>();
        ArrayList<String> position2 = new ArrayList<>();
        ArrayList<String> position3 = new ArrayList<>();
        resetWheels(position1,position2,position3);

        ArrayList<String> lines = new ArrayList<>();
        int rollAmount = Global.generateNumber(3,7);
        int Odds = ((bet*baseOdd)/minBet);
        boolean doesWin = generateLines(Odds, rollAmount, lines, position1, position2, position3);

        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle("Slot machine");
        emb.setColor(Global.embedColor);
        emb.setDescription(parseList(lines));
        emb.addField("Member", event.getMember().getAsMention()+" "+bet+" "+Global.broCoin.getAsMention(),true);
        emb.addField("Result","`Spinning...`",true);
        emb.setFooter("Based on bet calculated an odd of 1/"+Odds+" of winning");

        event.replyEmbeds(emb.build()).queue(
                hook -> {
                    new Thread(() -> {
                        for (int i = 0; i < rollAmount+1; i++) {
                            try {Thread.sleep(1000);} catch (InterruptedException e) {}
                            lines.remove(0);
                            emb.setDescription(parseList(lines));
                            hook.editOriginalEmbeds(emb.build()).queue();
                        }
                        if (doesWin) {
                            try {
                                broCoinsSQL.updateBrocoins(event.getMember(), bet*2);
                            } catch (SQLException e) {
                                emb.clear();
                                emb.setColor(Global.embedColor);
                                emb.setDescription("Error in DB");
                                hook.editOriginalEmbeds(emb.build()).queue();
                                return;
                            }
                            emb.clearFields();
                            emb.addField("Member", event.getMember().getAsMention()+" "+bet+" "+Global.broCoin.getAsMention(),true);
                            emb.addField("Result","**WINNER**",true);
                            emb.setColor(Color.green);
                            hook.editOriginalEmbeds(emb.build()).queue();
                        } else {
                            try {
                                broCoinsSQL.updateBrocoins(event.getMember(), -bet);
                            } catch (SQLException e) {
                                emb.clear();
                                emb.setColor(Global.embedColor);
                                emb.setDescription("Error in DB");
                                hook.editOriginalEmbeds(emb.build()).queue();
                                return;
                            }
                            emb.clearFields();
                            emb.addField("Member", event.getMember().getAsMention()+" "+bet+" "+Global.broCoin.getAsMention(),true);
                            emb.addField("Result","Looser",true);
                            hook.editOriginalEmbeds(emb.build()).queue();
                        }
                    }).start();
                }
        );

    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData cmd = Commands.slash("slotmachine", "Bet in a slot machine. Maybe you will win");
        OptionData bet = new OptionData(OptionType.INTEGER, "bet", "How many BroCoins you wanna bet", true);
        bet.setMinValue(10);
        cmd.addOptions(bet);
        return cmd;
    }

    private boolean generateLines(int oddsOfWinning, int rollAmount, ArrayList<String> lines, ArrayList<String> position1, ArrayList<String> position2, ArrayList<String> position3) {
        int rng = Global.generateNumber(1,oddsOfWinning);
        boolean shouldWin = (rng == 1);

        for (int i = 0; i < 3; i++) {
            lines.add(generateLoosing(position1,position2,position3));
        }
        if (shouldWin) {
            for (int i = 0; i < rollAmount - 1; i++) {
                lines.add(generateLoosing(position1,position2,position3));
            }
            lines.add(generateWinning(position1));
            lines.add(generateLoosing(position1,position2,position3));
        } else {
            for (int i = 0; i < rollAmount+1; i++) {
                lines.add(generateLoosing(position1,position2,position3));
            }
        }

        return shouldWin;
    }

    private String generateLoosing(ArrayList<String> p1, ArrayList<String> p2, ArrayList<String> p3) {
        String s1 = p1.get(0);
        String s2 = p2.get(0);
        String s3 = p3.get(0);

        Collections.shuffle(p1);
        Collections.shuffle(p2);
        Collections.shuffle(p3);

        while ( (p1.equals(p2) && p2.equals(p3)) ||
                (p1.equals(p3) && p3.equals(p2)) ||
                (p3.equals(p2) && p1.equals(p2))) {
            Collections.shuffle(p1);
            Collections.shuffle(p2);
            Collections.shuffle(p3);
        }
        return s1+s2+s3;
    }

    private String generateWinning(ArrayList<String> p1) {
        Collections.shuffle(p1);
        String selected = p1.get(0);
        return selected+selected+selected;
    }

    private void resetWheels(ArrayList<String> p1, ArrayList<String> p2, ArrayList<String> p3) {
        p1.addAll(List.of(figures));
        p2.addAll(List.of(figures));
        p3.addAll(List.of(figures));
    }

    private String parseList(ArrayList<String> list) {
        String r = "";
        for (int i = 0; i < 3; i++) {
            r+=list.get(i)+(i==1 ? "⬅️":"")+"\n";
        }
        return r;
    }
}
