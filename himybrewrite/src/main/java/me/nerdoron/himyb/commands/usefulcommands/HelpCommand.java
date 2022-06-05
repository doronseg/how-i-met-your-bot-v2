package me.nerdoron.himyb.commands.usefulcommands;

import me.nerdoron.himyb.commands.SlashCommand;
import me.nerdoron.himyb.modules.help.HelpEmbeds;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class HelpCommand extends SlashCommand {
    HelpEmbeds helpEmbeds = new HelpEmbeds();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String uid = event.getUser().getId();
        event.replyEmbeds(helpEmbeds.mainMenu)
                .addActionRow(Button.secondary(uid + ":main", "🔮 Main Menu"),
                        Button.secondary(uid + ":useful", "🛠️ Useful Commands"))
                .queue();
    }

}
