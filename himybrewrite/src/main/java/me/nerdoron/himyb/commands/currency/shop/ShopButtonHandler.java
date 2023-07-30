package me.nerdoron.himyb.commands.currency.shop;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ShopButtonHandler extends ListenerAdapter {
    ShopEmbeds shopEmbeds = new ShopEmbeds();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        if (!buttonId.contains("SHOP:"))
            return;

        String[] buttonSplit = buttonId.split(":");
        String uid = buttonSplit[1];
        String buttonCategory = buttonSplit[2];
        Message message = event.getMessage();

        if (!(uid.equals(event.getUser().getId()))) {
            event.deferReply().setEphemeral(true).setContent("You can not interact with this menu!").queue();
            return;
        }
        if (!message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            return;
        event.deferEdit().queue();

        if (buttonCategory.equals("main")) {
            message.editMessageEmbeds(shopEmbeds.mainEmbed).queue();
            return;
        } else if (buttonCategory.equals("xp")) {
            message.editMessageEmbeds(shopEmbeds.xpEmbed).queue();
            return;
        } else if (buttonCategory.equals("roles")) {
            message.editMessageEmbeds(shopEmbeds.roleEmbed).queue();
            return;
        }

    }
}
