/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import javax.swing.JFrame

/**
 * Called when a window is shown
 */
object EventWindowShown : AbstractEvent<JFrame>()
