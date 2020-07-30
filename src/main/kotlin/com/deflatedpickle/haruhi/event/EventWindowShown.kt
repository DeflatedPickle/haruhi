package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import java.awt.Window

/**
 * Called when a window is shown
 */
object EventWindowShown : AbstractEvent<Window>()