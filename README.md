# AFKApi

A lightweight Minecraft Paper API library for tracking AFK players. Exposes a simple singleton interface with event hooks and configurable detection settings.

## Installation

Add the dependency to your `build.gradle.kts`, check the current version  on [central](https://central.sonatype.com/artifact/com.system32dev.afkapi/AFKApi):

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.system32dev.afkapi:AFKApi:VERSION")
}
```

## Setup

Initialize the API in your plugin's `onEnable`:

```kotlin
AFKApi.init(this, ApiSettings(
    afkTimeMinutes = 5,
    afkCheckerTimerSeconds = 10,
    allowCameraMovement = true
))
```

## ApiSettings

| Field | Type | Description |
|---|---|---|
| `afkTimeMinutes` | `Long` | Minutes of idle time before a player is marked AFK |
| `afkCheckerTimerSeconds` | `Long` | Interval in seconds between AFK checks |
| `allowCameraMovement` | `Boolean` | If `true`, only block movement removes AFK status; camera-only rotation does not |

## Usage

```kotlin
// Check if a player is AFK
AFKApi.isAfk(player)

// Get the AFKPlayer model (null if not AFK)
AFKApi.get(player)

// Get all currently AFK players
AFKApi.getAfkPlayers()

// Manually mark a player as AFK
AFKApi.service.setAfk(player)

// Manually remove a player from AFK
AFKApi.service.removeAfk(player)
```

## AFKPlayer

The `AFKPlayer` data class holds AFK state for a player:

```kotlin
afkPlayer.uuid         // Player UUID
afkPlayer.afkSince     // Instant when they went AFK
afkPlayer.afkDuration  // Duration since they went AFK
afkPlayer.player       // Bukkit Player (nullable, null if offline)
```

## Events

Listen to AFK state changes using Bukkit's event system:

```kotlin
@EventHandler
fun onEnterAFK(event: PlayerEnterAFKEvent) {
    val afkPlayer = event.afkPlayer
    // player just went AFK
}

@EventHandler
fun onLeaveAFK(event: PlayerLeaveAFKEvent) {
    val afkPlayer = event.afkPlayer
    // player is no longer AFK
}
```

### Triggers that remove AFK status

- Block movement (X/Y/Z change)
- Camera rotation (if `allowCameraMovement` is `false`)
- Chat message
- Command execution
- Interaction (right/left click)
- Inventory click
- Player disconnect

## Notes

- The internal cache uses `ConcurrentHashMap`, making it safe for async access.
- `AFKApi.init()` throws if called more than once.
- AFK detection relies on Paper's `Player.idleDuration`, so this library requires a Paper-based server.