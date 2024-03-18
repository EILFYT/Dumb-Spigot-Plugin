package theneonfish.firstspigotproject.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class VeinMinerEnchantment extends CustomEnchantment {
    public VeinMinerEnchantment() {
        super(NamespacedKey.minecraft("veinminer"));
    }

    @Override
    public String getName() {
        return "Vein Miner";
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
        return EnchantmentTarget.TOOL.includes(itemStack) && !itemStack.getEnchantments().containsKey(EnchantRegister.BREAK_MORE_BLOCKS);
    }
}
