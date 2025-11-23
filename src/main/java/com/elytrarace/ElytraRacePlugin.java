/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace;

import com.elytrarace.commands.RaceCommand;
import com.elytrarace.commands.ReadyCommand;
import com.elytrarace.utils.TimerHelper;
import com.elytrarace.utils.WorldEditHelper;
import com.elytrarace.listeners.RaceListener;
import com.elytrarace.managers.ConfigManager;
import com.elytrarace.managers.RaceManager;
import com.elytrarace.managers.RegionManager;
import com.elytrarace.managers.StatsManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Main plugin class for ElytraRace.
 * Handles initialization, command registration, and manager lifecycle.
 * 
 * @author [Your Name]
 * @version 1.0.0
 */
public class ElytraRacePlugin extends JavaPlugin {

    private static ElytraRacePlugin instance;

    private RaceManager raceManager;
    private StatsManager statsManager;
    private ConfigManager configManager;
    private WorldEditHelper worldEditHelper;
    private TimerHelper timerHelper;
    private RegionManager regionManager;

    private File statsFile;
    private FileConfiguration statsConfig;

    /**
     * Gets the singleton instance of the plugin.
     * 
     * @return The plugin instance
     */
    public static ElytraRacePlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            // Load default configuration
            saveDefaultConfig();

            // Initialize managers
            configManager = new ConfigManager(this);
            statsManager = new StatsManager(this);
            raceManager = new RaceManager(this);
            regionManager = new RegionManager(this);
            timerHelper = new TimerHelper(this);
            worldEditHelper = new WorldEditHelper(this);

            // Setup stats file
            setupStatsFile();

            // Register commands
            registerCommands();

            // Register event listeners
            registerListeners();

            getLogger().info("╔════════════════════════════════════╗");
            getLogger().info("║   ElytraRace v" + getDescription().getVersion() + " enabled!        ║");
            getLogger().info("║   Ready for elytra racing!         ║");
            getLogger().info("╚════════════════════════════════════╝");

        } catch (Exception e) {
            getLogger().severe("Failed to enable ElytraRace!");
            getLogger().severe("Check the error below and ensure all dependencies are installed.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            // Shutdown managers gracefully
            if (raceManager != null) {
                raceManager.shutdown();
            }
            
            if (statsManager != null) {
                statsManager.saveStats();
            }

            getLogger().info("ElytraRace disabled successfully.");
        } catch (Exception e) {
            getLogger().severe("Error during plugin shutdown!");
            e.printStackTrace();
        }
    }

    /**
     * Registers all plugin commands.
     */
    private void registerCommands() {
        if (getCommand("er") != null) {
            getCommand("er").setExecutor(new RaceCommand(this));
        } else {
            getLogger().warning("Command 'er' not found in plugin.yml!");
        }

        if (getCommand("ready") != null) {
            getCommand("ready").setExecutor(new ReadyCommand(this));
        } else {
            getLogger().warning("Command 'ready' not found in plugin.yml!");
        }
    }

    /**
     * Registers all event listeners.
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new RaceListener(this), this);
        getLogger().info("Event listeners registered.");
    }

    /**
     * Sets up the stats configuration file.
     * Creates the file if it doesn't exist.
     */
    private void setupStatsFile() {
        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdirs()) {
                getLogger().info("Created plugin data folder.");
            } else {
                getLogger().warning("Failed to create plugin data folder!");
            }
        }

        statsFile = new File(getDataFolder(), "stats.yml");

        if (!statsFile.exists()) {
            try {
                if (statsFile.createNewFile()) {
                    getLogger().info("Created stats.yml file.");
                }
            } catch (IOException e) {
                getLogger().severe("Failed to create stats.yml!");
                e.printStackTrace();
            }
        }

        try {
            statsConfig = YamlConfiguration.loadConfiguration(statsFile);
        } catch (Exception e) {
            getLogger().severe("Failed to load stats.yml!");
            e.printStackTrace();
            statsConfig = new YamlConfiguration();
        }
    }

    /**
     * Saves the stats configuration to file.
     */
    public void saveStatsConfig() {
        try {
            if (statsConfig != null && statsFile != null) {
                statsConfig.save(statsFile);
                getLogger().fine("Stats saved to file.");
            }
        } catch (IOException e) {
            getLogger().severe("Failed to save stats.yml!");
            e.printStackTrace();
        }
    }

    // ============ Getters ============

    /**
     * Gets the RaceManager instance.
     * 
     * @return The RaceManager
     */
    public RaceManager getRaceManager() {
        return raceManager;
    }

    /**
     * Gets the StatsManager instance.
     * 
     * @return The StatsManager
     */
    public StatsManager getStatsManager() {
        return statsManager;
    }

    /**
     * Gets the ConfigManager instance.
     * 
     * @return The ConfigManager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Gets the WorldEditHelper instance.
     * 
     * @return The WorldEditHelper
     */
    public WorldEditHelper getWorldEditHelper() {
        return worldEditHelper;
    }

    /**
     * Gets the TimerHelper instance.
     * 
     * @return The TimerHelper
     */
    public TimerHelper getTimerHelper() {
        return timerHelper;
    }

    /**
     * Gets the RegionManager instance.
     * 
     * @return The RegionManager
     */
    public RegionManager getRegionManager() {
        return regionManager;
    }

    /**
     * Gets the stats FileConfiguration.
     * 
     * @return The stats configuration
     */
    public FileConfiguration getStatsConfig() {
        return statsConfig;
    }
}
