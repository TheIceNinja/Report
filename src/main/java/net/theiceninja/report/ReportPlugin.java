package net.theiceninja.report;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.theiceninja.report.commands.ReportCommand;
import net.theiceninja.report.commands.ReportReloadCommand;
import net.theiceninja.report.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.awt.*;

public final class ReportPlugin extends JavaPlugin {
    public static JDA bot;
    @Override
    public void onEnable() {
        getCommand("report").setExecutor(new ReportCommand(this));
        getCommand("reportreload").setExecutor(new ReportReloadCommand(this));

        getConfig().options().copyDefaults(false);
        saveDefaultConfig();
        try {
           bot = JDABuilder.createDefault(getConfig().getString("discord_token")).build().awaitReady();

            switch (getConfig().getString("discord_activity")) {
                case "watching":
                    bot.getPresence().setActivity(Activity.watching(getConfig().getString("discord_stats")));
                    break;
                case "playing":
                    bot.getPresence().setActivity(Activity.playing(getConfig().getString("discord_stats")));
                    break;
                case "listening":
                    bot.getPresence().setActivity(Activity.listening(getConfig().getString("discord_stats")));
                    break;
            }

        } catch (InterruptedException | LoginException e) {
            Bukkit.getConsoleSender().sendMessage(ColorUtils.color("&cInvalid token please set in the config a real token."));
        }
    }

    @Override
    public void onDisable() {
        if (bot != null) bot.shutdownNow();
    }

    public void sendMessage(String color, String title, String name, String value, String url) {
        TextChannel reportChannel = bot.getTextChannelById(getConfig().getString("discord_channel_id"));
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title)
                .addField(name, value, false)
                .setColor(Color.decode(color))
                .setThumbnail(url);
        if (reportChannel != null) {
            reportChannel.sendMessageEmbeds(eb.build()).queue();

        } else {
            Bukkit.getConsoleSender().sendMessage(ColorUtils.color(getConfig().getString("err")));
        }
    }
}
