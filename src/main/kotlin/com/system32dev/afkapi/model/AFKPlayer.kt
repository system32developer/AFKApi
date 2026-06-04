package com.system32dev.afkapi.model

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.Duration
import java.time.Instant
import java.util.UUID

data class AFKPlayer(
    val uuid: UUID,
    val afkSince: Instant = Instant.now()
){
    val afkDuration get() = Duration.between(afkSince, Instant.now())
    val player: Player? get() = Bukkit.getPlayer(uuid)
}