package theneonfish.firstspigotproject.commands;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import theneonfish.firstspigotproject.Main;
import theneonfish.firstspigotproject.runnables.CTFMinigame;
import theneonfish.firstspigotproject.runnables.RunnableThings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class StartMinigameCommand implements CommandExecutor {
    //   /startminigame <true if teams are in order, false if you want to randomize>
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(ChatColor.RED + "[ERROR]: Incorrect usage of the command! Please do /startminigame <\"randomize\" here if you want to randomize the teams, anything else if you don't>");
            return false;
        }
        if (Main.minigameList.size() < 2) {
            commandSender.sendMessage(ChatColor.RED + "not enough players in the queue! join the queue by doing /joinMinigameQueue");
            return false;
        }
        List<Player> playerList = Main.minigameList;
        if (Main.minigameList.size() > 12) {
            playerList.clear();
            for (int i = 11; i <= 0; i--) {
                playerList.add(Main.minigameList.get(i));
                Main.minigameList.remove(i);
            }
        } else {
            Main.minigameList.clear();
        }
        String shouldRandomize = strings[0].toLowerCase();
        if (shouldRandomize.equals("randomize")) {
            Collections.shuffle(playerList);
        }

        if (Main.remainingCTFworlds.empty()) {
            commandSender.sendMessage(ChatColor.RED + "The server is out of worlds! To fix this, restart the server!");
        }
        int size = playerList.size();

        List<Player> redTeam = new ArrayList<>();
        List<Player> blueTeam = new ArrayList<>();

        for (int i = 0; i < size / 2; i++)
            redTeam.add(playerList.get(i));

        for (int i = size / 2; i < size; i++)
            blueTeam.add(playerList.get(i));

        World world = Main.remainingCTFworlds.pop();
        Location loc = world.getSpawnLocation();
        for (Player player : playerList) {
            player.sendMessage(ChatColor.GOLD + "Sending to a capture the flag game in the world " + world.getName() + "!");
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(loc);
        }

        new CTFMinigame(world, playerList, redTeam, blueTeam);
        return true;
    }
}
