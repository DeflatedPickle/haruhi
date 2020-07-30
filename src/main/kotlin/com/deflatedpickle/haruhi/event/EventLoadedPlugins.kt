package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.api.plugin.Plugin

/**
 * Called once all plugins have been, with an ordered list of the plugins
 */
object EventLoadedPlugins : AbstractEvent<Collection<Plugin>>()