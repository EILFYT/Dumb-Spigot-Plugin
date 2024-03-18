package theneonfish.firstspigotproject.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import theneonfish.firstspigotproject.Main;

public class JoinMinigameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Must be a player to enter the queue!");
            return false;
        }
        Player p = (Player) commandSender;
        if (Main.minigameList.contains(p)) {
            p.sendMessage(ChatColor.RED + "You are already in the queue!");
            return false;
        }
        p.sendMessage(ChatColor.GOLD + "Joined the minigame queue!");
        Main.minigameList.add(p);
        if (Main.minigameList.size() == 12) {
            p.sendMessage(ChatColor.GOLD + "The queue has 12 players! do /startminigame <\"randomize\" here if you want to randomize the teams, anything else if you don't> <the name of the world to go to>");
        }
        return false;
    }
}
