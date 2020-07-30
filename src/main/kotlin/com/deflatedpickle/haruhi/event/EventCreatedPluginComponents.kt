package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.component.PluginPanel

/**
 * Called when all plugin components have been created
 */
object EventCreatedPluginComponents : AbstractEvent<List<PluginPanel>>()