/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import javax.swing.JMenu

/**
 * Called after a menu has been created but before it has been added to the bar
 */
@Suppress("unused")
object EventMenuBuild : AbstractEvent<JMenu>()
