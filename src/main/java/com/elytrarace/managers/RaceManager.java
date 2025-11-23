
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
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Central race manager with all features implemented
 */
public class RaceManager {

    private final ElytraRacePlugin plugin;
    private final ConfigManager cfg;
    private final RegionManager regionManager;
    private final TimerHelper timerHelper;

    private final Map<UUID, PlayerRaceData> racePlayers = new LinkedHashMap<>();
    private final Set<UUID> startLobbyPlayers = new LinkedHashSet<>();
    private final Set<UUID> readyPlayers = new LinkedHashSet<>();

    private BukkitTask countdownTask;
    private BukkitTask raceTimerTask;

    private long raceStartMillis;
    private long globalRaceSeconds;
    private boolean racing = false;

    public RaceManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getConfigManager();
        this.regionManager = plugin.getRegionManager();
        this.timerHelper = plugin.getTimerHelper();
    }

    public void playerEnteredStart(Player player) {
        startLobbyPlayers.add(player.getUniqueId());
        racePlayers.putIfAbsent(player.getUniqueId(), new PlayerRaceData(player.getUniqueId()));
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
            broadcastToStart(cfg.getPrefix() + "§cCountdown stopped – a player left/unreadied!");
        }
        broadcastToStart(cfg.getPrefix() + "§e" + player.getName() + " §cleft the start area (" + 
            startLobbyPlayers.size() + "/" + cfg.getMaxPlayers() + ")");
    }

    public boolean isPlayerInStart(Player player) {
        return startLobbyPlayers.contains(player.getUniqueId());
    }

    public void setReady(Player player) {
        UUID id = player.getUniqueId();

        if (!startLobbyPlayers.contains(id)) {
            player.sendMessage(cfg.getPrefix() + "§cYou must stand in the start area to ready up.");
            return;
        }

        if (readyPlayers.contains(id)) {
            readyPlayers.remove(id);
            player.sendMessage(cfg.getPrefix() + "§cYou are no longer ready.");
            broadcastToStart(cfg.getPrefix() + "§e" + player.getName() + " §cis NOT ready (" + 
                readyPlayers.size() + "/" + startLobbyPlayers.size() + ")");
            if (countdownTask != null) {
                countdownTask.cancel();
                countdownTask = null;
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
        final int[] step = {0};
        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (readyPlayers.size() < startLobbyPlayers.size()) {
                    cancel();
                    countdownTask = null;
                    broadcastToStart(cfg.getPrefix() + "§cCountdown cancelled – a player left/unreadied.");
                    return;
                }
                if (step[0] <= 4) {
                    timerHelper.broadcastCenterCountdownStep(step[0]);
                }
                if (step[0] == 4) {
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

                if (globalRaceSeconds >= cfg.getMaxTimeMinutes() * 60L) {
                    endRace();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
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
        readyPlayers.clear();
        startLobbyPlayers.clear();
        racePlayers.clear();
        racing = false;
    }

    public void passRing(Player player, String ringName) {
        if (!racing) return;
        PlayerRaceData data = racePlayers.get(player.getUniqueId());
        if (data == null || data.isDisqualified()) return;
        if (data.hasPassedRing(ringName)) return;
        
        data.passRing(ringName);

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
        plugin.getStatsManager().addWin(player.getUniqueId(), time);

        player.sendMessage(cfg.getPrefix() + cfg.getMessage("race-finished")
            .replace("{time}", String.format("%.2f", time)));
        broadcastToAll("§e" + player.getName() + " §afinished in §e" + 
            String.format("%.2f", time) + " §aseconds!");

        boolean allFinished = true;
        for (PlayerRaceData prd : racePlayers.values()) {
            if (!prd.isFinished()) {
                allFinished = false;
                break;
            }
        }
        if (allFinished) endRace();
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
                broadcastToAll("§e#" + pos + " §f" + p.getName() + " §7- §e" + 
                    String.format("%.2f", d.getFinishTime()) + "s");
                pos++;
            } else {
                broadcastToAll("§7DNF: §f" + p.getName());
            }
        }
        broadcastToAll("§6§l========================");

        readyPlayers.clear();
        startLobbyPlayers.clear();
        racePlayers.clear();
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
}
