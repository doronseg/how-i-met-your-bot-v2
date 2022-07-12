package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

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

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData cmd = Commands.slash("revive", "Send the chat revive message")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE));

        return cmd;
    }
}
