package me.nerdoron.himyb.commands.staff;

import me.nerdoron.himyb.commands.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SayCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)))
            return;
        Channel channel = event.getOption("channel").getAsGuildChannel();
        // Removed redoundant check since discord will not allow other types to be
        // selected.
        // if (!(channel.getType().equals(ChannelType.TEXT))) {
        // event.deferReply().setEphemeral(true).setContent("I can only say things in
        // text channels.").queue();
        // return;
        // }
        String message = event.getOption("message").getAsString();
        TextChannel textChannel = event.getGuild().getTextChannelById(channel.getId());
        if (message.equals("🐶")) {
            event.reply("stop.").setEphemeral(true).queue();
            event.getGuild().getTextChannelById("850438624024854548")
                    .sendMessage(event.getUser().getAsMention() + " tried to send a zitch dog with say command")
                    .queue();
            return;
        }
        textChannel.sendMessage(message).queue();
        event.deferReply().setEphemeral(true).setContent("Sent your message.").queue();
        event.getGuild().getTextChannelById("850447694673739816").sendMessage(event.getUser().getName() + "#"
                + event.getUser().getDiscriminator() + " used say command in " + textChannel.getAsMention()).queue();

    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData say = Commands.slash("say", "Make the bot say something");
        OptionData channel = new OptionData(OptionType.CHANNEL, "channel",
                "Which channel would you like to say it in?", true);
        channel.setChannelTypes(ChannelType.TEXT); // Limit this option to TextChannels
        say
                .addOption(OptionType.CHANNEL, "channel", "What channel would you like to send it to?",
                        true)
                .addOption(OptionType.STRING, "message", "What would you like to say?", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE));

        return say;
    }

}
