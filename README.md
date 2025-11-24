# ElytraRace

<div align="center">

![ElytraRace Banner](https://via.placeholder.com/800x200/4A90E2/FFFFFF?text=ElytraRace)

**A competitive elytra racing plugin for Minecraft Paper servers**

[![Build Status](https://img.shields.io/github/actions/workflow/status/Kartik-Fulara/ElytraRace/build.yml?branch=main)](https://github.com/Kartik-Fulara/ElytraRace/actions)
[![License](https://img.shields.io/github/license/Kartik-Fulara/ElytraRace)](LICENSE)
[![Version](https://img.shields.io/github/v/release/Kartik-Fulara/ElytraRace)](https://github.com/Kartik-Fulara/ElytraRace/releases)
[![Issues](https://img.shields.io/github/issues/Kartik-Fulara/ElytraRace)](https://github.com/Kartik-Fulara/ElytraRace/issues)
[![Discord](https://img.shields.io/discord/YOUR_DISCORD_ID?color=7289da&label=discord)](https://discord.gg/YOUR_INVITE)

[Features](#-features) • [Installation](#-quick-start) • [Commands](#-commands) • [Documentation](#-documentation) • [Contributing](#-contributing)

</div>

---

## 📖 Overview

ElytraRace transforms your Minecraft server into a competitive racing arena. Players navigate custom-designed courses, flying through sequential ring checkpoints while racing against the clock and each other. With built-in anti-cheat, comprehensive statistics tracking, and flexible course design tools, ElytraRace provides a complete racing experience.

### What Makes It Special

- **Intelligent Anti-Cheat**: Rocket limits, boundary checks, and order validation
- **Performance Optimized**: Cached calculations, minimal server impact
- **WorldGuard Integration**: Import existing regions as race courses instantly
- **Flexible Setup**: Manual placement or automated region import
- **Comprehensive Stats**: Personal bests, win rates, global leaderboards

---

## ✨ Features

### Core Racing System
- **Automatic Lobby Management** - Players join by entering start region
- **Ready-Check System** - Synchronized countdown with visual effects
- **Sequential Ring Navigation** - Must pass through checkpoints in order
- **Real-Time Timer** - Per-player and global race timing
- **Anti-Cheat Protection** - Prevents skipping, rocket abuse, and exploits

### NEW in v1.1.0
- 🚀 **Force-Join System** - Admins can teleport players to races
- 🗺️ **Region Import** - Auto-import WorldGuard regions as race rings
- 🎯 **Starting Platform** - Dramatic countdown with disappearing platforms
- 🧪 **Test Mode** - Admin testing without affecting statistics
- 🏆 **Personal Best Tracking** - Individual record keeping with rankings
- 👻 **Auto-Spectator** - Watch other racers after finishing
- ✨ **Ring Preview** - Visual particle effects for course design
- 🛡️ **Boundary System** - Prevents players from going off-course
- ⏱️ **Auto-Finish Timer** - Races end automatically after time limit
- 🎒 **Rocket Requirements** - Configurable validation before race start

### Statistics & Leaderboards
- **Personal Stats**: Wins, total races, best/average times, win rates
- **Global Leaderboards**: Top 10 rankings by multiple metrics
- **Personal Bests**: Individual fastest times with achievement dates
- **Persistent Storage**: All data saved between server restarts

### Anti-Cheat Features
- ✅ Ring skip detection with instant disqualification
- ✅ Rocket usage limits (configurable, default 3 per race)
- ✅ Backward navigation prevention
- ✅ Off-course boundary warnings and teleportation
- ✅ Disconnect handling (automatic DNF status)

---

## 📋 Requirements

| Requirement | Version | Status |
|------------|---------|--------|
| **Server** | Paper 1.21.4+ | Required |
| **Java** | Java 21+ | Required |
| **WorldEdit** | 7.3.3+ | Required |
| **WorldGuard** | 7.0.13+ | Optional* |

\* *Required for region import feature*

---

## 🚀 Quick Start

### Installation

1. **Download** the latest release
   ```bash
   # From GitHub Releases
   wget https://github.com/Kartik-Fulara/ElytraRace/releases/latest/download/ElytraRace.jar
   ```

2. **Install** the plugin
   ```bash
   # Place in your server's plugins folder
   mv ElytraRace.jar /path/to/server/plugins/
   ```

3. **Start** your server
   ```bash
   # The plugin will generate default configuration
   java -Xmx4G -jar paper.jar
   ```

4. **Configure** your first race
   ```bash
   # In-game as admin
   /er setup lobby              # Set lobby spawn
   /er setup start              # Define start region (WorldEdit)
   /er setup finish             # Define finish region (WorldEdit)
   /er import rings             # Import rings from WorldGuard
   # OR
   /er setup addring ring1      # Manually add rings
   ```

### Quick Setup Example

```bash
# Complete setup in 5 commands
/er setup lobby
/er setup start       # After making WorldEdit selection
/er setup finish      # After making WorldEdit selection
/er import rings      # Auto-imports ring1, ring2, etc.
/er preview          # Visualize the course
```

See the [Installation Guide](docs/INSTALLATION.md) for detailed instructions.

---

## 💻 Commands

### Player Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/er rules` | Display race rules | `race.use` |
| `/er join` | Join race lobby | `race.use` |
| `/ready` | Toggle ready status | `race.use` |
| `/er stats [player]` | View statistics | `race.stats` |
| `/er pb [player]` | View personal best | `race.use` |
| `/er top` | View leaderboard | `race.use` |
| `/er progress` | Check ring progress | `race.use` |
| `/er timer` | View race time | `race.use` |

### Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/er forcejoin <player>` | Force player into race | `race.admin` |
| `/er testmode` | Toggle test mode | `race.admin` |
| `/er import rings` | Import WorldGuard regions | `race.admin` |
| `/er preview` | Toggle ring preview | `race.admin` |
| `/er platform <create\|remove>` | Manage platforms | `race.admin` |
| `/er setup lobby` | Set lobby location | `race.admin` |
| `/er setup start` | Define start region | `race.admin` |
| `/er setup finish` | Define finish region | `race.admin` |
| `/er start` | Force start race | `race.admin` |
| `/er reset` | Reset active race | `race.admin` |

Full command reference: [COMMANDS.md](docs/COMMANDS.md)

---

## ⚙️ Configuration

### Basic Configuration

```yaml
race:
  min-players: 2
  max-players: 5
  required-rockets: 64
  auto-finish-time: 180

region-import:
  enabled: true
  prefix: "ring"

anti-cheat:
  boundary-distance: 50
  teleport-on-exceed: true
  warnings-before-teleport: 3

spectator:
  auto-enable: true
  return-to-lobby: true
  delay-seconds: 3
```

See [Configuration Guide](docs/CONFIGURATION.md) for all options.

---

## 📚 Documentation

- **[Installation Guide](docs/INSTALLATION.md)** - Complete setup instructions
- **[Commands Reference](docs/COMMANDS.md)** - Detailed command documentation
- **[Configuration Guide](docs/CONFIGURATION.md)** - All configuration options
- **[WorldEdit Integration](docs/WORLDEDIT.md)** - Region setup guide
- **[API Documentation](docs/API.md)** - For developers
- **[Changelog](docs/CHANGELOG.md)** - Version history

---

## 🎮 How It Works

```
┌─────────────┐
│   Player    │
│  Enters     │──▶ Joins Lobby
│  Region     │
└─────────────┘
       │
       ▼
┌─────────────┐
│   Types     │
│  /ready     │──▶ Ready Status
└─────────────┘
       │
       ▼
┌─────────────┐
│ All Ready?  │──▶ Countdown: 3, 2, 1, READY, GO!
└─────────────┘
       │
       ▼
┌─────────────┐
│   Race      │──▶ Fly through rings in order
│   Starts    │    Timer starts
└─────────────┘
       │
       ▼
┌─────────────┐
│  Complete   │──▶ Enter finish region
│  All Rings  │    Time recorded
└─────────────┘
       │
       ▼
┌─────────────┐
│  Results &  │──▶ Stats updated
│ Leaderboard │    Personal best checked
└─────────────┘
```

---

## 🏗️ Building from Source

```bash
# Clone the repository
git clone https://github.com/Kartik-Fulara/ElytraRace.git
cd ElytraRace

# Build with Maven
mvn clean package

# Output: target/ElytraRace-1.1.0.jar
```

### Requirements for Building
- Maven 3.8+
- Java 21 JDK
- Git

---

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md).

### Quick Contribution Guide

1. **Fork** the repository
2. **Create** a branch: `feature/123-your-feature`
3. **Commit** your changes with clear messages
4. **Test** thoroughly on Paper 1.21.4+
5. **Submit** a pull request to `develop` branch

### Branch Naming Convention

```
feature/   - New features
bugfix/    - Bug fixes
hotfix/    - Critical fixes
docs/      - Documentation
refactor/  - Code improvements
test/      - Test additions
```

---

## 🐛 Bug Reports & Feature Requests

- **Bug Reports**: [Open an Issue](https://github.com/Kartik-Fulara/ElytraRace/issues/new?template=bug_report.md)
- **Feature Requests**: [Start a Discussion](https://github.com/Kartik-Fulara/ElytraRace/discussions/new?category=ideas)
- **Questions**: [GitHub Discussions](https://github.com/Kartik-Fulara/ElytraRace/discussions)

---

## 🔒 Security

Found a security vulnerability? Please report it privately to **kartikfulara2003@gmail.com** instead of opening a public issue.

See [SECURITY.md](SECURITY.md) for our security policy.

---

## 📊 Statistics

<div align="center">

![GitHub stars](https://img.shields.io/github/stars/Kartik-Fulara/ElytraRace?style=social)
![GitHub forks](https://img.shields.io/github/forks/Kartik-Fulara/ElytraRace?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/Kartik-Fulara/ElytraRace?style=social)

</div>

---

## 📜 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

### What This Means
- ✅ Commercial use allowed
- ✅ Modification allowed
- ✅ Distribution allowed
- ✅ Private use allowed
- ⚠️ Liability and warranty not provided

---

## 🙏 Acknowledgments

### Contributors
- **Kartik Fulara** - *Creator & Lead Developer* - [@Kartik-Fulara](https://github.com/Kartik-Fulara)
- See full list: [Contributors](https://github.com/Kartik-Fulara/ElytraRace/graphs/contributors)

### Dependencies
- [Paper](https://papermc.io/) - High-performance Minecraft server
- [WorldEdit](https://enginehub.org/worldedit) - Region editing
- [WorldGuard](https://enginehub.org/worldguard) - Region protection

### Special Thanks
- Paper development team
- WorldEdit/WorldGuard contributors
- Community beta testers
- All contributors and supporters

---

## 📞 Support & Community

- 💬 **Discord**: [Join our server](https://discord.gg/YOUR_INVITE)
- 📖 **Wiki**: [Documentation](https://github.com/Kartik-Fulara/ElytraRace/wiki)
- 🐛 **Issues**: [Report bugs](https://github.com/Kartik-Fulara/ElytraRace/issues)
- 💡 **Discussions**: [Feature requests](https://github.com/Kartik-Fulara/ElytraRace/discussions)
- 📧 **Email**: kartikfulara2003@gmail.com

---

## 🗺️ Roadmap

### v1.2.0 (Planned)
- [ ] Team racing mode
- [ ] Economy integration (Vault)
- [ ] Custom particle effects
- [ ] Race replays
- [ ] PlaceholderAPI support

### v1.3.0 (Future)
- [ ] Multiple race tracks
- [ ] Tournament system
- [ ] Custom cosmetics
- [ ] Advanced statistics dashboard
- [ ] MySQL database support

See [Milestones](https://github.com/Kartik-Fulara/ElytraRace/milestones) for detailed roadmap.

---

<div align="center">

**Built with ❤️ for the Minecraft community**

[⬆ Back to Top](#elytrarace)

</div>