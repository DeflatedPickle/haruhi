/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import java.awt.Window

/**
 * Called when a window is shown
 */
@Suppress("unused")
object EventWindowShown : AbstractEvent<Window>()
