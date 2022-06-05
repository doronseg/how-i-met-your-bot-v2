package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ReviveCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.isFromGuild())) {
            event.deferReply().setEphemeral(true).setContent("This command can only be executed in the server.")
                    .queue();
            return;
        }
        if (!(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)))
            return;
        event.deferReply().setEphemeral(true).setContent("Revived chat!").queue();
        event.getChannel().sendMessage("<@&900487372251213955>").queue();
        event.getChannel()
                .sendMessage("https://tenor.com/view/googas-wet-wet-cat-dead-chat-dead-chat-xd-gif-20820186")
                .queue();
    }
}
