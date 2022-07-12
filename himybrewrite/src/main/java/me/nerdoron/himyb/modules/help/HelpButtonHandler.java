package me.nerdoron.himyb.modules.help;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpButtonHandler extends ListenerAdapter {
    HelpEmbeds helpEmbeds = new HelpEmbeds();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        if (!buttonId.contains(":"))
            return;
        String[] buttonSplit = buttonId.split(":");
        String uid = buttonSplit[0];
        String buttonType = buttonSplit[1];
        Message message = event.getMessage();

        if (!(uid.equals(event.getUser().getId()))) {
            event.deferReply().setEphemeral(true).setContent("You can not interact with this menu!").queue();
            return;
        }
        if (!message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            return;
        event.deferEdit().queue();

        switch (buttonType) {
            case "main":
                message.editMessageEmbeds(helpEmbeds.mainMenu).queue();
                break;
            case "useful":
                message.editMessageEmbeds(helpEmbeds.usefulMenu).queue();
                break;
            case "fun":
                message.editMessageEmbeds(helpEmbeds.funMenu).queue();
                break;
            case "currency":
                message.editMessageEmbeds(helpEmbeds.currencyMenu).queue();
                break;
        }
    }
}
