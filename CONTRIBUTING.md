# 🤝 Contributing to ElytraRace

Thank you for your interest in contributing to ElytraRace! This document provides guidelines for contributing to the project.

## 📋 Code of Conduct

- Be respectful and inclusive
- No harassment or discrimination
- Provide constructive feedback
- Help others learn and grow

## 🌿 Branch Naming Convention

All branches must follow this naming pattern: `type/issue-number-description`

### Branch Types

```
feature/     - New features (e.g., feature/1-particle-effects)
bugfix/      - Bug fixes (e.g., bugfix/2-fix-race-crash)
hotfix/      - Urgent production fixes (e.g., hotfix/3-critical-bug)
docs/        - Documentation updates (e.g., docs/4-update-readme)
refactor/    - Code refactoring (e.g., refactor/5-optimize-race-logic)
test/        - Tests and test improvements (e.g., test/6-add-unit-tests)
chore/       - Dependencies, config (e.g., chore/7-update-dependencies)
```

### Examples

```bash
# Good branch names:
feature/10-add-checkpoint-system
bugfix/11-prevent-double-join
hotfix/12-fix-leaderboard-crash
docs/13-add-api-documentation
refactor/14-optimize-race-manager
test/15-add-race-tests
chore/16-update-paper-api

# Bad branch names (don't use these):
feature           # Missing issue number and description
feature-1         # Should use slash, not dash
Feature/1-test    # Should be lowercase
my-fix            # Missing type and issue number
```

## 🔄 Pull Request Process

### Step 1: Create Your Branch

```bash
# Update your fork
git fetch origin
git checkout develop

# Create a feature branch
git checkout -b feature/123-your-feature-name
git push origin feature/123-your-feature-name
```

### Step 2: Make Your Changes

- Write clean, readable code
- Add comments for complex logic
- Follow existing code style
- Test your changes thoroughly

### Step 3: Commit with Clear Messages

```bash
# Good commit messages
git commit -m "Add particle effects to race finish line"
git commit -m "Fix NullPointerException in RaceManager"
git commit -m "Update WorldEdit integration documentation"

# Bad commit messages
git commit -m "fix stuff"
git commit -m "changes"
git commit -m "wip"
```

### Step 4: Create a Pull Request

1. Push your branch to GitHub
2. Open a Pull Request to `develop` (not `main`)
3. Use the PR template (auto-filled)
4. Provide:
   - What changed and why
   - How to test your changes
   - Screenshots (if UI changes)
   - Related issues (#123)

### Step 5: Review & Merge

- At least 1 maintainer review required
- Address requested changes promptly
- Once approved, PR will be merged automatically
- Main branch receives updates only from develop via scheduled releases

## ✅ Code Standards

### Java Code Style

```java
/*
 * Copyright (c) 2025 [Your Name]
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
 * Handles race join command logic
 */
public class RaceCommand implements CommandExecutor {
    
    private final RaceManager raceManager;
    
    public RaceCommand(RaceManager raceManager) {
        this.raceManager = raceManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, 
                           String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        
        if (args.length < 1) {
            player.sendMessage("Usage: /race join <race>");
            return true;
        }
        
        String raceName = args[0];
        raceManager.joinRace(player, raceName);
        return true;
    }
}
```

### Standards to Follow

- ✅ Always add copyright headers to new files
- ✅ Use meaningful variable names (`playerData`, not `pd`)
- ✅ Add JavaDoc for public methods
- ✅ Keep methods under 30 lines when possible
- ✅ Use try-catch for plugin operations
- ✅ Log important actions and errors
- ✅ Test code before submitting PR
- ✅ Update `pom.xml` if adding dependencies

### What NOT to Include

- ❌ Hardcoded sensitive data (tokens, passwords)
- ❌ IDE-specific files (.idea, .vscode)
- ❌ Compiled files (.class, .jar in src/)
- ❌ Personal configuration files
- ❌ Debug code or System.out.println()
- ❌ Code that removes/modifies copyright headers

## 🧪 Testing

Before submitting a PR:

```bash
# Build the project
mvn clean package

# Test locally on Paper 1.21.4 server
# Test all affected commands
# Verify no console errors
# Check for memory leaks
```

## 📚 Documentation

If your change affects functionality:
- Update relevant documentation in `docs/`
- Add command to [COMMANDS.md](docs/COMMANDS.md)
- Update [CONFIGURATION.md](docs/CONFIGURATION.md) if adding config options
- Add entry to [CHANGELOG.md](docs/CHANGELOG.md)

## 🐛 Bug Reports

Found a bug? Open an issue with:

- Clear description of the problem
- Steps to reproduce
- Expected behavior
- Actual behavior
- Plugin version
- Server version (Paper 1.21.4, etc.)
- Relevant logs or error messages
- Screenshots (if applicable)

## ✨ Feature Requests

Have an idea? Start a discussion:

- Describe what you want to achieve
- Explain why it would be useful
- Provide example usage
- Discuss potential implementation

## ⚖️ License & Attribution

### Contributing Agreement

By contributing to ElytraRace, you agree that:

1. Your code will be licensed under the MIT License
2. You own or have rights to the code you're submitting
3. You won't submit code that's copied from other projects without attribution
4. You won't submit code with malicious intent
5. You allow the project to use your contributions commercially if needed

### Attribution

All contributions are credited in:
- Commit history (automatically)
- [CHANGELOG.md](docs/CHANGELOG.md)
- GitHub contributors page (automatically)

### Copyright Notice

```java
/*
 * Copyright (c) 2025 [Your Name/Organization]
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */
```

## 🛡️ Anti-Plagiarism Policy

**DO NOT:**
- Copy code from other plugins without attribution
- Claim others' work as your own
- Remove or modify copyright headers
- Submit code you don't own or understand
- Include proprietary or licensed code

**All contributions must be:**
- Original work OR properly attributed
- Compatible with MIT License
- Free of malicious code
- Well-tested and functional

## 💡 Getting Help

- 📖 Read the [documentation](docs/)
- 💬 Start a [Discussion](https://github.com/USERNAME/ElytraRace/discussions)
- 🔍 Check existing [Issues](https://github.com/USERNAME/ElytraRace/issues)
- 📝 Review [SECURITY.md](SECURITY.md) for security concerns

## 🎉 Recognition

Contributors are recognized for:
- Code contributions (GitHub contributors page)
- Bug reports and feature requests
- Documentation improvements
- Community support

Thank you for making ElytraRace better! 🚀
