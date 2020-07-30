package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.api.plugin.Plugin

/**
 * Called when the load order of plugins is sorted
 */
object EventSortedPluginLoadOrder : AbstractEvent<List<Plugin>>()