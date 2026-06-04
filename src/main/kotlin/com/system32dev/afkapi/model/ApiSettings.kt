package com.system32dev.afkapi.model

data class ApiSettings(
    val allowCameraMovement: Boolean = false,
    val afkCheckerTimerSeconds: Int = 1,
    val afkTimeMinutes: Int = 1,
)