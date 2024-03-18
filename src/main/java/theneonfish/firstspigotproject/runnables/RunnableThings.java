package theneonfish.firstspigotproject.runnables;

import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.Rotation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftArrow;
import org.bukkit.entity.*;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import theneonfish.firstspigotproject.Main;
import theneonfish.firstspigotproject.events.EventListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RunnableThings {


    public static void superSkull() {
        List<ArmorStand> removalList = new ArrayList<>();
        for (ArmorStand stand : EventListener.list) {
            stand.setVelocity(stand.getLocation().getDirection().divide(new Vector(2, 2, 2)));
            Block b = stand.getLocation().getBlock().getRelative(BlockFace.UP);
            boolean overlaps = false;
            if (!b.getType().equals(Material.AIR) && !b.getType().equals(Material.CAVE_AIR) && !b.getType().equals(Material.WATER))
                overlaps = true;
            List<Entity> list = stand.getNearbyEntities(stand.getBoundingBox().getWidthX() / 2 + .1, stand.getBoundingBox().getHeight() / 2 + .1,
                    stand.getBoundingBox().getWidthZ() / 2 + .1);
            if (list.size() > 0) {
                if (list.size() == 1 && list.get(0) instanceof Player) {

                } else {
                    overlaps = true;
                }
            }
            if (overlaps) {
                stand.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, stand.getLocation(), 1);
                stand.getWorld().playSound(stand, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                List<Entity> entityList = stand.getNearbyEntities(3, 3, 3);
                for (Entity entity : entityList) {
                    if (entity instanceof LivingEntity) {
                        LivingEntity monster = (LivingEntity) entity;
                        monster.damage(6);
                    }
                }
                removalList.add(stand);
            }
        }
        for (ArmorStand stand : removalList) {
            EventListener.list.remove(stand);
            ((CraftArmorStand) stand).getHandle().discard();
        }
    }

    public static void curveArrows(Arrow arrow) {
            List<Player> playerList = new ArrayList<>();
            List<Monster> monsterList = new ArrayList<>();
            String uuid = arrow.getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(Main.class),
                    "shooter"), PersistentDataType.STRING);
                arrow.getNearbyEntities(64, 32, 64).forEach(entity -> {
                    if (entity instanceof Player && !Objects.equals(uuid, entity.getUniqueId().toString()))
                        playerList.add((Player) entity);
                    if (entity instanceof Monster)
                        monsterList.add((Monster) entity);
                });
            LivingEntity livingEntity = null;
            if (playerList.size() > 1) {
                for (Player player : playerList) {
                    if (!player.getUniqueId().toString().equals(uuid) && player.hasLineOfSight(arrow)) {
                        if (livingEntity == null) {
                            livingEntity = player;
                        } else if (arrow.getLocation().distance(player.getLocation()) < arrow.getLocation().distance(livingEntity.getLocation())) {
                            livingEntity = player;
                        }
                    }
                }
            } else if (monsterList.size() > 0) {
                for (Monster monster : monsterList) {
                    if (monster.hasLineOfSight(arrow)) {
                        if (livingEntity == null) {
                            livingEntity = monster;
                        } else if (arrow.getLocation().distance(monster.getLocation()) < arrow.getLocation().distance(livingEntity.getLocation())) {
                            livingEntity = monster;
                        }
                    }
                }
                if (livingEntity != null) {
                    arrow.setGravity(false);
                    double x = (arrow.getLocation().getX() - livingEntity.getLocation().getX());
                    double y = (arrow.getLocation().getY() - livingEntity.getLocation().getY());
                    double z = (arrow.getLocation().getZ() - livingEntity.getLocation().getZ());

                    float yaw = (float) ((float) (Math.atan2(z, x) * 180 / Math.PI) - (Math.PI / 2));
                    float pitch = (float) ((float) (Math.atan2(y, x) * 180 / Math.PI) - (Math.PI / 2));

                    arrow.getLocation().setYaw(yaw);
                    arrow.getLocation().setPitch(pitch);
                }
            }
    }

    public static List<Block> getBlockList(Block block, Location loc) {
        List<Block> bList = new ArrayList<>();
        bList.add(block);
        bList.add(loc.add(1, 0, 0).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(1, 0, 1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(0, 0, 1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(-1, 0, 1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(-1, 0, 0).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(-1, 0, -1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(1, 0, -1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(0, 0, -1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(2, 0, 0).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(2, 0, 1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(0, 0, 2).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(-1, 0, 2).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(-2, 0, 0).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(-1, 0, -2).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(2, 0, -1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(0, 0, -2).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(1, 0, -2).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(1, 0, 2).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(-2, 0, 1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(-2, 0, -1).getBlock());
        loc = block.getLocation();
        bList.add(loc.add(0, -1, 0).getBlock());
        bList.add(loc.add(1, 0, 0).getBlock());
        loc = block.getLocation().add(0, -1, 0);
        bList.add(loc.add(1, 0, 1).getBlock());
        loc = block.getLocation().add(0, -1, 0);
        bList.add(loc.add(0, 0, 1).getBlock());
        loc = block.getLocation().add(0, -1, 0);
        bList.add(loc.add(-1, 0, 1).getBlock());
        loc = block.getLocation().add(0, -1, 0);
        bList.add(loc.add(-1, 0, 0).getBlock());
        loc = block.getLocation().add(0, -1, 0);
        bList.add(loc.add(-1, 0, -1).getBlock());
        loc = block.getLocation().add(0, -1, 0);
        bList.add(loc.add(1, 0, -1).getBlock());
        loc = block.getLocation().add(0, -1, 0);
        bList.add(loc.add(0, 0, -1).getBlock());
        loc = block.getLocation().add(0, -1, 0);
        return bList;
    }

    public static boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

    //returns the world's instance of the Capture The Flag Minigame, and null if the world isn't running a CTFMinigame
    public static CTFMinigame getCTFInstanceFromWorld(World world) {
        if (CTFMinigame.runningGameList != null) {
            if (CTFMinigame.runningGameList.containsKey(world.getName())) {
                return CTFMinigame.runningGameList.get(world.getName());
            }
        }
        return null;
    }

    public static void copyWorld(World originalWorld, String newWorldName) {
        copyFileStructure(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
        Bukkit.createWorld(new WorldCreator(newWorldName));
    }

    private static void copyFileStructure(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}