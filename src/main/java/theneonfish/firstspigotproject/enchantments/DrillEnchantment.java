package theneonfish.firstspigotproject.enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import theneonfish.firstspigotproject.Main;

public class DrillEnchantment extends CustomEnchantment {
    public DrillEnchantment() {
        super(NamespacedKey.minecraft("industrial_drilling"));
    }

    @Override
    public String getName() {
        return "Industrial Drilling";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return EnchantmentTarget.TOOL.includes(itemStack) && !itemStack.getEnchantments().containsKey(EnchantRegister.MINE_ORE_VEINS);
    }
}
