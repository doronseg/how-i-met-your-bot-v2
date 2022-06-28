package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.selfpromo.SelfPromoModal;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SelfPromoCommand extends SlashCommand {

    SelfPromoModal selfPromoModal = new SelfPromoModal();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getMember().getRoles())
                .contains(event.getGuild().getRoleById("850464921040846928"))) {
            event.deferReply().setEphemeral(true).setContent(
                    "Sorry, only members who have reached level 10 are able to submit self promotion links. To check your rank use `=r` in <#850437596487483443>")
                    .queue();
            return;
        }

        event.replyModal(selfPromoModal.modal).queue();
    }

}
