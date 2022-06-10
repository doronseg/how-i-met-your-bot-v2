package me.nerdoron.himyb.modules.birthday;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class BirthdayFunction extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        JDA api = event.getJDA();
        BirthdayChecks birthdayChecks = new BirthdayChecks();
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Values from my testing server. Chance once everything is fine
        Guild guild = api.getGuildById("850396197646106624");
        Role role = guild.getRoleById("982972013687754753");
        TextChannel channel = guild.getTextChannelById("901497362160169010");

        ArrayList<String> birthdaysUserIDs = birthdayChecks.getBirthdays(day, month);

        guild.findMembersWithRoles(role).onSuccess(members -> { // Find members with the BDay role and remove the role
                                                                // from them
            boolean someoneHasBDayRole = false;
            for (Member member : members) {
                if (!birthdaysUserIDs.contains(member.getId())) {
                    guild.removeRoleFromMember(member, role).queue();
                } else {
                    someoneHasBDayRole = true;
                }
            }

            if (someoneHasBDayRole)
                return; // People already have the role

            if (birthdaysUserIDs.size() == 0)
                return; // If its no-one's birthday don't continue
            String mentionUsers = "";
            for (String birthdayUserID : birthdaysUserIDs) { // Iterate over the IDs adding them the BDay role
                guild.retrieveMemberById(birthdayUserID).queue(
                        member -> {
                            if (!member.getRoles().contains(role)) {
                                guild.addRoleToMember(member, role).queue();
                            }
                        });
                mentionUsers += "<@" + birthdayUserID + "> ";
            }

            channel.sendMessage(role.getAsMention() + " **to the folowing members:**\n" + mentionUsers).queue();
        });


    }

}
