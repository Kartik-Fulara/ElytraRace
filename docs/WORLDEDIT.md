# Changelog

All notable changes to ElytraRace are documented here.

Format based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).  
Versioning follows [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2025-11-22

Initial release of ElytraRace.

### Added

**Core Racing Features**
- Automatic race joining by entering start region
- Ready-check system with player countdown
- Ring detection with 5-block radius
- Order enforcement for rings (must be completed sequentially)
- Finish region detection
- Race timer system (per-player and global)

**Anti-Cheat System**
- Rocket usage tracking (max 3 per race)
- Ring skip detection
- Disqualification system with reasons
- Disconnect handling (automatic DNF)
- Race timeout (default 30 minutes)

**Statistics & Leaderboards**
- Win tracking
- Total races played counter
- Best time records
- Average time calculation
- Win rate percentage
- Top 10 leaderboard

**Commands**
- `/er rules` - Display race rules
- `/er stats [player]` - View statistics
- `/er top` - View leaderboard
- `/er timer` - View race time
- `/er progress` - View current progress
- `/er start` - Force start race (admin)
- `/er reset` - Reset race (admin)
- `/er setup lobby` - Set lobby location
- `/er setup start` - Define start region via WorldEdit
- `/er setup finish` - Define finish region via WorldEdit
- `/er setup addring <n>` - Add ring at location
- `/er setup addringwe <n>` - Add ring from WorldEdit selection
- `/er setup removering <n>` - Remove ring
- `/er listrings` - List configured rings
- `/ready` - Toggle ready status

**Configuration**
- Customizable min/max players (default 2-5)
- Adjustable countdown duration (default 5 seconds)
- Configurable ready timeout (default 2 minutes)
- Customizable max race time (default 30 minutes)
- Full message customization via config.yml
- Color code support in all messages
- Ring detection radius configuration

**Permissions**
- `race.use` - Basic race commands (default: true)
- `race.admin` - Admin commands (default: op)
- `race.stats` - View statistics (default: true)
- `race.top` - View leaderboard (default: true)

**Technical Features**
- WorldEdit integration for region setup
- Persistent data storage (YAML-based)
- Event-driven architecture
- Optimized ring detection with caching
- Thread-safe timer system
- Automatic region checking with 250ms cache
- Pre-computed squared distances for performance

**Documentation**
- Complete README with setup guide
- Detailed command reference (COMMANDS.md)
- Installation guide (INSTALLATION.md)
- WorldEdit integration guide (WORLDEDIT.md)
- Contributing guidelines (CONTRIBUTING.md)
- Security policy (SECURITY.md)
- Inline code documentation

### Technical Details

**Built for:**
- Minecraft 1.21.4+
- Paper/Spigot API
- Java 21+

**Dependencies:**
- WorldEdit 7.3.3+ (required)
- VoiidCountdownTimer (optional, auto-detected)

**Performance:**
- Optimized movement checking (ignores head turns)
- Cached region checks (5 tick interval)
- Pre-computed ring distances
- Async-compatible
- Minimal CPU usage (~2-4% per player)

---

## [Unreleased]

Features planned for future releases:

### Planned Features
- Multiple race track support
- Spectator mode
- Race rewards/economy integration
- PlaceholderAPI support
- Checkpoint system for long races
- Backward ring detection enhancement
- Race replays
- Custom particle effects
- Sound effect customization

### Under Consideration
- Multiple difficulty modes
- Team races
- Time trial mode
- Ghost racers (replay of best times)
- Custom elytra cosmetics

---

## Version History

### Version Format

**Major.Minor.Patch** (e.g., 1.0.0)

- **Major**: Breaking changes or major feature additions
- **Minor**: New features (backward compatible)
- **Patch**: Bug fixes (backward compatible)

### Support Policy

- **Latest version**: Full support and updates
- **Previous minor**: Security fixes only
- **Older versions**: Please upgrade

---

## Upgrading

### From Pre-1.0 to 1.0.0

This is the initial release - no upgrade path needed.

### General Upgrade Steps

1. Download new version from [Releases](https://github.com/Kartik-Fulara/ElytraRace/releases)
2. Stop your server
3. Backup `plugins/ElytraRace/` folder
4. Replace old JAR with new version
5. Start server
6. Check console for migration messages
7. Test functionality

**Note**: Always backup before updating!

---

## Breaking Changes

None yet - this is the initial release.

---

## Links

- [1.0.0 Release](https://github.com/Kartik-Fulara/ElytraRace/releases/tag/v1.0.0)
- [Unreleased Changes](https://github.com/Kartik-Fulara/ElytraRace/compare/v1.0.0...HEAD)
- [All Releases](https://github.com/Kartik-Fulara/ElytraRace/releases)

---

## Contributing

Found a bug? Want to suggest a feature? See [CONTRIBUTING.md](CONTRIBUTING.md) for how to contribute.