package theneonfish.firstspigotproject.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import theneonfish.firstspigotproject.Main;

public class LeaveMinigameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Must be a player to be in the queue!");
            return false;
        }
        Player p = (Player) commandSender;
        if (!Main.minigameList.contains(p)) {
            p.sendMessage(ChatColor.RED + "You are not already in the queue! Join the queue with /joinMinigameQueue!");
            return false;
        }
        Main.minigameList.remove(p);
        p.sendMessage("You have been removed from the queue!");
        return true;
    }
}
