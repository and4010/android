package com.example.survival.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
sealed class Screen() {

    companion object {
        val bottomNavItems = listOf(Home, Second)

        val ScreenSaver = Saver<Screen, String>(
            save = { screen ->
                Json.encodeToString(screen)
            },
            restore = { json ->
                try {
                    Json.decodeFromString<Screen>(json)
                } catch (e: Exception) {
                    Screen.Home // 預設回 Home
                }
            }
        )
    }


    @Serializable
    object Home : Screen()

    @Serializable
    object Second : Screen()


}


fun Screen.icon(): ImageVector {
    return when (this) {
        Screen.Home -> Icons.Filled.Home
        Screen.Second -> Icons.Filled.Edit
    }
}

fun Screen.text(): String {
    return when (this) {
        Screen.Home -> "Home"
        Screen.Second -> "Second"
    }
}