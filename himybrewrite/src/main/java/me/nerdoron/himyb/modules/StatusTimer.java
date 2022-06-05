package me.nerdoron.himyb.modules;

import java.util.Random;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class StatusTimer {
    public static void changeActivity(JDA api) {
        String[] statuses = { "When I get sad, I stop being sad and be awesome instead. True story. | /help",
                "THE MACHINES HAVE WON! | /help", "Mosbius designs has failed. | /help",
                "Whatever you do in life it's not legendary unless your friends are there to see it. | /help",
                "SUIT UP! | /help", "A lie is just a really great story that someone ruined with the truth. | /help",
                "Think of me like Yoda but instead of being small and green I wear suits and I'm awesome. | /help",
                "Suits are full of joy. They’re the sartorial equivalent of a baby’s smile. | /help",
                "ALL HAIL BEERCULES! | /help", "Andiamo fratello, non Mastroianni tutti i funyuns | /help",
                "Where's the poop, Robin? | /help", "Thank you, Linus | /help", "Come again for big fudge? | /help",
                "NOBODY ASKED YOU PATRICE! | /help", "El ganso con la riñonera! | /help" };
        Random random = new Random();
        String status = statuses[random.nextInt(statuses.length)];
        api.getPresence().setActivity(Activity.playing(status));

    }
}