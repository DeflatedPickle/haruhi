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
import javax.swing.JScrollPane

object DockUtil {
    private var currentX = 0.0
    private var currentY = 0.0

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

        if (plugin.componentVisible) {
            Haruhi.grid.add(
                this.currentX, this.currentY,
                plugin.componentWidth, plugin.componentHeight,
                panel.componentHolder.dock
            )
        } else {
            Haruhi.control.addDockable(panel.componentHolder.dock as DefaultSingleCDockable)
        }

        when (plugin.componentNormalPosition) {
            ComponentPositionNormal.SOUTH -> this.currentY += plugin.componentHeight
            ComponentPositionNormal.WEST -> this.currentX += plugin.componentWidth
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