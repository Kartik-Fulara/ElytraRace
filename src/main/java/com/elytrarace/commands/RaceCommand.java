/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.commands;

import com.elytrarace.ElytraRacePlugin;
import com.elytrarace.managers.PersonalBestManager;
import com.elytrarace.managers.RegionImportManager;
import com.elytrarace.managers.RegionManager;
import com.elytrarace.managers.StatsManager;
import com.elytrarace.utils.WorldEditHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * COMPLETE /er command with ALL 10 NEW FEATURES
 */
public class RaceCommand implements CommandExecutor, TabCompleter {

    private final ElytraRacePlugin plugin;
    private final WorldEditHelper we;
    private final RegionManager regionManager;
    private final RegionImportManager regionImportManager;
    
    // NEW: Feature 8 - Ring preview tracking
    private final Set<UUID> previewEnabled = new HashSet<>();
    private BukkitRunnable previewTask;

    public RaceCommand(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.we = plugin.getWorldEditHelper();
        this.regionManager = plugin.getRegionManager();
        this.regionImportManager = new RegionImportManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "rules":
                return handleRules(sender);
            case "progress":
                return handleProgress(sender);
            case "timer":
                return handleTimer(sender);
            case "stats":
                return handleStats(sender, args);
            case "top":
                return handleTop(sender);
            case "pb":
            case "personalbest":
                return handlePersonalBest(sender, args);
            case "start":
                return handleForceStart(sender);
            case "reset":
                return handleReset(sender);
            case "setup":
                return handleSetup(sender, args);
            case "listrings":
                return handleListRings(sender);
            case "forcejoin":
                return handleForceJoin(sender, args);
            case "testmode":
                return handleTestMode(sender);
            case "import":
                return handleImport(sender, args);
            case "preview":
                return handlePreview(sender);
            case "platform":
                return handlePlatform(sender, args);
            default:
                showHelp(sender);
                return true;
        }
    }

    private boolean handleRules(CommandSender sender) {
        sender.sendMessage("§6§l╔══════ RACE RULES ══════╗");
        sender.sendMessage("§e1. §fFly through §aALL rings §fin order");
        sender.sendMessage("§e2. §fDo NOT skip any rings");
        sender.sendMessage("§e3. §fDo NOT go backwards through rings");
        sender.sendMessage("§e4. §fMax §c" + plugin.getConfigManager().getRequiredRockets() + " firework rockets §fper race");
        sender.sendMessage("§e5. §fMust complete all §e" + plugin.getConfigManager().getRingLocations().size() + " rings §fbefore finish");
        sender.sendMessage("§e6. §fTime limit: §c" + plugin.getConfigManager().getAutoFinishTime() + " seconds");
        sender.sendMessage("");
        sender.sendMessage("§c§lDISQUALIFICATION:");
        sender.sendMessage("§8• §cSkipping any ring");
        sender.sendMessage("§8• §c3+ rocket violations");
        sender.sendMessage("§8• §cGoing backwards through rings");
        sender.sendMessage("§8• §cDisconnecting mid-race");
        sender.sendMessage("§6§l╚═════════════════════════╝");
        return true;
    }

    private boolean handleProgress(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can view progress!");
            return true;
        }

        Player player = (Player) sender;
        var data = plugin.getRaceManager().getRacePlayers().get(player.getUniqueId());
        
        if (data == null || !plugin.getRaceManager().isRacing()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cYou're not in an active race!");
            return true;
        }

        int total = plugin.getConfigManager().getRingLocations().size();
        int passed = data.getRingsCount();
        double time = data.getCurrentTime();

        sender.sendMessage("§6§l╔═══ YOUR PROGRESS ═══╗");
        sender.sendMessage("§eRings: §a" + passed + "§7/§e" + total);
        sender.sendMessage("§eTime: §a" + String.format("%.2f", time) + "s");
        sender.sendMessage("§eRockets Used: §a" + data.getRocketsUsed() + "§7/§c3");
        sender.sendMessage("§6§l╚══════════════════════╝");
        return true;
    }

    private boolean handleTimer(CommandSender sender) {
        if (!plugin.getRaceManager().isRacing()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cNo active race!");
            return true;
        }

        long time = plugin.getRaceManager().getGlobalRaceSeconds();
        sender.sendMessage(plugin.getConfigManager().getPrefix() + "§eRace Time: §a" + 
            plugin.getTimerHelper().formatTime(time));
        return true;
    }

    private boolean handleStats(CommandSender sender, String[] args) {
        Player target;
        
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cPlayer not found!");
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cSpecify a player: /er stats <player>");
                return true;
            }
            target = (Player) sender;
        }

        StatsManager.PlayerStats stats = plugin.getStatsManager().getStats(target.getUniqueId());
        
        sender.sendMessage("§6§l╔═══ " + target.getName() + "'s Stats ═══╗");
        sender.sendMessage("§eWins: §a" + stats.getWins());
        sender.sendMessage("§eTotal Races: §a" + stats.getRaces());
        sender.sendMessage("§eBest Time: §a" + (stats.getBestTime() > 0 ? String.format("%.2fs", stats.getBestTime()) : "N/A"));
        sender.sendMessage("§eAverage Time: §a" + (stats.getAverageTime() > 0 ? String.format("%.2fs", stats.getAverageTime()) : "N/A"));
        sender.sendMessage("§eWin Rate: §a" + String.format("%.1f%%", stats.getWinRate()));
        sender.sendMessage("§6§l╚═══════════════════════════╝");
        return true;
    }

    // NEW: Feature 6 - Personal best command
    private boolean handlePersonalBest(CommandSender sender, String[] args) {
        Player target;
        
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cPlayer not found!");
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cSpecify a player: /er pb <player>");
                return true;
            }
            target = (Player) sender;
        }

        PersonalBestManager.PersonalBest pb = plugin.getRaceManager()
            .getPersonalBestManager()
            .getPersonalBest(target.getUniqueId());

        if (pb == null) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                "§e" + target.getName() + " §7has no personal best yet.");
            return true;
        }

        int rank = plugin.getRaceManager().getPersonalBestManager().getRank(target.getUniqueId());

        sender.sendMessage("§6§l╔═══ Personal Best ═══╗");
        sender.sendMessage("§ePlayer: §a" + target.getName());
        sender.sendMessage("§eBest Time: §a" + String.format("%.2fs", pb.time));
        sender.sendMessage("§eAchieved: §7" + formatTimestamp(pb.achievedAt));
        sender.sendMessage("§eGlobal Rank: §e#" + rank);
        sender.sendMessage("§6§l╚════════════════════════╝");
        return true;
    }

    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) return "Unknown";
        
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        long days = diff / (1000 * 60 * 60 * 24);
        
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " weeks ago";
        return (days / 30) + " months ago";
    }

    private boolean handleTop(CommandSender sender) {
        List<StatsManager.PlayerStats> top = plugin.getStatsManager().getTopPlayers(10);
        
        if (top.isEmpty()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cNo stats recorded yet!");
            return true;
        }

        sender.sendMessage("§6§l╔═══════ TOP 10 RACERS ═══════╗");
        int pos = 1;
        for (StatsManager.PlayerStats stats : top) {
            String name = Bukkit.getOfflinePlayer(stats.getUuid()).getName();
            sender.sendMessage(String.format("§e#%d §f%s §7- §a%d wins §7(%.2fs best)", 
                pos++, name, stats.getWins(), stats.getBestTime()));
        }
        sender.sendMessage("§6§l╚═════════════════════════════╝");
        return true;
    }

    // NEW: Feature 1 - Force join command
    private boolean handleForceJoin(CommandSender sender, String[] args) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /er forcejoin <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cPlayer not found!");
            return true;
        }

        if (plugin.getRaceManager().forceJoinPlayer(target)) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                "§aForce-joined §e" + target.getName() + " §ato the race!");
        } else {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                "§cCouldn't force-join player (lobby full or start region not set).");
        }

        return true;
    }

    // NEW: Feature 5 - Test mode command
    private boolean handleTestMode(CommandSender sender) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use test mode!");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (plugin.getRaceManager().isInTestMode(uuid)) {
            plugin.getRaceManager().disableTestMode(player);
        } else {
            plugin.getRaceManager().enableTestMode(player);
        }

        return true;
    }

    // NEW: Feature 3 - Import regions command
    private boolean handleImport(CommandSender sender, String[] args) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can import regions!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2 || !args[1].equalsIgnoreCase("rings")) {
            sender.sendMessage("§cUsage: /er import rings");
            return true;
        }

        if (!regionImportManager.isAvailable()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMessage("dependencies-missing"));
            sender.sendMessage("§7Please install WorldEdit and WorldGuard to use this feature.");
            return true;
        }

        sender.sendMessage(plugin.getConfigManager().getPrefix() + "§eImporting rings from WorldGuard...");

        int count = regionImportManager.importRings(player.getWorld());

        if (count > 0) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                "§a✓ Successfully imported §e" + count + " §aring(s)!");
            sender.sendMessage("§7Use /er listrings to view them.");
            
            // Refresh ring cache in listener
            plugin.getRaceListener().refreshRingCache();
        } else {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                "§cNo rings found! Make sure you have regions named '" + 
                plugin.getConfigManager().getRegionPrefix() + "1', '" +
                plugin.getConfigManager().getRegionPrefix() + "2', etc.");
        }

        return true;
    }

    // NEW: Feature 8 - Preview rings command
    private boolean handlePreview(CommandSender sender) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can preview rings!");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (previewEnabled.contains(uuid)) {
            previewEnabled.remove(uuid);
            player.sendMessage(plugin.getConfigManager().getPrefix() + "§cRing preview disabled.");
            
            if (previewEnabled.isEmpty() && previewTask != null) {
                previewTask.cancel();
                previewTask = null;
            }
        } else {
            previewEnabled.add(uuid);
            player.sendMessage(plugin.getConfigManager().getPrefix() + "§aRing preview enabled!");
            player.sendMessage("§7You will see particles around each ring.");
            
            startPreviewTask();
        }

        return true;
    }

    private void startPreviewTask() {
        if (previewTask != null) {
            return; // Already running
        }

        previewTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (previewEnabled.isEmpty()) {
                    cancel();
                    previewTask = null;
                    return;
                }

                Map<String, Location> rings = plugin.getConfigManager().getRingLocations();
                Particle particle = getParticleType();
                int count = plugin.getConfigManager().getPreviewParticleCount();

                for (Location ringLoc : rings.values()) {
                    for (UUID uuid : previewEnabled) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player != null && player.getWorld().equals(ringLoc.getWorld())) {
                            // Create circle of particles
                            for (int i = 0; i < count; i++) {
                                double angle = 2 * Math.PI * i / count;
                                double x = ringLoc.getX() + 5 * Math.cos(angle);
                                double z = ringLoc.getZ() + 5 * Math.sin(angle);
                                Location particleLoc = new Location(ringLoc.getWorld(), x, ringLoc.getY(), z);
                                player.spawnParticle(particle, particleLoc, 1, 0, 0, 0, 0);
                            }
                        }
                    }
                }
            }
        };

        previewTask.runTaskTimer(plugin, 0L, 10L);
    }

    private Particle getParticleType() {
        String particleName = plugin.getConfigManager().getPreviewParticle();
        try {
            return Particle.valueOf(particleName);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid particle type: " + particleName + ", using VILLAGER_HAPPY");
            return Particle.HAPPY_VILLAGER;
        }
    }

    // NEW: Feature 4 - Platform command
    private boolean handlePlatform(CommandSender sender, String[] args) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /er platform <create|remove>");
            return true;
        }

        String action = args[1].toLowerCase();
        
        switch (action) {
            case "create":
                List<Player> players = new ArrayList<>();
                for (UUID uuid : plugin.getRaceManager().getStartLobbyPlayers()) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) players.add(p);
                }
                
                if (players.isEmpty()) {
                    sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                        "§cNo players in start lobby!");
                    return true;
                }
                
                plugin.getRaceManager().getPlatformManager().createPlatform(players);
                sender.sendMessage(plugin.getConfigManager().getPrefix() + 
                    "§aCreated starting platform for " + players.size() + " player(s)!");
                break;
                
            case "remove":
                plugin.getRaceManager().getPlatformManager().removePlatform();
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aRemoved starting platform!");
                break;
                
            default:
                sender.sendMessage("§cUsage: /er platform <create|remove>");
                break;
        }

        return true;
    }

    private boolean handleForceStart(CommandSender sender) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        if (plugin.getRaceManager().isRacing()) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "§cRace already in progress!");
            return true;
        }

        plugin.getRaceManager().forceStart();
        sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aForce started race!");
        return true;
    }

    private boolean handleReset(CommandSender sender) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }

        plugin.getRaceManager().endRace();
        sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aRace reset!");
        return true;
    }

    private boolean handleListRings(CommandSender sender) {
        Map<String, Location> rings = plugin.getConfigManager().getRingLocations();
        sender.sendMessage("§6§l╔═══ Configured Rings ═══╗");
        if (rings.isEmpty()) {
            sender.sendMessage("§7No rings configured.");
            sender.sendMessage("§7Use /er import rings or /er setup addring");
        } else {
            for (String ringName : rings.keySet()) {
                Location loc = rings.get(ringName);
                sender.sendMessage(String.format("§e• %s §7- (%.0f, %.0f, %.0f)", 
                    ringName, loc.getX(), loc.getY(), loc.getZ()));
            }
            sender.sendMessage("§7Total: §e" + rings.size() + " §7ring(s)");
        }
        sender.sendMessage("§6§l╚═════════════════════════╝");
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6§l╔═══════ ElytraRace Commands ═══════╗");
        sender.sendMessage("§e/er rules §7- View race rules");
        sender.sendMessage("§e/er progress §7- View your progress");
        sender.sendMessage("§e/er timer §7- View current race time");
        sender.sendMessage("§e/er stats [player] §7- View stats");
        sender.sendMessage("§e/er pb [player] §7- View personal best");
        sender.sendMessage("§e/er top §7- View leaderboard");
        sender.sendMessage("§e/er listrings §7- List all rings");
        sender.sendMessage("§e/ready §7- Toggle ready");
        
        if (sender.hasPermission("race.admin")) {
            sender.sendMessage("");
            sender.sendMessage("§c§lAdmin Commands:");
            sender.sendMessage("§e/er forcejoin <player> §7- Force player to race");
            sender.sendMessage("§e/er testmode §7- Toggle test mode");
            sender.sendMessage("§e/er import rings §7- Import WorldGuard regions");
            sender.sendMessage("§e/er preview §7- Toggle ring preview");
            sender.sendMessage("§e/er platform <create|remove> §7- Manage platform");
            sender.sendMessage("§e/er start §7- Force start race");
            sender.sendMessage("§e/er reset §7- Reset race");
            sender.sendMessage("§e/er setup lobby §7- Set lobby");
            sender.sendMessage("§e/er setup start §7- Set start region");
            sender.sendMessage("§e/er setup finish §7- Set finish region");
        }
        sender.sendMessage("§6§l╚════════════════════════════════════╝");
    }

    private boolean handleSetup(CommandSender sender, String[] args) {
        if (!sender.hasPermission("race.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use setup commands!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 2) {
            showHelp(sender);
            return true;
        }

        String sub = args[1].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "lobby":
                plugin.getConfigManager().setLobbyLocation(player.getLocation());
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aLobby set.");
                return true;
            case "start":
                if (!we.isWorldEditAvailable() || !we.hasSelection(player)) {
                    sender.sendMessage("§cYou need a WorldEdit selection first.");
                    return true;
                }
                Location min = we.getSelectionMin(player);
                Location max = we.getSelectionMax(player);
                if (min == null || max == null) {
                    sender.sendMessage("§cInvalid selection.");
                    return true;
                }
                regionManager.saveRegionFromSelection(min, max, RegionManager.RegionType.START);
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aStart region saved.");
                return true;
            case "finish":
                if (!we.isWorldEditAvailable() || !we.hasSelection(player)) {
                    sender.sendMessage("§cYou need a WorldEdit selection first.");
                    return true;
                }
                Location minF = we.getSelectionMin(player);
                Location maxF = we.getSelectionMax(player);
                if (minF == null || maxF == null) {
                    sender.sendMessage("§cInvalid selection.");
                    return true;
                }
                regionManager.saveRegionFromSelection(minF, maxF, RegionManager.RegionType.FINISH);
                sender.sendMessage(plugin.getConfigManager().getPrefix() + "§aFinish region saved.");
                return true;
            default:
                showHelp(sender);
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>(Arrays.asList(
                "rules", "progress", "timer", "stats", "pb", "top", "listrings"
            ));
            if (sender.hasPermission("race.admin")) {
                completions.addAll(Arrays.asList(
                    "setup", "start", "reset", "forcejoin", "testmode", 
                    "import", "preview", "platform"
                ));
            }
            return completions;
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "setup":
                    return Arrays.asList("lobby", "start", "finish");
                case "stats":
                case "pb":
                case "forcejoin":
                    return null; // Returns online player names
                case "import":
                    return Collections.singletonList("rings");
                case "platform":
                    return Arrays.asList("create", "remove");
            }
        }
        return Collections.emptyList();
    }
}