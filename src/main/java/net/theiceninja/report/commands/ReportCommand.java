package net.theiceninja.report.commands;

import net.theiceninja.report.Main;
import net.theiceninja.report.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {
    Main plugin;
    public ReportCommand(Main plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ColorUtils.color(plugin.getConfig().getString("player_err")));
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0){
            p.sendMessage(ColorUtils.color(plugin.getConfig().getString("usage")));
            return true;
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                p.sendMessage(ColorUtils.color(plugin.getConfig().getString("player_null")));
                return true;
            }
            if (target == p) {
                p.sendMessage(ColorUtils.color(plugin.getConfig().getString("self_report")));
                return true;
            }
            p.sendMessage(ColorUtils.color(plugin.getConfig().getString("usage")));
        } else if (args.length >= 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == p){
                p.sendMessage(ColorUtils.color(plugin.getConfig().getString("self_report")));
                return true;
            }
            if (target == null){
                p.sendMessage(ColorUtils.color(plugin.getConfig().getString("player_null")));
                return true;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; i++){
                builder.append(args[i]);
                builder.append(" ");
            }
            String reason = builder.toString();
            p.sendMessage(ColorUtils.color(plugin.getConfig().getString("success")));
            for (Player players : Bukkit.getOnlinePlayers()){
                if (players.hasPermission("report.read")){
                    players.sendMessage(ColorUtils.color(plugin.getConfig().getString("report-staff").replaceAll("%player%", p.getDisplayName()).replaceAll("%target%", target.getDisplayName()).replaceAll("%reason%", reason)));
                }

            }
            plugin.sendMessage(plugin.getConfig().getString("embed_color"), plugin.getConfig().getString("title"), plugin.getConfig().getString("name"),plugin.getConfig().getString("value").replaceAll("%player%", p.getDisplayName()).replaceAll("%target%", target.getDisplayName()).replaceAll("%reason%", reason), "https://crafatar.com/avatars/" + target.getUniqueId().toString());
        }


        return true;
    }
}
