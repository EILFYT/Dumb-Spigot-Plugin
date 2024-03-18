package theneonfish.firstspigotproject.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import theneonfish.firstspigotproject.items.ItemManager;

public class GiveCustomItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player pSender = (Player) sender;
            ItemStack stack = new ItemStack(Material.BARRIER);
            if (args.length != 1) {
                pSender.sendMessage(ChatColor.RED + "That command was the wrong length! The correct usage is \"/<command> <item-name>\". " +
                        "Here is a list of all possible items:" + ChatColor.AQUA + "\"SuperSkull\"" + ChatColor.RED + "," + ChatColor.AQUA
                        + "\"PigCannon\"" + ChatColor.RED + "," + ChatColor.AQUA + "\"IndustrialDrill\""
                        + ChatColor.RED + "," + ChatColor.AQUA + "\"LaunchBlade\""
                        + ChatColor.RED + "," + ChatColor.AQUA + "\"Longbow\""
                        + ChatColor.RED + "," + ChatColor.AQUA + "\"SniperHelmet\""
                        + ChatColor.RED + "," + ChatColor.AQUA + "\"VeinMiningDrill\"");
            } else {
                switch (args[0]) {
                    case ("SuperSkull"): {
                        stack = ItemManager.superSkull;
                        break;
                    } case ("PigCannon"): {
                        stack = ItemManager.pigCannon;
                        break;
                    } case ("IndustrialDrill"): {
                        stack = ItemManager.industrialDrill;
                        break;
                    } case ("LaunchBlade"): {
                        stack = ItemManager.launchBlade;
                        break;
                    } case ("Longbow"): {
                        stack = ItemManager.Longbow;
                        break;
                    } case ("SniperHelmet"): {
                        stack = ItemManager.sniperHelmet;
                        break;
                    } case ("VeinMiningDrill"): {
                        stack = ItemManager.veinMiningDrill;
                        break;
                    } case ("RedFlag"): {
                        stack = ItemManager.redFlag;
                        break;
                    } default: {
                        pSender.sendMessage(ChatColor.RED + "That item doesn't exist! Here is a list of all possible items:" +
                                   ChatColor.AQUA + "\"SuperSkull\"" + ChatColor.RED + "," + ChatColor.AQUA + "\"PigCannon\""
                                + ChatColor.RED + "," + ChatColor.AQUA + "\"IndustrialDrill\""
                                + ChatColor.RED + "," + ChatColor.AQUA + "\"LaunchBlade\""
                                + ChatColor.RED + "," + ChatColor.AQUA + "\"Longbow\""
                                + ChatColor.RED + "," + ChatColor.AQUA + "\"SniperHelmet\""
                                + ChatColor.RED + "," + ChatColor.AQUA + "\"VeinMiningDrill\"");
                    }
                }
                pSender.getInventory().addItem(stack);
            }
        }
        return true;
    }
}
