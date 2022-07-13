package me.nerdoron.himyb.modules._bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    private final Map<String, OffsetDateTime> COOLDOWNS = new HashMap<>();

    public CooldownManager() {}

    public static String commandID(SlashCommandInteractionEvent event) {
        return event.getUser().getId()+event.getName();
    }

    public void addCooldown(String identifier, int timeInSeconds) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime plus = now.plusSeconds(timeInSeconds);
        COOLDOWNS.putIfAbsent(identifier, plus);
    }

    public boolean hasCooldown(String identifier) {
        OffsetDateTime now = OffsetDateTime.now();
        if (COOLDOWNS.containsKey(identifier)) {
            OffsetDateTime cooldown = COOLDOWNS.get(identifier);
            if (cooldown.isAfter(now)) {
                return true;
            } else {
                COOLDOWNS.remove(identifier);
                return false;
            }
        } else {
            return false;
        }
    }

    public String parseCooldown(String identifier) {
        return parseOffsetDateTimeHumanText(this.COOLDOWNS.get(identifier));
    }

    private String parseOffsetDateTimeHumanText(OffsetDateTime timeCreated) {
        OffsetDateTime now= OffsetDateTime.now();

        long sec = ChronoUnit.SECONDS.between(timeCreated,now)%60;
        long min = ChronoUnit.MINUTES.between(timeCreated,now)%60;
        long Hur = ChronoUnit.HOURS.between(timeCreated,now)%24;
        long day = ChronoUnit.DAYS.between(timeCreated,now)%31;
        long mth = ChronoUnit.MONTHS.between(timeCreated,now)%12;
        long yhr = ChronoUnit.YEARS.between(timeCreated,now);



        String send = "";

        if (yhr != 0) {
            send+= ""+Math.abs(yhr)+" Year";
        }
        if (mth != 0) {
            send+= ", "+Math.abs(mth)+" Month";
        }
        if (day != 0) {
            send+= ", "+Math.abs(day)+" Day";
        }
        if (Hur != 0) {
            send+= " "+Math.abs(Hur)+" hours";
        }
        if (min != 0) {
            send+= " "+Math.abs(min)+" minutes";
        }
        if (sec != 0) {
            send+= " "+Math.abs(sec)+" seconds";
        }
        return send.trim();
    }
}
