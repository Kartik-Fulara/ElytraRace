# 🤝 Contributing to ElytraRace

Thank you for your interest in contributing to ElytraRace! This guide will help you get started with contributing to the project.

---

## 📋 Table of Contents

- [Code of Conduct](#-code-of-conduct)
- [How to Contribute](#-how-to-contribute)
- [Branch Naming Convention](#-branch-naming-convention)
- [Pull Request Process](#-pull-request-process)
- [Code Standards](#-code-standards)
- [Testing Requirements](#-testing-requirements)
- [Documentation](#-documentation)
- [License Agreement](#-license-agreement)

---

## 📜 Code of Conduct

### Our Standards

**Positive Behavior**:
- ✅ Being respectful and inclusive
- ✅ Providing constructive feedback
- ✅ Accepting criticism gracefully
- ✅ Focusing on what's best for the community
- ✅ Showing empathy towards others

**Unacceptable Behavior**:
- ❌ Harassment or discrimination of any kind
- ❌ Trolling, insulting, or derogatory comments
- ❌ Personal or political attacks
- ❌ Publishing others' private information
- ❌ Any conduct that could be considered inappropriate

### Enforcement

Instances of abusive, harassing, or otherwise unacceptable behavior may be reported to **kartikfulara2003@gmail.com**. All complaints will be reviewed and investigated promptly and fairly.

---

## 🌟 How to Contribute

### Ways to Contribute

1. **Report Bugs** - [Open an issue](https://github.com/Kartik-Fulara/ElytraRace/issues/new?template=bug_report.md)
2. **Suggest Features** - [Start a discussion](https://github.com/Kartik-Fulara/ElytraRace/discussions/new?category=ideas)
3. **Improve Documentation** - Submit PRs for typos, clarity, examples
4. **Write Code** - Fix bugs or implement features
5. **Test** - Help test new features and report issues
6. **Answer Questions** - Help others in discussions

---

## 🌿 Branch Naming Convention

**All branches MUST follow this naming pattern**: `type/issue-number-description`

### Branch Types

| Type | Purpose | Example |
|------|---------|---------|
| `feature/` | New features | `feature/123-team-racing` |
| `bugfix/` | Bug fixes | `bugfix/124-fix-timer-bug` |
| `hotfix/` | Urgent production fixes | `hotfix/125-critical-crash` |
| `docs/` | Documentation updates | `docs/126-update-readme` |
| `refactor/` | Code refactoring | `refactor/127-optimize-race-logic` |
| `test/` | Tests and improvements | `test/128-add-unit-tests` |
| `chore/` | Dependencies, config | `chore/129-update-paper-api` |

### Examples

✅ **Good Branch Names**:
```bash
feature/130-add-checkpoint-system
bugfix/131-prevent-double-join
hotfix/132-fix-leaderboard-crash
docs/133-add-api-documentation
refactor/134-optimize-race-manager
test/135-add-race-tests
chore/136-update-dependencies
```

❌ **Bad Branch Names**:
```bash
feature              # Missing issue number and description
feature-1            # Should use slash, not dash
Feature/1-test       # Should be lowercase
my-fix               # Missing type and issue number
new-feature          # Missing issue number
```

---

## 🔄 Pull Request Process

### Step 1: Fork & Clone

```bash
# Fork the repository on GitHub
# Then clone your fork
git clone https://github.com/YOUR_USERNAME/ElytraRace.git
cd ElytraRace

# Add upstream remote
git remote add upstream https://github.com/Kartik-Fulara/ElytraRace.git
```

---

### Step 2: Create Feature Branch

```bash
# Update develop branch
git fetch upstream
git checkout develop
git merge upstream/develop

# Create feature branch
git checkout -b feature/123-your-feature-name
```

---

### Step 3: Make Changes

1. **Write Clean Code** - Follow our code standards
2. **Add Comments** - Explain complex logic
3. **Test Thoroughly** - Ensure everything works
4. **Update Documentation** - If functionality changes

---

### Step 4: Commit Changes

```bash
# Stage changes
git add .

# Commit with clear message
git commit -m "feat: add team racing functionality

- Implement team join/leave commands
- Add team statistics tracking
- Create team leaderboard
- Update documentation

Closes #123"
```

#### Commit Message Format

```
type: short description

- Detailed change 1
- Detailed change 2
- Detailed change 3

Closes #issue-number
```

**Types**:
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `style:` - Code style changes (formatting)
- `refactor:` - Code refactoring
- `test:` - Adding tests
- `chore:` - Maintenance tasks

---

### Step 5: Push & Create PR

```bash
# Push to your fork
git push origin feature/123-your-feature-name
```

Then on GitHub:
1. Go to your fork
2. Click "Pull Request"
3. Select `develop` as the base branch (NOT `main`)
4. Fill out the PR template
5. Submit for review

---

### Step 6: Code Review

- ✅ At least **1 maintainer review** required
- ✅ All **CI checks must pass**
- ✅ No merge conflicts
- ✅ Documentation updated if needed

**Responding to Reviews**:
```bash
# Make requested changes
git add .
git commit -m "fix: address review comments"
git push origin feature/123-your-feature-name
```

---

### Step 7: Merge

Once approved:
- Maintainer will merge your PR into `develop`
- Your branch will be automatically deleted
- Changes will be included in next release

---

## ✅ Code Standards

### Java Code Style

```java
/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

/**
 * Handles team racing command logic.
 * 
 * @author YourName
 * @version 1.1.0
 * @since 1.2.0
 */
public class TeamCommand implements CommandExecutor {
    
    private final RaceManager raceManager;
    private final TeamManager teamManager;
    
    /**
     * Constructs a new TeamCommand.
     * 
     * @param raceManager The race manager instance
     * @param teamManager The team manager instance
     */
    public TeamCommand(RaceManager raceManager, TeamManager teamManager) {
        this.raceManager = raceManager;
        this.teamManager = teamManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, 
                           String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        
        if (args.length < 1) {
            player.sendMessage("§cUsage: /team <create|join|leave>");
            return true;
        }
        
        String action = args[0].toLowerCase();
        switch (action) {
            case "create" -> handleCreate(player, args);
            case "join" -> handleJoin(player, args);
            case "leave" -> handleLeave(player);
            default -> player.sendMessage("§cUnknown action: " + action);
        }
        
        return true;
    }
    
    /**
     * Handles team creation.
     * 
     * @param player The player creating the team
     * @param args Command arguments
     */
    private void handleCreate(Player player, String[] args) {
        // Implementation
    }
}
```

### Standards Checklist

- [ ] Copyright header added to new files
- [ ] JavaDoc for all public methods
- [ ] Meaningful variable names (`playerData`, not `pd`)
- [ ] Methods under 30 lines when possible
- [ ] Try-catch for risky operations
- [ ] Logging for important actions
- [ ] No hardcoded values (use config)
- [ ] No System.out.println() (use logger)
- [ ] No IDE-specific files committed

---

## 🧪 Testing Requirements

### Before Submitting PR

```bash
# 1. Build project
mvn clean package

# 2. Check for compilation errors
mvn compile

# 3. Run tests (if available)
mvn test
```

### Manual Testing

Test on a Paper 1.21.4 server:

1. **Install** plugin on test server
2. **Run** all affected commands
3. **Check** console for errors
4. **Verify** functionality works as expected
5. **Test** edge cases

### Test Checklist

- [ ] Plugin loads without errors
- [ ] All new commands work
- [ ] Existing functionality not broken
- [ ] No console errors
- [ ] No memory leaks after 30 min
- [ ] Tested with 2+ players
- [ ] Tested with max players
- [ ] Works with and without WorldGuard

---

## 📚 Documentation

### What to Update

If your change affects:

| Change Type | Update Required |
|------------|----------------|
| New command | [COMMANDS.md](docs/COMMANDS.md) |
| Config option | [CONFIGURATION.md](docs/CONFIGURATION.md) & config.yml |
| Setup process | [INSTALLATION.md](docs/INSTALLATION.md) |
| API changes | [API.md](docs/API.md) |
| Bug fix | [CHANGELOG.md](docs/CHANGELOG.md) |
| Feature | All of the above |

### Documentation Standards

```markdown
### `/er newcommand`
Brief description of what the command does.

**Permission**: `race.newpermission`  
**Usage**: `/er newcommand <arg1> [arg2]`

**Examples**:
\```bash
/er newcommand example
/er newcommand example optional
\```

**Output**:
\```
✅ Command executed successfully!
\```
```

---

## 🐛 Bug Reports

### Good Bug Report Template

```markdown
**Bug Description**
Clear description of what's wrong

**Steps to Reproduce**
1. Join race
2. Use /ready
3. Observe error

**Expected Behavior**
What should happen

**Actual Behavior**
What actually happens

**Environment**
- Plugin Version: 1.1.0
- Server: Paper 1.21.4
- Java: 21
- WorldEdit: 7.3.3
- WorldGuard: 7.0.13

**Console Errors**
\```
[Error logs here]
\```

**Screenshots**
[If applicable]
```

---

## ✨ Feature Requests

### Good Feature Request Template

```markdown
**Feature Description**
Clear description of the feature

**Use Case**
Why this feature would be useful

**Example Usage**
How it would work in practice

**Implementation Ideas**
Potential ways to implement (optional)

**Alternatives Considered**
Other solutions you've thought of
```

---

## ⚖️ License Agreement

### By Contributing, You Agree:

1. ✅ Your code will be licensed under **MIT License**
2. ✅ You own or have rights to your contribution
3. ✅ Your code is original or properly attributed
4. ✅ You allow the project to use your contribution
5. ✅ You're not submitting malicious code

### Copyright Notice

Add to all new files:

```java
/*
 * Copyright (c) 2025 [Your Name]
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */
```

---

## 🚫 Anti-Plagiarism Policy

### Strictly Forbidden:

- ❌ Copying code from other plugins without attribution
- ❌ Claiming others' work as your own
- ❌ Removing or modifying copyright headers
- ❌ Submitting code you don't own
- ❌ Including proprietary/licensed code
- ❌ Adding malicious code

### Required:

- ✅ Original work OR properly attributed
- ✅ Compatible with MIT License
- ✅ Free of malicious code
- ✅ Well-tested and functional
- ✅ Clear commit messages

---

## 💡 Getting Help

### Where to Ask

| Question Type | Platform |
|--------------|----------|
| General questions | [Discussions](https://github.com/Kartik-Fulara/ElytraRace/discussions) |
| Bug reports | [Issues](https://github.com/Kartik-Fulara/ElytraRace/issues) |
| Feature requests | [Discussions](https://github.com/Kartik-Fulara/ElytraRace/discussions/categories/ideas) |
| Security issues | kartikfulara2003@gmail.com |
| Code reviews | Pull Request comments |

### Response Times

- **Bug reports**: Within 48 hours
- **Feature requests**: Within 1 week
- **Pull requests**: Within 1 week
- **Security issues**: Within 24 hours

---

## 🎉 Recognition

### Contributors Are Recognized:

1. **GitHub Contributors Page** - Automatic
2. **CHANGELOG.md** - Listed in release notes
3. **README.md** - Top contributors featured
4. **Discord Role** - Contributor role (if available)

### Top Contributors

Special recognition for:
- Most commits
- Bug fixes
- Feature implementations
- Documentation improvements
- Community support

---

## 📞 Contact

- **Discord**: [Join server](https://discord.gg/YOUR_INVITE)
- **Email**: kartikfulara2003@gmail.com
- **GitHub**: [@Kartik-Fulara](https://github.com/Kartik-Fulara)
- **Issues**: [GitHub Issues](https://github.com/Kartik-Fulara/ElytraRace/issues)

---

## 🙏 Thank You!

Every contribution helps make ElytraRace better. Whether it's:
- Reporting a bug
- Suggesting a feature
- Fixing typos in documentation
- Implementing new functionality
- Helping other users

**All contributions are valuable and appreciated!** 🚀

---

**Contributing Guide v1.1.0**  
Last Updated: 2025-01-XX  
Maintained By: Kartik Fulara