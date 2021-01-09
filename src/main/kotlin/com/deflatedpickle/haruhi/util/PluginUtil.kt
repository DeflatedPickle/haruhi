/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.util

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import bibliothek.gui.dock.common.CLocation
import bibliothek.gui.dock.common.DefaultSingleCDockable
import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import com.deflatedpickle.haruhi.api.plugin.HaruhiPlugin
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.plugin.PluginCollection
import com.deflatedpickle.haruhi.api.plugin.PluginType
import com.deflatedpickle.haruhi.api.util.ComponentPosition
import com.deflatedpickle.haruhi.api.util.ComponentPositionNormal
import com.deflatedpickle.haruhi.component.PluginPanel
import com.deflatedpickle.haruhi.component.PluginPanelHolder
import com.deflatedpickle.haruhi.event.EventDiscoverPlugin
import com.deflatedpickle.haruhi.event.EventLoadAnnotationPlugin
import com.deflatedpickle.haruhi.event.EventLoadClassPlugin
import com.deflatedpickle.haruhi.event.EventPanelFocusGained
import com.deflatedpickle.haruhi.event.EventPanelFocusLost
import com.deflatedpickle.tosuto.ToastWindow
import io.github.classgraph.ClassInfo
import java.awt.Window
import java.io.File
import java.lang.IllegalArgumentException
import javax.swing.JScrollPane
import kotlin.properties.Delegates
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.apache.logging.log4j.LogManager

@Suppress("MemberVisibilityCanBePrivate", "unused")
object PluginUtil {
    private val logger = LogManager.getLogger()

    var isInDev by Delegates.notNull<Boolean>()

    lateinit var window: Window
    lateinit var toastWindow: ToastWindow
    lateinit var control: CControl
    lateinit var grid: CGrid

    private var currentX = 0.0
    private var currentY = 0.0

    /**
     * A list of found plugins, ordered for dependencies
     */
    val discoveredPlugins = PluginCollection()

    /**
     * A list of loaded plugins, unordered
     */
    val loadedPlugins = PluginCollection()

    /**
     * A list of plugins that threw errors during loading
     */
    val unloadedPlugins = PluginCollection()

    /**
     * A map of annotation plugins and their slugs
     */
    val slugToPlugin = mutableMapOf<String, Plugin>()

    // These are used to validate different strings
    private val versionRegex = Regex("[0-9].[0-9].[0-9]")
    private val slugRegex = Regex("[a-z]+@[a-z_]+#[0-9].[0-9].[0-9]")

    /**
     * A map of annotation plugins that were found when refreshed, paired with the object they were applied to
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val pluginMap = mutableMapOf<Plugin, ClassInfo>()

    /**
     * A map of class plugins that were found when refreshed, paired with the object they were applied to
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val classPluginMap = mutableMapOf<HaruhiPlugin, ClassInfo>()

    /**
     * Constructs a slug from a [Plugin]
     */
    fun pluginToSlug(plugin: Plugin): String =
        "${plugin.author.toLowerCase()}@${plugin.value.toLowerCase()}#${plugin.version}"

    /**
     * Creates the plugins folder
     */
    // This can throw an error, but we won't umbrella it
    // in case the user needs to see it
    fun createPluginsFolder(): File =
        File("plugins").apply { mkdir() }

    /**
     * Searches the class graph for all classes annotated with [Plugin], then loads them and dispatches events
     */
    @Deprecated("Replaced with a loader for the abstract class plugins")
    fun discoverAnnotationPlugins() {
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

        this.logger.info("Found $counter annotation-based plugin/s")
    }

    /**
     * Searches the class graph for all
     */
    fun discoverSubclassPlugins() {
        // Caches the classes found as plugins
        var counter = 0

        for (plugin in ClassGraphUtil.scanResults.getSubclasses(HaruhiPlugin::class.qualifiedName)) {
            logger.debug("Found the plugin ${plugin.simpleName} from ${plugin.packageName}")

            val instance = ClassGraphUtil.scanResults
                .getClassInfo(plugin.name)
                .loadClass()
                .kotlin
                .objectInstance as HaruhiPlugin

            this.discoveredPlugins.add(instance)

            counter++
        }

        this.logger.info("Found $counter abstract class-based plugin/s")
    }

    fun loadPlugins(validator: (Plugin) -> Boolean) {
        var counter = 0

        for (ann in discoveredPlugins) {
            when (ann) {
                is Plugin -> if (validator(ann)) {
                    this.pluginMap[ann]!!.loadClass().kotlin.objectInstance
                    this.loadedPlugins.add(ann)
                    EventLoadAnnotationPlugin.trigger(ann)

                    counter++
                } else {
                    this.unloadedPlugins.add(ann)
                }
                is HaruhiPlugin -> {
                    // Class plugins already get loaded when they're discovered, so we can just add it
                    this.loadedPlugins.add(ann)
                    EventLoadClassPlugin.trigger(ann)

                    counter++
                }
            }
        }

        this.logger.info("Loaded $counter plugin/s")
    }

    /**
     * Checks the [plugin]'s author isn't blank
     */
    fun validateAuthor(plugin: Plugin): Boolean {
        if (plugin.author.isNotBlank()) {
            return false
        }

        this.logger.warn("This plugin '${plugin.author}' is blank")
        return false
    }

    /**
     * Checks [plugin]'s version matches [versionRegex]
     */
    fun validateVersion(plugin: Plugin): Boolean {
        if (this.versionRegex.containsMatchIn(plugin.version)) {
            return true
        }

        this.logger.warn("The plugin ${plugin.value} doesn't have a valid version. Please use a semantic version")
        return false
    }

    /**
     * Checks [plugin]'s description includes a line break
     */
    fun validateDescription(plugin: Plugin): Boolean {
        if (plugin.description.contains("<br>")) {
            return true
        }

        this.logger.warn("The plugin ${plugin.value} doesn't contain a break tag")
        return false
    }

    /**
     * Checks [plugin]'s type has the proper field
     */
    fun validateType(plugin: Plugin): Boolean =
        when (plugin.type) {
            PluginType.CORE_API,
            PluginType.LAUNCHER,
            PluginType.API,
            PluginType.MENU_COMMAND,
            PluginType.DIALOG,
            PluginType.OTHER -> true

            PluginType.SETTING -> plugin.settings != Nothing::class
            PluginType.COMPONENT -> plugin.component != Nothing::class
        }

    /**
     * Checks [plugin]'s dependency slug's matches [slugRegex]
     */
    fun validateDependencySlug(plugin: Plugin): Boolean {
        for (dep in plugin.dependencies) {
            if (!this.slugRegex.matches(dep)) {
                return false
            }
        }

        return true
    }

    /**
     * Checks [plugin]'s dependencies exist
     */
    fun validateDependencyExistence(plugin: Plugin): Boolean {
        val suggestions = mutableMapOf<String, MutableList<String>>()

        for (dep in plugin.dependencies) {
            if (dep !in this.discoveredPlugins.map {
                    when (it) {
                        is Plugin -> this.pluginToSlug(it)
                        is HaruhiPlugin -> it.getSlug()
                        else -> IllegalArgumentException("Argument must be a Plugin or HaruhiPlugin")
                    }
                }) {
                suggestions.putIfAbsent(dep, mutableListOf())

                for (checkDep in this.pluginMap.keys) {
                    if (FuzzySearch.partialRatio(dep, checkDep.value) > 60) {
                        suggestions[dep]!!.add(checkDep.value)
                    }
                }
            }
        }

        if (suggestions.isEmpty()) {
            return true
        }

        for ((k, v) in suggestions) {
            this.logger.warn("The plugin ID \"$k\" wasn't found. Did you mean one of these? $v")
        }

        return false
    }

    /**
     * Creates all plugin components
     */
    fun createComponent(plugin: Plugin, panel: PluginPanel): PluginPanel {
        panel.plugin = plugin
        panel.scrollPane = JScrollPane(panel)

        panel.componentHolder = PluginPanelHolder()
        panel.componentHolder.dock = DefaultSingleCDockable(
            plugin.value,
            plugin.value.replace("_", " ").capitalize(),
            panel.scrollPane
        )
        panel.componentHolder.dock.addFocusListener(object : CFocusListener {
            override fun focusLost(dockable: CDockable) {
                EventPanelFocusLost.trigger(
                    ((dockable as DefaultSingleCDockable)
                        .contentPane
                        .getComponent(0) as JScrollPane)
                        .viewport
                        .view as PluginPanel
                )
            }

            override fun focusGained(dockable: CDockable) {
                EventPanelFocusGained.trigger(
                    ((dockable as DefaultSingleCDockable)
                        .contentPane
                        .getComponent(0) as JScrollPane)
                        .viewport
                        .view as PluginPanel
                )
            }
        })

        this.grid.add(
            this.currentX, this.currentY,
            plugin.componentWidth, plugin.componentHeight,
            panel.componentHolder.dock
        )

        when (plugin.componentNormalPosition) {
            ComponentPositionNormal.SOUTH -> this.currentY += plugin.componentWidth
            ComponentPositionNormal.WEST -> this.currentX += plugin.componentHeight
        }

        panel.componentHolder.dock.setLocation(
            when (plugin.componentMinimizedPosition) {
                ComponentPosition.NORTH -> CLocation.base().minimalNorth()
                ComponentPosition.EAST -> CLocation.base().minimalEast()
                ComponentPosition.SOUTH -> CLocation.base().minimalSouth()
                ComponentPosition.WEST -> CLocation.base().minimalWest()
            }
        )

        panel.componentHolder.dock.isVisible = true

        return panel
    }

    fun createComponent(plugin: HaruhiPlugin, panel: PluginPanel): PluginPanel {
        panel.classPlugin = plugin
        panel.scrollPane = JScrollPane(panel)

        panel.componentHolder = PluginPanelHolder()
        panel.componentHolder.dock = DefaultSingleCDockable(
            plugin.getName(),
            plugin.getName().replace("_", " ").capitalize(),
            panel.scrollPane
        )
        panel.componentHolder.dock.addFocusListener(object : CFocusListener {
            override fun focusLost(dockable: CDockable) {
                EventPanelFocusLost.trigger(
                    ((dockable as DefaultSingleCDockable)
                        .contentPane
                        .getComponent(0) as JScrollPane)
                        .viewport
                        .view as PluginPanel
                )
            }

            override fun focusGained(dockable: CDockable) {
                EventPanelFocusGained.trigger(
                    ((dockable as DefaultSingleCDockable)
                        .contentPane
                        .getComponent(0) as JScrollPane)
                        .viewport
                        .view as PluginPanel
                )
            }
        })

        this.grid.add(
            this.currentX, this.currentY,
            plugin.componentWidth, plugin.componentHeight,
            panel.componentHolder.dock
        )

        when (plugin.componentNormalPosition) {
            ComponentPositionNormal.SOUTH -> this.currentY += plugin.componentWidth
            ComponentPositionNormal.WEST -> this.currentX += plugin.componentHeight
        }

        panel.componentHolder.dock.setLocation(
            when (plugin.componentMinimizedPosition) {
                ComponentPosition.NORTH -> CLocation.base().minimalNorth()
                ComponentPosition.EAST -> CLocation.base().minimalEast()
                ComponentPosition.SOUTH -> CLocation.base().minimalSouth()
                ComponentPosition.WEST -> CLocation.base().minimalWest()
            }
        )

        panel.componentHolder.dock.isVisible = true

        return panel
    }
}
