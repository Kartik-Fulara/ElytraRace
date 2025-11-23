# ElytraRace

A competitive elytra racing plugin for Minecraft Paper servers. Race through rings, beat the clock, and climb the leaderboard.

[![Build Status](https://github.com/Kartik-Fulara/ElytraRace/workflows/Build%20and%20Deploy/badge.svg)](https://github.com/Kartik-Fulara/ElytraRace/actions)
[![License](https://img.shields.io/github/license/Kartik-Fulara/ElytraRace)](LICENSE)
[![Issues](https://img.shields.io/github/issues/Kartik-Fulara/ElytraRace)](https://github.com/Kartik-Fulara/ElytraRace/issues)

## What It Does

ElytraRace lets you create competitive elytra racing on your server. Players fly through rings in order, their times are tracked, and the fastest racers make it onto the leaderboard. It's straightforward to set up and includes anti-cheat features to keep races fair.

**Key Features:**
- Automatic race joining when players enter the start region
- Ready-check system with countdown
- Ring detection with order enforcement
- Anti-cheat: rocket limits (max 3 per race), ring skip detection
- Real-time statistics and leaderboards
- WorldEdit integration for easy setup

## Requirements

- **Server**: Paper 1.21.4+
- **Java**: Java 21+
- **Dependencies**: WorldEdit 7.3.3+ (required for region setup)

## Quick Start

1. Download the latest release from [Releases](https://github.com/Kartik-Fulara/ElytraRace/releases)
2. Drop `ElytraRace.jar` into your `plugins/` folder
3. Restart your server
4. Set up your race track (see [Installation Guide](docs/INSTALLATION.md))

## Basic Commands

**Players:**
```
/er rules          - View race rules
/er stats [player] - Check race statistics
/er top            - View leaderboard
/er progress       - Check your current progress
/er timer          - View race time
/ready             - Toggle ready status
```

**Admins:**
```
/er setup lobby     - Set lobby spawn point
/er setup start     - Define start region (requires WorldEdit selection)
/er setup finish    - Define finish region (requires WorldEdit selection)
/er setup addring <name>     - Add ring at current location
/er setup addringwe <name>   - Add ring from WorldEdit selection
/er setup removering <name>  - Remove a ring
/er listrings       - List all configured rings
/er start           - Force start the race
/er reset           - Reset current race
```

See [COMMANDS.md](docs/COMMANDS.md) for detailed command documentation.

## How It Works

1. **Setup**: Admin creates start/finish regions and places rings using WorldEdit
2. **Joining**: Players walk into the start region to join the lobby
3. **Ready Up**: Players use `/ready` when they're prepared to race
4. **Countdown**: When all players are ready, a 5-second countdown begins
5. **Race**: Players fly through rings in order - skip one and you're disqualified
6. **Finish**: Complete all rings, then fly through the finish region
7. **Results**: Times are recorded, stats updated, and winners announced

## Documentation

- [Installation & Setup](docs/INSTALLATION.md) - Get the plugin running
- [Commands Reference](docs/COMMANDS.md) - Complete command list
- [Configuration Guide](docs/CONFIGURATION.md) - Customize settings
- [WorldEdit Integration](docs/WORLDEDIT.md) - Setup guide with WorldEdit
- [Contributing Guidelines](CONTRIBUTING.md) - Help improve the plugin
- [Changelog](docs/CHANGELOG.md) - Version history

## Contributing

Found a bug? Want to add a feature? Contributions are welcome. Please read [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on:
- Branch naming conventions
- Pull request process
- Code standards
- How to report issues

## Security

Found a security vulnerability? Please report it privately to **kartikfulara2003@gmail.com** instead of opening a public issue. See [SECURITY.md](SECURITY.md) for details.

## License

This project is licensed under the MIT License - see [LICENSE](LICENSE) for details.

## Support

- **Bug Reports**: [Open an Issue](https://github.com/Kartik-Fulara/ElytraRace/issues)
- **Feature Requests**: [Start a Discussion](https://github.com/Kartik-Fulara/ElytraRace/discussions)
- **Questions**: [GitHub Discussions](https://github.com/Kartik-Fulara/ElytraRace/discussions)
- **Security Issues**: kartikfulara2003@gmail.com

## Credits

**Developer**: [Kartik Fulara](https://github.com/Kartik-Fulara)  
**Contributors**: See [Contributors](https://github.com/Kartik-Fulara/ElytraRace/graphs/contributors)

---

Built for Minecraft Paper servers • MIT License • [Report Issues](https://github.com/Kartik-Fulara/ElytraRace/issues)