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
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

/**
 * NEW FEATURE 3: Automatic Region Import System
 * Imports WorldGuard regions as race rings automatically
 */
public class RegionImportManager {

    private final ElytraRacePlugin plugin;
    private final boolean worldGuardAvailable;

    public RegionImportManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.worldGuardAvailable = checkWorldGuard();
    }

    private boolean checkWorldGuard() {
        return plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null &&
               plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null;
    }

    public boolean isAvailable() {
        return worldGuardAvailable;
    }

    /**
     * Import all regions matching the configured prefix (e.g., "ring1", "ring2")
     * 
     * @param world World to import from
     * @return Number of rings imported
     */
    public int importRings(World world) {
        if (!worldGuardAvailable) {
            plugin.getLogger().warning("WorldGuard/WorldEdit not found. Cannot import regions.");
            return 0;
        }

        if (!plugin.getConfigManager().isRegionImportEnabled()) {
            plugin.getLogger().warning("Region import is disabled in config.");
            return 0;
        }

        try {
            RegionManager regionManager = WorldGuard.getInstance()
                    .getPlatform()
                    .getRegionContainer()
                    .get(BukkitAdapter.adapt(world));

            if (regionManager == null) {
                plugin.getLogger().warning("No region manager found for world: " + world.getName());
                return 0;
            }

            String prefix = plugin.getConfigManager().getRegionPrefix();
            Map<String, ProtectedRegion> regions = regionManager.getRegions();
            
            // Find all regions matching the pattern (ring1, ring2, etc.)
            List<RingRegion> ringRegions = new ArrayList<>();
            
            for (Map.Entry<String, ProtectedRegion> entry : regions.entrySet()) {
                String regionName = entry.getKey().toLowerCase();
                
                // Check if region name starts with prefix
                if (regionName.startsWith(prefix)) {
                    try {
                        // Extract number from region name
                        String numberPart = regionName.substring(prefix.length());
                        int ringNumber = Integer.parseInt(numberPart);
                        
                        ProtectedRegion region = entry.getValue();
                        Location center = getRegionCenter(region, world);
                        
                        ringRegions.add(new RingRegion(regionName, ringNumber, center));
                        
                    } catch (NumberFormatException e) {
                        // Skip regions that don't have valid numbers
                        plugin.getLogger().warning("Skipping region '" + regionName + "' - invalid number format");
                    }
                }
            }

            // Sort by ring number
            ringRegions.sort(Comparator.comparingInt(r -> r.number));

            // Clear existing rings and import new ones
            plugin.getConfigManager().clearAllRings();
            
            for (RingRegion ringRegion : ringRegions) {
                plugin.getConfigManager().addRing(ringRegion.name, ringRegion.location);
                plugin.getLogger().info("Imported ring: " + ringRegion.name + " at " + 
                    formatLocation(ringRegion.location));
            }

            return ringRegions.size();

        } catch (Exception e) {
            plugin.getLogger().severe("Error importing regions: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get center location of a WorldGuard region
     */
    private Location getRegionCenter(ProtectedRegion region, World world) {
        com.sk89q.worldedit.math.BlockVector3 min = region.getMinimumPoint();
        com.sk89q.worldedit.math.BlockVector3 max = region.getMaximumPoint();
        
        double centerX = (min.getX() + max.getX()) / 2.0;
        double centerY = (min.getY() + max.getY()) / 2.0;
        double centerZ = (min.getZ() + max.getZ()) / 2.0;
        
        return new Location(world, centerX, centerY, centerZ);
    }

    /**
     * List all regions matching the ring prefix
     */
    public List<String> listAvailableRings(World world) {
        if (!worldGuardAvailable) {
            return Collections.emptyList();
        }

        try {
            RegionManager regionManager = WorldGuard.getInstance()
                    .getPlatform()
                    .getRegionContainer()
                    .get(BukkitAdapter.adapt(world));

            if (regionManager == null) {
                return Collections.emptyList();
            }

            String prefix = plugin.getConfigManager().getRegionPrefix();
            List<String> ringNames = new ArrayList<>();
            
            for (String regionName : regionManager.getRegions().keySet()) {
                if (regionName.toLowerCase().startsWith(prefix)) {
                    ringNames.add(regionName);
                }
            }
            
            ringNames.sort(String::compareToIgnoreCase);
            return ringNames;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error listing regions: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Get region bounds (for validation)
     */
    public RegionBounds getRegionBounds(World world, String regionName) {
        if (!worldGuardAvailable) {
            return null;
        }

        try {
            RegionManager regionManager = WorldGuard.getInstance()
                    .getPlatform()
                    .getRegionContainer()
                    .get(BukkitAdapter.adapt(world));

            if (regionManager == null) {
                return null;
            }

            ProtectedRegion region = regionManager.getRegion(regionName);
            if (region == null) {
                return null;
            }

            com.sk89q.worldedit.math.BlockVector3 min = region.getMinimumPoint();
            com.sk89q.worldedit.math.BlockVector3 max = region.getMaximumPoint();
            
            return new RegionBounds(
                new Location(world, min.getX(), min.getY(), min.getZ()),
                new Location(world, max.getX(), max.getY(), max.getZ())
            );
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error getting region bounds: " + e.getMessage());
            return null;
        }
    }

    private String formatLocation(Location loc) {
        return String.format("(%.1f, %.1f, %.1f)", loc.getX(), loc.getY(), loc.getZ());
    }

    // Helper classes
    private static class RingRegion {
        final String name;
        final int number;
        final Location location;

        RingRegion(String name, int number, Location location) {
            this.name = name;
            this.number = number;
            this.location = location;
        }
    }

    public static class RegionBounds {
        public final Location min;
        public final Location max;

        public RegionBounds(Location min, Location max) {
            this.min = min;
            this.max = max;
        }
    }
}