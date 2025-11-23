
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

    public String getMessage(String key) {
        return plugin.getConfig().getString("messages." + key, "&cMessage not found: " + key)
                .replace("&", "§");
    }

    public String getPrefix() {
        return getMessage("prefix");
    }
}
