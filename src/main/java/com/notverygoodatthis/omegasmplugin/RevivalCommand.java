package com.notverygoodatthis.omegasmplugin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RevivalCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            ItemStack heldItem = playerSender.getInventory().getItemInMainHand();
            if(heldItem.getType() == OmegaSMPlugin.getRevivalItem(1).getType() && heldItem.getItemMeta().displayName().equals(OmegaSMPlugin.getRevivalItem(1).getItemMeta().displayName())) {
                OfflinePlayer playerToRevive = Bukkit.getPlayerExact(args[0]);
                if(playerToRevive.isBanned()) {
                    Bukkit.getBanList(BanList.Type.NAME).pardon(playerToRevive.getName());
                    playerToRevive.setStatistic(Statistic.DEATHS, 0);
                    playerSender.sendMessage(ChatColor.AQUA + "You have successfully revived " + playerToRevive.getName() +
                            ". If there was a typo or you think there was a bug, contact NotVeryGoodAtThis#8575 on Discord.");
                    playerSender.getInventory().getItemInMainHand().setAmount(heldItem.getAmount() - 1);
                } else {
                    playerSender.sendMessage("That player was never banned.");
                }
            } else {
                playerSender.sendMessage(ChatColor.AQUA + "Hold a revival item in your main hand to revive someone.");
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabCompletions = new ArrayList<>();
        for(OfflinePlayer p : Bukkit.getBannedPlayers()) {
            tabCompletions.add(p.getName());
        }
        return tabCompletions;
    }
}
