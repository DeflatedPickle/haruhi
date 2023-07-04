/* Copyright (c) 2020-2021 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi

import ModernDocking.Docking
import ModernDocking.exception.DockingLayoutException
import ModernDocking.persist.AppState
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.plugin.PluginType
import com.deflatedpickle.tosuto.ToastWindow
import kotlinx.serialization.json.Json
import javax.swing.JFrame
import javax.swing.JToolBar
import javax.swing.SwingUtilities
import kotlin.properties.Delegates

@Plugin(
    value = "haruhi",
    author = "DeflatedPickle",
    version = "1.5.4",
    description = """
        <br>
        A plugin framework
    """,
    type = PluginType.API
)
object Haruhi {
    var isInDev by Delegates.notNull<Boolean>()

    // These are used to validate different strings
    val versionRegex = Regex("[0-9].[0-9].[0-9]")
    val slugRegex = Regex("[a-z]+@[a-z_]+#[0-9].[0-9].[0-9]")

    lateinit var window: JFrame
    lateinit var toolbar: JToolBar
    lateinit var toastWindow: ToastWindow

    lateinit var json: Json

    init {
        SwingUtilities.invokeLater {
            try {
                AppState.restore()
            } catch (e: DockingLayoutException) {
                e.printStackTrace()
            }

            AppState.setAutoPersist(true)
        }
    }
}
