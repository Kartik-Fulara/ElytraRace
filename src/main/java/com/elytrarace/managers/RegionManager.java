
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
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class RegionManager {

    private final ElytraRacePlugin plugin;

    public enum RegionType {
        LOBBY,
        START,
        FINISH
    }

    public RegionManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Check if player is inside a cuboid region.
     */
    public boolean isInsideRegion(Player player, RegionType type) {
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("regions." + type.name().toLowerCase());
        if (sec == null) return false;

        String worldName = sec.getString("world");
        if (worldName == null) return false;

        World world = plugin.getServer().getWorld(worldName);
        if (world == null) return false;

        Location loc = player.getLocation();
        if (!loc.getWorld().equals(world)) return false;

        double minX = sec.getDouble("min.x");
        double minY = sec.getDouble("min.y");
        double minZ = sec.getDouble("min.z");
        double maxX = sec.getDouble("max.x");
        double maxY = sec.getDouble("max.y");
        double maxZ = sec.getDouble("max.z");

        return (loc.getX() >= minX && loc.getX() <= maxX &&
                loc.getY() >= minY && loc.getY() <= maxY &&
                loc.getZ() >= minZ && loc.getZ() <= maxZ);
    }

    /**
     * Get the center location of a region (for /er join)
     */
    public Location getRegionCenter(RegionType type) {
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("regions." + type.name().toLowerCase());
        if (sec == null) return null;

        String worldName = sec.getString("world");
        if (worldName == null) return null;

        World world = plugin.getServer().getWorld(worldName);
        if (world == null) return null;

        double minX = sec.getDouble("min.x");
        double minY = sec.getDouble("min.y");
        double minZ = sec.getDouble("min.z");
        double maxX = sec.getDouble("max.x");
        double maxY = sec.getDouble("max.y");
        double maxZ = sec.getDouble("max.z");

        double centerX = (minX + maxX) / 2.0;
        double centerY = (minY + maxY) / 2.0;
        double centerZ = (minZ + maxZ) / 2.0;

        return new Location(world, centerX, centerY, centerZ);
    }

    /**
     * Saves region using two corner locations (min, max).
     */
    public void saveRegionFromSelection(Location min, Location max, RegionType type) {
        plugin.getConfig().set("regions." + type.name().toLowerCase() + ".world", min.getWorld().getName());

        plugin.getConfig().set("regions." + type.name().toLowerCase() + ".min.x", Math.min(min.getX(), max.getX()));
        plugin.getConfig().set("regions." + type.name().toLowerCase() + ".min.y", Math.min(min.getY(), max.getY()));
        plugin.getConfig().set("regions." + type.name().toLowerCase() + ".min.z", Math.min(min.getZ(), max.getZ()));

        plugin.getConfig().set("regions." + type.name().toLowerCase() + ".max.x", Math.max(min.getX(), max.getX()));
        plugin.getConfig().set("regions." + type.name().toLowerCase() + ".max.y", Math.max(min.getY(), max.getY()));
        plugin.getConfig().set("regions." + type.name().toLowerCase() + ".max.z", Math.max(min.getZ(), max.getZ()));

        plugin.saveConfig();
    }

    /**
     * Checks if region exists in config.
     */
    public boolean regionExists(RegionType type) {
        return plugin.getConfig().contains("regions." + type.name().toLowerCase());
    }
}
