package com.notverygoodatthis.omegasmplugin;

import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class DepositCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            try {
                int depositAmount = Integer.parseInt(args[0]);
                int playerLives = playerSender.getStatistic(Statistic.DEATHS) - OmegaSMPlugin.MAX_LIVES;
                if(playerLives - depositAmount > 0) {
                    playerSender.getInventory().addItem(OmegaSMPlugin.getLife(depositAmount));
                } else {
                    playerSender.sendMessage("You don't have this many lives to deposit");
                }
            } catch(NumberFormatException e) {
                playerSender.sendMessage("Enter how many lives you would like to deposit in a number form (e.g. 1, 2, 3...)");
            }
        }
        return false;
    }
}
