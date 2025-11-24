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
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import java.util.*;

public class ConfigManager {

    private final ElytraRacePlugin plugin;

    public ConfigManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        setupDefaults();
    }

    private void setupDefaults() {
        plugin.getConfig().addDefault("race.min-players", 2);
        plugin.getConfig().addDefault("race.max-players", 5);
        plugin.getConfig().addDefault("race.countdown-seconds", 5);
        plugin.getConfig().addDefault("race.ready-timeout-seconds", 120);
        plugin.getConfig().addDefault("race.max-time-minutes", 30);
        
        // NEW: Feature 2 - Rocket requirement
        plugin.getConfig().addDefault("race.required-rockets", 64);
        
        // NEW: Feature 10 - Auto-finish timer
        plugin.getConfig().addDefault("race.auto-finish-time", 180);
        
        // NEW: Feature 3 - Region import settings
        plugin.getConfig().addDefault("region-import.enabled", true);
        plugin.getConfig().addDefault("region-import.prefix", "ring");
        plugin.getConfig().addDefault("region-import.auto-detect", true);
        
        // NEW: Feature 9 - Anti-cheat boundary
        plugin.getConfig().addDefault("anti-cheat.boundary-distance", 50);
        plugin.getConfig().addDefault("anti-cheat.teleport-on-exceed", true);
        plugin.getConfig().addDefault("anti-cheat.warnings-before-teleport", 3);
        
        // NEW: Feature 7 - Auto-spectator
        plugin.getConfig().addDefault("spectator.auto-enable", true);
        plugin.getConfig().addDefault("spectator.return-to-lobby", true);
        plugin.getConfig().addDefault("spectator.delay-seconds", 3);
        
        // NEW: Feature 4 - Starting platform
        plugin.getConfig().addDefault("starting-platform.enabled", true);
        plugin.getConfig().addDefault("starting-platform.material", "GLASS");
        plugin.getConfig().addDefault("starting-platform.size", 3);
        plugin.getConfig().addDefault("starting-platform.height-offset", -1);
        
        // NEW: Feature 8 - Ring preview
        plugin.getConfig().addDefault("ring-preview.enabled", true);
        plugin.getConfig().addDefault("ring-preview.particle", "VILLAGER_HAPPY");
        plugin.getConfig().addDefault("ring-preview.particle-count", 20);

        plugin.getConfig().addDefault("messages.prefix", "&6[ElytraRace] &f");
        plugin.getConfig().addDefault("messages.race-started", "&aThe race has started! Fly through all the rings!");
        plugin.getConfig().addDefault("messages.race-finished", "&aYou finished in &e{time}&a seconds!");
        plugin.getConfig().addDefault("messages.ring-passed", "&aRing &e{ring}&a passed! (&e{current}&a/&e{total}&a)");
        plugin.getConfig().addDefault("messages.ready-up", "&aYou are ready! Waiting for other players...");
        plugin.getConfig().addDefault("messages.player-ready", "&e{player}&a is ready! (&e{ready}&a/&e{total}&a)");
        plugin.getConfig().addDefault("messages.countdown", "&eRace starting in &c{seconds}&e seconds!");
        plugin.getConfig().addDefault("messages.not-enough-players", "&cNot enough players to start! Need at least {min} players.");
        plugin.getConfig().addDefault("messages.already-ready", "&cYou are already ready!");
        plugin.getConfig().addDefault("messages.lobby-full", "&cThe lobby is full!");
        plugin.getConfig().addDefault("messages.no-permission", "&cYou don't have permission to use this command!");
        
        // NEW: Additional messages
        plugin.getConfig().addDefault("messages.insufficient-rockets", "&cYou need {required} rockets to ready up! You have {current}.");
        plugin.getConfig().addDefault("messages.inventory-not-empty", "&cYour inventory must be empty (except armor) to race!");
        plugin.getConfig().addDefault("messages.no-elytra", "&cYou must have an elytra equipped to race!");
        plugin.getConfig().addDefault("messages.boundary-warning", "&c⚠ WARNING: You're going off-course! ({warnings}/3)");
        plugin.getConfig().addDefault("messages.teleported-to-checkpoint", "&cYou went too far off-course! Teleported to last checkpoint.");
        plugin.getConfig().addDefault("messages.auto-finish", "&eTime's up! Race automatically finished.");
        plugin.getConfig().addDefault("messages.force-joined", "&aYou have been force-joined to the race by an admin!");
        plugin.getConfig().addDefault("messages.test-mode-enabled", "&e⚠ Test mode enabled - stats will not be saved.");
        plugin.getConfig().addDefault("messages.dependencies-missing", "&c⚠ WorldEdit/WorldGuard not found. Region import disabled.");

        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    public int getMinPlayers() {
        return plugin.getConfig().getInt("race.min-players", 2);
    }

    public int getMaxPlayers() {
        return plugin.getConfig().getInt("race.max-players", 5);
    }

    public int getCountdownSeconds() {
        return plugin.getConfig().getInt("race.countdown-seconds", 5);
    }

    public int getReadyTimeoutSeconds() {
        return plugin.getConfig().getInt("race.ready-timeout-seconds", 120);
    }

    public int getMaxTimeMinutes() {
        return plugin.getConfig().getInt("race.max-time-minutes", 30);
    }
    
    // NEW: Feature 2
    public int getRequiredRockets() {
        return plugin.getConfig().getInt("race.required-rockets", 64);
    }
    
    // NEW: Feature 10
    public int getAutoFinishTime() {
        return plugin.getConfig().getInt("race.auto-finish-time", 180);
    }
    
    // NEW: Feature 3
    public boolean isRegionImportEnabled() {
        return plugin.getConfig().getBoolean("region-import.enabled", true);
    }
    
    public String getRegionPrefix() {
        return plugin.getConfig().getString("region-import.prefix", "ring");
    }
    
    // NEW: Feature 9
    public int getBoundaryDistance() {
        return plugin.getConfig().getInt("anti-cheat.boundary-distance", 50);
    }
    
    public boolean isTeleportOnExceed() {
        return plugin.getConfig().getBoolean("anti-cheat.teleport-on-exceed", true);
    }
    
    public int getWarningsBeforeTeleport() {
        return plugin.getConfig().getInt("anti-cheat.warnings-before-teleport", 3);
    }
    
    // NEW: Feature 7
    public boolean isAutoSpectatorEnabled() {
        return plugin.getConfig().getBoolean("spectator.auto-enable", true);
    }
    
    public boolean isReturnToLobby() {
        return plugin.getConfig().getBoolean("spectator.return-to-lobby", true);
    }
    
    public int getSpectatorDelay() {
        return plugin.getConfig().getInt("spectator.delay-seconds", 3);
    }
    
    // NEW: Feature 4
    public boolean isStartingPlatformEnabled() {
        return plugin.getConfig().getBoolean("starting-platform.enabled", true);
    }
    
    public String getPlatformMaterial() {
        return plugin.getConfig().getString("starting-platform.material", "GLASS");
    }
    
    public int getPlatformSize() {
        return plugin.getConfig().getInt("starting-platform.size", 3);
    }
    
    public int getPlatformHeightOffset() {
        return plugin.getConfig().getInt("starting-platform.height-offset", -1);
    }
    
    // NEW: Feature 8
    public boolean isRingPreviewEnabled() {
        return plugin.getConfig().getBoolean("ring-preview.enabled", true);
    }
    
    public String getPreviewParticle() {
        return plugin.getConfig().getString("ring-preview.particle", "VILLAGER_HAPPY");
    }
    
    public int getPreviewParticleCount() {
        return plugin.getConfig().getInt("ring-preview.particle-count", 20);
    }

    public Location getLobbyLocation() {
        ConfigurationSection lobby = plugin.getConfig().getConfigurationSection("lobby");
        if (lobby == null) return null;

        String world = lobby.getString("world");
        double x = lobby.getDouble("x");
        double y = lobby.getDouble("y");
        double z = lobby.getDouble("z");
        float yaw = (float) lobby.getDouble("yaw", 0.0);
        float pitch = (float) lobby.getDouble("pitch", 0.0);

        return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    public void setLobbyLocation(Location loc) {
        plugin.getConfig().set("lobby.world", loc.getWorld().getName());
        plugin.getConfig().set("lobby.x", loc.getX());
        plugin.getConfig().set("lobby.y", loc.getY());
        plugin.getConfig().set("lobby.z", loc.getZ());
        plugin.getConfig().set("lobby.yaw", loc.getYaw());
        plugin.getConfig().set("lobby.pitch", loc.getPitch());
        plugin.saveConfig();
    }

    public Map<String, Location> getRingLocations() {
        Map<String, Location> rings = new LinkedHashMap<>();
        ConfigurationSection ringsSection = plugin.getConfig().getConfigurationSection("rings");

        if (ringsSection == null) return rings;

        for (String ringName : ringsSection.getKeys(false)) {
            ConfigurationSection ring = ringsSection.getConfigurationSection(ringName);
            if (ring == null) continue;

            String world = ring.getString("world");
            double x = ring.getDouble("x");
            double y = ring.getDouble("y");
            double z = ring.getDouble("z");

            rings.put(ringName, new Location(plugin.getServer().getWorld(world), x, y, z));
        }

        return rings;
    }

    public void addRing(String ringName, Location loc) {
        plugin.getConfig().set("rings." + ringName + ".world", loc.getWorld().getName());
        plugin.getConfig().set("rings." + ringName + ".x", loc.getX());
        plugin.getConfig().set("rings." + ringName + ".y", loc.getY());
        plugin.getConfig().set("rings." + ringName + ".z", loc.getZ());
        plugin.saveConfig();
    }

    public void removeRing(String ringName) {
        plugin.getConfig().set("rings." + ringName, null);
        plugin.saveConfig();
    }
    
    public void clearAllRings() {
        plugin.getConfig().set("rings", null);
        plugin.saveConfig();
    }

    public String getMessage(String key) {
        return plugin.getConfig().getString("messages." + key, "&cMessage not found: " + key)
                .replace("&", "§");
    }

    public String getPrefix() {
        return getMessage("prefix");
    }
}