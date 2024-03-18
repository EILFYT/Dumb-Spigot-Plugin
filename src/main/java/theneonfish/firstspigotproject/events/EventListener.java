package theneonfish.firstspigotproject.events;

import net.minecraft.world.item.DiggerItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import theneonfish.firstspigotproject.Main;
import theneonfish.firstspigotproject.enchantments.EnchantRegister;
import theneonfish.firstspigotproject.items.ItemManager;
import theneonfish.firstspigotproject.runnables.CTFMinigame;
import theneonfish.firstspigotproject.runnables.HomingTask;
import theneonfish.firstspigotproject.runnables.RunnableThings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EventListener implements Listener {

    public static List<ArmorStand> list = new ArrayList<>();
    public static List<Arrow> curvingArrowList = new ArrayList<>();

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (event.getItem().getItemMeta().getLore() == null) return;
        if (Objects.equals(event.getItem().getItemMeta().getLore(), ItemManager.superSkull.getItemMeta().getLore())) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                player.getWorld().spawn(player.getLocation(), ArmorStand.class, stand -> {
                    stand.setHeadPose(new EulerAngle(0, 0, 0));
                    stand.setGravity(true);
                    ((CraftArmorStand) stand).getHandle().collides = false;
                    ((CraftArmorStand) stand).getHandle().noPhysics = true;
                    stand.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(Main.class), "noarmorswap"),
                            PersistentDataType.INTEGER, 1);
                    ItemStack stack = new ItemStack(ItemManager.getSkull("TheNeonFish"));
                    stand.setHelmet(stack);
                    double x;
                    x = Math.toRadians(player.getLocation().getPitch());
                    EulerAngle a = new EulerAngle(x,0,0);
                    stand.setHeadPose(a);
                    stand.setVisible(false);
                    list.add(stand);
                });
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) event.setCancelled(true);
            }
        } else if (Objects.equals(event.getItem().getItemMeta().getLore(), ItemManager.pigCannon.getItemMeta().getLore())) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                player.getWorld().spawn(player.getLocation(), Pig.class, pig -> {
                    pig.teleport(pig.getLocation().add(new Vector(0, 1, 0)));
                    pig.setVelocity(player.getLocation().getDirection().multiply(2.4));
                    pig.setFallDistance(-500);
                });
            }
        } else if (event.getItem().getEnchantments().containsKey(EnchantRegister.HOMING_ARROWS) && event.getItem().getType() == Material.PLAYER_HEAD) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getPlayer().getInventory().getHelmet() == null) {
                ItemStack skullStack = event.getItem().clone();
                event.getPlayer().getInventory().setHelmet(skullStack);
                event.getPlayer().getWorld().playSound(event.getPlayer(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getItem().setAmount(event.getItem().getAmount() - 1);
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 1L);
            }
        }
    }

    @EventHandler
    public static void industrialDrill(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();
        boolean hasEnchantment = stack.getEnchantments().containsKey(EnchantRegister.BREAK_MORE_BLOCKS);
        if (hasEnchantment) {
            Block block = event.getBlock();
            List<Block> blockList = new ArrayList<>();
            int level = stack.getEnchantments().get(EnchantRegister.BREAK_MORE_BLOCKS);
            if (!(CraftItemStack.asNMSCopy(stack).getItem() instanceof DiggerItem)) return;
                net.minecraft.world.item.Item item = CraftItemStack.asNMSCopy(stack).getItem();
            for (int x = -level; x <= level; x++) {
                for (int y = -level; y <= level; y++) {
                    for (int z = -level; z <= level; z++) {
                            Block block2 = player.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);
                            if (item.isCorrectToolForDrops(CraftBlockData.fromData(((CraftBlockData)
                                    block2.getBlockData()).getState()).getState())) blockList.add(block2);
                    }
                }
            }
            for (Block block1 : blockList) {
                block1.breakNaturally(stack);
            }
        }
    }

    @EventHandler
    public static void veinMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();
        boolean hasEnchantment = stack.getEnchantments().containsKey(EnchantRegister.MINE_ORE_VEINS);
        if (hasEnchantment) {
            Block block = event.getBlock();
            if (block.getType() == Material.REDSTONE_ORE || block.getType() == Material.DEEPSLATE_REDSTONE_ORE ||
                    block.getType() == Material.COAL_ORE || block.getType() == Material.DEEPSLATE_COAL_ORE ||
                    block.getType() == Material.COPPER_ORE || block.getType() == Material.DEEPSLATE_COPPER_ORE ||
                    block.getType() == Material.DIAMOND_ORE || block.getType() == Material.DEEPSLATE_DIAMOND_ORE ||
                    block.getType() == Material.EMERALD_ORE || block.getType() == Material.DEEPSLATE_EMERALD_ORE ||
                    block.getType() == Material.GOLD_ORE || block.getType() == Material.DEEPSLATE_GOLD_ORE ||
                    block.getType() == Material.LAPIS_ORE || block.getType() == Material.DEEPSLATE_LAPIS_ORE ||
                    block.getType() == Material.IRON_ORE || block.getType() == Material.DEEPSLATE_IRON_ORE ||
                    block.getType() == Material.NETHER_GOLD_ORE || block.getType() == Material.NETHER_QUARTZ_ORE) {
                event.setCancelled(true);
                final List<Block>[] blockList = new List[]{new ArrayList<>()};
                blockList[0].add(block);
                Material m = block.getType();
                BukkitTask veinMineTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                            List<Block> replacementList = new ArrayList<>();
                            for (Block b : blockList[0]) {
                                for (int x = -1; x <= 1; x++) {
                                    for (int y = -1; y <= 1; y++) {
                                        for (int z = -1; z <= 1; z++) {
                                            if (!(x == 0 && y == 0 && z == 0)) {
                                                Block block1 = b.getWorld().getBlockAt(b.getX() + x, b.getY() + y, b.getZ() + z);
                                                if (block1.getType() == m && !replacementList.contains(block1)) {
                                                    replacementList.add(block1);
                                                }
                                            }
                                        }
                                    }
                                }
                                b.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
                            }
                            blockList[0] = replacementList;
                            if (blockList[0].isEmpty()) {
                                this.cancel();
                            }
                    }
                }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);
            }
        }
    }

    @EventHandler
    public static void launchBlade(EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();
        Entity damageeEntity = event.getEntity();
        if (damagerEntity instanceof Player && damageeEntity instanceof Monster) {
            Player player = (Player) damagerEntity;
            Monster monster = (Monster) damageeEntity;
            Block block = monster.getLocation().add(0, -1, 0).getBlock();
            Location loc = block.getLocation();
            if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
            if (player.getInventory().getItemInMainHand().getItemMeta().getLore() == null) return;
            if (Objects.equals(player.getInventory().getItemInMainHand().getItemMeta().getLore(), ItemManager.launchBlade.getItemMeta().getLore())) {
                List<Entity> entityList = monster.getNearbyEntities(2.5, 1.5, 2.5);
                List<Monster> list = new ArrayList<>();
                List<Block> bList = RunnableThings.getBlockList(block, loc);
                list.add(monster);
                for (Entity entity : entityList) {
                    if (entity instanceof Monster) {
                        list.add((Monster) entity);
                        }
                    }
            for (Block bloc : bList) {
                if (bloc.getType() != Material.AIR && bloc.getType() != Material.CAVE_AIR && Material.VOID_AIR != bloc.getType() &&
                        bloc.getType() != Material.WATER && bloc.getType() != Material.LAVA) {
                    FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(bloc.getLocation(), bloc.getBlockData());
                    bloc.setType(Material.AIR);
                    if (bloc.getLocation().getY() + 1 == monster.getLocation().getY()) {
                        fallingBlock.setVelocity(new Vector(0, 1.1, 0));
                    } else {
                        fallingBlock.setVelocity(new Vector(0, 1, 0));
                    }
                }
            }
            for (Monster monster1 : list) {
                    monster1.setVelocity(new Vector(0, 2.1, 0));
                    monster1.setFallDistance(-100);
                    monster1.damage(10);
                }
            event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public static void longbowEvent(EntityShootBowEvent event) {
        if (event.getBow() == null) return;
        boolean hasEnchantment = event.getBow().getEnchantments().containsKey(EnchantRegister.SHOOT_MORE_ARROWS);
        if (hasEnchantment) {
            LivingEntity entity = event.getEntity();
            Arrow arrow = (Arrow) event.getProjectile();
            Location loc = new Location(entity.getWorld(), 0D, 0D, 0D);
            Location arrowLoc = arrow.getLocation();
            Vector velocity = arrow.getVelocity();
            int enchLevel = event.getBow().getEnchantments().get(EnchantRegister.SHOOT_MORE_ARROWS);
            for (int i = 1; i <= enchLevel + 1; i++) {
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        loc.add(0D, .01D, 0D);
                        Arrow arrow1 = (Arrow) entity.getWorld().spawnEntity(arrowLoc.add(loc), EntityType.ARROW);
                        arrow1.setVelocity(velocity);
                        entity.getWorld().playSound(entity, Sound.ENTITY_ARROW_SHOOT, 1, 1);
                        arrow1.setShooter(entity);
                        arrow1.setDamage(arrow.getDamage());
                        arrow1.setPickupStatus(AbstractArrow.PickupStatus.ALLOWED);

                        if (event.getEntity() instanceof Player && ((Player) event.getEntity()).getInventory().getHelmet() != null && Objects.requireNonNull(((Player) event.getEntity()).getInventory().getHelmet()).getEnchantments().containsKey(EnchantRegister.HOMING_ARROWS)) {
                            double minAngle = 6.283185307179586D;
                            Entity minEntity = null;
                            for (Entity entity : arrow1.getNearbyEntities(64.0D, 64.0D, 64.0D)) {
                                if (((entity instanceof LivingEntity)) && (!entity.isDead())) {
                                    LivingEntity living = (LivingEntity) entity;
                                    if (living.hasLineOfSight(arrow1)) {
                                        Vector toTarget = entity.getLocation().toVector().clone().subtract(arrow1.getLocation().toVector());
                                        double angle = arrow1.getVelocity().angle(toTarget);
                                        if (angle < minAngle) {
                                            minAngle = angle;
                                            minEntity = entity;
                                        }
                                    }
                                }
                            }
                            if (minEntity != null) {
                                new HomingTask(arrow1, (LivingEntity) minEntity, Main.getPlugin(Main.class));
                            }
                        }
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 2L * i);
            }
        }

    }

    @EventHandler
    public void eventArrowFired(EntityShootBowEvent e) {
        if (((e.getEntity() instanceof Player)) && ((e.getProjectile() instanceof Arrow))) {
            Player player = (Player) e.getEntity();
            if (player.getInventory().getHelmet() == null) return;
            if (player.getInventory().getHelmet().getEnchantments().containsKey(EnchantRegister.HOMING_ARROWS)) {
                double minAngle = 6.283185307179586D;
                Entity minEntity = null;
                for (Entity entity : player.getNearbyEntities(64.0D, 64.0D, 64.0D)) {
                    if ((player.hasLineOfSight(entity)) && ((entity instanceof LivingEntity)) && (!entity.isDead())) {
                        Vector toTarget = entity.getLocation().toVector().clone().subtract(player.getLocation().toVector());
                        double angle = e.getProjectile().getVelocity().angle(toTarget);
                        if (angle < minAngle) {
                            minAngle = angle;
                            minEntity = entity;
                        }
                    }
                }
                if (minEntity != null) {
                    new HomingTask((Arrow) e.getProjectile(), (LivingEntity) minEntity, Main.getPlugin(Main.class));
                }
            }
        }
    }
    @EventHandler
    public static void cancelArmorStandEvent(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            if (event.getRightClicked().getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(Main.class), "noarmorswap"), PersistentDataType.INTEGER)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void tagEvent(PlayerInteractEntityEvent event) {
        if (RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld()) != null && event.getRightClicked() instanceof Player) {
            Player tagger = event.getPlayer();
            Player tagged = (Player) event.getRightClicked();
            // middle z = -398
            //red can be tagged in water z -404
            //blue can be tagged in water z -391
            CTFMinigame minigame = RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld());
            if (minigame.inTaggingPhase) {
                if (minigame.isPlayerOnRedTeam(tagger) && minigame.isPlayerOnBlueTeam(tagged)) {
                    if (tagged.getLocation().getZ() <= -398 || tagged.isInWater() && tagged.getLocation().getZ() <= -391
                    || tagged.getInventory().getHelmet() == ItemManager.redFlag) {
                        minigame.playerCaught(tagged, tagger);
                    }
                } else if (minigame.isPlayerOnBlueTeam(tagger) && minigame.isPlayerOnRedTeam(tagged)) {
                    if (tagged.getLocation().getZ() >= -398 || tagged.isInWater() && tagged.getLocation().getZ() >= -404
                    || tagged.getInventory().getHelmet() == ItemManager.blueFlag) {
                        minigame.playerCaught(tagged, tagger);
                    }
                }
            }
        }
    }

    @EventHandler
    public static void cancelFightingEvent(EntityDamageByEntityEvent e) {
        if (RunnableThings.getCTFInstanceFromWorld(e.getEntity().getWorld()) != null) {
            CTFMinigame minigame = RunnableThings.getCTFInstanceFromWorld(e.getEntity().getWorld());
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                if (minigame.inTaggingPhase) {
                    Player tagger = (Player) e.getDamager();
                    Player tagged = (Player) e.getEntity();
                    if (minigame.isPlayerOnRedTeam(tagger) && minigame.isPlayerOnBlueTeam(tagged)) {
                        if (tagged.getLocation().getZ() <= -398 || tagged.isInWater() && tagged.getLocation().getZ() <= -391) {
                            minigame.playerCaught(tagged, tagger);
                        }
                    } else if (minigame.isPlayerOnBlueTeam(tagger) && minigame.isPlayerOnRedTeam(tagged)) {
                        if (tagged.getLocation().getZ() >= -398 || tagged.isInWater() && tagged.getLocation().getZ() >= -404) {
                            minigame.playerCaught(tagged, tagger);
                        }
                    }
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void redFlagEvents(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().getItemMeta().getDisplayName().equals(
                    ItemManager.redFlag.getItemMeta().getDisplayName()) && event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                    RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld()) != null) {
                CTFMinigame minigame = RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld());
                Block b = Objects.requireNonNull(event.getClickedBlock()).getRelative(event.getBlockFace());
                final CTFMinigame[] list = {minigame};
                if (!minigame.redFlagPlaced) {
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (b.getType() == Material.RED_BANNER || b.getType() == Material.RED_WALL_BANNER) {
                            b.setMetadata("redFlag", new FixedMetadataValue(Main.getPlugin(Main.class), true));
                            CTFMinigame minigam = list[0];
                            minigam.redTeam.forEach((player) -> player.sendMessage(ChatColor.RED + "Red Flag Placed!"));
                            minigam.redFlagPlaced = true;
                            minigam.redFlag = b;
                            minigam.redFlagType = b.getType();
                            if (minigam.blueFlagPlaced) {
                                minigam.playerList.forEach((player) -> player.sendMessage(ChatColor.GOLD + "The wall will fall in 20 seconds!"));
                                BukkitTask task1 = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        minigam.startCapturingPhase();
                                    }
                                }.runTaskLater(Main.getPlugin(Main.class), 400);
                            }
                        }
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 2L);
            }
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld()) != null) {
                CTFMinigame minigame = RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld());
                if (minigame.inTaggingPhase && minigame.isPlayerOnBlueTeam(event.getPlayer())) {
                    Block b = event.getClickedBlock();
                    if (b.getType() == Material.RED_WALL_BANNER || b.getType() == Material.RED_BANNER) {
                        if (!b.getMetadata("redFlag").isEmpty()) {
                            b.setType(Material.AIR);
                            event.getPlayer().getInventory().setHelmet(ItemManager.redFlag);
                            event.getPlayer().addPotionEffect(PotionEffectType.GLOWING.createEffect(100000, 1));
                            minigame.playerList.forEach((player) -> player.sendMessage(ChatColor.GOLD + event.getPlayer().getDisplayName() + " has stolen the red flag!"));
                        }
                    }
                }
                if (minigame.inTaggingPhase && minigame.isPlayerOnRedTeam(event.getPlayer())) {
                    Block b = event.getClickedBlock();
                    if (b.getType() == Material.RED_WALL_BANNER || b.getType() == Material.RED_BANNER) {
                        if (!b.getMetadata("redFlag").isEmpty() && event.getPlayer().hasPotionEffect(PotionEffectType.GLOWING)) {
                            minigame.endGame(event.getPlayer());
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public static void blockBreakEvent(BlockBreakEvent e) {
        if (RunnableThings.getCTFInstanceFromWorld(e.getBlock().getWorld()) != null) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public static void blueFlagEvents(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().getItemMeta().getDisplayName().equals(
                    ItemManager.blueFlag.getItemMeta().getDisplayName()) && event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                    RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld()) != null) {
                CTFMinigame minigame = RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld());
                Block b = Objects.requireNonNull(event.getClickedBlock()).getRelative(event.getBlockFace());
                final CTFMinigame[] list = {minigame};
                if (!minigame.blueFlagPlaced) {
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (b.getType() == Material.CYAN_BANNER || b.getType() == Material.CYAN_WALL_BANNER) {
                                b.setMetadata("blueFlag", new FixedMetadataValue(Main.getPlugin(Main.class), true));
                                CTFMinigame minigam = list[0];
                                minigam.blueTeam.forEach((player) -> player.sendMessage(ChatColor.BLUE + "Blue Flag Placed!"));
                                minigam.blueFlagPlaced = true;
                                minigam.blueFlag = b;
                                minigam.blueFlagType = b.getType();
                                if (minigam.redFlagPlaced) {
                                    minigam.playerList.forEach((player) -> player.sendMessage(ChatColor.GOLD + "The wall will fall in 20 seconds!"));
                                    BukkitTask task1 = new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            minigam.startCapturingPhase();
                                        }
                                    }.runTaskLater(Main.getPlugin(Main.class), 400);
                                }
                            }
                        }
                    }.runTaskLater(Main.getPlugin(Main.class), 2L);
                }
            }
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld()) != null) {
            CTFMinigame minigame = RunnableThings.getCTFInstanceFromWorld(event.getPlayer().getWorld());
            if (minigame.inTaggingPhase && minigame.isPlayerOnRedTeam(event.getPlayer())) {
            Block b = event.getClickedBlock();
            if (b.getType() == Material.CYAN_WALL_BANNER || b.getType() == Material.CYAN_BANNER) {
                if (!b.getMetadata("blueFlag").isEmpty()) {
                    b.setType(Material.AIR);
                    event.getPlayer().getInventory().setHelmet(ItemManager.blueFlag);
                    event.getPlayer().addPotionEffect(PotionEffectType.GLOWING.createEffect(100000, 1));
                    minigame.playerList.forEach((player) -> player.sendMessage(ChatColor.GOLD + event.getPlayer().getDisplayName() + " has stolen the blue flag!"));
                    }
                }
            }
            if (minigame.inTaggingPhase && minigame.isPlayerOnBlueTeam(event.getPlayer())) {
                Block b = event.getClickedBlock();
                if (b.getType() == Material.CYAN_WALL_BANNER || b.getType() == Material.CYAN_BANNER) {
                    if (!b.getMetadata("blueFlag").isEmpty() && event.getPlayer().hasPotionEffect(PotionEffectType.GLOWING)) {
                        minigame.endGame(event.getPlayer());
                    }
                }
            }
        }
    }
}