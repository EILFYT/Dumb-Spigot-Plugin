package theneonfish.firstspigotproject.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import theneonfish.firstspigotproject.enchantments.EnchantRegister;
import theneonfish.firstspigotproject.runnables.CTFMinigame;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    public static ItemStack superSkull;
    public static ItemStack pigCannon;
    public static ItemStack industrialDrill;
    public static ItemStack launchBlade;
    public static ItemStack Longbow;
    public static ItemStack sniperHelmet;
    public static ItemStack veinMiningDrill;
    public static ItemStack redFlag;
    public static ItemStack blueFlag;

    public static void init() {
        createSuperSkull();
        createPigCannon();
        createIndustrialDrill();
        createLaunchBlade();
        createLongbow();
        createSniperHelmet();
        createVeinMiningDrill();
        createBannerOne();
        createBannerTwo();
    }

    public static void createSuperSkull() {
        ItemStack stack = new ItemStack(getSkull("TheNeonFish"));
        stack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.YELLOW + "Super Skull");
        List<String> list = new ArrayList<>();
        list.add(ChatColor.AQUA + "This skull shoots out of your hand,");
        list.add(ChatColor.AQUA + "damaging everything in its path");
        meta.setLore(list);
        stack.setItemMeta(meta);
        superSkull = stack;
    }

    public static void createPigCannon() {
        ItemStack stack = new ItemStack(Material.GOLDEN_HORSE_ARMOR);
        stack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Pig Cannon");
        List<String> list = new ArrayList<>();
        list.add(ChatColor.AQUA + "This weapon shoots out pigs,");
        list.add(ChatColor.AQUA + "that launch out and probably die of fall damage");
        meta.setLore(list);
        stack.setItemMeta(meta);
        pigCannon = stack;
    }

    public static void createIndustrialDrill() {
        ItemStack stack = new ItemStack(Material.DIAMOND_PICKAXE);
        stack.addEnchantment(EnchantRegister.BREAK_MORE_BLOCKS, 1);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.BLUE + "Industrial Drill");
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GRAY + "Industrial Drilling I");
        meta.setLore(list);
        stack.setItemMeta(meta);
        industrialDrill = stack;
    }

    public static void createLaunchBlade() {
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        stack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Launch Blade");
        List<String> list = new ArrayList<>();
        list.add(ChatColor.AQUA + "This sword launches whatever");
        list.add(ChatColor.AQUA + "you hit into the sky, including");
        list.add(ChatColor.AQUA + "the blocks they're standing on.");
        meta.setLore(list);
        stack.setItemMeta(meta);
        launchBlade = stack;
    }

    public static void createLongbow() {
        ItemStack stack = new ItemStack(Material.BOW);
        stack.addEnchantment(EnchantRegister.SHOOT_MORE_ARROWS, 3);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Longbow");
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GRAY + "Longbow III");
        meta.setLore(list);
        stack.setItemMeta(meta);
        Longbow = stack;
    }

    public static void createSniperHelmet() {
        ItemStack stack = new ItemStack(getSkullFromURL("http://textures.minecraft.net/texture/6380843d188b068132011072697521d6f9309832ab7814d3dbd29d8f5c374394"));
        stack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        stack.addUnsafeEnchantment(EnchantRegister.HOMING_ARROWS, 1);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + "Sniper Helmet");
        List<String> list = new ArrayList<>();
        list.add(ChatColor.AQUA + "This helmet gives every arrow");
        list.add(ChatColor.AQUA + "you shoot tracing to the nearest");
        list.add(ChatColor.AQUA + "player or monster.");
        meta.setLore(list);
        stack.setItemMeta(meta);
        sniperHelmet = stack;
    }

    public static void createVeinMiningDrill() {
        ItemStack stack = new ItemStack(Material.DIAMOND_PICKAXE);
        stack.addEnchantment(EnchantRegister.MINE_ORE_VEINS, 1);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_AQUA + "Vein Mining Drill");
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GRAY + "Vein Mining I");
        meta.setLore(list);
        stack.setItemMeta(meta);
        veinMiningDrill = stack;
    }

    public static void createBannerOne() {
        ItemStack stack = new ItemStack(Material.RED_BANNER);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Red Team Banner");
        List<String> str = new ArrayList<>();
        str.add("This is the banner for the red team!");
        meta.setLore(str);
        stack.setItemMeta(meta);
        redFlag = stack;
    }

    public static void createBannerTwo() {
        ItemStack stack = new ItemStack(Material.CYAN_BANNER);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.BLUE + "Blue Team Banner");
        List<String> str = new ArrayList<>();
        str.add("This is the banner for the blue team!");
        meta.setLore(str);
        stack.setItemMeta(meta);
        blueFlag = stack;
    }

    public static ItemStack getSkull(String name) {
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        assert skullMeta != null;
        skullMeta.setOwner(name);
        skullItem.setItemMeta(skullMeta);

        return skullItem;
    }

    public static ItemStack getSkullFromURL(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
