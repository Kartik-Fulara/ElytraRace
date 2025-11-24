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
import com.elytrarace.data.PlayerRaceData;
import com.elytrarace.utils.TimerHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Central race manager with ALL 10 NEW FEATURES implemented
 */
public class RaceManager {

    private final ElytraRacePlugin plugin;
    private final ConfigManager cfg;
    private final RegionManager regionManager;
    private final TimerHelper timerHelper;
    private final StartingPlatformManager platformManager;
    private final PersonalBestManager personalBestManager;

    private final Map<UUID, PlayerRaceData> racePlayers = new LinkedHashMap<>();
    private final Set<UUID> startLobbyPlayers = new LinkedHashSet<>();
    private final Set<UUID> readyPlayers = new LinkedHashSet<>();
    private final Set<UUID> finishedPlayers = new LinkedHashSet<>();
    
    // NEW: Feature 9 - Boundary tracking
    private final Map<UUID, Integer> boundaryWarnings = new HashMap<>();
    private final Map<UUID, String> lastCheckpoints = new HashMap<>();
    
    // NEW: Feature 5 - Test mode tracking
    private final Set<UUID> testModePlayers = new LinkedHashSet<>();

    private BukkitTask countdownTask;
    private BukkitTask raceTimerTask;
    private BukkitTask autoFinishTask;

    private long raceStartMillis;
    private long globalRaceSeconds;
    private boolean racing = false;

    public RaceManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getConfigManager();
        this.regionManager = plugin.getRegionManager();
        this.timerHelper = plugin.getTimerHelper();
        this.platformManager = new StartingPlatformManager(plugin);
        this.personalBestManager = new PersonalBestManager(plugin);
    }

    // NEW: Feature 1 - Force join
    public boolean forceJoinPlayer(Player player) {
        if (startLobbyPlayers.size() >= cfg.getMaxPlayers()) {
            return false;
        }
        
        // Teleport to start region
        org.bukkit.Location startCenter = regionManager.getRegionCenter(RegionManager.RegionType.START);
        if (startCenter != null) {
            player.teleport(startCenter);
            player.sendMessage(cfg.getPrefix() + cfg.getMessage("force-joined"));
            showJoinRules(player);
            return true;
        }
        
        return false;
    }

    // NEW: Feature 1 - Show rules on join
    private void showJoinRules(Player player) {
        player.sendMessage("§6§l╔═══════════════════════════════╗");
        player.sendMessage("§6§l║      RACE RULES & INFO        ║");
        player.sendMessage("§6§l╠═══════════════════════════════╣");
        player.sendMessage("§e1. §fType §a/ready §fto become ready");
        player.sendMessage("§e2. §fElytra must be equipped in chestplate");
        player.sendMessage("§e3. §fInventory must be empty (except armor)");
        player.sendMessage("§e4. §fRequired rockets: §c" + cfg.getRequiredRockets());
        player.sendMessage("§e5. §fRace time limit: §c" + cfg.getAutoFinishTime() + "s");
        player.sendMessage("§e6. §fFollow all rings in order");
        player.sendMessage("§6§l╚═══════════════════════════════╝");
    }

    public void playerEnteredStart(Player player) {
        startLobbyPlayers.add(player.getUniqueId());
        racePlayers.putIfAbsent(player.getUniqueId(), new PlayerRaceData(player.getUniqueId()));
        
        // Show rules on enter
        showJoinRules(player);
        
        broadcastToStart(cfg.getPrefix() + "§e" + player.getName() + " §aentered the start area (" + 
            startLobbyPlayers.size() + "/" + cfg.getMaxPlayers() + ")");
    }

    public void playerLeftStart(Player player) {
        UUID id = player.getUniqueId();
        startLobbyPlayers.remove(id);
        boolean wasReady = readyPlayers.remove(id);
        if (wasReady && countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
            platformManager.clearPlatform();
            broadcastToStart(cfg.getPrefix() + "§cCountdown stopped – a player left/unreadied!");
        }
        broadcastToStart(cfg.getPrefix() + "§e" + player.getName() + " §cleft the start area (" + 
            startLobbyPlayers.size() + "/" + cfg.getMaxPlayers() + ")");
    }

    public boolean isPlayerInStart(Player player) {
        return startLobbyPlayers.contains(player.getUniqueId());
    }

    // NEW: Feature 2 - Rocket and inventory validation
    public void setReady(Player player) {
        UUID id = player.getUniqueId();

        if (!startLobbyPlayers.contains(id)) {
            player.sendMessage(cfg.getPrefix() + "§cYou must stand in the start area to ready up.");
            return;
        }

        // NEW: Validate requirements before allowing ready
        if (!validateReadyRequirements(player)) {
            return; // Validation messages sent in method
        }

        if (readyPlayers.contains(id)) {
            readyPlayers.remove(id);
            player.sendMessage(cfg.getPrefix() + "§cYou are no longer ready.");
            broadcastToStart(cfg.getPrefix() + "§e" + player.getName() + " §cis NOT ready (" + 
                readyPlayers.size() + "/" + startLobbyPlayers.size() + ")");
            if (countdownTask != null) {
                countdownTask.cancel();
                countdownTask = null;
                platformManager.clearPlatform();
                broadcastToStart(cfg.getPrefix() + "§cCountdown stopped – someone unreadied.");
            }
            return;
        }

        readyPlayers.add(id);
        player.sendMessage(cfg.getPrefix() + cfg.getMessage("ready-up"));
        broadcastToStart(cfg.getPrefix() + cfg.getMessage("player-ready")
                .replace("{player}", player.getName())
                .replace("{ready}", String.valueOf(readyPlayers.size()))
                .replace("{total}", String.valueOf(startLobbyPlayers.size())));

        if (readyPlayers.size() >= cfg.getMinPlayers()
                && readyPlayers.size() == startLobbyPlayers.size()
                && startLobbyPlayers.size() <= cfg.getMaxPlayers()) {
            startCountdown();
        }
    }

    // NEW: Feature 2 - Validate ready requirements
    private boolean validateReadyRequirements(Player player) {
        // Check elytra
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null || chestplate.getType() != Material.ELYTRA) {
            player.sendMessage(cfg.getPrefix() + cfg.getMessage("no-elytra"));
            return false;
        }

        // Check inventory (must be empty except armor)
        int itemCount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                itemCount++;
            }
        }
        
        // Count rockets
        int rocketCount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.FIREWORK_ROCKET) {
                rocketCount += item.getAmount();
            }
        }

        // Check if only rockets and armor
        if (itemCount > 0) {
            if (rocketCount != itemCount) {
                player.sendMessage(cfg.getPrefix() + cfg.getMessage("inventory-not-empty"));
                return false;
            }
        }

        // Check rocket count
        int required = cfg.getRequiredRockets();
        if (rocketCount < required) {
            player.sendMessage(cfg.getPrefix() + cfg.getMessage("insufficient-rockets")
                .replace("{required}", String.valueOf(required))
                .replace("{current}", String.valueOf(rocketCount)));
            return false;
        }

        return true;
    }

    // NEW: Feature 5 - Test mode
    public void enableTestMode(Player player) {
        testModePlayers.add(player.getUniqueId());
        racePlayers.putIfAbsent(player.getUniqueId(), new PlayerRaceData(player.getUniqueId()));
        
        PlayerRaceData data = racePlayers.get(player.getUniqueId());
        data.startRace();
        
        player.sendMessage(cfg.getPrefix() + cfg.getMessage("test-mode-enabled"));
        player.sendMessage(cfg.getPrefix() + "§7Fly through the rings to test the course!");
        
        // Start timer for test mode
        if (!racing) {
            racing = true;
            raceStartMillis = System.currentTimeMillis();
        }
    }

    public boolean isInTestMode(UUID uuid) {
        return testModePlayers.contains(uuid);
    }

    public void disableTestMode(Player player) {
        testModePlayers.remove(player.getUniqueId());
        racePlayers.remove(player.getUniqueId());
        
        if (testModePlayers.isEmpty() && startLobbyPlayers.isEmpty()) {
            racing = false;
        }
        
        player.sendMessage(cfg.getPrefix() + "§aTest mode ended.");
    }

    /**
     * Force start race (admin command)
     */
    public void forceStart() {
        if (racing) return;
        if (startLobbyPlayers.isEmpty()) {
            return;
        }
        
        // Mark all lobby players as ready
        readyPlayers.clear();
        readyPlayers.addAll(startLobbyPlayers);
        
        startCountdown();
    }

    private void startCountdown() {
        if (countdownTask != null) return;
        
        // NEW: Feature 4 - Create starting platform
        List<Player> players = new ArrayList<>();
        for (UUID uuid : startLobbyPlayers) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) players.add(p);
        }
        platformManager.createPlatform(players);
        
        final int[] step = {0};
        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (readyPlayers.size() < startLobbyPlayers.size()) {
                    cancel();
                    countdownTask = null;
                    platformManager.clearPlatform();
                    broadcastToStart(cfg.getPrefix() + "§cCountdown cancelled – a player left/unreadied.");
                    return;
                }
                if (step[0] <= 4) {
                    timerHelper.broadcastCenterCountdownStep(step[0]);
                }
                if (step[0] == 4) {
                    // NEW: Feature 4 - Remove platform on GO
                    platformManager.removePlatformAnimated(5);
                    startRace();
                    cancel();
                    countdownTask = null;
                    return;
                }
                step[0]++;
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private void startRace() {
        racing = true;
        raceStartMillis = System.currentTimeMillis();
        globalRaceSeconds = 0;
        finishedPlayers.clear();
        boundaryWarnings.clear();
        lastCheckpoints.clear();

        broadcastToAll(cfg.getMessage("race-started"));

        for (UUID uuid : startLobbyPlayers) {
            PlayerRaceData data = racePlayers.get(uuid);
            if (data != null) data.startRace();
        }

        raceTimerTask = new BukkitRunnable() {
            @Override
            public void run() {
                globalRaceSeconds = (System.currentTimeMillis() - raceStartMillis) / 1000;
                timerHelper.showTimerToAll(globalRaceSeconds);

                for (UUID uuid : racePlayers.keySet()) {
                    Player p = Bukkit.getPlayer(uuid);
                    PlayerRaceData d = racePlayers.get(uuid);
                    if (p != null && d != null && !d.isFinished()) {
                        timerHelper.updatePlayerTime(p, d.getCurrentTime());
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);

        // NEW: Feature 10 - Auto-finish timer
        int autoFinishTime = cfg.getAutoFinishTime();
        autoFinishTask = new BukkitRunnable() {
            @Override
            public void run() {
                broadcastToAll(cfg.getPrefix() + cfg.getMessage("auto-finish"));
                endRace();
            }
        }.runTaskLater(plugin, autoFinishTime * 20L);
    }

    public void shutdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        if (raceTimerTask != null) {
            raceTimerTask.cancel();
            raceTimerTask = null;
        }
        if (autoFinishTask != null) {
            autoFinishTask.cancel();
            autoFinishTask = null;
        }
        platformManager.clearPlatform();
        readyPlayers.clear();
        startLobbyPlayers.clear();
        racePlayers.clear();
        testModePlayers.clear();
        finishedPlayers.clear();
        boundaryWarnings.clear();
        lastCheckpoints.clear();
        racing = false;
    }

    public void passRing(Player player, String ringName) {
        if (!racing && !isInTestMode(player.getUniqueId())) return;
        PlayerRaceData data = racePlayers.get(player.getUniqueId());
        if (data == null || data.isDisqualified()) return;
        if (data.hasPassedRing(ringName)) return;
        
        data.passRing(ringName);
        
        // NEW: Feature 9 - Update last checkpoint
        lastCheckpoints.put(player.getUniqueId(), ringName);
        boundaryWarnings.remove(player.getUniqueId()); // Reset warnings

        int totalRings = cfg.getRingLocations().size();
        int current = data.getRingsCount();

        player.sendMessage(cfg.getPrefix() + cfg.getMessage("ring-passed")
                .replace("{ring}", ringName)
                .replace("{current}", String.valueOf(current))
                .replace("{total}", String.valueOf(totalRings)));

        if (current >= totalRings) {
            player.sendMessage(cfg.getPrefix() + "§eNow enter the finish region to complete the race.");
        }
    }

    // NEW: Feature 9 - Boundary check
    public void checkPlayerBoundary(Player player) {
        if (!racing) return;
        if (isInTestMode(player.getUniqueId())) return; // No boundary in test mode
        
        PlayerRaceData data = racePlayers.get(player.getUniqueId());
        if (data == null || data.isFinished()) return;

        String lastRing = lastCheckpoints.get(player.getUniqueId());
        if (lastRing == null) return;

        org.bukkit.Location ringLoc = cfg.getRingLocations().get(lastRing);
        if (ringLoc == null) return;

        double distance = player.getLocation().distance(ringLoc);
        int maxDistance = cfg.getBoundaryDistance();

        if (distance > maxDistance) {
            int warnings = boundaryWarnings.getOrDefault(player.getUniqueId(), 0) + 1;
            boundaryWarnings.put(player.getUniqueId(), warnings);

            player.sendMessage(cfg.getPrefix() + cfg.getMessage("boundary-warning")
                .replace("{warnings}", String.valueOf(warnings)));

            if (warnings >= cfg.getWarningsBeforeTeleport() && cfg.isTeleportOnExceed()) {
                player.teleport(ringLoc);
                player.sendMessage(cfg.getPrefix() + cfg.getMessage("teleported-to-checkpoint"));
                boundaryWarnings.put(player.getUniqueId(), 0);
            }
        }
    }

    public void tryFinish(Player player) {
        PlayerRaceData data = racePlayers.get(player.getUniqueId());
        if (data == null) return;
        if (data.isFinished()) return;
        if (data.isDisqualified()) {
            player.sendMessage(cfg.getPrefix() + "§cYou are disqualified: " + data.getDisqualificationReason());
            return;
        }

        int totalRings = cfg.getRingLocations().size();
        if (data.getRingsCount() < totalRings) {
            player.sendMessage(cfg.getPrefix() + "§cYou haven't passed all rings yet!");
            return;
        }

        double time = data.finishRace();
        finishedPlayers.add(player.getUniqueId());
        
        // NEW: Feature 5 - Don't save stats in test mode
        if (!isInTestMode(player.getUniqueId())) {
            plugin.getStatsManager().addWin(player.getUniqueId(), time);
            
            // NEW: Feature 6 - Check and update personal best
            boolean isNewBest = personalBestManager.checkAndUpdateBest(player.getUniqueId(), time);
            
            player.sendMessage(cfg.getPrefix() + cfg.getMessage("race-finished")
                .replace("{time}", String.format("%.2f", time)));
            
            if (!isNewBest) {
                player.sendMessage(cfg.getPrefix() + personalBestManager.getImprovement(player.getUniqueId(), time));
            }
        } else {
            player.sendMessage(cfg.getPrefix() + "§eTest completed in §a" + 
                String.format("%.2f", time) + "§es §7(not saved)");
        }

        broadcastToAll("§e" + player.getName() + " §afinished in §e" + 
            String.format("%.2f", time) + " §aseconds!");

        // NEW: Feature 7 - Auto-spectator mode
        if (cfg.isAutoSpectatorEnabled() && !isInTestMode(player.getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    enableSpectatorMode(player);
                }
            }.runTaskLater(plugin, cfg.getSpectatorDelay() * 20L);
        }

        boolean allFinished = true;
        for (UUID uuid : startLobbyPlayers) {
            PlayerRaceData prd = racePlayers.get(uuid);
            if (prd != null && !prd.isFinished()) {
                allFinished = false;
                break;
            }
        }
        if (allFinished) endRace();
    }

    // NEW: Feature 7 - Enable spectator mode
    private void enableSpectatorMode(Player player) {
        if (!player.isOnline()) return;
        
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(cfg.getPrefix() + "§7You are now in spectator mode. Watch the other racers!");
        player.sendMessage(cfg.getPrefix() + "§7You'll return to lobby when the race ends.");
    }

    public void endRace() {
        if (!racing) return;
        racing = false;

        if (raceTimerTask != null) {
            raceTimerTask.cancel();
            raceTimerTask = null;
        }
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        if (autoFinishTask != null) {
            autoFinishTask.cancel();
            autoFinishTask = null;
        }

        List<Map.Entry<UUID, PlayerRaceData>> results = new ArrayList<>(racePlayers.entrySet());
        results.sort((a, b) -> {
            boolean fa = a.getValue().isFinished() && !a.getValue().isDisqualified();
            boolean fb = b.getValue().isFinished() && !b.getValue().isDisqualified();
            if (!fa && !fb) return 0;
            if (!fa) return 1;
            if (!fb) return -1;
            return Double.compare(a.getValue().getFinishTime(), b.getValue().getFinishTime());
        });

        broadcastToAll("§6§l===== RACE RESULTS =====");
        int pos = 1;
        for (Map.Entry<UUID, PlayerRaceData> e : results) {
            Player p = Bukkit.getPlayer(e.getKey());
            if (p == null) continue;
            PlayerRaceData d = e.getValue();
            
            if (d.isDisqualified()) {
                broadcastToAll("§c§lDQ: §f" + p.getName() + " §7- " + d.getDisqualificationReason());
            } else if (d.isFinished()) {
                String pbInfo = "";
                PersonalBestManager.PersonalBest pb = personalBestManager.getPersonalBest(e.getKey());
                if (pb != null && Math.abs(pb.time - d.getFinishTime()) < 0.01) {
                    pbInfo = " §a§l(PB!)";
                }
                broadcastToAll("§e#" + pos + " §f" + p.getName() + " §7- §e" + 
                    String.format("%.2f", d.getFinishTime()) + "s" + pbInfo);
                pos++;
            } else {
                broadcastToAll("§7DNF: §f" + p.getName());
            }
        }
        broadcastToAll("§6§l========================");

        // NEW: Feature 7 - Return spectators to lobby
        if (cfg.isReturnToLobby()) {
            org.bukkit.Location lobbyLoc = cfg.getLobbyLocation();
            if (lobbyLoc != null) {
                for (UUID uuid : finishedPlayers) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null && p.getGameMode() == GameMode.SPECTATOR) {
                        p.setGameMode(GameMode.ADVENTURE);
                        p.teleport(lobbyLoc);
                        p.sendMessage(cfg.getPrefix() + "§aReturned to lobby.");
                    }
                }
            }
        }

        readyPlayers.clear();
        startLobbyPlayers.clear();
        racePlayers.clear();
        finishedPlayers.clear();
        boundaryWarnings.clear();
        lastCheckpoints.clear();
        platformManager.clearPlatform();
    }

    private void broadcastToStart(String message) {
        for (UUID uuid : startLobbyPlayers) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) p.sendMessage(message);
        }
    }

    public void broadcastToAll(String message) {
        for (UUID uuid : racePlayers.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) p.sendMessage(message);
        }
    }

    // Getters
    public Map<UUID, PlayerRaceData> getRacePlayers() { return racePlayers; }
    public Set<UUID> getStartLobbyPlayers() { return startLobbyPlayers; }
    public Set<UUID> getReadyPlayers() { return readyPlayers; }
    public boolean isRacing() { return racing; }
    public long getGlobalRaceSeconds() { return globalRaceSeconds; }
    public PersonalBestManager getPersonalBestManager() { return personalBestManager; }
    public StartingPlatformManager getPlatformManager() { return platformManager; }
}