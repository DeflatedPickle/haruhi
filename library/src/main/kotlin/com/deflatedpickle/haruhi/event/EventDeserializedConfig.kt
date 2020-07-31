/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import java.io.File

/**
 * Called when a config is deserialized
 */
@Suppress("unused")
object EventDeserializedConfig : AbstractEvent<File>()
