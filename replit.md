# PaintOff - Minecraft Spigot Plugin

## Overview
PaintOff is a custom Minecraft Spigot/Paper plugin that implements a paintball-style team-based game within Minecraft. Players compete in teams to paint territory using various weapons and abilities in arena-based matches.

## Project Information
- **Plugin Name**: PaintOff Beta
- **Version**: 1.0.0-BETA
- **Minecraft API**: Spigot 1.21
- **Build Tool**: Maven
- **Java Version**: 19 (originally Java 21, adapted for Replit)
- **Main Class**: `mc.cws.paintOff.PaintOffMain`

## Project Structure
```
PaintOff/
├── src/main/java/mc/cws/paintOff/
│   ├── PaintOffMain.java          # Main plugin entry point
│   ├── Configuration.java          # Game configuration settings
│   ├── Game/                       # Game mechanics
│   │   ├── Arena/                  # Arena management
│   │   ├── Extras/                 # Game utilities (painting, teleport, etc.)
│   │   ├── Management/             # Game flow control
│   │   ├── Points/                 # Point system
│   │   ├── Resources/              # Resource management
│   │   └── Shop/                   # In-game shop
│   ├── Listener/                   # Event listeners
│   ├── Po/                         # Command executors and handlers
│   ├── PrimarysWeapons/            # Primary weapon implementations
│   ├── SecondaryWeapons/           # Secondary weapon implementations
│   ├── Ultimates/                  # Ultimate abilities
│   └── Scoreboards/                # Scoreboard management
├── src/main/resources/
│   ├── plugin.yml                  # Plugin configuration
│   └── Patchnotes                  # Version history
├── pom.xml                         # Maven build configuration
└── target/                         # Build output (gitignored)
```

## Recent Changes
- **2025-10-12**: Initial import to Replit environment
  - Adapted Java version from 21 to 19 for compatibility
  - Fixed Java 21 `getFirst()` calls to use Java 19 compatible `get(0)`
  - Set up Maven build workflow
  - Added comprehensive .gitignore for Java/Maven projects

## Key Features
Based on the codebase analysis:
- **Arena System**: Create and manage multiple game arenas
- **Team-based Gameplay**: Two-team matches with color-coded teams
- **Weapon System**: Primary weapons, secondary weapons, and ultimate abilities
- **Point System**: Fuel points and ultimate points for abilities
- **Shop System**: In-game arsenal and upgrades
- **Queue System**: Match-making and game lobby management
- **Custom Commands**: `/po`, `/poqueue`, `/poleave`, `/popoints`, `/postart`, `/postop`, `/poarsenal`, `/pomenu`

## Game Configuration
Default settings (can be modified in `Configuration.java`):
- Game time: 8 minutes
- Max players per match: 4 (normal) + 2 additional
- Queue waiting time: 30 seconds
- Spawn height: 80 blocks
- Max fuel points: 200
- Ultimate points: 500

## Building the Plugin

### Using the Workflow
The project has a "Build Plugin" workflow configured that automatically builds the plugin JAR file.

### Manual Build
```bash
mvn clean package
```

The compiled plugin JAR will be located at:
- `target/PaintOff-1.0.0-BETA.jar` (shaded version with dependencies)

## Installation & Usage
This is a **Minecraft server plugin** for Spigot/Paper 1.21+ servers:

1. **Build the Plugin**: Run the "Build Plugin" workflow or execute `mvn clean package`
2. **Download JAR**: Get `target/PaintOff-1.0.0-BETA.jar`
3. **Install on Server**:
   - Copy the JAR file to your Minecraft server's `plugins/` folder
   - Restart the server
4. **Configure**:
   - Admin commands require `po.admin` permission (default: op)
   - Player commands require `po.player` permission (default: all players)

## Commands
- `/po` - Main plugin command with admin subcommands
  - `/po create [arena]` - Create a new arena
  - `/po delete [arena]` - Delete an arena
  - `/po spawn [arena]` - Set spawn point
  - `/po lobby` - Set lobby spawn
  - `/po pos1/pos2` - Set arena boundaries
- `/poqueue` - View queue information
- `/poleave` - Leave the queue/game
- `/popoints` - View your points
- `/postart` - Start the game (admin)
- `/postop` - Stop the game (admin)
- `/poarsenal` - View weapon arsenal
- `/pomenu` - Open admin menu

## Development Notes
- **No Frontend**: This is a server-side plugin only
- **No Database**: Plugin uses file-based storage for arenas and configurations
- **Testing**: Requires a Minecraft Spigot/Paper 1.21 server to test functionality
- **Dependencies**: Spigot API is provided by the server (scope: provided)

## Technical Details
- Uses Bukkit/Spigot event system for game mechanics
- Implements custom listeners for block placement, damage, movement, etc.
- Metadata system for tracking projectile sources
- Scoreboard integration for team management
- Particle effects and sound effects for visual feedback

## User Preferences
None configured yet.

## Architecture Notes
- Plugin follows standard Spigot plugin architecture
- Event-driven design with listener classes
- Singleton pattern for plugin instance across weapon/ability classes
- Queue-based matchmaking system
- Arena system supports multiple concurrent games
