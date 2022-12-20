package net.theiceninja.report.commands;

import net.theiceninja.report.ReportPlugin;
import net.theiceninja.report.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportReloadCommand implements CommandExecutor {
   private final ReportPlugin plugin;
    public ReportReloadCommand(ReportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.color(plugin.getConfig().getString("player_err")));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("report.reload")) {
            player.sendMessage(ColorUtils.color(plugin.getConfig().getString("no_perms")));
            return true;
        }

        plugin.reloadConfig();
        player.sendMessage(ColorUtils.color(plugin.getConfig().getString("plugin_reload")));

        return true;
    }
}
