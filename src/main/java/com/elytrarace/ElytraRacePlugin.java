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
 * Main plugin class for ElytraRace v1.1.0
 * Handles initialization, command registration, and manager lifecycle.
 * 
 * @author Kartik Fulara
 * @version 1.1.0
 */
public class ElytraRacePlugin extends JavaPlugin {

    private static ElytraRacePlugin instance;

    private RaceManager raceManager;
    private StatsManager statsManager;
    private ConfigManager configManager;
    private WorldEditHelper worldEditHelper;
    private TimerHelper timerHelper;
    private RegionManager regionManager;
    private RaceListener raceListener;

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
            // ============ DEPENDENCY CHECK ============
            getLogger().info("╔═══════════════════════════════════════╗");
            getLogger().info("║   Checking Dependencies...            ║");
            getLogger().info("╚═══════════════════════════════════════╝");
            
            boolean worldEditFound = checkDependency("WorldEdit");
            boolean worldGuardFound = checkDependency("WorldGuard");
            
            if (!worldEditFound || !worldGuardFound) {
                getLogger().warning("╔═══════════════════════════════════════╗");
                getLogger().warning("║   ⚠️  MISSING DEPENDENCIES            ║");
                getLogger().warning("╠═══════════════════════════════════════╣");
                if (!worldEditFound) {
                    getLogger().warning("║   ❌ WorldEdit: NOT FOUND             ║");
                }
                if (!worldGuardFound) {
                    getLogger().warning("║   ❌ WorldGuard: NOT FOUND            ║");
                }
                getLogger().warning("╠═══════════════════════════════════════╣");
                getLogger().warning("║   Region import features DISABLED     ║");
                getLogger().warning("║   Manual ring setup still works       ║");
                getLogger().warning("║                                       ║");
                getLogger().warning("║   Download from:                      ║");
                getLogger().warning("║   https://enginehub.org/worldedit     ║");
                getLogger().warning("║   https://enginehub.org/worldguard    ║");
                getLogger().warning("╚═══════════════════════════════════════╝");
            }
            
            // ============ END DEPENDENCY CHECK ============

            // Load default configuration
            saveDefaultConfig();

            // FIX: Setup stats file BEFORE initializing managers
            setupStatsFile();

            // Initialize managers in correct order
            configManager = new ConfigManager(this);
            statsManager = new StatsManager(this);
            regionManager = new RegionManager(this);
            timerHelper = new TimerHelper(this);
            worldEditHelper = new WorldEditHelper(this);
            
            // RaceManager depends on statsConfig being ready
            raceManager = new RaceManager(this);

            // Register commands
            registerCommands();

            // Register event listeners
            registerListeners();

            getLogger().info("╔═══════════════════════════════════════╗");
            getLogger().info("║   ElytraRace v" + getDescription().getVersion() + " enabled!        ║");
            getLogger().info("║   Ready for elytra racing!            ║");
            getLogger().info("║                                       ║");
            getLogger().info("║   NEW FEATURES:                       ║");
            getLogger().info("║   • Force Join System                 ║");
            getLogger().info("║   • Region Import (WorldGuard)        ║");
            getLogger().info("║   • Starting Platform                 ║");
            getLogger().info("║   • Admin Test Mode                   ║");
            getLogger().info("║   • Personal Best Tracking            ║");
            getLogger().info("║   • Auto-Spectator Mode               ║");
            getLogger().info("║   • Ring Preview                      ║");
            getLogger().info("║   • Anti-Cheat Boundary               ║");
            getLogger().info("║   • Auto-Finish Timer                 ║");
            getLogger().info("║   • Rocket Requirements               ║");
            getLogger().info("╚═══════════════════════════════════════╝");

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

            getLogger().info("ElytraRace v" + getDescription().getVersion() + " disabled successfully.");
        } catch (Exception e) {
            getLogger().severe("Error during plugin shutdown!");
            e.printStackTrace();
        }
    }

    /**
     * Checks if a plugin dependency is loaded
     * 
     * @param pluginName Name of the plugin to check
     * @return true if plugin is loaded, false otherwise
     */
    private boolean checkDependency(String pluginName) {
        boolean found = getServer().getPluginManager().getPlugin(pluginName) != null;
        if (found) {
            getLogger().info("  ✅ " + pluginName + ": FOUND");
        } else {
            getLogger().warning("  ❌ " + pluginName + ": NOT FOUND");
        }
        return found;
    }

    /**
     * Checks if WorldEdit and WorldGuard are both available
     * 
     * @return true if both plugins are loaded
     */
    public boolean areRegionDependenciesAvailable() {
        return getServer().getPluginManager().getPlugin("WorldEdit") != null &&
               getServer().getPluginManager().getPlugin("WorldGuard") != null;
    }

    /**
     * Registers all plugin commands.
     */
    private void registerCommands() {
        if (getCommand("er") != null) {
            RaceCommand raceCommand = new RaceCommand(this);
            getCommand("er").setExecutor(raceCommand);
            getCommand("er").setTabCompleter(raceCommand);
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
        raceListener = new RaceListener(this);
        getServer().getPluginManager().registerEvents(raceListener, this);
        getLogger().info("Event listeners registered.");
    }

    /**
     * Sets up the stats configuration file.
     * Creates the file if it doesn't exist.
     * 
     * FIX: This must be called BEFORE initializing RaceManager
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
            getLogger().info("Loaded stats.yml configuration.");
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
     * Gets the RaceListener instance.
     * 
     * @return The RaceListener
     */
    public RaceListener getRaceListener() {
        return raceListener;
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