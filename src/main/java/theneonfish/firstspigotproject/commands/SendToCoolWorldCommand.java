package theneonfish.firstspigotproject.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import theneonfish.firstspigotproject.Main;

public class SendToCoolWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            World world = Bukkit.getServer().getWorld("CustomCoolWorld");
            if (p.getWorld() != world) {
                p.sendMessage(ChatColor.GOLD + "Teleporting to cool world!");
                p.teleport(world.getSpawnLocation());
            } else {
                p.sendMessage(ChatColor.GOLD + "Exiting cool world!");
                p.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
            }
        }
        return true;
    }
}
