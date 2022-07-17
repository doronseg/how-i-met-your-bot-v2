package me.nerdoron.himyb.commands.staff;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import groovy.lang.GroovyShell;
import groovy.util.logging.Commons;
import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules._bot.BotCommandsHandler;
import me.nerdoron.himyb.modules.brocoins.BroCoinsSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class EvalCommand extends SlashCommand {
    private final GroovyShell engine;
    private final String imports;
    private final EventWaiter waiter;
    private final BotCommandsHandler manager;
    private final BroCoinsSQL broCoinsSQL = new BroCoinsSQL();
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public EvalCommand(EventWaiter waiter,BotCommandsHandler manager) {
        this.waiter = waiter;
        this.engine = new GroovyShell();
        this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.api.*;\n" +
                "import net.dv8tion.jda.api.entities.*;\n" +
                "import net.dv8tion.jda.api.managers.*;" +
                "import net.dv8tion.jda.api.events.*;\n" +
                "import net.dv8tion.jda.api.events.interaction.*;\n" +
                "import net.dv8tion.jda.api.interactions.*;\n" +
                "import net.dv8tion.jda.api.interactions.commands.*;\n" +
                "import net.dv8tion.jda.api.interactions.commands.build.*;\n" +
                "import me.nerdoron.himyb.*;\n" +
                "import me.nerdoron.himyb.commands.*;  ";
        this.manager = manager;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        EmbedBuilder emb = new EmbedBuilder();

        if (!member.getId().equals("229016449593769984") && !member.getId().equals("221204198287605770")) {
            event.reply(":x: **Not allowed.**").setEphemeral(true).queue();
            return;
        }

        Modal.Builder modal = Modal.create(event.getUser().getId()+":Eval", "Eval");
        TextInput TIcode = TextInput.create("code", "What to execute in the bot", TextInputStyle.PARAGRAPH).build();
        modal.addActionRow(TIcode);

        event.replyModal(modal.build()).queue(
                __ -> {
                    awaitModal(event, emb, waiter, manager);
                }
        );
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("eval", "Run java code on the bot");
    }

    private void awaitModal(SlashCommandInteractionEvent ctx, EmbedBuilder emb, EventWaiter waiter, BotCommandsHandler manager) {
        waiter.waitForEvent(
                ModalInteractionEvent.class, (event) -> {
                    User user = event.getUser();
                    return (!user.getId().equals("229016449593769984") || !user.getId().equals("221204198287605770")) && event.getInteraction().getModalId().contains(ctx.getUser().getId()+":Eval");
                },
                (event) -> {

                    String code = event.getValue("code").getAsString();

                    if (code.toLowerCase().contains("token")) {
                        emb.setTitle("Eval canceled");
                        emb.setDescription("The eval command detected a special keyword on the command.");
                        event.replyEmbeds(emb.build()).setEphemeral(true).queue();
                        return;
                    }

                    emb.setTitle("Eval received");
                    emb.setDescription("Processing...");
                    event.replyEmbeds(emb.build()).setEphemeral(true).queue(
                            interactionHook -> {
                                eval(code, ctx, emb, interactionHook, waiter, manager);
                                logger.warn("EVAL | "+event.getUser().getAsTag()+" IS EVALING: "+code);
                            }
                    );

                },
                30, TimeUnit.SECONDS,
                () -> {

                }
        );
    }

    public void eval(String args, SlashCommandInteractionEvent event, EmbedBuilder emb, InteractionHook message, EventWaiter waiter, BotCommandsHandler manager) {
        try {
            engine.setProperty("args", args);
            engine.setProperty("event", event);
            engine.setProperty("channel", event.getChannel());
            engine.setProperty("jda", event.getJDA());
            engine.setProperty("guild", event.getGuild());
            engine.setProperty("member", event.getMember());
            engine.setProperty("event", event);
            engine.setProperty("waiter", waiter);
            engine.setProperty("manager", manager);
            engine.setProperty("message", message);
            engine.setProperty("brocoins", broCoinsSQL);

            String script = imports + args;
            Object out = engine.evaluate(script);

            emb.setColor(Global.embedColor);
            emb.setTitle("Eval results");
            if (out == null) {
                emb.setDescription("**Eval returned no errors**");
                logger.warn("EVAL RETURNED NO ERRORS");
            } else {
                emb.setDescription("```" + out.toString() + "```");
                logger.warn("EVAL RESULT: "+out.toString());
            }
            EmbedBuilder finalEmb = emb;
            message.editOriginalEmbeds(emb.build()).queue(
                    message1 -> {},
                    (__) -> {
                        finalEmb.setDescription("```" + "Output too large to display" + "```");
                        message.editOriginalEmbeds(finalEmb.build()).queue(
                                message1 -> {}
                        );
                    }
            );
        } catch (Exception e) {
            emb.setColor(Color.orange);
            emb.setTitle("Eval results");
            emb.setDescription("```" + e.getMessage() + "```");
            EmbedBuilder finalEmb1 = emb;
            message.editOriginalEmbeds(emb.build()).queue();
        }
    }


}
