package com.system32dev.afkapi.service

import com.system32dev.afkapi.events.PlayerEnterAFKEvent
import com.system32dev.afkapi.events.PlayerLeaveAFKEvent
import com.system32dev.afkapi.model.AFKPlayer
import com.system32dev.afkapi.model.ApiSettings
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class AFKService(private val apiSettings: ApiSettings): Listener {

    private val cache = ConcurrentHashMap<UUID, AFKPlayer>()

    fun get(player: Player): AFKPlayer? {
        return cache[player.uniqueId]
    }

    fun get(uuid: UUID): AFKPlayer? {
        return cache[uuid]
    }

    fun isAfk(player: Player): Boolean {
        return cache.containsKey(player.uniqueId)
    }

    fun isAfk(uuid: UUID): Boolean {
        return cache.containsKey(uuid)
    }

    fun setAfk(player: Player): AFKPlayer {
        cache[player.uniqueId]?.let { return it }

        val afkPlayer = AFKPlayer(player.uniqueId)

        cache[player.uniqueId] = afkPlayer

        PlayerEnterAFKEvent(afkPlayer).callEvent()

        return afkPlayer
    }

    fun removeAfk(player: Player): AFKPlayer? {
        val afkPlayer = cache.remove(player.uniqueId)
            ?: return null

        PlayerLeaveAFKEvent(afkPlayer).callEvent()

        return afkPlayer
    }

    fun removeAfk(uuid: UUID): AFKPlayer? {
        val afkPlayer = cache.remove(uuid)
            ?: return null

        PlayerLeaveAFKEvent(afkPlayer).callEvent()

        return afkPlayer
    }

    fun getAfkPlayers(): Collection<AFKPlayer> {
        return cache.values.toList()
    }

    @EventHandler
    private fun onMove(event: PlayerMoveEvent) {
        if (!apiSettings.allowCameraMovement) {
            removeAfk(event.player)
            return
        }

        val from = event.from
        val to = event.to

        if (
            from.blockX != to.blockX ||
            from.blockY != to.blockY ||
            from.blockZ != to.blockZ
        ) {
            removeAfk(event.player)
        }
    }

    @EventHandler
    private fun onChat(event: AsyncChatEvent) {
        event.player.resetIdleDuration()
        removeAfk(event.player)
    }

    @EventHandler
    private fun onCommand(event: PlayerCommandPreprocessEvent) {
        event.player.resetIdleDuration()
        removeAfk(event.player)
    }

    @EventHandler
    private fun onQuit(event: PlayerQuitEvent) {
        removeAfk(event.player)
    }

    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        removeAfk(event.player)
    }

    @EventHandler
    private fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        removeAfk(player)
    }
}