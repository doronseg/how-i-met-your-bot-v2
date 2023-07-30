package me.nerdoron.himyb.modules.tickets;

import me.nerdoron.himyb.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Panels {

        public static MessageEmbed AdminStaff = new EmbedBuilder().setTitle("📇 Admin/Staff Ticket")
                        .setDescription("Select admin below to contact the **Administraton Team**\nThis should be used "
                                        + "that the Admins need to handle, so punishment appeals, mod reports, and etc.\n\n*Be advised that response times here "
                                        + "will be longer, so please use the other ticket system if it's not important*\n\n"
                                        +
                                        "Select staff to contact the staff team of the server")
                        .setFooter(Global.footertext, Global.footerpfp)

                        .setColor(Global.embedColor)
                        .build();

        public static MessageEmbed adminWelcome = new EmbedBuilder().setTitle("📇 Admin Ticket ").setDescription(
                        "Hello, the administration team will be with you as soon as they can. In the meantime. please let us know what you need.\nTo close this ticket, use the button")
                        .setColor(Global.embedColor)
                        .setFooter(Global.footertext, Global.footerpfp)

                        .build();

        public static MessageEmbed generalWelcome = new EmbedBuilder().setTitle("📇 Staff Ticket ").setDescription(
                        "Hello, the staff team will be with you as soon as they can. In the meantime. please let us know what you need.\nTo close this ticket, use the button")
                        .setColor(Global.embedColor)
                        .setFooter(Global.footertext, Global.footerpfp)
                        .build();

}
