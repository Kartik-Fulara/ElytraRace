
/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.managers;

import com.elytrarace.ElytraRacePlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class StatsManager {

    private final ElytraRacePlugin plugin;

    public StatsManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
    }

    public void addWin(UUID uuid, double time) {
        String path = "players." + uuid.toString();

        int wins = plugin.getStatsConfig().getInt(path + ".wins", 0);
        int races = plugin.getStatsConfig().getInt(path + ".races", 0);
        double bestTime = plugin.getStatsConfig().getDouble(path + ".best-time", 0.0);
        double totalTime = plugin.getStatsConfig().getDouble(path + ".total-time", 0.0);

        plugin.getStatsConfig().set(path + ".wins", wins + 1);
        plugin.getStatsConfig().set(path + ".races", races + 1);
        plugin.getStatsConfig().set(path + ".total-time", totalTime + time);

        if (bestTime == 0.0 || time < bestTime) {
            plugin.getStatsConfig().set(path + ".best-time", time);
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        plugin.getStatsConfig().set(path + ".name", player.getName());

        saveStats();
    }

    public void addRace(UUID uuid) {
        String path = "players." + uuid.toString();

        int races = plugin.getStatsConfig().getInt(path + ".races", 0);
        plugin.getStatsConfig().set(path + ".races", races + 1);

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        plugin.getStatsConfig().set(path + ".name", player.getName());

        saveStats();
    }

    public PlayerStats getStats(UUID uuid) {
        String path = "players." + uuid.toString();

        if (!plugin.getStatsConfig().contains(path)) {
            return new PlayerStats(uuid, 0, 0, 0.0, 0.0);
        }

        int wins = plugin.getStatsConfig().getInt(path + ".wins", 0);
        int races = plugin.getStatsConfig().getInt(path + ".races", 0);
        double bestTime = plugin.getStatsConfig().getDouble(path + ".best-time", 0.0);
        double totalTime = plugin.getStatsConfig().getDouble(path + ".total-time", 0.0);

        return new PlayerStats(uuid, wins, races, bestTime, totalTime);
    }

    public List<PlayerStats> getTopPlayers(int limit) {
        List<PlayerStats> topPlayers = new ArrayList<>();

        if (plugin.getStatsConfig().getConfigurationSection("players") == null) return topPlayers;

        for (String uuidStr : plugin.getStatsConfig().getConfigurationSection("players").getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            topPlayers.add(getStats(uuid));
        }

        topPlayers.sort((a, b) -> {
            if (a.getWins() != b.getWins()) {
                return Integer.compare(b.getWins(), a.getWins());
            }
            if (a.getBestTime() == 0.0) return 1;
            if (b.getBestTime() == 0.0) return -1;
            return Double.compare(a.getBestTime(), b.getBestTime());
        });

        return topPlayers.subList(0, Math.min(limit, topPlayers.size()));
    }

    public void saveStats() {
        plugin.saveStatsConfig();
    }

    public static class PlayerStats {
        private final UUID uuid;
        private final int wins;
        private final int races;
        private final double bestTime;
        private final double totalTime;

        public PlayerStats(UUID uuid, int wins, int races, double bestTime, double totalTime) {
            this.uuid = uuid;
            this.wins = wins;
            this.races = races;
            this.bestTime = bestTime;
            this.totalTime = totalTime;
        }

        public UUID getUuid() {
            return uuid;
        }

        public int getWins() {
            return wins;
        }

        public int getRaces() {
            return races;
        }

        public double getBestTime() {
            return bestTime;
        }

        public double getTotalTime() {
            return totalTime;
        }

        public double getAverageTime() {
            if (races == 0) return 0.0;
            return totalTime / races;
        }

        public double getWinRate() {
            if (races == 0) return 0.0;
            return (double) wins / races * 100.0;
        }
    }
}
