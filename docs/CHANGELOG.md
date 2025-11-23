# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-11-22

### Added
- Initial release of ElytraRace plugin
- Automatic race start system with ready-up mechanic
- Per-player timer tracking
- Global race timer
- Ring detection system with configurable radius
- Comprehensive statistics system
  - Wins tracking
  - Total races played
  - Best time records
  - Average time calculation
  - Win rate percentage
- Leaderboard system (top 10 players)
- Full message customization via config.yml
- Admin commands for race management
- Player commands for race participation
- Persistent stats storage in stats.yml
- Dynamic ring addition/removal system
- Lobby spawn system
- Player disconnect handling
- Race timeout system (max 30 minutes default)
- Tab completion for all commands

### Commands Added
- `/er join` - Join race lobby
- `/ready` - Mark as ready
- `/er stats [player]` - View statistics
- `/er top` - View leaderboard
- `/er timer` - View race timer
- `/er progress` - View ring progress
- `/er rules` - View race rules
- `/er start` - Force start race (admin)
- `/er reset` - Reset race (admin)
- `/er setup lobby` - Set lobby location (admin)
- `/er setup addring <n>` - Add ring (admin)
- `/er setup removering <n>` - Remove ring (admin)
- `/er setup listrings` - List rings (admin)

### Configuration
- Customizable min/max players (default 2-5)
- Adjustable countdown duration (default 5 seconds)
- Configurable ready timeout (default 2 minutes)
- Customizable max race time (default 30 minutes)
- Full color code support in messages
- Ring detection radius (5 blocks default)

### Permissions
- `race.use` - Basic race commands (default: true)
- `race.admin` - Admin commands (default: op)
- `race.stats` - View statistics (default: true)
- `race.top` - View leaderboard (default: true)

### Technical
- Built for Minecraft 1.21.10+
- Paper/Spigot API compatibility
- Java 21+ required
- Maven build system
- Persistent data storage
- Event-driven architecture
- Thread-safe timer system

### Documentation
- Complete README.md
- Detailed SETUP_GUIDE.md
- Inline code documentation
- Configuration examples

## [Unreleased]

### Planned Features
- Multiple race track support
- Team races (2v2, 3v3)
- Spectator mode
- Race replay system
- Custom power-ups
- Economy plugin integration
- Web-based leaderboard
- Race checkpoints
- Practice mode
- Time trial mode
- Ghost racing (race against best time)
- Custom race modifiers
- Anti-cheat integration
- PlaceholderAPI support
- Holographic leaderboards
- Race rewards system

---

## Version History

### Version Format
- **Major.Minor.Patch** (e.g., 1.0.0)
- **Major**: Breaking changes
- **Minor**: New features (backwards compatible)
- **Patch**: Bug fixes (backwards compatible)

### Support Policy
- Latest version: Full support
- Previous minor: Security fixes only
- Older versions: No support (please upgrade)

---

## How to Update

1. Download the latest version from [Releases](../../releases)
2. Stop your server
3. Backup your `config.yml` and `stats.yml`
4. Replace the old JAR with the new one
5. Start your server
6. Check console for any migration messages

### Breaking Changes
None yet - this is the initial release!

---

[1.0.0]: https://github.com/yourKartik-Fulara/ElytraRace/releases/tag/v1.0.0
[Unreleased]: https://github.com/yourKartik-Fulara/ElytraRace/compare/v1.0.0...HEAD
