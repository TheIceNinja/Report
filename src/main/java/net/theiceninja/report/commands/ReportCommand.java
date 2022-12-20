package net.theiceninja.report.commands;

import net.theiceninja.report.ReportPlugin;
import net.theiceninja.report.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {
    private ReportPlugin plugin;
    private List<UUID> inCooldown = new ArrayList<>();

    public ReportCommand(ReportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.color(plugin.getConfig().getString("player_err")));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ColorUtils.color(plugin.getConfig().getString("usage")));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ColorUtils.color(plugin.getConfig().getString("player_null")));
                return true;
            }

            if (target == player) {
                player.sendMessage(ColorUtils.color(plugin.getConfig().getString("self_report")));
                return true;
            }

            if (args.length == 1) {
                player.sendMessage(ColorUtils.color(plugin.getConfig().getString("usage")));
                return true;
            }

            if (inCooldown.contains(player.getUniqueId())) {
                player.sendMessage(ColorUtils.color(plugin.getConfig().getString("cooldown")));
                return true;
            }

            if (!inCooldown.contains(player.getUniqueId())) {
                inCooldown.add(player.getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        inCooldown.remove(player.getUniqueId());
                    }
                }.runTaskLater(plugin, 2400);
            }

            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]);
                builder.append(" ");
            }

            String reason = builder.toString();
            player.sendMessage(ColorUtils.color(plugin.getConfig().getString("success")));
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.hasPermission("report.read")) {
                    players.sendMessage(ColorUtils.color(
                            plugin.getConfig().getString("report-staff")
                                    .replaceAll("%player%", player.getDisplayName())
                                    .replaceAll("%target%", target.getDisplayName())
                                    .replaceAll("%reason%", reason))
                    );
                }
            }

            plugin.sendMessage(
                    plugin.getConfig().getString("embed_color"),
                    plugin.getConfig().getString("title"),
                    plugin.getConfig().getString("name"),
                    plugin.getConfig().getString("value")
                .replaceAll("%player%", player.getDisplayName())
                .replaceAll("%target%", target.getDisplayName())
                .replaceAll("%reason%", reason), "https://crafatar.com/avatars/" + target.getUniqueId()
            );

        }
        return true;
    }
}
