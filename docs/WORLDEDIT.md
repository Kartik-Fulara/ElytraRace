# WorldEdit & VoiidCountdownTimer Integration Guide

Complete guide for setting up rings with WorldEdit and using VoiidCountdownTimer for enhanced timer displays.

## 📦 Prerequisites

Make sure you have these plugins installed:
- ✅ **WorldEdit** (for ring selection and placement)
- ✅ **VoiidCountdownTimer** (for enhanced timer display)
- ✅ **ElytraRace** (this plugin)

All of these are already in your plugins folder based on your screenshot!

---

## 🎨 Setting Up Rings with WorldEdit

### Method 1: Using Your Current Location (Simple)
```bash
# Fly to the ring location
# Run this command:
/er setup addring ring1
```

### Method 2: Using WorldEdit Selection (Recommended for Precise Placement)

This is perfect for placing rings at the center of structures you've built!

#### Step-by-Step:

1. **Get WorldEdit Wand**
   ```bash
   //wand
   ```
   This gives you a wooden axe (by default)

2. **Select the Ring Area**
   - **Left-click** with the wand on one corner of your ring structure
   - **Right-click** with the wand on the opposite corner
   - You'll see messages showing your selection

3. **Add the Ring**
   ```bash
   /er setup addringwe ring1
   ```
   This places the ring at the CENTER of your WorldEdit selection!

4. **Verify Placement**
   ```bash
   /er setup listrings
   ```

#### Example Workflow:

```bash
# 1. Build a ring structure (using blocks or WorldEdit)
//sphere glass 10

# 2. Select the ring with WorldEdit wand
# Left-click bottom corner
# Right-click top corner

# 3. Add to race system
/er setup addringwe ring1

# 4. Repeat for all rings
/er setup addringwe ring2
/er setup addringwe ring3
# ... up to ring14 or more
```

---

## 🎯 Advanced WorldEdit Ring Techniques

### Creating Ring Structures with WorldEdit

#### Circular Rings
```bash
# Stand where you want the ring
//sphere glass 15
//hsphere air 13
# This creates a hollow glass sphere - perfect for flying through!

# Add to race
/er setup addringwe ring1
```

#### Vertical Rings
```bash
# Create a vertical ring
//cyl glass 10 1
//hcyl air 8 1
# Creates a hollow cylinder (vertical ring)

/er setup addringwe ring2
```

#### Diagonal Rings
```bash
# Create rotated structures
//rotate 45
//sphere glass 12
//hsphere air 10

/er setup addringwe ring3
```

### Tips for Ring Placement

1. **Spacing**: Space rings 100-200 blocks apart
2. **Height Variation**: Mix high and low rings for challenge
3. **Visibility**: Use bright blocks (glass, glowstone) for night racing
4. **Size**: Recommend 10-15 block diameter for elytra racing

---

## ⏱️ VoiidCountdownTimer Integration

The plugin automatically detects and uses VoiidCountdownTimer if installed!

### What You Get

With VoiidCountdownTimer installed:
- **Better countdown display** during race start
- **Action bar timer** showing current race time
- **Smooth animations** for countdown
- **Sound effects** for race start

### Timer Display Features

#### During Race Start (Countdown)
```
     ╔═══════╗
     ║   5   ║  <-- Large title display
     ║Get ready...║  <-- Subtitle
     ╚═══════╝
```

#### During Race
```
Action Bar: ⏱ 02:34 | Rings: 7/14
```
Shows:
- Current race time (MM:SS)
- Rings passed / Total rings

### Customizing Timer Display

Edit `config.yml`:
```yaml
timer:
  show-action-bar: true
  show-title: true
  update-interval: 1  # seconds
```

---

## 🎮 Complete Setup Workflow

### Initial Setup
```bash
# 1. Install plugins
- WorldEdit
- VoiidCountdownTimer
- ElytraRace

# 2. Restart server

# 3. Set lobby
/er setup lobby

# 4. Build and add rings (choose one method):

# Method A: Manual placement
/er setup addring ring1
# Fly to next location
/er setup addring ring2
# etc...

# Method B: WorldEdit selection
//wand
# Select ring area
/er setup addringwe ring1
# Select next ring area
/er setup addringwe ring2
# etc...

# 5. Verify setup
/er setup listrings

# 6. Test!
/er join
/ready
```

---

## 📍 Ring Configuration in config.yml

After adding rings, they're saved in `config.yml`:

```yaml
rings:
  ring1:
    world: world
    x: 100.5
    y: 100.0
    z: 50.5
  ring2:
    world: world
    x: 250.5
    y: 120.0
    z: 150.5
  ring3:
    world: world
    x: 400.5
    y: 95.0
    z: 250.5
  # ... up to ring14 and beyond
```

You can also manually edit these coordinates if needed!

---

## 🔧 Troubleshooting

### WorldEdit Selection Not Working

**Problem**: `/er setup addringwe` says no selection

**Solution**:
```bash
# Get wand
//wand

# Make sure you have a selection
//size  # This shows your current selection

# If no selection, select area again
# Left-click and right-click with wand
```

### Ring Not Detecting Players

**Problem**: Players fly through rings but they don't register

**Solution 1**: Increase detection radius
Edit `RaceListener.java`:
```java
private static final double RING_DETECTION_RADIUS = 10.0;  // Was 5.0
```

**Solution 2**: Check ring placement
```bash
/er setup listrings  # Verify rings exist
# Teleport to ring to check location
/tp @s <x> <y> <z>
```

### Timer Not Showing

**Problem**: No timer display during race

**Solution**:
1. Check VoiidCountdownTimer is enabled: `/plugins`
2. Check console for errors
3. Timer falls back to action bar if VoiidCountdownTimer isn't available

---

## 🎨 Creating Beautiful Race Tracks

### Example Race Track Design

```bash
# Ring 1: Start - Large easy ring
//sphere glass 20
//hsphere air 18
/er setup addringwe ring1

# Ring 2-5: Medium difficulty
# (100-150 blocks apart)
//sphere sea_lantern 15
//hsphere air 13
/er setup addringwe ring2

# Ring 6-10: Challenging
# (Smaller, higher, or lower)
//sphere glowstone 12
//hsphere air 10
/er setup addringwe ring6

# Ring 11-14: Expert
# (Tight spaces, diagonal angles)
//rotate 45
//sphere glass 10
//hsphere air 8
/er setup addringwe ring11
```

### Theme Ideas

**Nether Track**:
- Use netherrack, soul sand, and nether brick
- Place in nether dimension
- Add fire/lava decorations

**Ocean Track**:
- Rings underwater (partial)
- Use prismarine and sea lanterns
- Kelp and coral decorations

**Sky Track**:
- Very high altitude (y: 200+)
- Cloud-themed with white blocks
- Lightning effects

---

## 📊 Performance Optimization

### For Large Tracks (20+ rings)

1. **Use Async Chunks**: Paper/Spigot handles this automatically

2. **Optimize Ring Detection**:
   ```java
   // Only check nearby rings (future optimization)
   // Currently checks all rings every tick
   ```

3. **Limit Particle Effects**: Reduce decorative particles near rings

4. **WorldGuard Protection**: Protect race track from griefing
   ```bash
   //sel
   /region define race_track
   /region flag race_track pvp deny
   ```

---

## 🎯 Advanced Features

### Multiple Race Tracks

You can create multiple configs:
```bash
config.yml       # Main track
config-nether.yml  # Nether track
config-sky.yml     # Sky track
```

Then switch between them (requires plugin modification).

### Checkpoint System

For longer races, add checkpoint indicators:
```bash
# At ring 5, 10, 15, etc.
# Use different colored blocks
```

### Spectator Areas

Build viewing platforms:
```bash
//sphere glass 30
//hsphere air 28
# Hollow sphere around ring for spectators
```

---

## 📝 Quick Command Reference

### WorldEdit Commands
```bash
//wand              # Get selection wand
//pos1              # Set position 1
//pos2              # Set position 2
//size              # Show selection size
//sphere [block] [radius]  # Create sphere
//hsphere [block] [radius] # Hollow sphere
//cyl [block] [radius] [height]  # Cylinder
//rotate [angle]    # Rotate selection
```

### ElytraRace Commands
```bash
/er setup lobby                # Set lobby
/er setup addring <n>          # Add ring at location
/er setup addringwe <n>        # Add ring from WorldEdit selection
/er setup removering <n>       # Remove ring
/er setup listrings            # List all rings
```

### Testing Commands
```bash
/er join           # Join race
/ready             # Mark ready
/er progress       # Check progress
/er timer          # View time
/er reset          # Reset (admin)
```

---

## 🚀 Ready to Race!

You now have:
- ✅ WorldEdit integration for easy ring placement
- ✅ VoiidCountdownTimer for enhanced displays
- ✅ Complete race track setup
- ✅ All rings configured

**Start racing!** 🏁

```bash
/er join
/ready
# FLY!
```

---

## 💡 Pro Tips

1. **Test fly-through** each ring before adding to race
2. **Number rings** in the order players should fly through them
3. **Use landmarks** to help players find the next ring
4. **Add particle effects** at rings for visibility
5. **Create shortcuts** for experienced players (advanced tracks)

Happy racing! 🚀✈️