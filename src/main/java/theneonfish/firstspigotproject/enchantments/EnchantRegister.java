package theneonfish.firstspigotproject.enchantments;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

public class EnchantRegister {

    public static final DrillEnchantment BREAK_MORE_BLOCKS = new DrillEnchantment();
    public static final LongbowEnchantment SHOOT_MORE_ARROWS = new LongbowEnchantment();
    public static final SniperEnchantment HOMING_ARROWS = new SniperEnchantment();
    public static final VeinMinerEnchantment MINE_ORE_VEINS = new VeinMinerEnchantment();

    public static void init() {
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
            field.setAccessible(false);
            Enchantment.registerEnchantment(BREAK_MORE_BLOCKS);
            Enchantment.registerEnchantment(SHOOT_MORE_ARROWS);
            Enchantment.registerEnchantment(HOMING_ARROWS);
            Enchantment.registerEnchantment(MINE_ORE_VEINS);
            field.setAccessible(true);
            field.set(null, false);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
