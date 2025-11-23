# 📋 Commands Reference

## Player Commands

### `/race help`
Shows all available commands and their descriptions.

**Permission**: `elytrarace.player.*`
**Usage**: `/race help [page]`

```
Example:
/race help
/race help 2  # Show second page
```

---

### `/race list`
Lists all available races on the server.

**Permission**: `elytrarace.player.list`
**Usage**: `/race list [page]`

```
Example:
/race list
/race list 2  # Show second page
```

**Output**:
```
Available Races:
1. SkyChallenge - Started: 12s ago - Players: 3/8
2. MountainRush - Enabled - Players: 0/10
3. NeonCircuit - Enabled - Players: 5/5
```

---

### `/race join <race>`
Join an available race.

**Permission**: `elytrarace.player.join`
**Usage**: `/race join <race_name>`

```
Examples:
/race join SkyChallenge
/race join MountainRush
```

**Messages**:
- ✅ `You have joined the race SkyChallenge!`
- ❌ `Race is full! Cannot join.`
- ❌ `Race not found: InvalidRace`

---

### `/race leave`
Leave your current race.

**Permission**: `elytrarace.player.join`
**Usage**: `/race leave`

```
Example:
/race leave
```

**Messages**:
- ✅ `You left the race SkyChallenge`
- ❌ `You are not in a race!`

---

### `/race start`
Start a race (if you're the race owner).

**Permission**: `elytrarace.player.join`
**Usage**: `/race start`

```
Example:
/race start
```

**Messages**:
- ✅ `Starting race in 3, 2, 1...`
- ❌ `You are not a race owner!`

---

### `/race stats`
View your personal race statistics.

**Permission**: `elytrarace.player.stats`
**Usage**: `/race stats [player]`

```
Examples:
/race stats
/race stats PlayerName  # Admin only - view other player's stats
```

**Stats Shown**:
- Total races completed
- Personal best times
- Races won
- Total distance traveled
- Total playtime

---

### `/race leaderboard`
View the top players on the server.

**Permission**: `elytrarace.player.leaderboard`
**Usage**: `/race leaderboard [type] [page]`

```
Examples:
/race leaderboard         # Overall leaderboard
/race leaderboard wins    # By races won
/race leaderboard best    # By best time
/race leaderboard page 2  # Second page
```

**Types**:
- `overall` - By races completed
- `wins` - By races won
- `best` - By fastest time
- `distance` - By distance traveled

---

## Admin Commands

### `/race create <name>`
Create a new race.

**Permission**: `elytrarace.admin.create`
**Usage**: `/race create <race_name> [max_players]`

```
Examples:
/race create SkyChallenge
/race create MountainRush 8  # Set max players to 8
```

**Messages**:
- ✅ `Race SkyChallenge created! Use /race setstart to set start point.`
- ❌ `Race already exists: SkyChallenge`

---

### `/race delete <name>`
Delete an existing race.

**Permission**: `elytrarace.admin.delete`
**Usage**: `/race delete <race_name>`

```
Example:
/race delete OldRace
```

**Messages**:
- ✅ `Race OldRace deleted successfully`
- ❌ `Race not found: InvalidRace`

---

### `/race setstart [race]`
Set the start point of a race at your current location.

**Permission**: `elytrarace.admin.setstart`
**Usage**: `/race setstart [race_name]`

```
Examples:
/race setstart                # Set for current race
/race setstart SkyChallenge   # Set for specific race
```

**Messages**:
- ✅ `Start point set for SkyChallenge at X: 100 Y: 150 Z: -50`
- ❌ `Race not found!`

---

### `/race setfinish [race]`
Set the finish point of a race at your current location.

**Permission**: `elytrarace.admin.setfinish`
**Usage**: `/race setfinish [race_name]`

```
Examples:
/race setfinish
/race setfinish SkyChallenge
```

**Messages**:
- ✅ `Finish point set for SkyChallenge at X: 200 Y: 120 Z: 100`
- ❌ `Race not found!`

---

### `/race checkpoint <name> [add|remove]`
Add or remove checkpoints to a race (requires WorldEdit).

**Permission**: `elytrarace.admin.checkpoint`
**Usage**: `/race checkpoint <race_name> [add|remove]`

```
Examples:
/race checkpoint SkyChallenge add     # Add checkpoint at current location
/race checkpoint SkyChallenge remove  # Remove nearest checkpoint
```

**Messages**:
- ✅ `Checkpoint added to SkyChallenge`
- ❌ `WorldEdit not installed!`

---

### `/race enable <name>`
Enable a race so players can join.

**Permission**: `elytrarace.admin.enable`
**Usage**: `/race enable <race_name>`

```
Example:
/race enable SkyChallenge
```

**Messages**:
- ✅ `Race SkyChallenge enabled!`
- ❌ `Race already enabled!`

---

### `/race disable <name>`
Disable a race so players cannot join.

**Permission**: `elytrarace.admin.disable`
**Usage**: `/race disable <race_name>`

```
Example:
/race disable SkyChallenge
```

**Messages**:
- ✅ `Race SkyChallenge disabled!`
- ❌ `Race already disabled!`

---

### `/race reload`
Reload configuration files.

**Permission**: `elytrarace.admin.reload`
**Usage**: `/race reload`

```
Example:
/race reload
```

**Messages**:
- ✅ `Configuration reloaded successfully!`

---

### `/race info <name>`
Get detailed information about a race.

**Permission**: `elytrarace.admin.info`
**Usage**: `/race info <race_name>`

```
Example:
/race info SkyChallenge
```

**Information Shown**:
- Race status (enabled/disabled)
- Max players
- Current players
- Start/Finish points
- Number of checkpoints
- Race difficulty

---

### `/race reset <name>`
Reset all statistics and data for a specific race.

**Permission**: `elytrarace.admin.reset`
**Usage**: `/race reset <race_name>`

```
Example:
/race reset SkyChallenge
```

**Messages**:
- ✅ `Race SkyChallenge statistics reset!`
- ⚠️ `This action cannot be undone!`

---

### `/race resetplayer <player>`
Reset all statistics for a specific player.

**Permission**: `elytrarace.admin.reset`
**Usage**: `/race resetplayer <player_name>`

```
Example:
/race resetplayer PlayerName
```

**Messages**:
- ✅ `Statistics for PlayerName reset!`
- ❌ `Player not found: InvalidPlayer`

---

## Command Aliases

All `/race` commands can be shortened:

```bash
/r help      # /race help
/r join      # /race join
/r leave     # /race leave
/r list      # /race list
/r stats     # /race stats
/r board     # /race leaderboard
```

---

## Permission Tree

```
elytrarace.*                          # All permissions
├── elytrarace.player.*               # All player commands
│   ├── elytrarace.player.join        # Join races
│   ├── elytrarace.player.list        # List races
│   ├── elytrarace.player.stats       # View stats
│   └── elytrarace.player.leaderboard # View leaderboard
├── elytrarace.admin.*                # All admin commands
│   ├── elytrarace.admin.create       # Create races
│   ├── elytrarace.admin.delete       # Delete races
│   ├── elytrarace.admin.setstart     # Set start point
│   ├── elytrarace.admin.setfinish    # Set finish point
│   ├── elytrarace.admin.enable       # Enable race
│   ├── elytrarace.admin.disable      # Disable race
│   ├── elytrarace.admin.reload       # Reload config
│   ├── elytrarace.admin.info         # Get race info
│   ├── elytrarace.admin.reset        # Reset race data
│   └── elytrarace.admin.checkpoint   # Manage checkpoints
└── elytrarace.bypasscooldown         # Bypass race cooldown
```

---

## Command Examples

### Creating Your First Race

```bash
# As admin/OP:
/race create SkyRace 5          # Create race, max 5 players
/race setstart SkyRace           # Stand at start, run command
/race setfinish SkyRace          # Stand at finish, run command
/race checkpoint SkyRace add     # (Optional) Add checkpoints
/race enable SkyRace             # Enable race

# Players can now:
/race join SkyRace
/race start                      # Race begins!
```

### Checking Race Info

```bash
/race info SkyRace               # See detailed race info
/race list                       # See all races
/race stats                      # See your performance
```

### Managing Races

```bash
/race disable SkyRace            # Disable race temporarily
/race delete OldRace             # Remove race permanently
/race reset SkyRace              # Clear race statistics
/race reload                     # Reload all configs
```

---

## Error Messages & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| `Race not found` | Invalid race name | Use `/race list` to see available races |
| `You don't have permission` | Missing permission | Contact admin or ask for permission |
| `Race is full` | Max players reached | Wait for race to finish or join different race |
| `You are not in a race` | Trying to leave when not joined | Use `/race join` first |
| `Race already enabled` | Trying to enable enabled race | Use `/race disable` first to disable it |

---

## Tips & Tricks

1. **Tab Completion**: Type `/race` and press TAB to see available options
2. **Bulk Commands**: Use WorldEdit with `/race checkpoint` for easier setup
3. **Quick Join**: `/race join` + race name can be autocompleted
4. **Check Performance**: `/race stats` to track your improvement

---

## Need Help?

- 📖 See [INSTALLATION.md](INSTALLATION.md) for setup
- 🔧 See [CONFIGURATION.md](CONFIGURATION.md) for advanced options
- 🆘 See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) for common issues
- 💬 Ask in [Discussions](https://github.com/USERNAME/ElytraRace/discussions)