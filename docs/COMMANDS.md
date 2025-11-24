# 📋 Commands Reference

Complete command reference for ElytraRace v1.1.0

---

## Table of Contents

- [Player Commands](#-player-commands)
- [Admin Commands](#-admin-commands)
- [Setup Commands](#-setup-commands)
- [Diagnostic Commands](#-diagnostic-commands)
- [Permission Reference](#-permission-reference)
- [Command Aliases](#-command-aliases)
- [Usage Examples](#-usage-examples)

---

## 👤 Player Commands

### `/er rules`
Display comprehensive race rules and requirements.

**Permission**: `race.use`  
**Aliases**: `/race rules`, `/elytra rules`  
**Usage**: `/er rules`

**Shows**:
- Ring navigation rules
- Anti-cheat warnings
- Disqualification conditions
- Rocket limits
- Time limits

```
Example output:
╔══════ RACE RULES ══════╗
║                        ║
1. Fly through ALL rings in order
2. Do NOT skip any rings
3. Do NOT go backwards
4. Max 64 rockets per race
5. Time limit: 180 seconds
║                        ║
DISQUALIFICATION:
• Skipping rings
• 3+ rocket violations
• Disconnecting mid-race
╚════════════════════════╝
```

---

### `/er join`
**DEPRECATED** - Players now auto-join by entering start region.

**Permission**: `race.use`  
**Status**: ⚠️ Auto-join enabled by default

Players automatically join when they walk into the start region. No command needed!

---

### `/ready`
Toggle your ready status for race start.

**Permission**: `race.use`  
**Aliases**: None  
**Usage**: `/ready`

**Requirements**:
- Must be in start region
- Must have elytra equipped
- Must have required rockets (default: 64)
- Inventory must be empty (except armor)

```
Success: ✅ You are ready! Waiting for other players...
Error:   ❌ You need 64 rockets to ready up! You have 32.
Error:   ❌ You must have an elytra equipped!
```

---

### `/er stats [player]`
View race statistics for yourself or another player.

**Permission**: `race.stats` (own), `race.admin` (others)  
**Aliases**: `/race stats`, `/elytra stats`  
**Usage**: 
- `/er stats` - Your stats
- `/er stats PlayerName` - Another player's stats

```
╔═══ YourName's Stats ═══╗
║ Wins: 15               ║
║ Total Races: 42        ║
║ Best Time: 45.23s      ║
║ Average Time: 52.18s   ║
║ Win Rate: 35.7%        ║
╚════════════════════════╝
```

---

### `/er pb [player]`
**NEW v1.1.0** - View personal best time.

**Permission**: `race.use`  
**Aliases**: `/race pb`, `/elytra personalbest`  
**Usage**:
- `/er pb` - Your personal best
- `/er pb PlayerName` - Another player's PB

```
╔═══ Personal Best ═══╗
║ Player: YourName    ║
║ Best Time: 45.23s   ║
║ Achieved: 3 days ago║
║ Global Rank: #7     ║
╚════════════════════════╝
```

---

### `/er top`
View the top 10 players on the leaderboard.

**Permission**: `race.use`  
**Aliases**: `/race leaderboard`, `/race board`  
**Usage**: `/er top`

```
╔═══════ TOP 10 RACERS ═══════╗
#1 ProRacer - 25 wins (42.15s best)
#2 SpeedyFlyer - 23 wins (43.89s best)
#3 WingMaster - 20 wins (44.22s best)
...
╚═════════════════════════════╝
```

---

### `/er progress`
Check your current race progress.

**Permission**: `race.use`  
**Aliases**: `/race status`  
**Usage**: `/er progress`

**Requirements**: Must be in an active race

```
╔═══ YOUR PROGRESS ═══╗
║ Rings: 8/12         ║
║ Time: 34.56s        ║
║ Rockets Used: 2/3   ║
╚═════════════════════╝
```

---

### `/er timer`
View the current race time.

**Permission**: `race.use`  
**Aliases**: `/race time`  
**Usage**: `/er timer`

**Requirements**: Must be in an active race

```
Response: [ElytraRace] Race Time: 02:15
```

---

### `/er listrings`
List all configured race rings.

**Permission**: `race.use`  
**Usage**: `/er listrings`

```
╔═══ Configured Rings ═══╗
• ring1 - (100, 150, -50)
• ring2 - (150, 160, -30)
• ring3 - (200, 155, -10)
Total: 3 ring(s)
╚════════════════════════╝
```

---

## 🔧 Admin Commands

### `/er forcejoin <player>`
**NEW v1.1.0** - Force teleport a player to the race lobby.

**Permission**: `race.admin`  
**Usage**: `/er forcejoin <player>`

**Features**:
- Teleports player to start region
- Shows rules automatically
- Validates lobby capacity

```
Example:
/er forcejoin SpeedyFlyer

Success: ✅ Force-joined SpeedyFlyer to the race!
Error:   ❌ Lobby is full!
```

---

### `/er testmode`
**NEW v1.1.0** - Toggle admin test mode.

**Permission**: `race.admin`  
**Usage**: `/er testmode`

**Features**:
- Solo testing without affecting stats
- Bypasses all requirements
- Independent from normal races
- Toggle on/off

```
Enabled:  ⚠ Test mode enabled - stats will not be saved.
Disabled: ✅ Test mode ended.
```

---

### `/er import rings`
**NEW v1.1.0** - Import WorldGuard regions as race rings.

**Permission**: `race.admin`  
**Usage**: `/er import rings`

**Requirements**:
- WorldEdit installed
- WorldGuard installed
- Regions named `ring1`, `ring2`, `ring3`, etc.

```
Process:
1. Create WorldGuard regions: ring1, ring2, ring3
2. Run /er import rings
3. Rings automatically imported and sorted

Success: ✅ Successfully imported 12 ring(s)!
Error:   ❌ WorldEdit/WorldGuard not found!
```

---

### `/er preview`
**NEW v1.1.0** - Toggle ring preview with particles.

**Permission**: `race.admin`  
**Usage**: `/er preview`

**Features**:
- Shows particle effects around rings
- Admin-only visibility
- Helps with course design
- Toggle on/off

```
Enabled:  ✅ Ring preview enabled!
Disabled: ❌ Ring preview disabled.
```

---

### `/er platform <create|remove>`
**NEW v1.1.0** - Manage starting platforms.

**Permission**: `race.admin`  
**Usage**: 
- `/er platform create` - Create platform
- `/er platform remove` - Remove platform

```
Example:
/er platform create

Success: ✅ Created starting platform for 5 player(s)!
Success: ✅ Removed starting platform!
```

---

### `/er start`
Force start the race (bypasses ready checks).

**Permission**: `race.admin`  
**Usage**: `/er start`

**Requirements**: At least 1 player in start lobby

```
Success: ✅ Force started race!
Error:   ❌ Race already in progress!
```

---

### `/er reset`
Reset the current active race.

**Permission**: `race.admin`  
**Usage**: `/er reset`

```
Success: ✅ Race reset!
```

---

## ⚙️ Setup Commands

### `/er setup lobby`
Set the lobby spawn location at your current position.

**Permission**: `race.admin`  
**Usage**: `/er setup lobby`

```
Success: ✅ Lobby set at X: 100 Y: 64 Z: -50
```

---

### `/er setup start`
Define the start region using WorldEdit selection.

**Permission**: `race.admin`  
**Usage**: `/er setup start`

**Requirements**: 
- WorldEdit installed
- Active WorldEdit selection

```
Process:
1. Make WorldEdit selection (/wand)
2. Select start area
3. Run /er setup start

Success: ✅ Start region saved.
Error:   ❌ You need a WorldEdit selection first.
```

---

### `/er setup finish`
Define the finish region using WorldEdit selection.

**Permission**: `race.admin`  
**Usage**: `/er setup finish`

**Requirements**:
- WorldEdit installed
- Active WorldEdit selection

```
Success: ✅ Finish region saved.
```

---

### `/er setup addring <name>`
Add a ring at your current location.

**Permission**: `race.admin`  
**Usage**: `/er setup addring <ring_name>`

```
Example:
/er setup addring ring1

Success: ✅ Ring 'ring1' added at X: 150 Y: 100 Z: -30
```

---

### `/er setup removering <name>`
Remove a configured ring.

**Permission**: `race.admin`  
**Usage**: `/er setup removering <ring_name>`

```
Example:
/er setup removering ring1

Success: ✅ Ring 'ring1' removed.
Error:   ❌ Ring not found: ring1
```

---

## 🔍 Diagnostic Commands

### `/er reload`
**PLANNED** - Reload plugin configuration.

**Permission**: `race.admin`  
**Status**: Coming in v1.2.0

---

### `/er debug`
**PLANNED** - Toggle debug mode.

**Permission**: `race.admin`  
**Status**: Coming in v1.2.0

---

## 🔐 Permission Reference

### Permission Hierarchy

```
elytrarace.*                          # All permissions
├── race.use                          # Basic race participation
│   ├── race.rules                    # View rules
│   ├── race.stats                    # View own stats
│   ├── race.top                      # View leaderboard
│   ├── race.progress                 # View progress
│   ├── race.timer                    # View timer
│   └── race.pb                       # View personal best
└── race.admin                        # Admin commands
    ├── race.forcejoin                # Force join players
    ├── race.testmode                 # Test mode
    ├── race.import                   # Import regions
    ├── race.preview                  # Ring preview
    ├── race.platform                 # Platform management
    ├── race.setup                    # Setup commands
    ├── race.start                    # Force start
    └── race.reset                    # Reset race
```

### Default Permissions

| Permission | Default | Description |
|-----------|---------|-------------|
| `race.use` | `true` | Basic player commands |
| `race.admin` | `op` | Admin commands |
| `race.stats` | `true` | View statistics |
| `race.top` | `true` | View leaderboard |

### Permission Examples

#### LuckPerms
```bash
# Give player basic access
/lp user PlayerName permission set race.use true

# Give admin access
/lp user AdminName permission set race.admin true

# Give group access
/lp group default permission set race.use true
/lp group admins permission set race.admin true
```

#### PermissionsEx
```bash
/pex user PlayerName add race.use
/pex user AdminName add race.admin
/pex group default add race.use
```

---

## 🔄 Command Aliases

All `/er` commands can use alternative prefixes:

| Primary | Aliases |
|---------|---------|
| `/er` | `/race`, `/elytra` |
| `/ready` | None |

```bash
# These are equivalent:
/er stats
/race stats
/elytra stats

# These work the same:
/er top
/race leaderboard
/race board
```

---

## 📝 Usage Examples

### Complete Setup Walkthrough

```bash
# Step 1: Create race regions
//wand                           # Get WorldEdit wand
# Select start area with wand
/er setup start                  # Define start region

# Select finish area with wand
/er setup finish                 # Define finish region

# Step 2: Import or add rings
/er import rings                 # Import from WorldGuard
# OR manually add rings
/er setup addring ring1
/er setup addring ring2
/er setup addring ring3

# Step 3: Set lobby
/er setup lobby                  # Stand where you want lobby

# Step 4: Preview course
/er preview                      # Toggle ring visualization

# Step 5: Test
/er testmode                     # Enter test mode
# Fly through the course
/er testmode                     # Exit test mode
```

---

### Running a Race

```bash
# Players:
# 1. Walk into start region (auto-joins)
# 2. Check rules
/er rules

# 3. Ready up when prepared
/ready

# 4. Race starts when all ready
# 5. Check progress during race
/er progress

# 6. View results
/er stats
/er pb
```

---

### Admin Management

```bash
# Force a player to join
/er forcejoin SlowPlayer

# Start race immediately (bypass ready)
/er start

# Reset if something goes wrong
/er reset

# Check ring configuration
/er listrings

# Preview course for validation
/er preview
```

---

## ❓ Common Issues

### "You don't have permission"
**Solution**: Admin needs to grant you `race.use` or `race.admin` permission

```bash
# LuckPerms
/lp user YourName permission set race.use true
```

---

### "Race not found"
**Solution**: Race hasn't been set up yet. Admin needs to:
1. Define start region
2. Define finish region
3. Add rings
4. Set lobby

---

### "You need X rockets to ready up"
**Solution**: Get more firework rockets in your inventory

```bash
/give @s firework_rocket 64
```

---

### "WorldEdit/WorldGuard not found"
**Solution**: Install required plugins:
1. Download WorldEdit from [EngineHub](https://enginehub.org/worldedit)
2. Download WorldGuard from [EngineHub](https://enginehub.org/worldguard)
3. Place in `plugins/` folder
4. Restart server

---

## 📚 Additional Resources

- **[Configuration Guide](CONFIGURATION.md)** - Customize settings
- **[Installation Guide](INSTALLATION.md)** - Setup instructions
- **[WorldEdit Integration](WORLDEDIT.md)** - Region setup guide
- **[Troubleshooting](TROUBLESHOOTING.md)** - Common problems

---

## 💡 Tips & Tricks

1. **Tab Completion**: Press TAB after `/er` to see available commands
2. **Quick Stats**: `/er stats` shows your stats instantly
3. **Course Testing**: Use `/er testmode` to test without affecting stats
4. **Visual Validation**: Use `/er preview` to verify ring placement
5. **Batch Import**: Create WorldGuard regions then use `/er import rings`

---

## 🆘 Need Help?

- 💬 [Discord Support](https://discord.gg/YOUR_INVITE)
- 📖 [Full Documentation](https://github.com/Kartik-Fulara/ElytraRace/wiki)
- 🐛 [Report Issues](https://github.com/Kartik-Fulara/ElytraRace/issues)
- 📧 Email: kartikfulara2003@gmail.com

---

**Last Updated**: v1.1.0  
**Maintained By**: Kartik Fulara