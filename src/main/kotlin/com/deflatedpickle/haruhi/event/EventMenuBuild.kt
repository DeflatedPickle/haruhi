package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import javax.swing.JMenu

/**
 * Called after a menu has been created but before it has been added to the bar
 */
object EventMenuBuild : AbstractEvent<JMenu>()