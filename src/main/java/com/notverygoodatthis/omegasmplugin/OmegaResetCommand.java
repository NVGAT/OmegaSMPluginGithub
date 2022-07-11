package com.notverygoodatthis.omegasmplugin;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OmegaResetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player affectedPlayer = Bukkit.getPlayerExact(args[0]);
        affectedPlayer.setStatistic(Statistic.DEATHS, 0);
        return false;
    }
}
