package me.nerdoron.himyb.modules.selfpromo;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class SelfPromoHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String send = "__**Self Promotion Channel**__\n" +
                "In this channel, any person who is above level 10 with <@339254240012664832> (`=rank`), can submit a self promotion link using /selfpromo. \n"
                +
                "\n" +
                "Due to past incidents, all messages will require Moderator Approval before being posted. ";
        if (!event.getChannel().getId().equals("978994328561147914"))
            return; // Change ID

        if (event.getAuthor().isBot())
            return;

        TextChannel channel = event.getTextChannel();
        channel.getIterableHistory().takeAsync(3).thenApplyAsync(
                messages -> {
                    boolean found = false;
                    for (Message message : messages) {
                        if (message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                            found = true;
                            message.delete().queue(
                                    __ -> {
                                        channel.sendMessage(send).queue();
                                    });
                        }
                    }
                    if (!found) {
                        channel.sendMessage(send).queue();
                    }
                    return 0;
                });

    }

}
