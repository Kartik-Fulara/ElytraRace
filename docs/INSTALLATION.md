# 📦 Installation & Setup Guide

Complete installation guide for ElytraRace v1.1.0

---

## 📋 Table of Contents

- [System Requirements](#-system-requirements)
- [Pre-Installation](#-pre-installation)
- [Installation Steps](#-installation-steps)
- [Initial Configuration](#-initial-configuration)
- [Course Setup](#-course-setup)
- [Verification](#-verification)
- [Troubleshooting](#-troubleshooting)
- [Updating](#-updating)

---

## 💻 System Requirements

### Minimum Requirements

| Component | Requirement | Notes |
|-----------|-------------|-------|
| **Server Software** | Paper 1.21.4+ | Spigot/Purpur also supported |
| **Java** | Java 21+ | Required for Paper 1.21.4+ |
| **RAM** | 2GB allocated | 4GB recommended for larger servers |
| **Disk Space** | 50MB free | For plugin and data storage |
| **CPU** | 2 cores | More cores recommended for 20+ players |

### Recommended Setup

- **Server**: Paper 1.21.4 or newer
- **RAM**: 4-8GB allocated to server
- **CPU**: 4+ cores for optimal performance
- **Players**: Tested with up to 50 concurrent players

---

## 📦 Pre-Installation

### Required Dependencies

#### WorldEdit (Required)
**Version**: 7.3.3 or newer  
**Download**: [EngineHub WorldEdit](https://enginehub.org/worldedit/)

```bash
# Download and install
cd plugins/
wget https://dev.bukkit.org/projects/worldedit/files/latest
```

### Optional Dependencies

#### WorldGuard (Recommended for v1.1.0+)
**Version**: 7.0.13 or newer  
**Download**: [EngineHub WorldGuard](https://enginehub.org/worldguard/)

**Features Enabled**:
- Automatic region import (`/er import rings`)
- Region-based course design

```bash
# Download and install
cd plugins/
wget https://dev.bukkit.org/projects/worldguard/files/latest
```

#### Permission Plugin (Recommended)
Choose one:
- **LuckPerms** (Recommended) - [Download](https://luckperms.net/)
- **PermissionsEx** - [Download](https://github.com/PEXPlugins/PermissionsEx)

---

## 🚀 Installation Steps

### Step 1: Download ElytraRace

#### Option A: Download from Releases (Recommended)

```bash
# Download latest release
cd ~/minecraft-server/plugins/
wget https://github.com/Kartik-Fulara/ElytraRace/releases/latest/download/ElytraRace.jar
```

#### Option B: Build from Source

```bash
# Clone repository
git clone https://github.com/Kartik-Fulara/ElytraRace.git
cd ElytraRace

# Build with Maven
mvn clean package

# Copy to server
cp target/ElytraRace-1.1.0.jar ~/minecraft-server/plugins/
```

---

### Step 2: Install the Plugin

```bash
# Navigate to server directory
cd ~/minecraft-server

# Stop server if running
./stop.sh

# Verify plugin is in plugins folder
ls -lh plugins/ElytraRace.jar

# Start server
./start.sh
```

---

### Step 3: Verify Installation

Watch the server console for:

```
[ElytraRace] Checking Dependencies...
  ✅ WorldEdit: FOUND
  ✅ WorldGuard: FOUND

[ElytraRace] Plugin enabled successfully (v1.1.0)
[ElytraRace] Ready for elytra racing!
```

#### If Dependencies Missing:

```
╔═══════════════════════════════════════╗
║   ⚠️  MISSING DEPENDENCIES            ║
╠═══════════════════════════════════════╣
║   ❌ WorldEdit: NOT FOUND             ║
║   ❌ WorldGuard: NOT FOUND            ║
╠═══════════════════════════════════════╣
║   Region import features DISABLED     ║
║   Manual ring setup still works       ║
╚═══════════════════════════════════════╝
```

**Solution**: Install missing dependencies and restart.

---

## ⚙️ Initial Configuration

### Step 4: Generated Files

After first start, these files are created:

```
plugins/ElytraRace/
├── config.yml          # Main configuration
├── stats.yml           # Player statistics
└── data/               # Future data storage
```

---

### Step 5: Configure Settings

Edit `plugins/ElytraRace/config.yml`:

```yaml
############################################################
#              ELYTRA RACE CONFIGURATION v1.1.0
############################################################

race:
  min-players: 2                # Minimum to start
  max-players: 5                # Maximum allowed
  required-rockets: 64          # Rockets needed to ready
  auto-finish-time: 180         # Auto-end after 3 minutes

region-import:
  enabled: true                 # Enable WorldGuard import
  prefix: "ring"                # Detect ring1, ring2, etc.

anti-cheat:
  boundary-distance: 50         # Off-course warning distance
  teleport-on-exceed: true      # Teleport back if too far
  warnings-before-teleport: 3   # Warnings before teleport

spectator:
  auto-enable: true             # Auto-spectator after finish
  return-to-lobby: true         # Return to lobby on race end
  delay-seconds: 3              # Delay before spectator mode

starting-platform:
  enabled: true                 # Create platforms on countdown
  material: "GLASS"             # Platform block type
  size: 3                       # Platform radius
  height-offset: -1             # Blocks below player

ring-preview:
  enabled: true                 # Admin ring visualization
  particle: "VILLAGER_HAPPY"    # Particle effect type
  particle-count: 20            # Particles per ring

messages:
  prefix: "&6[ElytraRace] &f"
  race-started: "&aThe race has started! Fly through all the rings!"
  # ... (full message customization)
```

Save and run `/er reload` (when implemented) or restart server.

---

## 🏗️ Course Setup

### Method 1: Import from WorldGuard (Recommended)

#### Step 1: Create WorldGuard Regions

```bash
# In-game with WorldEdit wand
//wand

# Select first ring area
//pos1
//pos2

# Create WorldGuard region
/rg define ring1

# Repeat for all rings
/rg define ring2
/rg define ring3
# ... etc
```

#### Step 2: Import Regions

```bash
# Import all rings automatically
/er import rings
```

Output:
```
[ElytraRace] Importing rings from WorldGuard...
[ElytraRace] ✅ Successfully imported 12 ring(s)!
[ElytraRace] Use /er listrings to view them.
```

---

### Method 2: Manual Setup

#### Step 1: Set Lobby

```bash
# Stand where you want lobby spawn
/er setup lobby
```

#### Step 2: Define Start Region

```bash
# Make WorldEdit selection
//wand
# Select start area
//pos1
//pos2

# Save start region
/er setup start
```

#### Step 3: Define Finish Region

```bash
# Select finish area with WorldEdit
//pos1
//pos2

# Save finish region
/er setup finish
```

#### Step 4: Add Rings

```bash
# Stand at each ring location and run:
/er setup addring ring1
/er setup addring ring2
/er setup addring ring3
# ... continue for all rings
```

---

### Step 5: Verify Course Setup

```bash
# List all rings
/er listrings

# Preview with particles
/er preview

# Test the course
/er testmode
# Fly through the course
/er testmode  # Exit test mode
```

---

## ✅ Verification

### Checklist

Complete this checklist to verify installation:

- [ ] Plugin loaded without errors
- [ ] WorldEdit dependency detected
- [ ] WorldGuard dependency detected (if installed)
- [ ] `/er help` command works
- [ ] Lobby location set
- [ ] Start region defined
- [ ] Finish region defined
- [ ] At least 3 rings added/imported
- [ ] Ring preview shows particles
- [ ] Test mode works correctly
- [ ] Permissions configured

### Test Commands

```bash
# As admin
/er rules           # Should show race rules
/er listrings       # Should list all rings
/er preview         # Should show particles
/er testmode        # Should enable test mode

# As player
/er stats           # Should show your stats
/er pb              # Should show personal best
/er top             # Should show leaderboard
```

---

## 🔧 Troubleshooting

### Plugin Won't Load

**Symptoms**: Plugin not in `/plugins` list

**Solutions**:
1. Check Java version: `java -version` (must be 21+)
2. Check Paper version: Should be 1.21.4+
3. Check console for errors
4. Verify file downloaded completely
5. Check file permissions: `chmod 644 ElytraRace.jar`

---

### WorldEdit Not Detected

**Symptoms**: Warning about missing WorldEdit

**Solutions**:
```bash
# Check if WorldEdit is installed
ls plugins/ | grep -i worldedit

# Download WorldEdit if missing
cd plugins/
wget https://dev.bukkit.org/projects/worldedit/files/latest

# Restart server
```

---

### Commands Not Working

**Symptoms**: "Unknown command" or permission errors

**Solutions**:

1. **Check Permissions**:
```bash
# LuckPerms
/lp user YourName permission set race.use true
/lp user YourName permission set race.admin true

# Or give OP
/op YourName
```

2. **Check plugin.yml exists** inside JAR
3. **Restart server** after permission changes

---

### Regions Not Working

**Symptoms**: Players not joining when entering start region

**Solutions**:

1. **Verify region defined**:
```bash
/er setup start   # After making selection
```

2. **Check selection was made**:
```bash
//wand
//pos1
//pos2
```

3. **Test region manually**:
```bash
/er testmode
# Walk into start area
```

---

### Ring Import Failed

**Symptoms**: "No rings found" when importing

**Solutions**:

1. **Check WorldGuard installed**
2. **Verify region names**:
```bash
/rg list   # Should show ring1, ring2, etc.
```

3. **Check region prefix in config**:
```yaml
region-import:
  prefix: "ring"   # Must match region names
```

4. **Ensure regions are in same world**

---

## 🔄 Updating

### From v1.0.x to v1.1.0

#### Step 1: Backup Current Installation

```bash
# Stop server
./stop.sh

# Backup plugin folder
cp -r plugins/ElytraRace ~/backups/ElytraRace-$(date +%Y%m%d)

# Backup configurations
tar -czf ~/backups/elytrarace-config-$(date +%Y%m%d).tar.gz plugins/ElytraRace/*.yml
```

#### Step 2: Download New Version

```bash
cd plugins/
mv ElytraRace.jar ElytraRace-1.0.0-backup.jar
wget https://github.com/Kartik-Fulara/ElytraRace/releases/latest/download/ElytraRace.jar
```

#### Step 3: Start Server

```bash
cd ..
./start.sh
```

#### Step 4: Verify Update

Check console for:
```
[ElytraRace] Plugin enabled successfully (v1.1.0)
[ElytraRace] NEW FEATURES:
[ElytraRace]   • Force Join System
[ElytraRace]   • Region Import
[ElytraRace]   ... (all 10 features listed)
```

#### Step 5: Use New Features

```bash
# Import rings if using WorldGuard
/er import rings

# Try new commands
/er forcejoin PlayerName
/er testmode
/er preview
```

---

### Rollback Procedure

If issues occur after update:

```bash
# Stop server
./stop.sh

# Restore backup
rm plugins/ElytraRace.jar
mv plugins/ElytraRace-1.0.0-backup.jar plugins/ElytraRace.jar

# Restore configs if needed
tar -xzf ~/backups/elytrarace-config-YYYYMMDD.tar.gz -C plugins/

# Start server
./start.sh
```

---

## 🔐 Permissions Setup

### LuckPerms (Recommended)

```bash
# Create groups
/lp creategroup racers
/lp creategroup raceadmins

# Assign permissions
/lp group racers permission set race.use true
/lp group raceadmins parent set racers
/lp group raceadmins permission set race.admin true

# Add players to groups
/lp user PlayerName parent set racers
/lp user AdminName parent set raceadmins
```

---

### PermissionsEx

```bash
# Set group permissions
/pex group default add race.use
/pex group admins add race.admin

# Set player permissions
/pex user PlayerName add race.use
/pex user AdminName add race.admin
```

---

## 📊 Performance Optimization

### For Large Servers (50+ players)

```yaml
# config.yml - Performance tuning section
performance:
  region-check-interval: 500      # Increase from 250ms
  boundary-check-interval: 1000   # Increase from 500ms
  ring-detection-radius: 4.0      # Decrease from 5.0
```

### Java Arguments

```bash
# Optimized JVM flags for race servers
java -Xms4G -Xmx4G \
  -XX:+UseG1GC \
  -XX:+ParallelRefProcEnabled \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UnlockExperimentalVMOptions \
  -XX:+DisableExplicitGC \
  -XX:G1HeapRegionSize=16M \
  -jar paper.jar nogui
```

---

## 📚 Next Steps

After installation:

1. **Read** [Commands Reference](COMMANDS.md)
2. **Configure** [config.yml](CONFIGURATION.md)
3. **Setup** your first race course
4. **Test** with `/er testmode`
5. **Invite** players to try it out

---

## 🆘 Getting Help

- **Discord**: [Join support server](https://discord.gg/YOUR_INVITE)
- **GitHub**: [Open an issue](https://github.com/Kartik-Fulara/ElytraRace/issues)
- **Wiki**: [Read documentation](https://github.com/Kartik-Fulara/ElytraRace/wiki)
- **Email**: kartikfulara2003@gmail.com

---

**Installation Guide v1.1.0**  
Last Updated: 2025-01-XX  
Maintained By: Kartik Fulara