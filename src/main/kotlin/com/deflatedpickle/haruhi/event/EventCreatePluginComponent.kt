package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.component.PluginPanel

/**
 * Called when a plugin component is created
 */
object EventCreatePluginComponent : AbstractEvent<PluginPanel>()