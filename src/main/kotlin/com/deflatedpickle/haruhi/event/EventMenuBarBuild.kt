package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import javax.swing.JMenuBar

/**
 * Called after a menu bar has been created
 */
object EventMenuBarBuild : AbstractEvent<JMenuBar>()