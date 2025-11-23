# 🔒 Security Policy

## Supported Versions

| Version | Status | Support Until |
|---------|--------|----------------|
| 1.x.x   | ✅ Active Support | Current + 1 minor |
| < 1.0   | ❌ Unsupported | End of Life |

## Reporting a Security Vulnerability

**⚠️ DO NOT open a public GitHub issue for security vulnerabilities.**

Instead, please report to: **kartikfulara2003@gmail.com** or use GitHub Security Advisories.

### What to Include

When reporting a vulnerability, please provide:

1. **Description** - What is the vulnerability?
2. **Affected Versions** - Which plugin versions are affected?
3. **Steps to Reproduce** - How can we reproduce it?
4. **Proof of Concept** - Code example or detailed steps
5. **Impact** - What damage could this cause?
6. **Suggested Fix** - If you have one (optional)
7. **Contact Info** - How to reach you

### Example Report

```
Subject: Security Vulnerability - RaceManager Data Exposure

Description:
The RaceManager class exposes sensitive player data through 
a public getter method without permission checks.

Affected Versions:
- 1.0.0
- 1.0.1

Steps to Reproduce:
1. Use console command to call getRaceData() method
2. Access private player information

Impact:
High - Allows reading all player statistics and race data
without proper permissions.

Suggested Fix:
Add permission checks to getRaceData() method before 
returning sensitive data.
```

## Response Timeline

We will respond to security reports following this timeline:

- **Within 24 hours**: Initial acknowledgment
- **Within 48 hours**: Assessment and severity rating
- **Within 7 days**: Release fix or mitigation plan
- **Within 14 days**: Public disclosure (coordinated with reporter)

## Vulnerability Severity Levels

### 🔴 Critical (CVSS 9.0-10.0)
- Remote code execution
- Complete data breach
- Server crash on startup
- Affects all users immediately

**Response Time**: Emergency fix within 24 hours

### 🟠 High (CVSS 7.0-8.9)
- Unauthorized access to data
- Privilege escalation
- Data corruption
- Affects many users

**Response Time**: Hot-fix within 48 hours

### 🟡 Medium (CVSS 4.0-6.9)
- Limited data access
- Partial functionality loss
- Performance impact
- Affects some configurations

**Response Time**: Fix in next minor release

### 🟢 Low (CVSS 0.1-3.9)
- Minor information disclosure
- Cosmetic issues
- Limited impact

**Response Time**: Fix in next release cycle

## Security Best Practices for Users

### For Server Administrators

1. **Keep Updated** - Always use the latest version
2. **Backup Data** - Regular backups of race data
3. **Permissions** - Restrict `/race` commands to trusted players
4. **Monitor Logs** - Check for suspicious activity
5. **Whitelist** - Use server whitelist for untrusted servers
6. **Validate Input** - Don't accept untrusted race definitions

### Environment Setup

```yaml
# config.yml - Security recommendations
security:
  verify-player-data: true
  log-all-races: true
  require-permission-join: true
  max-races-per-player: 10
  timeout-inactive-players: 300s
  clear-old-data: 30d
```

### Database Security

- Never expose database files
- Use strong permissions (600 or 700)
- Regularly back up race data
- Don't share config files containing sensitive data

## Known Issues & Advisories

### None Currently

Check back for security advisories related to:
- Paper API vulnerabilities
- WorldEdit/WorldGuard vulnerabilities
- Java security updates
- Minecraft server vulnerabilities

## Dependency Vulnerabilities

We use the following dependencies. Please keep them updated:

- **Paper 1.21.4** - Check [PaperMC](https://papermc.io/)
- **WorldEdit 7.3.3** - Check [EngineHub](https://enginehub.org/)
- **WorldGuard 7.0.13** - Check [EngineHub](https://enginehub.org/)

Monitor security updates for these projects:

```bash
# Check for outdated dependencies
mvn versions:display-dependency-updates

# Update dependencies
mvn versions:use-latest-versions
```

## Security Checklist for Contributors

If you're contributing code, ensure:

- ✅ No hardcoded API keys or passwords
- ✅ Input validation on all player inputs
- ✅ Permission checks before sensitive operations
- ✅ SQL injection prevention (if using database)
- ✅ No arbitrary command execution
- ✅ No file system traversal vulnerabilities
- ✅ Proper exception handling (no sensitive data in errors)
- ✅ Logging of security-relevant events

## Security Fixes We've Made

### Version 1.0.0
- Initial release - no known vulnerabilities

## Acknowledgments

We thank the security researchers and community members who responsibly report vulnerabilities.

If your report leads to a security fix, you will be:
- Credited in the changelog
- Listed as a security researcher (if you wish)
- Given advance notice of the fix (before public release)

## Questions?

- 📧 Email: kartikfulara2003@gmail.com
- 💬 GitHub Discussions: [Ask a Question](https://github.com/Kartik-Fulara/ElytraRace/discussions)
- 📋 GitHub Issues: [Report Non-Security Issue](https://github.com/Kartik-Fulara/ElytraRace/issues)

---

**Thank you for helping keep ElytraRace secure!**