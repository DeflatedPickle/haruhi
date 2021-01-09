package com.deflatedpickle.haruhi.api.plugin

import com.deflatedpickle.haruhi.api.util.ComponentPosition
import com.deflatedpickle.haruhi.api.util.ComponentPositionNormal
import com.deflatedpickle.haruhi.component.PluginPanel
import kotlin.reflect.KClass
import org.apache.logging.log4j.LogManager

@Suppress("unused", "SpellCheckingInspection")
abstract class HaruhiPlugin(
    /**
     * The author of the plugin
     *
     * This should be provided as-is.
     *
     * Example: DeflatedPickle
     */
    val author: String,
    /**
     * The version the plugin is currently at
     */
    val version: String,
    /**
     * The full description of the plugin as a HTML string
     */
    val description: String = "<br>",
    /**
     * The website for this plugin
     */
    val website: String = "",
    /**
     * The type of plugin this is
     */
    val type: PluginType = PluginType.API,
    /**
     * The component this plugin provides
     */
    val component: KClass<out PluginPanel>? = null,
    /**
     * The side the component will be minimized to
     */
    val componentMinimizedPosition: ComponentPosition = ComponentPosition.NORTH,
    /**
     * The side the component will be added to
     */
    val componentNormalPosition: ComponentPositionNormal = ComponentPositionNormal.WEST,
    /**
     * The width of the dock of this component
     */
    val componentWidth: Double = 1.0,
    /**
     * The height of the dock of this component
     */
    val componentHeight: Double = 1.0,
    /**
     * The plugin IDs this plugin should load after
     */
    val dependencies: Array<String> = arrayOf(),
    /**
     * The config for this plugin
     */
    val settings: KClass<*>? = null
) {
    private val logger = LogManager.getLogger()

    init {
        this.logger.trace("Initialized ${getName()}")
    }

    fun getName(): String = this::class.simpleName!!
        .replace(" ", "_")
        .toLowerCase()
        .replace("plugin", "")

    fun getSlug(): String = "${author.toLowerCase()}@${getName().toLowerCase()}#${version}"

    fun load() {}
    fun unload() {}
}