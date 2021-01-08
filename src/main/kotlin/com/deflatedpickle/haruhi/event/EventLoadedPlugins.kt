/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.api.plugin.Plugin

/**
 * Called once all plugins have been, with an ordered list of the plugins
 */
@Suppress("unused")
object EventLoadedPlugins : AbstractEvent<Collection<Plugin>>()
