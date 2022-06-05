package me.nerdoron.himyb.modules;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LeaveJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TextChannel joinLeaves = event.getGuild().getTextChannelById("867770758976372766");
        joinLeaves.sendMessage("**" + event.getUser().getName() + "**#" + event.getUser().getDiscriminator()
                + " is now the Blitz! <:awman:853645381215715360>").queue();
        updateChannelName(event.getGuild());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel joinLeaves = event.getGuild().getTextChannelById("867770758976372766");
        if (event.getMember() == null)
            return;

        if (event.getMember().getUser().getName().toLowerCase().contains("clonex")) {
            System.out.println(event.getMember().getUser().getName() + "was banned.");
            event.getGuild().ban(event.getMember(), 7, "Clonex alt. Please stop.").queue();
            return;
        }

        joinLeaves.sendMessage("Welcome " + event.getMember().getAsMention()
                + ", to **how i met your discord** <:hello:851462988153618452>").queue();
        updateChannelName(event.getGuild());

    }

    public void updateChannelName(Guild guild) {
        VoiceChannel memberCountNum = guild.getVoiceChannelById("851044075175411712");
        int memberCount = guild.getMemberCount();
        memberCountNum.getManager().setName("Member Count: " + memberCount).queue();
    }
}