/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.api.plugin.Plugin

/**
 * Called when a plugin is found
 */
object EventDiscoverPlugin : AbstractEvent<Plugin>()
