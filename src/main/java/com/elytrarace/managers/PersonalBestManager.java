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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * NEW FEATURE 6: Personal Best Time Tracking
 * Tracks and displays personal best times for each player
 */
public class PersonalBestManager {

    private final ElytraRacePlugin plugin;
    private final Map<UUID, PersonalBest> cache = new HashMap<>();

    public PersonalBestManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        loadAllPersonalBests();
    }

    /**
     * Load all personal bests from stats config
     */
    private void loadAllPersonalBests() {
        FileConfiguration config = plugin.getStatsConfig();
        
        // FIX: Check if config is null before using it
        if (config == null) {
            plugin.getLogger().warning("Stats config is null, skipping personal best loading");
            return;
        }
        
        ConfigurationSection playersSection = config.getConfigurationSection("players");
        
        // FIX: Check if section exists before iterating
        if (playersSection == null) {
            plugin.getLogger().info("No players section found in stats.yml");
            return;
        }

        for (String uuidStr : playersSection.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidStr);
                double bestTime = config.getDouble("players." + uuidStr + ".best-time", 0.0);
                long achievedAt = config.getLong("players." + uuidStr + ".best-time-date", 0L);
                int totalRaces = config.getInt("players." + uuidStr + ".races", 0);
                
                if (bestTime > 0) {
                    cache.put(uuid, new PersonalBest(uuid, bestTime, achievedAt, totalRaces));
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in stats: " + uuidStr);
            }
        }

        plugin.getLogger().info("Loaded " + cache.size() + " personal best records");
    }

    /**
     * Check and update personal best for a player
     * 
     * @return true if this is a new personal best
     */
    public boolean checkAndUpdateBest(UUID uuid, double time) {
        PersonalBest current = cache.get(uuid);
        
        boolean isNewBest = false;
        double previousBest = 0.0;
        
        if (current == null || time < current.time) {
            previousBest = current != null ? current.time : 0.0;
            isNewBest = true;
            
            PersonalBest newBest = new PersonalBest(uuid, time, System.currentTimeMillis(), 0);
            cache.put(uuid, newBest);
            
            // Save to config
            FileConfiguration config = plugin.getStatsConfig();
            if (config != null) {
                String path = "players." + uuid.toString();
                config.set(path + ".best-time", time);
                config.set(path + ".best-time-date", newBest.achievedAt);
                
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                config.set(path + ".name", player.getName());
                
                plugin.saveStatsConfig();
            }
            
            // Announce if player is online
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player.isOnline() && player.getPlayer() != null) {
                if (previousBest > 0) {
                    double improvement = previousBest - time;
                    player.getPlayer().sendMessage(
                        plugin.getConfigManager().getPrefix() +
                        String.format("§a§l🎉 NEW PERSONAL BEST! §e%.2fs §7(improved by %.2fs)", 
                            time, improvement)
                    );
                } else {
                    player.getPlayer().sendMessage(
                        plugin.getConfigManager().getPrefix() +
                        String.format("§a§l🎉 FIRST PERSONAL BEST! §e%.2fs", time)
                    );
                }
            }
        }
        
        return isNewBest;
    }

    /**
     * Get personal best for a player
     */
    public PersonalBest getPersonalBest(UUID uuid) {
        return cache.get(uuid);
    }

    /**
     * Get formatted personal best string
     */
    public String getFormattedBest(UUID uuid) {
        PersonalBest pb = cache.get(uuid);
        if (pb == null) {
            return "§7No personal best yet";
        }
        
        return String.format("§e%.2fs §7(%s)", pb.time, formatDate(pb.achievedAt));
    }

    /**
     * Get top personal bests globally
     */
    public List<PersonalBest> getTopPersonalBests(int limit) {
        List<PersonalBest> sorted = new ArrayList<>(cache.values());
        sorted.sort(Comparator.comparingDouble(pb -> pb.time));
        
        return sorted.subList(0, Math.min(limit, sorted.size()));
    }

    /**
     * Get rank of player's personal best
     */
    public int getRank(UUID uuid) {
        PersonalBest playerBest = cache.get(uuid);
        if (playerBest == null) {
            return -1;
        }

        List<PersonalBest> sorted = new ArrayList<>(cache.values());
        sorted.sort(Comparator.comparingDouble(pb -> pb.time));
        
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).uuid.equals(uuid)) {
                return i + 1;
            }
        }
        
        return -1;
    }

    /**
     * Get improvement since last race
     */
    public String getImprovement(UUID uuid, double currentTime) {
        PersonalBest pb = cache.get(uuid);
        if (pb == null) {
            return "§aFirst completion!";
        }
        
        double diff = currentTime - pb.time;
        if (diff < 0) {
            return String.format("§a%.2fs faster! §7(New PB!)", Math.abs(diff));
        } else if (diff == 0) {
            return "§eTied personal best!";
        } else {
            return String.format("§c%.2fs slower §7(PB: %.2fs)", diff, pb.time);
        }
    }

    private String formatDate(long timestamp) {
        if (timestamp == 0) {
            return "Unknown";
        }
        
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        
        long days = diff / (1000 * 60 * 60 * 24);
        if (days == 0) {
            return "Today";
        } else if (days == 1) {
            return "Yesterday";
        } else if (days < 7) {
            return days + " days ago";
        } else if (days < 30) {
            return (days / 7) + " weeks ago";
        } else {
            return (days / 30) + " months ago";
        }
    }

    /**
     * Clear cache (for reload)
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * Reload from config
     */
    public void reload() {
        clearCache();
        loadAllPersonalBests();
    }

    /**
     * Personal Best data class
     */
    public static class PersonalBest {
        public final UUID uuid;
        public final double time;
        public final long achievedAt;
        public final int totalRaces;

        public PersonalBest(UUID uuid, double time, long achievedAt, int totalRaces) {
            this.uuid = uuid;
            this.time = time;
            this.achievedAt = achievedAt;
            this.totalRaces = totalRaces;
        }

        public String getPlayerName() {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            return player.getName() != null ? player.getName() : "Unknown";
        }
    }
}