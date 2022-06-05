package me.nerdoron.himyb.commands.usefulhandlers;

import me.nerdoron.himyb.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ReviveCommand extends Command {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getChannel().getType().isGuild()))
            return;
        if (!(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)))
            return;
        event.deferReply().setEphemeral(true).setContent("Revived chat!").queue();
        event.getChannel().sendMessage("<@&900487372251213955>").queue();
        event.getChannel()
                .sendMessage("https://tenor.com/view/googas-wet-wet-cat-dead-chat-dead-chat-xd-gif-20820186")
                .queue();
    }
}
