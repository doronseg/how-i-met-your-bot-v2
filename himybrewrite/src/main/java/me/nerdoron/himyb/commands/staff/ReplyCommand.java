package me.nerdoron.himyb.commands.staff;

import me.nerdoron.himyb.commands.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class ReplyCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)))
            return;
        String messageId = event.getOption("replyto").getAsString();
        Message messageToReply = event.getTextChannel().retrieveMessageById(messageId).complete();
        String message = event.getOption("message").getAsString();
        messageToReply.reply(message).queue();
        event.deferReply().setEphemeral(true).setContent("Sent your message.").queue();
        event.getGuild().getTextChannelById("850447694673739816").sendMessage(event.getUser().getName() + "#"
                + event.getUser().getDiscriminator() + " used reply command in "
                + event.getTextChannel().getAsMention())
                .queue();
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData cmd = Commands.slash("reply", "Make the bot reply to a message")
                .addOption(OptionType.STRING, "replyto",
                        "What message would you like to reply to? (ID ONLY, MAKE SURE YOU ARE IN THE SAME CHANNEL)",
                        true)
                .addOption(OptionType.STRING, "message", "What would you like to say?", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE));

        return cmd;
    }

}
