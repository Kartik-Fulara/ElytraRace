
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
import com.elytrarace.managers.RegionManager;
import com.elytrarace.managers.StatsManager;
import com.elytrarace.utils.WorldEditHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * COMPLETE /er command with all features from requirements
 */
public class RaceCommand implements CommandExecutor, TabCompleter {

    private final ElytraRacePlugin plugin;
    private final WorldEditHelper we;
    private final RegionManager regionManager;

    public RaceCommand(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.we = plugin.getWorldEditHelper();
        this.regionManager = plugin.getRegionManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "rules":
                return handleRules(sender);
            case "progress":
                return handleProgress(sender);
            case "timer":
                return handleTimer(sender);
            case "stats":
                return handleStats(sender, args);
            case "top":
                return handleTop(sender);
            case "start":
                return handleForceStart(sender);
            case "reset":
                return handleReset(sender);
            case "setup":
                return handleSetup(sender, args);
            case "listrings":
                Map<String, Location> rings = plugin.getConfigManager().getRingLocations();
                sender.sendMessage("§6§lRings:");
                if (rings.isEmpty()) sender.sendMessage("§7No rings configured.");
                else rings.keySet().forEach(k -> sender.sendMessage("§e- " + k));
                return true;
            default:
                showHelp(sender);
                return true;
        }
    }

    private boolean handleRules(CommandSender sender) {
        sender.sendMessage("§6§l━━━━━━━ RACE RULES ━━━━━━━");
        sender.sendMessage("§e1. §fFly through §aALL rings §fin order");
        sender.sendMessage("§e2. §fDo NOT skip any rings");
        sender.sendMessage("§e3. §fDo NOT go backwards through rings");
        sender.sendMessage("§e4. §fMax §c3 firework rockets §fper race");
        sender.sendMessage("§e5. §fMust complete all §e" + plugin.getConfigManager().getRingLocations().size() + " rings §fbefore finish");
        sender.sendMessage("");
        sender.sendMessage("§c§lDISQUALIFICATION:");
        sender.sendMessage("§8• §cSkipping any ring");
        sender.sendMessage("§8• §c3+ rocket violations");
        sender.sendMessage("§8• §cGoing backwards through rings");
        sender.sendMessage("§8• §cDisconnecting mid-race");
        sender.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━━━━");
        return true;
    }

    private boolean handleProgress(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can view progress!");
            return true;
        }

        Player player = (Player) sender;
        var data = plugin.getRaceManager().getRacePlayers().get(player.getUniqueId());
        
        if (data == null || !plugin.getRaceManager().isRacing()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cYou're not in an active race!");
            return true;
        }

        int total = plugin.getConfigManager().getRingLocations().size();
        int passed = data.getRingsCount();
        double time = data.getCurrentTime();

        sender.sendMessage("§6§l━━━ YOUR PROGRESS ━━━");
        sender.sendMessage("§eRings: §a" + passed + "§7/§e" + total);
        sender.sendMessage("§eTime: §a" + String.format("%.2f", time) + "s");
        sender.sendMessage("§eRockets Used: §a" + data.getRocketsUsed() + "§7/§c3");
        sender.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━");
        return true;
    }

    private boolean handleTimer(CommandSender sender) {
        if (!plugin.getRaceManager().isRacing()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cNo active race!");
            return true;
        }

        long time = plugin.getRaceManager().getGlobalRaceSeconds();
        sender.sendMessage(plugin.getConfigManager().getPrefix() + "§eRace Time: §a" + 
            plugin.getTimerHelper().formatTime(time));
        return true;
    }

    private boolean handleStats(CommandSender sender, String[] args) {
        Player target;
        
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cPlayer not found!");
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cSpecify a player: /er stats <player>");
                return true;
            }
            target = (Player) sender;
        }

        StatsManager.PlayerStats stats = plugin.getStatsManager().getStats(target.getUniqueId());
        
        sender.sendMessage("§6§l━━━ " + target.getName() + "'s Stats ━━━");
        sender.sendMessage("§eWins: §a" + stats.getWins());
        sender.sendMessage("§eTotal Races: §a" + stats.getRaces());
        sender.sendMessage("§eBest Time: §a" + (stats.getBestTime() > 0 ? String.format("%.2fs", stats.getBestTime()) : "N/A"));
        sender.sendMessage("§eAverage Time: §a" + (stats.getAverageTime() > 0 ? String.format("%.2fs", stats.getAverageTime()) : "N/A"));
        sender.sendMessage("§eWin Rate: §a" + String.format("%.1f%%", stats.getWinRate()));
        sender.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━━");
        return true;
    }

    private boolean handleTop(CommandSender sender) {
        List<StatsManager.PlayerStats> top = plugin.getStatsManager().getTopPlayers(10);
        
        if (top.isEmpty()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cNo stats recorded yet!");
            return true;
        }

        sender.sendMessage("§6§l━━━━━ TOP 10 RACERS ━━━━━");
        int pos = 1;
        for (StatsManager.PlayerStats stats : top) {
            String name = Bukkit.getOfflinePlayer(stats.getUuid()).getName();
            sender.sendMessage(String.format("§e#%d §f%s §7- §a%d wins §7(%.2fs best)", 
                pos++, name, stats.getWins(), stats.getBestTime()));
        }
        sender.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━━━━");
        return true;
    }

    private boolean handleForceStart(CommandSender sender) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (plugin.getRaceManager().isRacing()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cRace already in progress!");
            return true;
        }

        plugin.getRaceManager().forceStart();
        sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aForce started race!");
        return true;
    }

    private boolean handleReset(CommandSender sender) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        plugin.getRaceManager().endRace();
        sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aRace reset!");
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6§l━━━━━ ElytraRace Commands ━━━━━");
        sender.sendMessage("§e/er join §7- Join race (teleport to start)");
        sender.sendMessage("§e/er rules §7- View race rules");
        sender.sendMessage("§e/er progress §7- View your progress");
        sender.sendMessage("§e/er timer §7- View current race time");
        sender.sendMessage("§e/er stats [player] §7- View stats");
        sender.sendMessage("§e/er top §7- View leaderboard");
        sender.sendMessage("§e/ready §7- Toggle ready");
        
        if (sender.hasPermission("race.admin")) {
            sender.sendMessage("");
            sender.sendMessage("§c§lAdmin Commands:");
            sender.sendMessage("§e/er start §7- Force start race");
            sender.sendMessage("§e/er reset §7- Reset race");
            sender.sendMessage("§e/er setup lobby §7- Set lobby");
            sender.sendMessage("§e/er setup start §7- Set start region (WorldEdit)");
            sender.sendMessage("§e/er setup finish §7- Set finish region (WorldEdit)");
            sender.sendMessage("§e/er listrings §7- List all rings");
        }
        sender.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    private boolean handleSetup(CommandSender sender, String[] args) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use setup commands!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 2) {
            showHelp(sender);
            return true;
        }

        String sub = args[1].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "lobby":
                plugin.getConfigManager().setLobbyLocation(player.getLocation());
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aLobby set.");
                return true;
            case "start":
                if (!we.isWorldEditAvailable() || !we.hasSelection(player)) {
                    sender.sendMessage("§cYou need a WorldEdit selection first.");
                    return true;
                }
                Location min = we.getSelectionMin(player);
                Location max = we.getSelectionMax(player);
                if (min == null || max == null) {
                    sender.sendMessage("§cInvalid selection.");
                    return true;
                }
                regionManager.saveRegionFromSelection(min, max, RegionManager.RegionType.START);
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aStart region saved.");
                return true;
            case "finish":
                if (!we.isWorldEditAvailable() || !we.hasSelection(player)) {
                    sender.sendMessage("§cYou need a WorldEdit selection first.");
                    return true;
                }
                Location minF = we.getSelectionMin(player);
                Location maxF = we.getSelectionMax(player);
                if (minF == null || maxF == null) {
                    sender.sendMessage("§cInvalid selection.");
                    return true;
                }
                regionManager.saveRegionFromSelection(minF, maxF, RegionManager.RegionType.FINISH);
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aFinish region saved.");
                return true;
            default:
                showHelp(sender);
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>(Arrays.asList(
                "rules", "progress", "timer", "stats", "top", "listrings"
            ));
            if (sender.hasPermission("race.admin")) {
                completions.addAll(Arrays.asList("setup", "start", "reset"));
            }
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setup")) {
            return Arrays.asList("lobby", "start", "finish");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("stats")) {
            return null; // Returns online player names
        }
        return Collections.emptyList();
    }
}
