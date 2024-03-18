package theneonfish.firstspigotproject.runnables;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import theneonfish.firstspigotproject.Main;
import theneonfish.firstspigotproject.items.ItemManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CTFMinigame {
    public static Map<String, CTFMinigame> runningGameList = new HashMap<>();
    public boolean isRunning;
    public World currentWorld;
    public List<Player> playerList;
    public List<Player> redTeam;
    public List<Player> blueTeam;
    public Block redSpawn;
    public Block blueSpawn;
    public boolean blueFlagPlaced = false, redFlagPlaced = false;
    public boolean inTaggingPhase = false;
    public List<Location> waterList = new ArrayList<>();
    public List<Location> airList = new ArrayList<>();
    public Block blueFlag;
    public Block redFlag;
    public Material redFlagType;
    public Material blueFlagType;

    public CTFMinigame(World world, List<Player> playerList, List<Player> redTeam, List<Player> blueTeam) {
        this.isRunning = true;
        this.currentWorld = world;
        this.playerList = playerList;
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
        this.redSpawn = world.getBlockAt(-349, 38, -448);
        this.blueSpawn = world.getBlockAt(-298, 38, -346);
        runningGameList.put(world.getName(), this);
        for (Player player : playerList) {
            player.sendMessage(ChatColor.GOLD + "The game is starting in 20 seconds!");
        }
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                startGame();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 400);
    }

    public void startGame() {
        boolean redFlagGiven = false;
        boolean blueFlagGiven = false;
        String pWithFlag = "";
        ItemStack redChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        redChestplate.addEnchantment(Enchantment.BINDING_CURSE, 1);
        LeatherArmorMeta lam = ((LeatherArmorMeta) redChestplate.getItemMeta());
        lam.setColor(Color.fromRGB(201, 41, 20));
        lam.setDisplayName(ChatColor.RED + "Red Team Chestplate");
        redChestplate.setItemMeta(lam);
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Team redsbTeam = board.registerNewTeam("redTeam" + currentWorld.getName());
        redsbTeam.setColor(ChatColor.RED);
        redsbTeam.setAllowFriendlyFire(false);
        redsbTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        for (Player redPlayer : redTeam) {
            if (!redFlagGiven) {
                redFlagGiven = true;
                redPlayer.getInventory().addItem(ItemManager.redFlag);
                pWithFlag = redPlayer.getDisplayName();
            }
            redPlayer.sendMessage(ChatColor.GOLD + "Flag given to " + pWithFlag + "!");
            redPlayer.teleport(redSpawn.getLocation());
            redPlayer.setBedSpawnLocation(redSpawn.getLocation(), true);
            redPlayer.getInventory().setChestplate(redChestplate);
        }
        ItemStack blueChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        blueChestplate.addEnchantment(Enchantment.BINDING_CURSE, 1);
        LeatherArmorMeta lam2 = ((LeatherArmorMeta) blueChestplate.getItemMeta());
        lam2.setColor(Color.fromRGB(20, 41, 201));
        lam.setDisplayName(ChatColor.DARK_BLUE + "Blue Team Chestplate");
        blueChestplate.setItemMeta(lam2);
        Team bluesbTeam = board.registerNewTeam("blueTeam" + currentWorld.getName());
        bluesbTeam.setColor(ChatColor.BLUE);
        bluesbTeam.setAllowFriendlyFire(false);
        bluesbTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        for (Player bluePlayer : blueTeam) {
            if (!blueFlagGiven) {
                blueFlagGiven = true;
                bluePlayer.getInventory().addItem(ItemManager.blueFlag);
                pWithFlag = bluePlayer.getDisplayName();
            }
            bluePlayer.sendMessage(ChatColor.GOLD + "Flag given to " + pWithFlag + "!");
            bluePlayer.teleport(blueSpawn.getLocation());
            bluePlayer.setBedSpawnLocation(blueSpawn.getLocation(), true);
            bluePlayer.getInventory().setChestplate(blueChestplate);
        }

        for (int x = -362; x <= -294; x++) {
            for (int y = 33; y <= 60; y++) {
                Block b = currentWorld.getBlockAt(x, y, -398);
                if (b.getType() == Material.WATER) waterList.add(b.getLocation());
                if (b.getType() == Material.AIR) airList.add(b.getLocation());
                if (b.getType() == Material.WATER || b.getType() == Material.AIR) b.setType(Material.DIRT);
            }
        }


    }
    public void startCapturingPhase() {
        for (Location loc : airList) {
            currentWorld.getBlockAt(loc).setType(Material.AIR);
        }
        for (Location loc : waterList) {
            currentWorld.getBlockAt(loc).setType(Material.WATER);
        }
        inTaggingPhase = true;
        playerList.forEach((player) -> player.sendMessage(ChatColor.GOLD + "The wall has fallen!"));
    }

    public boolean isPlayerOnRedTeam(Player p) {
        return redTeam.contains(p);
    }

    public boolean isPlayerOnBlueTeam(Player p) {
        return blueTeam.contains(p);
    }

    public void endGame(Player winningPlayer) {
        String team = "blue";
        if (isPlayerOnRedTeam(winningPlayer)) team = "red";
        String[] str = {team};
        playerList.forEach((player) -> {
            player.sendMessage(ChatColor.GOLD + "The " + str[0] + " team has won!");
            isRunning = false;
            inTaggingPhase = false;
        });
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                playerList.forEach((player -> player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation())));
                BukkitTask tasc = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.unloadWorld(currentWorld, false);
                        RunnableThings.deleteWorld(currentWorld.getWorldFolder());
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 2);
            }
        }.runTaskLater(Main.getPlugin(Main.class), 200);
    }

    public void playerCaught(Player tagged, Player tagger) {
        tagged.teleport(currentWorld.getSpawnLocation());
        tagged.sendMessage(ChatColor.GOLD + "Tagged! by " + tagger.getDisplayName());
        tagger.sendMessage(ChatColor.GOLD + "Tagged " + tagged.getDisplayName() + "!");
        if (tagged.hasPotionEffect(PotionEffectType.GLOWING)) {
            ItemStack stack = tagged.getInventory().getHelmet();
            tagged.removePotionEffect(PotionEffectType.GLOWING);
            if (isPlayerOnBlueTeam(tagged))  {
                redFlag.setType(redFlagType);
                redFlag.setMetadata("redFlag", new FixedMetadataValue(Main.getPlugin(Main.class), true));
            } else if (isPlayerOnRedTeam(tagged)) {
                blueFlag.setType(blueFlagType);
                blueFlag.setMetadata("blueFlag", new FixedMetadataValue(Main.getPlugin(Main.class), true));
            }
        }
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (isPlayerOnRedTeam(tagged)) tagged.teleport(redSpawn.getLocation());
                if (isPlayerOnBlueTeam(tagged)) tagged.teleport(blueSpawn.getLocation());
            }
        }.runTaskLater(Main.getPlugin(Main.class), 100);
    }
}
