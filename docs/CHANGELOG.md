# Changelog

All notable changes to ElytraRace are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.1.0] - 2025-01-XX

### 🎉 Major Release - Enhanced Systems Update

This release introduces **10 major new features** focused on automation, admin tools, and competitive features, plus critical bug fixes.

---

### Added

#### 🚀 Feature 1: Force-Join System
- **`/er forcejoin <player>`** - Admins can force teleport players to race lobby
- Automatic rules display on join (all requirements shown clearly)
- Auto-join when entering lobby region (no command needed)
- Join validation with clear, helpful error messages
- Lobby capacity checking and enforcement

#### 🗺️ Feature 2: Automatic Region Import
- **`/er import rings`** - Import WorldGuard regions as race rings automatically
- Detects regions named `ring1`, `ring2`, `ring3`, etc.
- Preserves exact WorldGuard region boundaries
- Automatic sorting by ring number
- Dependency checking with helpful warnings if WorldGuard missing
- Clear feedback on import success/failure

#### 🎯 Feature 3: Starting Platform System
- **`/er platform <create|remove>`** - Manage starting platforms
- Auto-creates platform on countdown start
- Configurable material (default: GLASS) and size (default: 3 blocks radius)
- Animated platform removal on race start (dramatic effect)
- Automatic cleanup on race end or cancellation
- Platform appears 1 block below player feet

#### 🧪 Feature 4: Admin Test Mode
- **`/er testmode`** - Solo testing mode for admins
- Bypasses ALL race requirements (rockets, elytra, inventory)
- Does NOT save statistics (temporary data only)
- Independent from normal races
- Toggle on/off easily
- Shows clear indicator when test mode active

#### 🏆 Feature 5: Personal Best Tracking
- **`/er pb [player]`** - View personal best times
- Individual fastest time tracking per player
- Automatic new PB detection and celebration messages
- Improvement calculations (faster/slower by X seconds)
- Global ranking system (see your rank)
- Achievement dates tracked and displayed
- Shows time since achievement (e.g., "3 days ago")

#### 👻 Feature 6: Auto-Spectator Mode
- Automatic spectator mode after finishing race
- Configurable delay (default 3 seconds)
- Watch other racers complete the course
- Auto-return to lobby when race ends
- Permission-safe spectator switching
- Clear messages about spectator status

#### ✨ Feature 7: Glowing Ring Preview
- **`/er preview`** - Toggle particle effects around rings
- Admin-only visibility
- Configurable particle type (default: VILLAGER_HAPPY) and count (default: 20)
- Helps with course design and validation
- Performance optimized (10 tick/500ms updates)
- Shows exact ring boundaries with particles

#### 🛡️ Feature 8: Soft Anti-Cheat Boundary System
- Warns players when going off-course
- Configurable boundary distance (default 50 blocks from last checkpoint)
- Progressive warning system (default: 3 warnings)
- Automatic teleport to last checkpoint after warnings exceeded
- Clear warning messages with warning count
- Resets warnings when passing new checkpoint

#### ⏱️ Feature 9: Auto-Finish Timer
- Configurable race time limit (default 180 seconds / 3 minutes)
- Automatic race end when timer expires
- Players still racing marked as DNF (Did Not Finish)
- Shown in join rules
- Clean race state cleanup
- Broadcast message when time expires

#### 🎒 Feature 10: Rocket Requirements System
- Configurable required rocket count (default 64)
- Validation before allowing `/ready`
- Checks elytra equipped in chestplate slot
- Validates empty inventory (except armor)
- Clear error messages for each requirement
- Shows current vs required rocket count

---

### Changed

#### Configuration Enhancements
```yaml
# NEW v1.1.0 config options added
race:
  required-rockets: 64          # NEW
  auto-finish-time: 180         # NEW

region-import:                   # NEW SECTION
  enabled: true
  prefix: "ring"
  auto-detect: true

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
- Added 6 new commands: `forcejoin`, `testmode`, `import`, `preview`, `platform`, `pb`
- Enhanced help menu with feature categories
- Improved tab completion for all commands
- Better error messages throughout
- Consistent command structure
- Added command aliases for convenience

#### Performance Optimizations
- Boundary checking cached (500ms intervals instead of every tick)
- Ring preview optimized particle rendering
- Reduced memory usage for large player counts
- Improved region checking performance with caching
- Pre-computed squared distances for ring detection
- Optimized movement checking (ignores head rotations)

#### User Experience Improvements
- **Startup Messages**: Now shows all 10 new features in console
- **Dependency Detection**: Visual startup warnings for missing plugins
- **Race Results**: Shows personal best indicators (PB!)
- **Statistics Display**: More detailed with PB information
- **Error Messages**: Clearer validation messages for all requirements
- **Join Experience**: Automatic rules display when entering start region

---

### Fixed

#### Critical Fixes
- **🐛 CRITICAL: NullPointerException on plugin load** - Fixed initialization order bug where `PersonalBestManager` tried to load stats before `statsConfig` was initialized
- **🐛 CRITICAL: Plugin failing to enable** - Moved `setupStatsFile()` to run before manager initialization

#### Race Management Fixes
- Race state not properly resetting on server restart
- Timer synchronization issues during countdown
- Region boundary detection edge cases
- Spectator mode permission conflicts
- Platform cleanup on race cancellation
- Memory leaks in long-running races

#### Performance Fixes
- Optimized region checking (now cached for 250ms)
- Fixed excessive particle spawning in preview mode
- Reduced CPU usage during active races
- Fixed memory leak in boundary checking system

---

### Technical Changes

#### New Classes
- `RegionImportManager.java` - Handles WorldGuard region import
- `StartingPlatformManager.java` - Manages starting platforms
- `PersonalBestManager.java` - Tracks personal bests
- Added null safety checks throughout

#### Updated Classes
- `ElytraRacePlugin.java` - **Fixed initialization order**, added dependency checking
- `ConfigManager.java` - 20+ new config options
- `RaceManager.java` - All 10 features integrated, boundary checking added
- `RaceCommand.java` - 6 new command handlers
- `ReadyCommand.java` - Rocket and inventory validation
- `RaceListener.java` - Boundary checking, optimized movement tracking
- `PersonalBestManager.java` - **Added null safety for config**

#### Initialization Order Fix
**Before (BROKEN):**
```java
configManager → statsManager → raceManager → setupStatsFile() ❌
```

**After (FIXED):**
```java
setupStatsFile() → configManager → statsManager → raceManager ✅
```

#### Dependencies
- Added soft dependency: **WorldGuard** (optional)
- Maintained requirement: **WorldEdit** (required)
- Both now properly checked with helpful error messages

---

### Security

- No security vulnerabilities fixed in this release
- All new features include proper permission checks
- Input validation added for all new commands
- Safe handling of player teleportation
- Protected admin-only features

---

### Deprecated

- **`/er join`** - Players now auto-join by entering start region (command still works but not needed)

---

### Known Issues

- None currently reported
- Please report any bugs at: https://github.com/Kartik-Fulara/ElytraRace/issues

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

---

## Version History Summary

| Version | Release Date | Type | Description | Status |
|---------|-------------|------|-------------|--------|
| **1.1.0** | 2025-01-XX | Major | Enhanced Systems (10 new features + critical fixes) | ✅ STABLE |
| **1.0.0** | 2025-11-22 | Initial | Core racing functionality | Legacy |

---

## Upgrade Guide

### From 1.0.x to 1.1.0

#### ✅ Automatic Migration
**No manual action required** - All existing data will be preserved automatically.

#### What Happens Automatically:
1. ✅ `config.yml` updated with new options (defaults added)
2. ✅ Existing statistics maintained and enhanced
3. ✅ Personal bests calculated from existing data
4. ✅ All rings preserved
5. ✅ No data loss

#### Steps to Upgrade:
```bash
# 1. Backup (RECOMMENDED)
cp -r plugins/ElytraRace ~/backups/ElytraRace-backup

# 2. Stop server
./stop.sh

# 3. Download new version
wget https://github.com/Kartik-Fulara/ElytraRace/releases/latest/download/ElytraRace.jar

# 4. Replace JAR
mv ElytraRace.jar plugins/

# 5. Start server
./start.sh

# 6. Check console for success message
tail -f logs/latest.log
```

#### New Features Available After Upgrade:
- Run `/er import rings` if using WorldGuard
- Configure new settings in `config.yml`
- Enable features you want to use
- Test with `/er testmode`

#### Breaking Changes:
**✅ NONE** - This release is 100% backward compatible!

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
| **1.1.x** | ✅ Current | All updates | Current + 1 minor |
| 1.0.x | ⚠️ Maintenance | Security only | 3 months after 1.1.0 |
| < 1.0 | ❌ Unsupported | None | End of Life |

---

## Links

- **[Latest Release](https://github.com/Kartik-Fulara/ElytraRace/releases/latest)**
- **[All Releases](https://github.com/Kartik-Fulara/ElytraRace/releases)**
- **[Compare v1.0.0...v1.1.0](https://github.com/Kartik-Fulara/ElytraRace/compare/v1.0.0...v1.1.0)**
- **[Milestones](https://github.com/Kartik-Fulara/ElytraRace/milestones)**
- **[Report Issues](https://github.com/Kartik-Fulara/ElytraRace/issues)**

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for:
- How to report bugs
- How to suggest features
- How to submit code changes
- Branch naming conventions

---

## Questions?

- 📖 **Documentation**: [docs/](docs/)
- 💬 **Discussions**: [GitHub Discussions](https://github.com/Kartik-Fulara/ElytraRace/discussions)
- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/Kartik-Fulara/ElytraRace/issues)
- 📧 **Email**: kartikfulara2003@gmail.com
- 💬 **Discord**: [Join Server](https://discord.gg/YOUR_INVITE)

---

## Special Thanks

Thanks to everyone who:
- 🐛 Reported bugs and issues
- 💡 Suggested features and improvements
- 🧪 Tested beta versions
- 📖 Improved documentation
- ⭐ Starred the repository

**Your feedback makes ElytraRace better!** 🚀

---

**Changelog v1.1.0**  
Last Updated: 2025-01-XX  
Maintained By: Kartik Fulara

[1.1.0]: https://github.com/Kartik-Fulara/ElytraRace/releases/tag/v1.1.0
[1.0.0]: https://github.com/Kartik-Fulara/ElytraRace/releases/tag/v1.0.0