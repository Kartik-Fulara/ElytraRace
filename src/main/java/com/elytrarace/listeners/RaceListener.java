
/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.listeners;

import com.elytrarace.ElytraRacePlugin;
import com.elytrarace.managers.RegionManager;
import com.elytrarace.managers.RaceManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * HEAVILY OPTIMIZED RaceListener
 * 
 * KEY OPTIMIZATIONS:
 * 1. Only check movement if player actually MOVED (not just head turn)
 * 2. Cache region checks (only check every 5 ticks)
 * 3. Only check rings for racing players
 * 4. Pre-compute ring locations squared distance
 * 5. Track rocket usage
 */
public class RaceListener implements Listener {

    private final ElytraRacePlugin plugin;
    private final RaceManager raceManager;
    private final RegionManager regionManager;

    // OPTIMIZATION: Cache last location to avoid unnecessary checks
    private final Map<UUID, Location> lastCheckedLocation = new HashMap<>();
    
    // OPTIMIZATION: Cache region states to avoid repeated region checks
    private final Map<UUID, Boolean> inStartCache = new HashMap<>();
    private final Map<UUID, Long> lastRegionCheck = new HashMap<>();
    
    // OPTIMIZATION: Pre-compute squared distances for rings
    private final Map<String, RingData> ringDataCache = new HashMap<>();
    
    // Check regions only every 250ms (5 ticks) instead of every tick
    private static final long REGION_CHECK_INTERVAL = 250;
    
    // Ring detection radius
    private static final double RING_DETECTION_RADIUS = 5.0;
    private static final double RING_DETECTION_RADIUS_SQUARED = RING_DETECTION_RADIUS * RING_DETECTION_RADIUS;

    public RaceListener(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.raceManager = plugin.getRaceManager();
        this.regionManager = plugin.getRegionManager();
        cacheRingData();
    }

    /**
     * OPTIMIZATION: Pre-cache ring locations to avoid repeated config lookups
     */
    private void cacheRingData() {
        Map<String, Location> rings = plugin.getConfigManager().getRingLocations();
        for (Map.Entry<String, Location> entry : rings.entrySet()) {
            ringDataCache.put(entry.getKey(), new RingData(entry.getValue()));
        }
    }

    /**
     * Refresh ring cache (call this after adding/removing rings)
     */
    public void refreshRingCache() {
        ringDataCache.clear();
        cacheRingData();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        // OPTIMIZATION 1: Ignore head movements (only check if player actually moved blocks)
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null || (from.getBlockX() == to.getBlockX() && 
                           from.getBlockY() == to.getBlockY() && 
                           from.getBlockZ() == to.getBlockZ())) {
            return; // Player only turned their head, skip all checks
        }

        // OPTIMIZATION 2: Cache-based region checking (only check every 250ms)
        long now = System.currentTimeMillis();
        Long lastCheck = lastRegionCheck.get(uuid);
        if (lastCheck == null || (now - lastCheck) > REGION_CHECK_INTERVAL) {
            lastRegionCheck.put(uuid, now);
            checkRegions(player, uuid);
        }

        // OPTIMIZATION 3: Only check rings if player is actively racing
        if (raceManager.isRacing() && raceManager.getRacePlayers().containsKey(uuid)) {
            checkRings(player, uuid, to);
        }
    }

    /**
     * OPTIMIZED: Region checking with caching
     */
    private void checkRegions(Player player, UUID uuid) {
        boolean inStart = regionManager.isInsideRegion(player, RegionManager.RegionType.START);
        boolean inFinish = regionManager.isInsideRegion(player, RegionManager.RegionType.FINISH);
        
        Boolean wasInStart = inStartCache.get(uuid);

        // Handle start region transitions
        if (inStart && (wasInStart == null || !wasInStart)) {
            raceManager.playerEnteredStart(player);
            inStartCache.put(uuid, true);
        } else if (!inStart && wasInStart != null && wasInStart) {
            raceManager.playerLeftStart(player);
            inStartCache.put(uuid, false);
        }

        // Handle finish region
        if (inFinish && raceManager.isRacing()) {
            raceManager.tryFinish(player);
        }
    }

    /**
     * OPTIMIZED: Ring detection using cached squared distances
     */
    private void checkRings(Player player, UUID uuid, Location playerLoc) {
        var playerData = raceManager.getRacePlayers().get(uuid);
        if (playerData == null || playerData.isFinished()) {
            return; // Don't check if not racing or already finished
        }

        // OPTIMIZATION: Use squared distance to avoid expensive sqrt()
        for (Map.Entry<String, RingData> entry : ringDataCache.entrySet()) {
            RingData ringData = entry.getValue();
            
            // Quick world check
            if (!playerLoc.getWorld().equals(ringData.location.getWorld())) {
                continue;
            }

            // OPTIMIZATION: Use distanceSquared instead of distance
            if (playerLoc.distanceSquared(ringData.location) <= RING_DETECTION_RADIUS_SQUARED) {
                raceManager.passRing(player, entry.getKey());
            }
        }
    }

    /**
     * Track firework rocket usage for disqualification
     */
    @EventHandler
    public void onRocketLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        
        Player player = (Player) event.getEntity().getShooter();
        
        // Check if player is gliding (using elytra)
        if (!player.isGliding()) return;
        
        // Check if it's a firework rocket
        if (event.getEntity().getType().toString().equals("FIREWORK")) {
            var data = raceManager.getRacePlayers().get(player.getUniqueId());
            if (data != null && !data.isFinished()) {
                boolean allowed = data.useRocket();
                if (!allowed) {
                    event.setCancelled(true);
                    player.sendMessage(plugin.getConfigManager().getPrefix() + 
                        "§c§lDISQUALIFIED: §fExceeded rocket limit!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        // Clean up caches
        lastCheckedLocation.remove(uuid);
        inStartCache.remove(uuid);
        lastRegionCheck.remove(uuid);
        
        // Handle race disconnect
        if (raceManager.isPlayerInStart(player)) {
            raceManager.playerLeftStart(player);
        }
        
        // Mark as DNF if racing
        if (raceManager.isRacing()) {
            var data = raceManager.getRacePlayers().get(uuid);
            if (data != null && !data.isFinished()) {
                data.disqualify("Disconnected mid-race");
                raceManager.broadcastToAll("§c" + player.getName() + " disconnected (DNF)");
            }
        }
    }

    /**
     * Helper class to cache ring data
     */
    private static class RingData {
        final Location location;
        
        RingData(Location location) {
            this.location = location;
        }
    }
}
