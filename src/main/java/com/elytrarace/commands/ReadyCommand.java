
/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.commands;

import com.elytrarace.ElytraRacePlugin;
import com.elytrarace.managers.RaceManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /ready — toggles ready/unready while in start region
 */
public class ReadyCommand implements CommandExecutor {

    private final ElytraRacePlugin plugin;
    private final RaceManager raceManager;

    public ReadyCommand(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.raceManager = plugin.getRaceManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        raceManager.setReady(player);
        return true;
    }
}
