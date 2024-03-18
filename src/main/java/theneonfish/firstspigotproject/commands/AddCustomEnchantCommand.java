package theneonfish.firstspigotproject.commands;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import theneonfish.firstspigotproject.enchantments.EnchantRegister;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AddCustomEnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (((Player) commandSender).getPlayer());
            if (EnchantmentTarget.TOOL.includes(player.getInventory().getItemInMainHand())) {
                player.getInventory().getItemInMainHand().addEnchantment(EnchantRegister.BREAK_MORE_BLOCKS, 1);
                ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                List<String> list = new ArrayList<>();
                list.add(ChatColor.GRAY + "Industrial Drilling I");
                meta.setLore(list);
                player.getInventory().getItemInMainHand().setItemMeta(meta);
            }
        }
        return true;
    }
}
