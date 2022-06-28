package me.nerdoron.himyb.modules.selfpromo;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class SelfPromoModal {

        TextInput link = TextInput
                        .create("selfpromo-link", "What is the link you want to promote?", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setMaxLength(100)
                        .setPlaceholder("discord.gg/himym")
                        .build();

        TextInput description = TextInput
                        .create("selfpromo-desc", "Describe what you want to promote:", TextInputStyle.PARAGRAPH)
                        .setMinLength(10)
                        .setMaxLength(1000)
                        .setRequired(true)
                        .setPlaceholder("This is an epic server I created, everyone should join it!")
                        .build();

        TextInput additional = TextInput
                        .create("selfpromo-additional", "Any additional information?", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("This is a discord partnered server!")
                        .setRequired(false)
                        .setMaxLength(1000)
                        .build();

        public Modal modal = Modal.create("selfpromo-modal", "Submit a link for Self Promotion.")
                        .addActionRows(ActionRow.of(link), ActionRow.of(description), ActionRow.of(additional)).build();

}
