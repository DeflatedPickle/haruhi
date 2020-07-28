/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.window

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import com.deflatedpickle.haruhi.event.EventWindowShown
import javax.swing.JFrame

object Window : JFrame() {
    @Suppress("MemberVisibilityCanBePrivate")
    val control = CControl(this)

    @Suppress("MemberVisibilityCanBePrivate")
    val grid = CGrid(control)

    init {
        this.defaultCloseOperation = EXIT_ON_CLOSE

        this.add(control.contentArea)

        this.pack()
    }

    fun deploy() {
        control.contentArea.deploy(grid)
    }

    override fun setVisible(b: Boolean) {
        super.setVisible(b)
        EventWindowShown.trigger(this)
    }
}
