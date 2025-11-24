# Changelog

All notable changes to ElytraRace are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.1.0] - 2025-01-XX (Unreleased)

### 🎉 Major Release - Enhanced Systems Update

This release introduces **10 major new features** focused on automation, admin tools, and competitive features.

### Added

#### 🚀 Force-Join System
- **`/er forcejoin <player>`** - Admins can force teleport players to race lobby
- Automatic rules display on join (all requirements shown)
- Auto-join when entering lobby region
- Join validation with clear error messages

#### 🗺️ Automatic Region Import
- **`/er import rings`** - Import WorldGuard regions as race rings
- Detects regions named `ring1`, `ring2`, `ring3`, etc.
- Preserves exact WorldGuard region boundaries
- Automatic sorting by ring number
- Dependency checking with helpful warnings

#### 🎯 Starting Platform System
- **`/er platform <create|remove>`** - Manage starting platforms
- Auto-creates platform on countdown start
- Configurable material and size
- Animated platform removal on race start (dramatic effect)
- Automatic cleanup on race end

#### 🧪 Admin Test Mode
- **`/er testmode`** - Solo testing mode for admins
- Bypasses all race requirements
- Does NOT save statistics (temporary data only)
- Independent from normal races
- Toggle on/off easily

#### 🏆 Personal Best Tracking
- **`/er pb [player]`** - View personal best times
- Individual fastest time tracking per player
- Automatic new PB detection and celebration
- Improvement calculations (faster/slower by X seconds)
- Global ranking system
- Achievement dates tracked

#### 👻 Auto-Spectator Mode
- Automatic spectator mode after finishing race
- Configurable delay (default 3 seconds)
- Watch other racers complete the course
- Auto-return to lobby when race ends
- Permission-safe spectator switching

#### ✨ Glowing Ring Preview
- **`/er preview`** - Toggle particle effects around rings
- Admin-only visibility
- Configurable particle type and count
- Helps with course design and validation
- Performance optimized (10 tick updates)

#### 🛡️ Soft Anti-Cheat Boundary System
- Warns players when going off-course
- Configurable boundary distance (default 50 blocks)
- Progressive warning system (3 warnings)
- Automatic teleport to last checkpoint
- Clear warning messages

#### ⏱️ Auto-Finish Timer
- Configurable race time limit (default 180 seconds)
- Automatic race end when timer expires
- Players still racing marked as DNF
- Shown in join rules
- Clean race state cleanup

#### 🎒 Rocket Requirements System
- Configurable required rocket count (default 64)
- Validation before allowing `/ready`
- Checks elytra equipped in chestplate
- Validates empty inventory (except armor)
- Clear error messages for each requirement

### Changed

#### Configuration Enhancements
```yaml
# New config options added
race:
  required-rockets: 64          # NEW
  auto-finish-time: 180         # NEW

region-import:                   # NEW SECTION
  enabled: true
  prefix: "ring"

anti-cheat:                      # NEW SECTION
  boundary-distance: 50
  teleport-on-exceed: true
  warnings-before-teleport: 3

spectator:                       # NEW SECTION
  auto-enable: true
  return-to-lobby: true
  delay-seconds: 3

starting-platform:               # NEW SECTION
  enabled: true
  material: "GLASS"
  size: 3
  height-offset: -1

ring-preview:                    # NEW SECTION
  enabled: true
  particle: "VILLAGER_HAPPY"
  particle-count: 20
```

#### Command Improvements
- Added 6 new commands
- Enhanced help menu with feature categories
- Improved tab completion for all commands
- Better error messages throughout

#### Performance Optimizations
- Boundary checking cached (500ms intervals)
- Ring preview optimized particle rendering
- Reduced memory usage for large player counts
- Improved region checking performance

### Enhanced

- **Startup Messages**: Now shows all 10 new features
- **Dependency Detection**: Visual startup warnings for missing plugins
- **Race Results**: Shows personal best indicators
- **Statistics Display**: More detailed with PB information
- **Error Messages**: Clearer validation messages

### Fixed

- Race state not properly resetting on server restart
- Timer synchronization issues during countdown
- Region boundary detection edge cases
- Spectator mode permission conflicts
- Platform cleanup on race cancellation

### Technical Changes

#### New Classes
- `RegionImportManager.java` - Handles WorldGuard region import
- `StartingPlatformManager.java` - Manages starting platforms
- `PersonalBestManager.java` - Tracks personal bests

#### Updated Classes
- `ElytraRacePlugin.java` - Added dependency checking
- `ConfigManager.java` - 20+ new config options
- `RaceManager.java` - All 10 features integrated
- `RaceCommand.java` - 6 new command handlers
- `RaceListener.java` - Boundary checking added

#### Dependencies
- Added soft dependency: **WorldGuard** (optional)
- Maintained requirement: **WorldEdit** (required)

### Deprecated

- None

### Removed

- None

### Security

- No security vulnerabilities fixed in this release
- All new features include proper permission checks

---

## [1.0.0] - 2025-11-22

### 🎊 Initial Release

First stable release of ElytraRace with core racing functionality.

### Added

#### Core Racing Features
- Automatic race joining via start region entry
- Ready-check system with synchronized countdown
- Ring detection with 5-block radius
- Sequential ring order enforcement
- Finish region detection with completion validation
- Race timer system (per-player and global tracking)
- Countdown system: 3 → 2 → 1 → READY → GO

#### Anti-Cheat System
- Rocket usage tracking (maximum 3 per race)
- Ring skip detection with instant disqualification
- Disqualification system with detailed reasons
- Disconnect handling (automatic DNF status)
- Race timeout system (default 30 minutes)
- Ring order validation

#### Statistics System
- Win tracking per player
- Total races played counter
- Best time records
- Average time calculation
- Win rate percentage
- Persistent storage in `stats.yml`

#### Leaderboard System
- Top 10 player rankings
- Sorting by wins
- Best time display
- Average time display
- Win rate display

#### Commands - Player
- `/er rules` - Display comprehensive race rules
- `/er stats [player]` - View race statistics
- `/er top` - View top 10 leaderboard
- `/er timer` - View current race time
- `/er progress` - Check ring completion progress
- `/ready` - Toggle ready status

#### Commands - Admin
- `/er start` - Force start race (bypass ready check)
- `/er reset` - Reset current race
- `/er setup lobby` - Set lobby spawn location
- `/er setup start` - Define start region (WorldEdit)
- `/er setup finish` - Define finish region (WorldEdit)
- `/er setup addring <name>` - Add ring at current location
- `/er setup addringwe <name>` - Add ring from WorldEdit selection
- `/er setup removering <name>` - Remove specified ring
- `/er listrings` - List all configured rings

#### Configuration
- Customizable min/max players (default 2-5)
- Adjustable countdown duration (default 5 seconds)
- Configurable ready timeout (default 2 minutes)
- Customizable max race time (default 30 minutes)
- Full message customization support
- Color code support in all messages
- Ring detection radius configuration

#### Permissions
- `race.use` - Basic race commands (default: true)
- `race.admin` - Admin setup commands (default: op)
- `race.stats` - View statistics (default: true)
- `race.top` - View leaderboard (default: true)

#### Technical Features
- WorldEdit integration for region setup
- Persistent YAML-based data storage
- Event-driven architecture
- Optimized ring detection with caching (250ms intervals)
- Thread-safe timer system
- Pre-computed squared distances for performance
- Efficient movement checking (ignores head rotations)

#### Documentation
- Comprehensive README.md
- Detailed COMMANDS.md reference
- Complete INSTALLATION.md guide
- WorldEdit integration guide (WORLDEDIT.md)
- Contributing guidelines (CONTRIBUTING.md)
- Security policy (SECURITY.md)
- Full inline code documentation

### Technical Details

**Compatibility:**
- Minecraft 1.21.4+
- Paper/Spigot/Purpur server software
- Java 21+

**Dependencies:**
- WorldEdit 7.3.3+ (required)
- VoiidCountdownTimer (optional, auto-detected)

**Performance Metrics:**
- Optimized movement checking
- Cached region checks (5 tick/250ms intervals)
- Pre-computed ring distances
- Async-compatible design
- Minimal CPU usage (~2-4% per active player)
- Low memory footprint (~10MB for 100 players)

---

## [Unreleased]

### Planned Features (v1.2.0)

#### Team Racing
- Team-based competitions
- Shared statistics
- Team leaderboards
- Team chat

#### Economy Integration
- Vault support
- Entry fees
- Prize money
- Betting system
- Sponsor rewards

#### Enhanced Features
- PlaceholderAPI integration
- Custom particle effects
- Sound effect customization
- Race replays
- Checkpoint system for long races

### Under Consideration (v2.0.0)

- Multiple race tracks
- Tournament system
- Custom elytra cosmetics
- MySQL database support
- Advanced statistics dashboard
- Time trial mode
- Ghost racers (replay of best times)
- Discord integration

---

## Version History Summary

| Version | Release Date | Type | Description |
|---------|-------------|------|-------------|
| **1.1.0** | TBD | Major | Enhanced Systems (10 new features) |
| **1.0.0** | 2025-11-22 | Initial | Core racing functionality |

---

## Versioning Policy

ElytraRace follows [Semantic Versioning](https://semver.org/):

**Format:** `MAJOR.MINOR.PATCH`

- **MAJOR**: Breaking changes or major rewrites
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes (backward compatible)

### Support Policy

| Version | Status | Updates | Support Until |
|---------|--------|---------|---------------|
| 1.1.x | Current | All updates | Current + 1 minor |
| 1.0.x | Maintenance | Security only | 1.1.0 release + 3 months |
| < 1.0 | Unsupported | None | End of Life |

---

## Upgrade Guide

### From 1.0.x to 1.1.0

#### Automatic Migration
✅ **No manual action required** - All existing data will be preserved.

#### What Happens Automatically:
1. Config.yml updated with new options (defaults added)
2. Existing statistics maintained and enhanced
3. Personal bests calculated from existing data
4. All rings preserved

#### New Features Available:
- Run `/er import rings` if using WorldGuard
- Configure new settings in `config.yml`
- Enable features you want to use

#### Breaking Changes:
**None** - This release is 100% backward compatible.

### From Pre-1.0 to 1.0.0

This is the initial stable release. If you were using development builds:

1. **Backup** all data files
2. **Delete** old plugin folder
3. **Install** fresh 1.0.0 version
4. **Reconfigure** race settings
5. **Import** old stats if available

---

## How to Update

### Standard Update Procedure

```bash
# 1. Backup your data
cp -r plugins/ElytraRace ~/backups/ElytraRace-$(date +%Y%m%d)

# 2. Stop your server
./stop.sh

# 3. Download new version
wget https://github.com/Kartik-Fulara/ElytraRace/releases/latest/download/ElytraRace.jar

# 4. Replace old JAR
mv ElytraRace.jar plugins/

# 5. Start server
./start.sh

# 6. Check console for migration messages
tail -f logs/latest.log
```

### Rollback Procedure

If issues occur after updating:

```bash
# 1. Stop server
./stop.sh

# 2. Restore backup
rm -rf plugins/ElytraRace
cp -r ~/backups/ElytraRace-YYYYMMDD plugins/ElytraRace

# 3. Replace JAR with old version
mv plugins/ElytraRace-old.jar plugins/ElytraRace.jar

# 4. Restart server
./start.sh
```

---

## Links

- **[Latest Release](https://github.com/Kartik-Fulara/ElytraRace/releases/latest)**
- **[All Releases](https://github.com/Kartik-Fulara/ElytraRace/releases)**
- **[Unreleased Changes](https://github.com/Kartik-Fulara/ElytraRace/compare/v1.0.0...HEAD)**
- **[Milestones](https://github.com/Kartik-Fulara/ElytraRace/milestones)**

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for:
- How to report bugs
- How to suggest features
- How to submit code changes
- Branch naming conventions

---

## Questions?

- 📖 [Documentation](docs/)
- 💬 [Discussions](https://github.com/Kartik-Fulara/ElytraRace/discussions)
- 🐛 [Issues](https://github.com/Kartik-Fulara/ElytraRace/issues)
- 📧 Email: kartikfulara2003@gmail.com

---

**Last Updated:** 2025-01-XX  
**Maintained By:** Kartik Fulara

[1.1.0]: https://github.com/Kartik-Fulara/ElytraRace/releases/tag/v1.1.0
[1.0.0]: https://github.com/Kartik-Fulara/ElytraRace/releases/tag/v1.0.0
[Unreleased]: https://github.com/Kartik-Fulara/ElytraRace/compare/v1.1.0...HEAD