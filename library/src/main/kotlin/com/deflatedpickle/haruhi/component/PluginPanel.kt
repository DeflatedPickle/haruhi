/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.component

import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.redraw.RedrawActive
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JScrollPane
import org.jdesktop.swingx.JXPanel

/**
 * A superclass of [JXPanel] providing utilities for Rawky
 */
abstract class PluginPanel : JXPanel() {
    /**
     * The plugin this component belongs to
     */
    lateinit var plugin: Plugin

    /**
     * The [JScrollPane] this panel was added to
     */
    lateinit var scrollPane: JScrollPane

    /**
     * A component to hold this one. Helpful for adding toolbars
     */
    lateinit var componentHolder: PluginPanelHolder

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
                }
            }
        }
    }

    override fun repaint() {
        super.invalidate()
        super.revalidate()

        super.repaint()
    }
}
