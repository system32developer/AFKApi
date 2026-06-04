package com.system32dev.afkapi.events

import com.system32dev.afkapi.model.AFKPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerEnterAFKEvent(
    val afkPlayer: AFKPlayer,
): Event() {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}