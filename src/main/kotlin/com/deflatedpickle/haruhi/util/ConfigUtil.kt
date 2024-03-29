/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.util

import com.deflatedpickle.haruhi.Haruhi
import com.deflatedpickle.haruhi.api.config.Config
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.event.EventDeserializedConfig
import com.deflatedpickle.haruhi.event.EventSerializeConfig
import java.io.File
import java.io.FileOutputStream
import kotlin.reflect.full.createInstance
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.apache.logging.log4j.LogManager

@Suppress("MemberVisibilityCanBePrivate", "unused")
object ConfigUtil {
    private val logger = LogManager.getLogger(this::class.simpleName)

    /**
     * A map to get config settings for a plugin ID
     */
    private val idToSettings = mutableMapOf<String, Config>()

    // TODO: getSettingsFile

    fun <T : Any> getSettings(author: String, value: String, version: String): T? {
        for (plug in PluginUtil.loadedPlugins) {
            if (plug.author.toLowerCase() == author && plug.value == value) {
                for ((k, v) in idToSettings) {
                    val otherAuthor = k.substringBefore("@")
                    val otherValue = k.substringAfter("@").substringBefore("#")

                    if (author == otherAuthor && value == otherValue) {
                        return v as T
                    }
                }
            }
        }

        return null
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getSettings(id: String): T? {
        // return idToSettings[id] as T

        val author = id.substringBefore("@")
        val value = id.substringAfter("@").substringBefore("#")
        val version = id.substringAfter("#")

        return getSettings(author, value, version)
    }

    fun createConfigFolder(): File = File("config")

    fun createConfigFile(id: String): File =
        File("config/$id.json").apply {
            if (createNewFile()) {
                this@ConfigUtil.logger.info("Created a config file for $id at ${this.absolutePath}")
            }
        }

    fun hasConfigFile(id: String): Boolean =
        File("config/$id.json").exists()

    fun createAllConfigFiles(): List<File> {
        val list = mutableListOf<File>()

        for (id in PluginUtil.discoveredPlugins) {
            list.add(this.createConfigFile(id.value))
        }

        return list
    }

    @InternalSerializationApi
    fun serializeConfigToInstance(file: File, instance: Any): File {
        @Suppress("UNCHECKED_CAST")
        val serializer = instance::class.serializer() as KSerializer<Any>

        val jsonData = Haruhi.json.encodeToString(serializer, instance)

        val out = FileOutputStream(file, false)

        out.write(jsonData.toByteArray())
        out.flush()
        out.close()

        EventSerializeConfig.trigger(file)

        return file
    }

    @OptIn(InternalSerializationApi::class)
    fun serializeConfig(plugin: String) {
        serializeConfig(
            plugin, File("config/$plugin.json")
        )
    }

    @OptIn(InternalSerializationApi::class)
    fun serializeConfig(plugin: Plugin) {
        val id = PluginUtil.pluginToSlug(plugin)
        serializeConfig(
            id, File("config/$id.json")
        )
    }

    @InternalSerializationApi
    fun serializeConfig(id: String, file: File): File? {
        if (PluginUtil.slugToPlugin[id]!!.settings == Nothing::class) return null

        val settings = PluginUtil.slugToPlugin[id]!!.settings

        this.idToSettings.getOrPut(file.nameWithoutExtension) { settings.createInstance() }
        val instance = this.idToSettings[file.nameWithoutExtension]!!

        this.serializeConfigToInstance(file, instance)

        return file
    }

    @InternalSerializationApi
    fun deserializeConfig(file: File): Boolean {
        val obj = PluginUtil.slugToPlugin[file.nameWithoutExtension] ?: return false
        val settings = obj.settings

        val instance = settings.createInstance()

        this.deserializeConfigToInstance(file, instance)

        return true
    }

    @InternalSerializationApi
    fun deserializeConfigToInstance(file: File, instance: Any): Any {
        @Suppress("UNCHECKED_CAST")
        val serializer = instance::class.serializer() as KSerializer<Any>

        val jsonObj = Haruhi.json.decodeFromString(serializer, file.readText())

        this.idToSettings[file.nameWithoutExtension] = jsonObj as Config

        EventDeserializedConfig.trigger(file)

        return jsonObj
    }

    @InternalSerializationApi
    fun serializeAllConfigs(): List<File> {
        val list = mutableListOf<File>()

        for (plugin in PluginUtil.discoveredPlugins) {
            val id = PluginUtil.pluginToSlug(plugin)
            val file = File("config/$id.json")

            this.serializeConfig(id, file)
        }

        return list
    }
}
