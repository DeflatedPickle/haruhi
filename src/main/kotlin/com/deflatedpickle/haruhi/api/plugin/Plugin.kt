/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.api.plugin

import com.deflatedpickle.haruhi.api.config.Config
import com.deflatedpickle.haruhi.api.util.ComponentPosition
import com.deflatedpickle.haruhi.api.util.ComponentPositionNormal
import com.deflatedpickle.haruhi.component.PluginPanel
import com.deflatedpickle.haruhi.util.PluginUtil
import kotlin.reflect.KClass

/**
 * A plugin to add extra features
 *
 * Discovered by [PluginUtil.discoverPlugins]
 */
@Suppress("unused")
@Target(AnnotationTarget.CLASS)
annotation class Plugin(
    /**
     * The name/id of the plugin
     *
     * This should be provided as lower-case, with words separated by underscores.
     *
     * Example: pixel_grid
     */
    // TODO: Plugin IDs could be guessed by using the lowercased name of the object they're attached to
    val value: String,
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
    val component: KClass<out PluginPanel> = Nothing::class,
    /**
     * The plugin IDs this plugin should load after
     */
    val dependencies: Array<String> = [],
    /**
     * The config for this plugin
     */
    val settings: KClass<out Config> = Nothing::class
)
