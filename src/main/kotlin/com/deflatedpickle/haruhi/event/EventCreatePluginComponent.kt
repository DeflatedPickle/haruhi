/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.component.PluginPanel

/**
 * Called when a plugin component is created
 */
@Suppress("unused")
object EventCreatePluginComponent : AbstractEvent<PluginPanel>()
