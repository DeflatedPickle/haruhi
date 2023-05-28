package com.deflatedpickle.haruhi.util

import bibliothek.gui.dock.common.CLocation
import bibliothek.gui.dock.common.DefaultSingleCDockable
import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import com.deflatedpickle.haruhi.Haruhi
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.util.ComponentPosition
import com.deflatedpickle.haruhi.api.util.ComponentPositionNormal
import com.deflatedpickle.haruhi.component.PluginPanel
import com.deflatedpickle.haruhi.component.PluginPanelHolder
import com.deflatedpickle.haruhi.event.EventPanelFocusGained
import com.deflatedpickle.haruhi.event.EventPanelFocusLost
import java.awt.Panel
import java.awt.Rectangle
import javax.swing.JScrollPane

object DockUtil {
    internal var current: String = "Default"
    internal val categories = mutableMapOf<String, MutableMap<PluginPanel, Rectangle>>()

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

        return panel
    }

    fun addToCategory(category: String, panel: PluginPanel, rectangle: Rectangle) {
        categories.getOrPut(category) { mutableMapOf() }[panel] = rectangle
    }

    fun deployCategory(category: String) {
        for ((p, r) in categories[category]!!) {
            Haruhi.grid.add(
                r.getX() / 100, r.getY() / 100,
                r.getWidth() / 100, r.getHeight() / 100,
                p.componentHolder.dock
            )
        }
    }
}