package net.elicodes.bot.listeners;

import com.nickuc.login.api.events.AsyncAuthenticateEvent;
import com.nickuc.login.api.events.AuthenticateEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.elicodes.bot.Main;
import net.elicodes.bot.bot.Bot;
import net.elicodes.bot.controllers.BotController;
import net.elicodes.bot.models.StaffUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.awt.Color;

public class nLoginJoinEvent implements Listener {

    @EventHandler
    void evento(AuthenticateEvent e) {
        Player player = e.getPlayer();
        if (Bot.jda == null) {
            player.kickPlayer(Main.getPlugin().getConfig().getString("Messages.nao-iniciou").replace("&", "§"));
        } else {
            if (Main.getPlugin().getController().isStaffUser(player.getName())) {
                StaffUser staffUser = Main.getPlugin().getController().getStaffUser(player.getName());
                staffUser.setLogado(true);
                try {
                    String userID = staffUser.getUserID();
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(Color.yellow);
                    embedBuilder.setTitle(Main.getPlugin().getConfig().getString("Messages.embed.title").replace("@player", player.getName()));
                    embedBuilder.addField(Main.getPlugin().getConfig().getString("Messages.embed.field.name").replace("@player", player.getName()), Main.getPlugin().getConfig().getString("Messages.embed.field.value").replace("@player", player.getName()), false);
                    embedBuilder.setFooter(Main.getPlugin().getConfig().getString("Messages.embed.footer.text").replace("@player", player.getName()), Main.getPlugin().getConfig().getString("Messages.embed.footer.image"));

                    System.out.println(Main.getPlugin().getConfig().getString("Bot.channel"));
                    System.out.println(Bot.jda);
                    TextChannel channel = Bot.jda.getTextChannelById(Main.getPlugin().getConfig().getString("Bot.channel"));
                    player.sendMessage(Main.getPlugin().getConfig().getString("Messages.se-autentique").replace("&", "§"));
                    if (channel != null) {
                        channel.sendMessage(embedBuilder.build()).queue(msg -> {
                            BotController.messages.put(msg.getId(), userID);
                            msg.addReaction("❌").queue();
                            msg.addReaction("✅").queue();
                        });
                        Main.getPlugin().getController().startTimer(staffUser);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
