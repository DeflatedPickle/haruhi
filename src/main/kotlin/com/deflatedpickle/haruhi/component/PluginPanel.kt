/* Copyright (c) 2020 DeflatedPickle under the MIT license */

@file:Suppress("LeakingThis")

package com.deflatedpickle.haruhi.component

import ModernDocking.Dockable
import ModernDocking.DockableStyle
import ModernDocking.Docking
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.redraw.RedrawActive
import com.deflatedpickle.haruhi.util.PluginUtil
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JScrollPane
import org.jdesktop.swingx.JXPanel
import java.awt.Rectangle
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.Icon

/**
 * A superclass of [JXPanel] providing utilities for Rawky
 */
abstract class PluginPanel : JXPanel(), Dockable {
    /**
     * The plugin this component belongs to
     */
    lateinit var plugin: Plugin

    /**
     * The [JScrollPane] this panel was added to
     */
    lateinit var scrollPane: JScrollPane

    init {
        this.isOpaque = true

        this.processAnnotations()
    }

    private fun processAnnotations() {
        for (annotation in this::class.annotations) {
            when (annotation) {
                is RedrawActive -> {
                    this.addMouseListener(object : MouseAdapter() {
                        override fun mouseClicked(e: MouseEvent) = repaint()
                        override fun mousePressed(e: MouseEvent) = repaint()
                        override fun mouseMoved(e: MouseEvent) = repaint()
                        override fun mouseDragged(e: MouseEvent) = repaint()
                    }.apply { addMouseMotionListener(this) })

                    this.addKeyListener(object : KeyAdapter() {
                        override fun keyPressed(e: KeyEvent) = repaint()
                        override fun keyReleased(e: KeyEvent) = repaint()
                        override fun keyTyped(e: KeyEvent) = repaint()
                    })
                }
            }
        }
    }

    override fun repaint() {
        super.invalidate()
        super.revalidate()

        super.repaint()
    }

    // Start implementing Dockable

    override fun getPersistentID() = PluginUtil.pluginToSlug(plugin)
    override fun getType() = 0
    override fun getTabText() = plugin.value.split("_").joinToString(" ") { it.capitalize() }
    override fun getIcon() = null
    override fun isFloatingAllowed() = true
    override fun shouldLimitToRoot() = false
    override fun getStyle() = DockableStyle.BOTH
    override fun canBeClosed() = true
    override fun allowPinning() = true
    override fun allowMinMax() = true
    override fun hasMoreOptions() = false

    // Finish Dockable
}
