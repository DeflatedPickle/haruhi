/* Copyright (c) 2020-2021 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.plugin.PluginType
import com.deflatedpickle.haruhi.component.PluginPanel
import com.deflatedpickle.haruhi.event.EventCreateJsonSerializer
import com.deflatedpickle.haruhi.util.DockUtil
import com.deflatedpickle.tosuto.ToastWindow
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.awt.Rectangle
import javax.swing.JFrame
import javax.swing.JToolBar
import kotlin.properties.Delegates

@Suppress("unused", "SpellCheckingInspection")
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
    lateinit var control: CControl
    lateinit var grid: CGrid

    lateinit var json: Json
}
