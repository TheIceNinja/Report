package net.theiceninja.report.commands;

import net.theiceninja.report.Main;
import net.theiceninja.report.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportReloadCommand implements CommandExecutor {
    Main plugin;
    public ReportReloadCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.color(plugin.getConfig().getString("player_err")));
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("report.reload")) {
            p.sendMessage(ColorUtils.color(plugin.getConfig().getString("no_perms")));
            return true;
        }
        plugin.reloadConfig();
        p.sendMessage(ColorUtils.color(plugin.getConfig().getString("plugin_reload")));

        return true;
    }
}
