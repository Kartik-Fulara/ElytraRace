# Plugin Integrations & Publishing Guide

Guide for integrating ElytraRace with other plugins and publishing to plugin marketplaces.

---

## Publishing Platforms

### Understanding Plugin Marketplaces

**Important**: ElytraRace is a Bukkit/Spigot/Paper plugin, not a Forge/Fabric mod. This affects where you can publish.

#### CurseForge
- **Status**: ❌ Not Recommended
- **Why**: Primarily hosts Forge/Fabric mods
- **Bukkit Support**: Limited and not promoted
- **Better Alternative**: Use SpigotMC or Modrinth instead

#### Modrinth
- **Status**: ✅ Recommended
- **Plugin Support**: Yes, actively supported
- **Requirements**: Open source recommended, clear documentation
- **Benefits**: Modern platform, growing community

---

## Recommended Publishing Platforms

### 1. SpigotMC Resources

The largest Bukkit/Spigot community. Best for maximum visibility.

**Setup Process**:

1. Create account at [SpigotMC.org](https://www.spigotmc.org/)
2. Go to Resources → Manage Resources → Post Resource
3. Fill in details:
   - **Name**: ElytraRace
   - **Tagline**: Competitive elytra racing with timers and leaderboards
   - **Category**: Minigames
   - **Price**: Free
   - **Versions**: 1.21.4+

**Required Files**:
- `ElytraRace-1.0.0.jar`
- Screenshots (minimum 3, recommended 5+)
- Icon/Logo (256x256 PNG)
- Description (converted to BBCode)

**BBCode Example**:
```bbcode
[CENTER][SIZE=6][B][COLOR=#FFD700]ElytraRace[/COLOR][/B][/SIZE]
[I]Competitive Elytra Racing for Paper Servers[/I][/CENTER]

[SIZE=5][B]Features[/B][/SIZE]
[LIST]
[*]Automatic race joining via start region
[*]Ready-check system with countdown
[*]Ring detection with order enforcement
[*]Anti-cheat: rocket limits and skip detection
[*]Statistics and leaderboard system
[*]WorldEdit integration
[/LIST]

[SIZE=5][B]Commands[/B][/SIZE]
[CODE]/er rules - View race rules
/er stats - View your statistics
/er top - View leaderboard
/ready - Toggle ready status[/CODE]

[SIZE=5][B]Requirements[/B][/SIZE]
• Paper 1.21.4+
• Java 21+
• WorldEdit 7.3.3+
```

---

### 2. Modrinth

Modern platform with good plugin support.

**Setup Process**:

1. Create account at [Modrinth.com](https://modrinth.com/)
2. Click "Create a Project"
3. Select "Plugin" as project type

**Project Configuration**:
```yaml
Name: ElytraRace
Summary: Competitive elytra racing with rings, timers, and anti-cheat
Description: [Use Markdown from README.md]
  
Categories:
  - Minigames
  - Adventure
  
Loaders:
  - Paper
  - Spigot
  - Purpur
  
Game Versions:
  - 1.21.4
  
License: MIT
```

**Version Upload**:
```yaml
Version Number: 1.0.0
Version Title: Initial Release
Changelog: See CHANGELOG.md
Dependencies:
  - WorldEdit (required)
  - VoiidCountdownTimer (optional)
```

---

### 3. Hangar (PaperMC Official)

Official Paper plugin repository.

**Setup Process**:

1. Create account at [Hangar.papermc.io](https://hangar.papermc.io/)
2. Create new project

**Configuration**:
```yaml
Name: ElytraRace
Description: Competitive elytra racing with anti-cheat and statistics
Category: Minigames
License: MIT
  
Platform: Paper 1.21.4+
  
Links:
  Source: https://github.com/Kartik-Fulara/ElytraRace
  Issues: https://github.com/Kartik-Fulara/ElytraRace/issues
  Documentation: https://github.com/Kartik-Fulara/ElytraRace/docs
```

---

### 4. Bukkit Dev (Legacy)

Older platform, less active but still used.

**Status**: Lower priority, but available for legacy servers.

---

## Plugin Integrations

### WorldEdit (Already Integrated)

Used for region selection and setup.

**Maven Dependency**:
```xml
<dependency>
    <groupId>com.sk89q.worldedit</groupId>
    <artifactId>worldedit-bukkit</artifactId>
    <version>7.3.3</version>
    <scope>provided</scope>
</dependency>
```

**Usage in Plugin**:
- Region selection for start/finish areas
- Ring placement from selection center
- Zone definition

---

### PlaceholderAPI (Future Integration)

For displaying race stats in scoreboards, tab lists, etc.

**Maven Dependency**:
```xml
<dependency>
    <groupId>me.clip</groupId>
    <artifactId>placeholderapi</artifactId>
    <version>2.11.5</version>
    <scope>provided</scope>
</dependency>
```

**Proposed Placeholders**:
```
%elytrarace_wins%       - Player's total wins
%elytrarace_races%      - Total races completed
%elytrarace_best_time%  - Best race time
%elytrarace_avg_time%   - Average race time
%elytrarace_win_rate%   - Win percentage
%elytrarace_rank%       - Leaderboard position
```

**Implementation Example**:
```java
public class RacePlaceholders extends PlaceholderExpansion {
    
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";
        
        StatsManager.PlayerStats stats = plugin.getStatsManager()
            .getStats(player.getUniqueId());
        
        return switch (identifier) {
            case "wins" -> String.valueOf(stats.getWins());
            case "races" -> String.valueOf(stats.getRaces());
            case "best_time" -> String.format("%.2f", stats.getBestTime());
            case "win_rate" -> String.format("%.1f%%", stats.getWinRate());
            default -> null;
        };
    }
}
```

---

### Vault (Economy Integration)

Add entry fees and win rewards.

**Maven Dependency**:
```xml
<dependency>
    <groupId>net.milkbowl.vault</groupId>
    <artifactId>VaultAPI</artifactId>
    <version>1.7</version>
    <scope>provided</scope>
</dependency>
```

**Proposed Features**:
- Entry fee to join races
- Prize money for winners
- Betting system (spectators)
- Sponsor rewards

**Config Example**:
```yaml
economy:
  enabled: true
  entry-fee: 100
  prizes:
    first: 500
    second: 250
    third: 100
```

---

### LuckPerms (Permission Management)

Already compatible. Works out of the box with permission nodes:

```
race.use        - Basic commands
race.admin      - Setup commands
race.stats      - View statistics
race.top        - View leaderboard
```

**Setup**:
```bash
/lp group default permission set race.use true
/lp group admin permission set race.admin true
```

---

### Discord Integration

Send race results to Discord via webhooks.

**Implementation Example**:
```java
public class DiscordNotifier {
    private final String webhookUrl;
    
    public void postRaceResults(String winner, double time) {
        JSONObject embed = new JSONObject()
            .put("title", "Race Completed!")
            .put("description", winner + " won in " + time + " seconds")
            .put("color", 0xFFD700);
        
        JSONObject payload = new JSONObject()
            .put("embeds", new JSONArray().put(embed));
        
        // Send POST request to webhookUrl
    }
}
```

**Config**:
```yaml
discord:
  enabled: true
  webhook-url: "https://discord.com/api/webhooks/..."
  post-results: true
  post-records: true
  mention-role: "123456789"  # Role ID to mention
```

---

### Multiverse-Core (Multi-World Support)

Already compatible. Each world can have its own race track.

No additional code needed - the plugin already stores world names with locations.

---

## Publishing Checklist

Before you publish:

### Testing
- [ ] Plugin loads without errors
- [ ] All commands work correctly
- [ ] Regions detect properly
- [ ] Rings register correctly
- [ ] Statistics save and load
- [ ] No memory leaks after extended use
- [ ] Tested with 2-10 players simultaneously

### Documentation
- [ ] README is clear and complete
- [ ] All commands documented
- [ ] Setup guide tested by someone else
- [ ] Screenshots taken (5+ high quality)
- [ ] Configuration examples provided

### Assets
- [ ] Plugin JAR built and tested
- [ ] Icon/Logo created (256x256)
- [ ] Screenshots prepared (1920x1080)
- [ ] Banner image created (1920x400)
- [ ] Demo video recorded (optional but recommended)

### Legal
- [ ] LICENSE file included (MIT)
- [ ] Copyright headers in all files
- [ ] No plagiarized code
- [ ] Dependencies properly attributed

---

## Release Notes Template

Use this format for releases:

```markdown
## Version 1.0.0 - Initial Release

### Features
- Automatic race joining via start region
- Ready check system with countdown
- Ring detection with order enforcement
- Anti-cheat: rocket limits and skip detection
- Statistics and leaderboard system
- WorldEdit integration for setup

### Anti-Cheat
- Ring skip detection
- Rocket limit enforcement (max 3)
- Disconnect handling (automatic DNF)
- Ring order validation

### Performance
- Optimized movement checking
- Cached region checks (250ms interval)
- Pre-computed ring distances
- Minimal CPU impact (~2-4% per player)

### Known Issues
- None currently reported

### Requirements
- Paper 1.21.4+
- Java 21+
- WorldEdit 7.3.3+
```

---

## Marketing Strategy

### GitHub
- Detailed README with examples
- Complete documentation
- Active issue tracker
- Regular updates
- Clear contribution guidelines

### Social Media

**Reddit**:
- r/admincraft (server admin community)
- r/minecraft (general)

**Discord**:
- Minecraft plugin development servers
- Paper community
- Server admin communities

**YouTube**:
- Setup tutorial
- Race demonstration
- Server showcase

**Twitter/X**:
- Release announcements
- Development updates
- Community highlights

---

## Analytics

### bStats Integration

Track plugin usage statistics:

**Maven Dependency**:
```xml
<dependency>
    <groupId>org.bstats</groupId>
    <artifactId>bstats-bukkit</artifactId>
    <version>3.0.2</version>
    <scope>compile</scope>
</dependency>
```

**Implementation**:
```java
@Override
public void onEnable() {
    int pluginId = 12345; // Get from bStats
    Metrics metrics = new Metrics(this, pluginId);
}
```

**Metrics Tracked**:
- Server count
- Player count
- Plugin version distribution
- Server software (Paper/Spigot/Purpur)

---

## License Considerations

### MIT License (Recommended)

**Pros**:
- Very permissive
- Allows commercial use
- Simple and clear
- Widely recognized

**Cons**:
- Others can fork without contributing back
- No patent protection

### GPL v3

**Pros**:
- Copyleft - forks must remain open source
- Patent protection
- Community-focused

**Cons**:
- More restrictive
- May limit adoption
- Requires derivative works to use GPL

### Current License

ElytraRace uses MIT License - good choice for maximum adoption and flexibility.

---

## Launch Timeline

### Week Before Launch
- [ ] Finalize documentation
- [ ] Create demo video
- [ ] Prepare press kit
- [ ] Line up beta testers
- [ ] Pre-announce on social media

### Launch Day
- [ ] Publish to all platforms simultaneously
- [ ] Post to Reddit r/admincraft
- [ ] Share on Discord servers
- [ ] Release demo video
- [ ] Respond to initial feedback

### Week After Launch
- [ ] Monitor feedback closely
- [ ] Fix critical bugs quickly
- [ ] Release hotfix if needed
- [ ] Thank early adopters
- [ ] Gather feature requests

---

## Support Resources

### Documentation Sites
- [SpigotMC Wiki](https://www.spigotmc.org/wiki/)
- [Paper Documentation](https://docs.papermc.io/)
- [Modrinth Docs](https://docs.modrinth.com/)

### Communities
- SpigotMC Discord
- PaperMC Discord
- r/admincraft subreddit
- Minecraft Plugin Development Discord

---

## Next Steps

1. Finish any remaining features
2. Complete all testing
3. Prepare assets (logo, screenshots, video)
4. Write final documentation
5. Choose publishing platforms
6. Create accounts on chosen platforms
7. Prepare launch announcement
8. Publish!

Good luck with your release!