package me.nerdoron.himyb.modules.selfpromo;

import me.nerdoron.himyb.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SubmitLinks extends ListenerAdapter {

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("selfpromo-modal")) {
            String link = event.getValue("selfpromo-link").getAsString();
            String description = event.getValue("selfpromo-desc").getAsString();
            String additional = event.getValue("selfpromo-additional").getAsString();

            MessageEmbed selfpromo = new EmbedBuilder()
                    .setTitle("Self Promotion Link Submission")
                    .setDescription("Sent by: " + event.getUser().getAsMention() + "\nID:"
                            + event.getUser().getId())
                    .addField("Link of promotion", link, false)
                    .addField("Description of promotion", description, false)
                    .addField("Additional information", additional, false)
                    .setColor(Global.embedColor)
                    .setFooter(Global.footertext, Global.footerpfp)
                    .build();

            event.getGuild().getTextChannelById("991281346699870238").sendMessageEmbeds(selfpromo).queue();
            event.deferReply().setEphemeral(true).setContent("Sent your link for staff review.").queue();

        }
    }

}
