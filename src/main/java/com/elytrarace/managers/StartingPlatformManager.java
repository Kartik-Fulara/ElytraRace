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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * NEW FEATURE 4: Starting Platform System
 * Creates and manages starting platforms that disappear on race start
 */
public class StartingPlatformManager {

    private final ElytraRacePlugin plugin;
    private final Map<Location, Material> platformBlocks = new HashMap<>();
    private boolean platformActive = false;

    public StartingPlatformManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Create starting platform for all players in start lobby
     */
    public void createPlatform(Collection<Player> players) {
        if (!plugin.getConfigManager().isStartingPlatformEnabled()) {
            return;
        }

        clearPlatform(); // Clear any existing platform
        
        Material material = Material.getMaterial(
            plugin.getConfigManager().getPlatformMaterial()
        );
        
        if (material == null || !material.isBlock()) {
            plugin.getLogger().warning("Invalid platform material, using GLASS");
            material = Material.GLASS;
        }

        int size = plugin.getConfigManager().getPlatformSize();
        int heightOffset = plugin.getConfigManager().getPlatformHeightOffset();

        for (Player player : players) {
            Location playerLoc = player.getLocation();
            Location platformCenter = playerLoc.clone().add(0, heightOffset, 0);

            // Create platform around each player
            for (int x = -size; x <= size; x++) {
                for (int z = -size; z <= size; z++) {
                    Location blockLoc = platformCenter.clone().add(x, 0, z);
                    Block block = blockLoc.getBlock();
                    
                    // Save original block state
                    platformBlocks.put(blockLoc, block.getType());
                    
                    // Set platform block
                    block.setType(material);
                }
            }
        }

        platformActive = true;
        plugin.getLogger().info("Created starting platform for " + players.size() + " players");
    }

    /**
     * Remove the platform (called when race starts)
     */
    public void removePlatform() {
        if (!platformActive) {
            return;
        }

        // Restore original blocks
        for (Map.Entry<Location, Material> entry : platformBlocks.entrySet()) {
            Block block = entry.getKey().getBlock();
            
            // Set to AIR instead of restoring original (better for dramatic effect)
            block.setType(Material.AIR);
        }

        platformBlocks.clear();
        platformActive = false;
        
        plugin.getLogger().info("Removed starting platform");
    }

    /**
     * Remove platform with animation (gradual disappearance)
     */
    public void removePlatformAnimated(int delayTicks) {
        if (!platformActive) {
            return;
        }

        // Convert blocks to list for animation
        List<Location> blockLocations = new ArrayList<>(platformBlocks.keySet());
        Collections.shuffle(blockLocations); // Random removal order

        // Remove blocks gradually
        new BukkitRunnable() {
            int index = 0;
            final int blocksPerTick = Math.max(1, blockLocations.size() / 10);

            @Override
            public void run() {
                for (int i = 0; i < blocksPerTick && index < blockLocations.size(); i++) {
                    Location loc = blockLocations.get(index);
                    loc.getBlock().setType(Material.AIR);
                    index++;
                }

                if (index >= blockLocations.size()) {
                    platformBlocks.clear();
                    platformActive = false;
                    cancel();
                }
            }
        }.runTaskTimer(plugin, delayTicks, 1L);
    }

    /**
     * Clear platform immediately (cleanup)
     */
    public void clearPlatform() {
        if (platformActive) {
            removePlatform();
        }
    }

    public boolean isPlatformActive() {
        return platformActive;
    }
}