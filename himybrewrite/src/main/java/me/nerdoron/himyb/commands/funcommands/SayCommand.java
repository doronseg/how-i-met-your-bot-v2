package me.nerdoron.himyb.commands.funcommands;

import me.nerdoron.himyb.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SayCommand extends Command {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)))
            return;
        Channel channel = event.getOption("channel").getAsGuildChannel();
        if (!(channel.getType().equals(ChannelType.TEXT))) {
            event.deferReply().setEphemeral(true).setContent("I can only say things in text channels.").queue();
            return;
        }
        String message = event.getOption("message").getAsString();
        TextChannel textChannel = event.getGuild().getTextChannelById(channel.getId());
        textChannel.sendMessage(message).queue();
        event.deferReply().setEphemeral(true).setContent("Sent your message.").queue();
        event.getGuild().getTextChannelById("850447694673739816").sendMessage(event.getUser().getName() + "#"
                + event.getUser().getDiscriminator() + " used say command in " + textChannel.getAsMention()).queue();

    }

}
