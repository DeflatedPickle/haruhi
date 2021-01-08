/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import javax.swing.JMenuBar

/**
 * Called after a menu bar has been created
 */
@Suppress("unused")
object EventMenuBarBuild : AbstractEvent<JMenuBar>()
