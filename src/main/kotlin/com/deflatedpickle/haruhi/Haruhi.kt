/* Copyright (c) 2020-2021 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi

import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.plugin.PluginType
import com.deflatedpickle.haruhi.event.EventCreateJsonSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

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
    lateinit var json: Json
}
