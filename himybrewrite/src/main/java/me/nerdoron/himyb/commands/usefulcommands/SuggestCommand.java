package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SuggestCommand extends Command {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String suggestion = event.getOption("suggestion").getAsString();
        String type = event.getOption("type").getAsString();
        User user = event.getUser();

        if (!(event.isFromGuild())) {
            event.deferReply().setEphemeral(true).setContent("This command can only be executed in the server.")
                    .queue();
            return;
        }

        MessageEmbed suggestionEmbed = new EmbedBuilder()
                .setAuthor(user.getAsTag() + "'s suggestion", null, user.getAvatarUrl())
                .setDescription(suggestion)
                .setColor(Global.embedColor)
                .setFooter(Global.footertext, Global.footerpfp)
                .build();

        switch (type) {
            case "server":
                TextChannel serversuggestions = event.getGuild().getTextChannelById("857560927134285824");
                serversuggestions.sendMessageEmbeds(suggestionEmbed).queue((message) -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❌").queue();
                });
                event.deferReply().setEphemeral(true)
                        .setContent("Suggestion sent! Remember that sending troll suggestions might result in a strike")
                        .queue();
                break;
            case "video":
                TextChannel vidsuggestions = event.getGuild().getTextChannelById("869853502741020752");
                vidsuggestions.sendMessageEmbeds(suggestionEmbed).queue((message) -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❌").queue();
                });
                event.deferReply().setEphemeral(true)
                        .setContent("Suggestion sent! Remember that sending troll suggestions might result in a strike")
                        .queue();
                break;
            case "bot":
                TextChannel botsuggestions = event.getGuild().getTextChannelById("869853782949908481");
                botsuggestions.sendMessageEmbeds(suggestionEmbed).queue((message) -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❌").queue();
                });
                event.deferReply().setEphemeral(true)
                        .setContent("Suggestion sent! Remember that sending troll suggestions might result in a strike")
                        .queue();
                break;
            case "chain":
                TextChannel chainsuggestions = event.getGuild().getTextChannelById("884195377883013200");
                chainsuggestions.sendMessageEmbeds(suggestionEmbed).queue((message) -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❌").queue();
                });
                event.deferReply().setEphemeral(true)
                        .setContent("Suggestion sent! Remember that sending troll suggestions might result in a strike")
                        .queue();
                break;
            default:
                event.deferReply().setEphemeral(true)
                        .setContent("Unknown type. Available types: `server`, `video`, `bot` & `chain`.").queue();
                break;
        }
    }

}
