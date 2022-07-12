package me.nerdoron.himyb.commands.funcommands;

import java.util.Random;

import me.nerdoron.himyb.commands.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class EightBallCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String[] responses = {
                "http://www.redkid.net/generator/8ball/newsign.php?line1=As+I+&line2=see+it%2C&line3=Yes.&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Ask&line2=Again&line3=Later&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Cannot&line2=predict&line3=now&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Don%27t&line2=count&line3=on+it&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Decidedly&line2=so&line3=&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Most&line2=likely&line3=&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=My+reply&line2=is&line3=no&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Outlook+&line2=not+so&line3=good&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=I+don%27t&line2=think+&line3=so&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Very&line2=doubtfu&line3=l&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Without&line2=a&line3=doubt&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=&line2=Yes.&line3=&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=You+may&line2=relay&line3=on+it&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Probably&line2=not&line3=&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=Reply+&line2=Hazy.&line3=&Shake+Me=Shake+Me",
                "http://www.redkid.net/generator/8ball/newsign.php?line1=I+don%27t&line2=think&line3=so&Shake+Me=Shake+Me" };
        Random random = new Random();
        String question = event.getOption("question").getAsString();
        if (!(question.endsWith("?"))) {
            event.reply("That doesn't look like a question.").setEphemeral(true).queue();
            return;
        }
        event.reply(event.getUser().getAsMention() + " asked me: " + question).queue((m) -> {
            event.getChannel().sendMessage(responses[random.nextInt(responses.length)]).queue();
        });
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData cmd = Commands.slash("8ball", "Ask the magic 8ball a question..")
                .addOption(OptionType.STRING, "question", "What's the question?",
                        true);

        return cmd;
    }

}
