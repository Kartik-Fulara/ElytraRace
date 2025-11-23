# 📦 Installation & Setup Guide

## System Requirements

- **Server Software**: Paper 1.21.4 (or later compatible version)
- **Java**: Java 21 or higher
- **RAM**: Minimum 2GB allocated to server (4GB recommended)
- **Disk Space**: 50MB free space for plugin and data

## Pre-Installation Dependencies (Optional)

These plugins are optional but recommended:

- **WorldEdit 7.3.3+** - For easy race course creation
- **WorldGuard 7.0.13+** - For region protection (optional)

## Installation Steps

### Step 1: Download the Plugin

#### Option A: Download Release
1. Go to [GitHub Releases](https://github.com/Kartik-Fulara/ElytraRace/releases)
2. Download the latest `ElytraRace-x.x.x.jar`
3. Save to your computer

#### Option B: Build from Source
```bash
git clone https://github.com/Kartik-Fulara/ElytraRace.git
cd ElytraRace
mvn clean package
# JAR will be in target/ folder
```

### Step 2: Install the Plugin

1. Stop your Paper server
2. Locate your server's `plugins/` folder
3. Copy `ElytraRace.jar` into the `plugins/` folder
4. Restart your server

### Step 3: Initial Configuration

After first restart, these files will be generated:

```
plugins/ElytraRace/
├── config.yml          # Main configuration
├── messages.yml        # Customizable messages
├── races.yml           # Race definitions
└── data/
    └── playerdata.yml  # Player statistics
```

### Step 4: Configure the Plugin

Edit `plugins/ElytraRace/config.yml`:

```yaml
# ElytraRace Configuration
plugin:
  version: 1.0.0
  debug: false

server:
  world: world                    # Default race world
  lobby-location: 0, 100, 0      # Spawn location
  max-races: 10                   # Max concurrent races
  race-timeout: 300              # Seconds (5 minutes)

permissions:
  require-permission: true        # Require /race permission
  admin-commands: op              # Who can use admin commands

features:
  particles-enabled: true         # Show particle effects
  sound-enabled: true             # Play completion sounds
  statistics-enabled: true        # Track player stats
  leaderboard-enabled: true       # Show top races

database:
  type: yaml                      # yaml or mysql
  yaml:
    file: playerdata.yml
  mysql:
    host: localhost
    port: 3306
    database: elytrarace
    Kartik-Fulara: root
    password: ""
```

### Step 5: Set Up Basic Race

Create your first race in-game:

```bash
# In-game as admin:
/race create myrace
/race setstart
/race setfinish
/race enable myrace
```

## Verification

### Check if plugin loaded correctly:

1. **Server Console**: Look for message like:
   ```
   [ElytraRace] Plugin enabled successfully (v1.0.0)
   ```

2. **In-game Command**: 
   ```
   /race help
   ```
   Should show available commands

3. **Permissions**: 
   ```
   /race list
   ```
   Should show races (if any created)

## Troubleshooting Installation

### Plugin didn't load
- Check server log for errors
- Ensure Java 21+ is installed: `java -version`
- Verify `ElytraRace.jar` is in correct folder
- Check file permissions

### Commands not working
- Ensure you have `elytrarace.admin` permission
- Check `/op @s` in-game or give permission in permission plugin
- Restart server after permission changes

### WorldEdit not recognized
- Ensure WorldEdit JAR is in `plugins/` folder
- Restart server
- Check server log for WorldEdit errors

## Permissions

### Basic Permissions

```yaml
# Allow all commands (default with op)
elytrarace.*

# Player permissions
elytrarace.player.join       # Join races
elytrarace.player.list       # List races
elytrarace.player.stats      # View stats
elytrarace.player.leaderboard # View leaderboard

# Admin permissions
elytrarace.admin.*           # All admin commands
elytrarace.admin.create      # Create races
elytrarace.admin.delete      # Delete races
elytrarace.admin.setstart    # Set start point
elytrarace.admin.setfinish   # Set finish point
elytrarace.admin.enable      # Enable/disable race
elytrarace.admin.reload      # Reload config
```

### Configure with Permission Plugin

#### LuckPerms Example:
```bash
/lp user Kartik-Fulara permission set elytrarace.player.* true
/lp group admins permission set elytrarace.admin.* true
```

#### PermissionsEx Example:
```bash
/pex user Kartik-Fulara add elytrarace.player.*
/pex group admins add elytrarace.admin.*
```

## First Run Checklist

- [ ] Java 21+ installed
- [ ] Paper 1.21.4+ running
- [ ] JAR placed in plugins/ folder
- [ ] Server restarted
- [ ] No errors in console log
- [ ] `/race help` works in-game
- [ ] Permissions configured
- [ ] First race created with `/race create`
- [ ] config.yml customized to your needs

## Updating the Plugin

### Updating to Newer Version

1. Stop server
2. Backup `plugins/ElytraRace/` folder
3. Replace `ElytraRace.jar` with new version
4. Delete old JAR file
5. Restart server

**Note**: Your config and data files will be preserved!

### Rollback to Previous Version

1. Stop server
2. Restore backup of `plugins/ElytraRace/` folder
3. Replace JAR with old version
4. Restart server

## Backup & Recovery

### Regular Backups

Backup these important files:

```bash
# Linux/Mac
cp -r plugins/ElytraRace ~/backup/ElytraRace-$(date +%Y%m%d).bak

# Windows
robocopy plugins\ElytraRace backup\ElytraRace-backup-2025 /E
```

### Restore from Backup

1. Stop server
2. Delete corrupted `plugins/ElytraRace/` folder
3. Copy backup to `plugins/` as `ElytraRace`
4. Restart server

## Getting Help

- 📖 See [CONFIGURATION.md](CONFIGURATION.md) for advanced setup
- 💬 Join [Discussions](https://github.com/Kartik-Fulara/ElytraRace/discussions)
- 🐛 Report issues at [GitHub Issues](https://github.com/Kartik-Fulara/ElytraRace/issues)
- 📚 Check [Troubleshooting](TROUBLESHOOTING.md) for common problems
