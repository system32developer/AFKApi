package com.system32dev.afkapi

import com.system32dev.afkapi.model.ApiSettings
import com.system32dev.afkapi.service.AFKService
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object AFKApi {

    lateinit var service: AFKService
        private set

    private var initialized = false

    fun init(plugin: JavaPlugin, apiSettings: ApiSettings) {
        check(!initialized) {
            "AFKApi has already been initialized."
        }

        initialized = true
        service = AFKService(apiSettings)
        Bukkit.getPluginManager().registerEvents(service, plugin)
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            for (player in Bukkit.getOnlinePlayers()) {
                if (player.idleDuration.toMinutes() >= apiSettings.afkTimeMinutes && !service.isAfk(player)) service.setAfk(player)
            }
        }, 0L,apiSettings.afkCheckerTimerSeconds * 20L)
    }

    fun isAfk(player: Player) = service.isAfk(player)

    fun get(player: Player) = service.get(player)

    fun getAfkPlayers() = service.getAfkPlayers()
}