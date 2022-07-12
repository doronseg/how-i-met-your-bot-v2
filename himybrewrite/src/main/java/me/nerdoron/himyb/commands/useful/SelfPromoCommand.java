package me.nerdoron.himyb.commands.useful;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.selfpromo.SelfPromoModal;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

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

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("selfpromo", "Submit a self promotion link");
    }

}
