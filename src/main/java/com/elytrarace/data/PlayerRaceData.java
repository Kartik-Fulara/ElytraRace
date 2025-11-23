
/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.data;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Stores all race-related data for a single player.
 * NOW INCLUDES: Rocket tracking, disqualification tracking, ring order validation
 */
public class PlayerRaceData {

    private final UUID playerUuid;
    private final Set<String> ringsPassed;

    private long startTime;
    private long finishTime;

    private boolean finished = false;
    private boolean disqualified = false;
    private String disqualificationReason = null;

    // Rocket tracking
    private int rocketsUsed = 0;
    private static final int MAX_ROCKETS = 3;

    // Ring order tracking (for backward detection)
    private String lastRingPassed = null;

    public PlayerRaceData(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.ringsPassed = new LinkedHashSet<>();
    }

    /** Reset data and start fresh */
    public void startRace() {
        ringsPassed.clear();
        finished = false;
        disqualified = false;
        disqualificationReason = null;
        rocketsUsed = 0;
        lastRingPassed = null;
        startTime = System.currentTimeMillis();
        finishTime = 0;
    }

    /** Mark a ring as passed */
    public void passRing(String ringName) {
        ringsPassed.add(ringName);
        lastRingPassed = ringName;
    }

    /** Check if a ring has been passed */
    public boolean hasPassedRing(String ringName) {
        return ringsPassed.contains(ringName);
    }

    /** Use a rocket */
    public boolean useRocket() {
        if (rocketsUsed >= MAX_ROCKETS) {
            disqualify("Exceeded maximum rockets (" + MAX_ROCKETS + ")");
            return false;
        }
        rocketsUsed++;
        return true;
    }

    /** Disqualify player */
    public void disqualify(String reason) {
        this.disqualified = true;
        this.disqualificationReason = reason;
        this.finished = true; // Mark as finished to stop tracking
        this.finishTime = System.currentTimeMillis();
    }

    /** Finish the race */
    public double finishRace() {
        if (finished) return getFinishTime();
        finished = true;
        finishTime = System.currentTimeMillis();
        return getFinishTime();
    }

    /** Total time taken */
    public double getFinishTime() {
        if (!finished) return 0.0;
        return (finishTime - startTime) / 1000.0;
    }

    /** Get current running time (if still racing) */
    public double getCurrentTime() {
        if (startTime == 0) return 0.0;
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }

    // Getters
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Set<String> getRingsPassed() {
        return new LinkedHashSet<>(ringsPassed);
    }

    public int getRingsCount() {
        return ringsPassed.size();
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isDisqualified() {
        return disqualified;
    }

    public String getDisqualificationReason() {
        return disqualificationReason;
    }

    public int getRocketsUsed() {
        return rocketsUsed;
    }

    public int getRemainingRockets() {
        return MAX_ROCKETS - rocketsUsed;
    }

    public String getLastRingPassed() {
        return lastRingPassed;
    }
}
