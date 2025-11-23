# 🔌 Plugin Integrations & Publishing Guide

This guide explains how to integrate with other plugins and publish ElytraRace to plugin marketplaces.

---

## 📦 Publishing Platforms

### ⚠️ CurseForge & Modrinth Clarification

**Important:** ElytraRace is a **Bukkit/Spigot/Paper plugin**, NOT a Forge/Fabric mod.

#### CurseForge
- **Can you publish?** ❌ **NO** - CurseForge primarily hosts mods (Forge/Fabric)
- **Bukkit Plugins:** CurseForge has limited support for Bukkit plugins (they have a "Bukkit Plugins" category, but it's not actively promoted)
- **Recommendation:** Use SpigotMC, Hangar, or Modrinth instead

#### Modrinth
- **Can you publish?** ✅ **YES** - Modrinth now supports plugins!
- **Requirements:**
  - Plugin must be open source (recommended)
  - Follow Modrinth's content policy
  - Provide clear documentation
  - Include proper versioning

---

## 🚀 Recommended Publishing Platforms

### 1. SpigotMC Resources (Primary)
**Best for:** Maximum visibility, largest Bukkit/Spigot community

#### Setup:
1. Create account at [SpigotMC.org](https://www.spigotmc.org/)
2. Go to **Resources** → **Manage Resources** → **Post Resource**
3. Fill in resource information:
   - **Name:** ElytraRace
   - **Tag line:** Competitive Elytra Racing with Timers & Leaderboards
   - **Category:** Minigames
   - **Price:** Free
   - **Supported versions:** 1.21+

#### Required Files:
```
ElytraRace-1.0.0.jar
README.md (converted to BBCode)
Screenshots (at least 3)
Icon/Logo (256x256)
```

#### SpigotMC BBCode Format:
```bbcode
[CENTER][SIZE=6][B][COLOR=#FFD700]ElytraRace[/COLOR][/B][/SIZE]
[I]Competitive Elytra Racing Plugin[/I][/CENTER]

[SIZE=5][B]Features[/B][/SIZE]
[LIST]
[*]Automatic join by walking into start region
[*]Ready check system with visual countdown
[*]Ring detection with order enforcement
[*]Anti-cheat: rocket limits, ring skipping detection
[*]Real-time stats and leaderboards
[*]WorldEdit integration for easy setup
[/LIST]

[SIZE=5][B]Commands[/B][/SIZE]
[CODE]/er rules - View race rules
/er stats - View your statistics
/er top - View leaderboard
/ready - Toggle ready status[/CODE]
```

---

### 2. Modrinth (Recommended)
**Best for:** Modern platform, plugin support, open-source friendly

#### Setup:
1. Create account at [Modrinth.com](https://modrinth.com/)
2. Click **Create a Project**
3. Select **Plugin** as project type
4. Configure project:

```yaml
Project Information:
  Name: ElytraRace
  Summary: Competitive multiplayer Elytra racing with rings, timers, and strict rules
  Description: [Use Markdown from README.md]
  
Categories:
  - Minigames
  - Adventure
  
Supported Loaders:
  - Paper
  - Spigot
  - Purpur
  
Supported Versions:
  - 1.21
  - 1.21.1
```

#### Create Version:
```bash
# Upload file
ElytraRace-1.0.0.jar

# Version Information
Version Number: 1.0.0
Version Title: Initial Release
Changelog: See CHANGELOG.md

# Dependencies
Required: WorldEdit
```

---

### 3. Hangar (PaperMC Official)
**Best for:** Paper server owners, official Paper platform

#### Setup:
1. Create account at [Hangar.papermc.io](https://hangar.papermc.io/)
2. Create new project
3. Configure settings:

```yaml
Project Details:
  Name: ElytraRace
  Description: Competitive Elytra racing plugin with anti-cheat
  Category: Minigames
  License: MIT
  
Platform Support:
  - Paper (1.21+)
  
Links:
  Source: https://github.com/yourKartik-Fulara/ElytraRace
  Issues: https://github.com/yourKartik-Fulara/ElytraRace/issues
  Discord: https://discord.gg/yourserver
```

---

### 4. Bukkit Dev (Legacy)
**Best for:** Legacy servers, older versions

**Note:** Bukkit Dev is less active but still used by some server owners.

---

## 🔗 Plugin Integrations

### WorldEdit (Required)
Already integrated! Used for region selection.

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.sk89q.worldedit</groupId>
    <artifactId>worldedit-bukkit</artifactId>
    <version>7.2.15</version>
    <scope>provided</scope>
</dependency>
```

---

### PlaceholderAPI (Planned Integration)

#### Implementation:
```java
// Add to pom.xml
<dependency>
    <groupId>me.clip</groupId>
    <artifactId>placeholderapi</artifactId>
    <version>2.11.5</version>
    <scope>provided</scope>
</dependency>
```

#### Placeholders:
```
%elytrarace_wins% - Player's total wins
%elytrarace_races% - Player's total races
%elytrarace_best_time% - Player's best time
%elytrarace_win_rate% - Player's win rate
%elytrarace_rank% - Player's leaderboard rank
```

---

### Vault (Economy Integration)

#### Planned Features:
- Entry fees
- Win rewards
- Sponsor rewards

```xml
<dependency>
    <groupId>net.milkbowl.vault</groupId>
    <artifactId>VaultAPI</artifactId>
    <version>1.7</version>
    <scope>provided</scope>
</dependency>
```

---

### LuckPerms (Permissions)

Works out of the box! Supports these permissions:
```yaml
race.use - Basic player commands
race.admin - Admin commands
```

---

### Discord Integration

#### Via Discord Webhooks:
```java
// Post race results to Discord
public void postToDiscord(String message) {
    String webhookUrl = config.getString("discord.webhook");
    // Send HTTP POST with JSON payload
}
```

#### Example Config:
```yaml
discord:
  enabled: true
  webhook: "https://discord.com/api/webhooks/..."
  post-results: true
  post-records: true
```

---

### MultiVerse-Core (Multi-World Support)

Already compatible! Each world can have its own race track.

---

## 📋 Publishing Checklist

### Before Publishing:
- [ ] Plugin thoroughly tested
- [ ] All features working correctly
- [ ] Documentation complete (README, Setup Guide)
- [ ] Screenshots taken (at least 5)
- [ ] Video demo recorded (recommended)
- [ ] License file included (MIT recommended)
- [ ] Source code hosted on GitHub
- [ ] Version number finalized
- [ ] Dependencies documented
- [ ] Performance tested (no lag)

### Required Assets:

#### 1. Plugin JAR
```bash
# Build final release
mvn clean package

# Verify JAR works
java -jar target/ElytraRace-1.0.0.jar
```

#### 2. Logo/Icon (256x256 PNG)
```
Background: Transparent
Content: Elytra wings + racing flag
Colors: Gold/Yellow theme
Format: PNG with transparency
```

#### 3. Screenshots (1920x1080)
Required shots:
1. Start region with players ready
2. Visual countdown in action
3. Flying through rings
4. Finish screen with results
5. Leaderboard display
6. Setup command examples

#### 4. Banner Image (1920x400)
For project header on publishing sites

---

## 📝 Release Notes Template

### Version 1.0.0 - Initial Release

**New Features:**
- ✨ Automatic race joining via start region
- ✨ Ready check system with countdown
- ✨ Ring detection and order enforcement
- ✨ Rocket usage tracking (max 3 per race)
- ✨ Real-time timer on action bar
- ✨ Statistics and leaderboard system
- ✨ WorldEdit integration for region setup

**Anti-Cheat:**
- 🚫 Ring skipping detection
- 🚫 Rocket limit enforcement
- 🚫 Mid-race disconnect handling
- 🚫 Ring order validation

**Performance:**
- ⚡ Optimized movement checking
- ⚡ Cached region checks
- ⚡ Pre-computed ring distances
- ⚡ Minimal CPU usage (~2-4% per player)

**Known Issues:**
- Ring management commands not yet available (manual config)
- Backward ring detection needs enhancement

---

## 🎯 Marketing Strategy

### 1. GitHub Repository
```markdown
Repository Features:
- Detailed README with GIFs
- Complete documentation
- Issue tracker
- Wiki with tutorials
- Releases with changelogs
```

### 2. Social Media
**Platforms:**
- Reddit: r/admincraft, r/minecraft
- Discord: Minecraft server admin communities
- YouTube: Setup tutorial video
- Twitter/X: Release announcements

**Content Ideas:**
- Setup tutorial video
- Race demonstration
- Server spotlight
- Development updates

### 3. SpigotMC Forums
**Post in:**
- Plugin Releases
- Plugin Development
- Server Discussion

### 4. Planet Minecraft
Create server listing with ElytraRace as featured plugin

---

## 🔐 License Recommendations

### MIT License (Recommended)
**Pros:**
- Very permissive
- Allows commercial use
- Simple and clear

**Cons:**
- No warranty protection
- Others can fork without attribution

### GPL v3
**Pros:**
- Strong copyleft
- Forces modifications to remain open source

**Cons:**
- More restrictive
- May limit adoption

### Custom License
**Example:**
```
Free for non-commercial use
Commercial servers require donation/sponsorship
Must credit original author
```

---

## 📊 Analytics & Tracking

### bStats Integration
```xml
<dependency>
    <groupId>org.bstats</groupId>
    <artifactId>bstats-bukkit</artifactId>
    <version>3.0.2</version>
    <scope>compile</scope>
</dependency>
```

**Tracks:**
- Server count
- Player count
- Plugin version distribution
- Server software types

---

## 🎉 Launch Preparation

### Pre-Launch (1 week before):
1. Announce on Discord/social media
2. Create demo video
3. Prepare press kit
4. Line up beta testers

### Launch Day:
1. Publish to all platforms simultaneously
2. Post announcement on Reddit/Discord
3. Share demo video
4. Respond to initial feedback

### Post-Launch (1 week after):
1. Monitor feedback
2. Fix critical bugs
3. Release patch if needed
4. Thank early adopters

---

## 📞 Support Resources

### Documentation Sites:
- **SpigotMC Wiki:** https://www.spigotmc.org/wiki/
- **Paper Docs:** https://docs.papermc.io/
- **Modrinth Docs:** https://docs.modrinth.com/

### Communities:
- SpigotMC Discord
- PaperMC Discord
- r/admincraft subreddit

---

**Ready to publish? Follow this guide step by step for maximum success!** 🚀
