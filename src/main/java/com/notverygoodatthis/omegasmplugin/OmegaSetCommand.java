package com.notverygoodatthis.omegasmplugin;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OmegaSetCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player affectedPlayer = Bukkit.getPlayerExact(args[0]);
        try {
            int newLifeInput = Integer.parseInt(args[1]);
            affectedPlayer.setStatistic(Statistic.DEATHS, OmegaSMPlugin.MAX_LIVES - newLifeInput);
            OmegaSMPlugin.updateTablistForPlayer(affectedPlayer);
            sender.sendMessage("Successfully set the lives of " + affectedPlayer.getName() + " to " + newLifeInput);
            if(affectedPlayer.getStatistic(Statistic.DEATHS) > OmegaSMPlugin.MAX_LIVES - 1) {
                Bukkit.getBanList(BanList.Type.NAME).pardon(affectedPlayer.getName());
            }
            return true;
        } catch(NumberFormatException e) {
            sender.sendMessage("Please enter a valid number in the second input field");
        } catch(NullPointerException e) {
            sender.sendMessage("Specify the player name and the new life amount");
        } catch(IllegalArgumentException e) {
            sender.sendMessage("Enter a valid number in the second argument");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> tabCompletions = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            tabCompletions.add(p.getName());
        }
        return tabCompletions;
    }
}
