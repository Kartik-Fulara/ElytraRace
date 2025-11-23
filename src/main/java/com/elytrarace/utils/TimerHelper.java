
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
import com.elytrarace.data.PlayerRaceData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Timer helper — shows action bar timer and center countdown titles.
 * - ActionBar updates every 1s (you selected A)
 * - Center countdown uses 0.5s steps (10 ticks)
 */
public class TimerHelper {

    private final ElytraRacePlugin plugin;
    private final boolean timerAvailable;

    public TimerHelper(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.timerAvailable = plugin.getServer().getPluginManager().getPlugin("VoiidCountdownTimer") != null;
    }

    public boolean isTimerAvailable() {
        return timerAvailable;
    }

    public void showTimer(Player player, long seconds) {
        String timeStr = formatTime(seconds);
        player.sendActionBar("§6⏱ Race Time: §e" + timeStr);
    }

    public void showTimerToAll(long seconds) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.getRaceManager().getRacePlayers().containsKey(player.getUniqueId())) {
                showTimer(player, seconds);
            }
        }
    }

    public String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public String formatTimeDetailed(double seconds) {
        long minutes = (long) (seconds / 60);
        double remaining = seconds % 60;
        return String.format("%02d:%05.2f", minutes, remaining);
    }

    public void updatePlayerTime(Player player, double currentTime) {
        PlayerRaceData data = plugin.getRaceManager().getRacePlayers().get(player.getUniqueId());
        if (data == null) return;

        int ringsTotal = plugin.getConfigManager().getRingLocations().size();
        int ringsPassed = data.getRingsCount();

        String timeStr = formatTimeDetailed(currentTime);
        String progressStr = ringsPassed + "/" + ringsTotal;

        player.sendActionBar("§6⏱ §e" + timeStr + " §7| §aRings: §e" + progressStr);
    }

    /**
     * Show the center countdown step:
     * steps: 3 -> 2 -> 1 -> READY -> GO
     * color: yellow (user requested)
     * duration per step: 0.5s (10 ticks)
     */
    public void showCenterCountdownStep(Player player, int stepIndex) {
        // stepIndex: 0 => "3", 1 => "2", 2 => "1", 3 => "READY", 4 => "GO"
        String title;
        String subtitle = "";
        switch (stepIndex) {
            case 0: title = "§e3"; subtitle = "§7Get ready..."; break;
            case 1: title = "§e2"; subtitle = "§7Get ready..."; break;
            case 2: title = "§e1"; subtitle = "§7Get ready..."; break;
            case 3: title = "§eREADY"; subtitle = "§7Almost..."; break;
            case 4: title = "§a§lGO!"; subtitle = "§7Fly!"; break;
            default: title = "§e"; break;
        }
        player.sendTitle(title, subtitle, 0, 10, 2);
    }

    public void broadcastCenterCountdownStep(int stepIndex) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (plugin.getRaceManager().getRacePlayers().containsKey(p.getUniqueId())) {
                showCenterCountdownStep(p, stepIndex);
            }
        }
    }
}
