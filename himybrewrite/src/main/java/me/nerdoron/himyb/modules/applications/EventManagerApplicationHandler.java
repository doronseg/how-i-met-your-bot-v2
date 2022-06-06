package me.nerdoron.himyb.modules.applications;

import me.nerdoron.himyb.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventManagerApplicationHandler extends ListenerAdapter {

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("event-modal")) {
            String typesOfEvents = event.getValue("event-types").getAsString();
            String howOften = event.getValue("event-often").getAsString();
            String irlCommitments = event.getValue("event-irl").getAsString();
            String whyPick = event.getValue("event-picked").getAsString();

            MessageEmbed application = new EmbedBuilder()
                    .setTitle("Event Manager Application")
                    .setDescription("Application sent by: " + event.getUser().getAsMention() + "\nID:"
                            + event.getUser().getId())
                    .addField("What types of events are you able to host?", typesOfEvents, false)
                    .addField("How often are you able to host events?", howOften, false)
                    .addField("What IRL commitments would affect your activity?", irlCommitments, false)
                    .addField("Why should we pick you over other candidates?", whyPick, false)
                    .setColor(Global.embedColor)
                    .setFooter(Global.footertext, Global.footerpfp)
                    .build();

            event.getGuild().getTextChannelById("983357026463805510").sendMessageEmbeds(application).queue();
            event.deferReply().setEphemeral(true).setContent("Sent your application!").queue();

        }
    }

}
