package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import java.io.File

/**
 * Called when a config is deserialized
 */
object EventDeserializedConfig : AbstractEvent<File>()