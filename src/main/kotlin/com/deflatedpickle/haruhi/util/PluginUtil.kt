/* Copyright (c) 2020-2021 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.util

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import bibliothek.gui.dock.common.CLocation
import bibliothek.gui.dock.common.DefaultSingleCDockable
import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import com.deflatedpickle.haruhi.Haruhi
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.plugin.PluginType
import com.deflatedpickle.haruhi.api.util.ComponentPosition
import com.deflatedpickle.haruhi.api.util.ComponentPositionNormal
import com.deflatedpickle.haruhi.component.PluginPanel
import com.deflatedpickle.haruhi.component.PluginPanelHolder
import com.deflatedpickle.haruhi.event.EventDiscoverPlugin
import com.deflatedpickle.haruhi.event.EventLoadPlugin
import com.deflatedpickle.haruhi.event.EventPanelFocusGained
import com.deflatedpickle.haruhi.event.EventPanelFocusLost
import com.deflatedpickle.tosuto.ToastWindow
import com.github.zafarkhaja.semver.Version
import io.github.classgraph.ClassInfo
import java.awt.Window
import java.io.File
import javax.swing.JScrollPane
import kotlin.properties.Delegates
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.apache.logging.log4j.LogManager
import javax.swing.JFrame
import javax.swing.JToolBar

object PluginUtil {
    private val logger = LogManager.getLogger()

    /**
     * A list of found plugins, ordered for dependencies
     */
    val discoveredPlugins = mutableListOf<Plugin>()

    /**
     * A list of loaded plugins, unordered
     */
    val loadedPlugins = mutableListOf<Plugin>()

    /**
     * A list of plugins that threw errors during loading
     */
    val unloadedPlugins = mutableListOf<Plugin>()

    val slugToPlugin = mutableMapOf<String, Plugin>()

    /**
     * A map of plugins that were found when refreshed, paired with the object they were applied to
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val pluginMap = mutableMapOf<Plugin, ClassInfo>()

    fun valuesToSlug(author: String, value: String) =
        "${author.toLowerCase()}@${value.toLowerCase()}"

    /**
     * Constructs a slug from a [Plugin]
     */
    fun pluginToSlug(plugin: Plugin): String =
        valuesToSlug(plugin.author, plugin.value)

    /**
     * Get's a [Plugin] from a slug
     */
    fun slugToPlugin(slug: String): Plugin? {
        val author = slug.substringBefore("@")
        val value = slug.substringAfter("@").substringBefore("#")

        val slug = valuesToSlug(author, value)

        return if (slug in slugToPlugin) {
            slugToPlugin[slug]
        } else {
            slugToPlugin.filterValues {
                it.value == value && it.author.toLowerCase() == author
            }.values.first()
        }
    }

    fun slugToPlugin(author: String, value: String) = slugToPlugin("$author@$value")

    /**
     * Creates the plugins folder
     */
    // This can throw an error, but we won't umbrella it
    // in case the user needs to see it
    fun createPluginsFolder(): File =
        File("plugins").apply { mkdir() }

    /**
     * Calls [run] for all classes annotated with [Plugin], to find and validate each plugin
     */
    fun discoverPlugins() {
        // Caches the classes found as plugins
        var counter = 0

        for (plugin in ClassGraphUtil.scanResults.getClassesWithAnnotation(Plugin::class.qualifiedName)) {
            logger.debug("Found the plugin ${plugin.simpleName} from ${plugin.packageName}")

            val annotation = ClassGraphUtil.scanResults
                .getClassInfo(plugin.name)
                .getAnnotationInfo(Plugin::class.qualifiedName)
                .loadClassAndInstantiate() as Plugin

            this.discoveredPlugins.add(annotation)

            this.pluginMap[annotation] = plugin

            this.slugToPlugin[
                    this.pluginToSlug(annotation)
            ] = annotation

            EventDiscoverPlugin.trigger(annotation)
            counter++
        }

        this.logger.info("Found $counter plugin/s")
    }

    fun loadPlugins(validator: (Plugin) -> Boolean) {
        var counter = 0

        for (ann in discoveredPlugins) {
            if (validator(ann)) {
                this.pluginMap[ann]!!.loadClass().kotlin.objectInstance
                this.loadedPlugins.add(ann)
                EventLoadPlugin.trigger(ann)

                counter++
            } else {
                this.unloadedPlugins.add(ann)
            }
        }

        this.logger.info("Loaded $counter plugin/s")
    }
}
