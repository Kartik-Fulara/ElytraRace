
/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.utils;

import com.elytrarace.ElytraRacePlugin;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Helper class for WorldEdit integration
 */
public class WorldEditHelper {

    private final ElytraRacePlugin plugin;
    private final boolean worldEditAvailable;

    public WorldEditHelper(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.worldEditAvailable = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null;
    }

    public boolean isWorldEditAvailable() {
        return worldEditAvailable;
    }

    public Location getSelectionCenter(Player player) {
        if (!worldEditAvailable) return null;
        try {
            WorldEdit we = WorldEdit.getInstance();
            LocalSession session = we.getSessionManager().get(BukkitAdapter.adapt(player));
            Region region = session.getSelection(BukkitAdapter.adapt(player.getWorld()));
            BlockVector3 center = region.getCenter().toBlockPoint();
            return new Location(player.getWorld(), center.getX(), center.getY(), center.getZ());
        } catch (IncompleteRegionException e) {
            player.sendMessage("§cYou must make a WorldEdit selection first!");
            return null;
        } catch (Exception e) {
            plugin.getLogger().warning("WorldEdit selection error: " + e.getMessage());
            return null;
        }
    }

    public Location getSelectionMin(Player player) {
        if (!worldEditAvailable) return null;
        try {
            WorldEdit we = WorldEdit.getInstance();
            LocalSession session = we.getSessionManager().get(BukkitAdapter.adapt(player));
            Region region = session.getSelection(BukkitAdapter.adapt(player.getWorld()));
            BlockVector3 min = region.getMinimumPoint();
            return new Location(player.getWorld(), min.getX(), min.getY(), min.getZ());
        } catch (Exception e) {
            return null;
        }
    }

    public Location getSelectionMax(Player player) {
        if (!worldEditAvailable) return null;
        try {
            WorldEdit we = WorldEdit.getInstance();
            LocalSession session = we.getSessionManager().get(BukkitAdapter.adapt(player));
            Region region = session.getSelection(BukkitAdapter.adapt(player.getWorld()));
            BlockVector3 max = region.getMaximumPoint();
            return new Location(player.getWorld(), max.getX(), max.getY(), max.getZ());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasSelection(Player player) {
        if (!worldEditAvailable) return false;
        try {
            WorldEdit we = WorldEdit.getInstance();
            LocalSession session = we.getSessionManager().get(BukkitAdapter.adapt(player));
            session.getSelection(BukkitAdapter.adapt(player.getWorld()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
