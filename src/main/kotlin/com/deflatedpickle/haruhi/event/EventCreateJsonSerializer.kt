/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.api.util.Document
import com.deflatedpickle.haruhi.api.util.DocumentCreationType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

/**
 * Called when a window is shown
 */
@Suppress("unused")
object EventCreateJsonSerializer : AbstractEvent<JsonBuilder>()
