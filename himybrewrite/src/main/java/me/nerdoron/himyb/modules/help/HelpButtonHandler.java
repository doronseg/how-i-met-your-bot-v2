package me.nerdoron.himyb.modules.help;

import me.nerdoron.himyb.Global;
import me.nerdoron.himyb.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HelpButtonHandler extends ListenerAdapter {
    HelpEmbeds helpEmbeds = new HelpEmbeds();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        if (!buttonId.contains("HELP:"))
            return;
        String[] buttonSplit = buttonId.split(":");
        String uid = buttonSplit[0];
        String buttonCategory = buttonSplit[1];
        Message message = event.getMessage();

        if (!(uid.equals(event.getUser().getId()))) {
            event.deferReply().setEphemeral(true).setContent("You can not interact with this menu!").queue();
            return;
        }
        if (!message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            return;
        event.deferEdit().queue();

        if (buttonCategory.equals("main")) {
            message.editMessageEmbeds(helpEmbeds.mainMenu).queue();
            return;
        }

        String embedDescription = "A list of all commands under the `"+buttonCategory+"` category\n\n";
        for (SlashCommand command : Global.COMMANDS_HANDLER.commands) {
            if (!command.getCategory().equals(buttonCategory)) continue;

            String options = " ";
            for (OptionData option : command.getSlash().getOptions()) {
                options+="["+option.getName()+"] ";
            }

            String commandName = command.getSlash().getName()+options;
            commandName = commandName.trim();

            embedDescription+="`/"+commandName+"` - ";
            embedDescription+=command.getSlash().getDescription()+"\n";
        }
        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle(Global.COMMANDS_HANDLER.getCategoryDetailedName(buttonCategory));
        emb.setDescription(embedDescription);
        emb.setColor(Global.embedColor);
        emb.setFooter(Global.footertext, Global.footerpfp);
        message.editMessageEmbeds(emb.build()).queue();
    }
}
