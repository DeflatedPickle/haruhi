package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import javax.swing.JFrame

/**
 * Called when a window is shown
 */
object EventWindowShown : AbstractEvent<JFrame>()