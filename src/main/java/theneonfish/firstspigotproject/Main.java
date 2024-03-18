package theneonfish.firstspigotproject;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_19_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import theneonfish.firstspigotproject.commands.*;
import theneonfish.firstspigotproject.enchantments.EnchantRegister;
import theneonfish.firstspigotproject.events.EventListener;
import theneonfish.firstspigotproject.items.ItemManager;
import theneonfish.firstspigotproject.runnables.RunnableThings;
import theneonfish.firstspigotproject.worlds.CustomChunkGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    public static List<Player> minigameList = new ArrayList<>();
    public static Stack<World> remainingCTFworlds = new Stack<>();

    @Override
    public void onEnable() {
        if (getServer().getWorld("CustomCoolWorld") == null) {
            WorldCreator wc = new WorldCreator("CustomCoolWorld");
            wc.generator(new CustomChunkGenerator());
            getServer().createWorld(wc);
        }
        if (Bukkit.getWorld("CTFWorld1") != null) RunnableThings.deleteWorld(getServer().getWorld("CTFWorld1").getWorldFolder());
        if (Bukkit.getWorld("CTFWorld2") != null) RunnableThings.deleteWorld(getServer().getWorld("CTFWorld2").getWorldFolder());
        if (Bukkit.getWorld("CTFWorld3") != null) RunnableThings.deleteWorld(getServer().getWorld("CTFWorld3").getWorldFolder());
        RunnableThings.copyWorld(getServer().getWorld("CustomCoolWorld"), "CTFWorld1");
        remainingCTFworlds.push(getServer().getWorld("CTFWorld1"));
        RunnableThings.copyWorld(getServer().getWorld("CustomCoolWorld"), "CTFWorld2");
        remainingCTFworlds.push(getServer().getWorld("CTFWorld2"));
        RunnableThings.copyWorld(getServer().getWorld("CustomCoolWorld"), "CTFWorld3");
        remainingCTFworlds.push(getServer().getWorld("CTFWorld3"));
        EnchantRegister.init();
        ItemManager.init();

        Objects.requireNonNull(this.getCommand("getitem")).setExecutor(new GiveCustomItemCommand());
        Objects.requireNonNull(this.getCommand("adddrilling")).setExecutor(new AddCustomEnchantCommand());
        Objects.requireNonNull(this.getCommand("startminigame")).setExecutor(new StartMinigameCommand());
        Objects.requireNonNull(this.getCommand("joinMinigameQueue")).setExecutor(new JoinMinigameCommand());
        Objects.requireNonNull(this.getCommand("leaveMinigameQueue")).setExecutor(new LeaveMinigameCommand());
        Objects.requireNonNull(this.getCommand("sendtocoolworld")).setExecutor(new SendToCoolWorldCommand());

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                RunnableThings.superSkull();
            }
        }.runTaskTimer(this, 0L, 1L);
    }
}