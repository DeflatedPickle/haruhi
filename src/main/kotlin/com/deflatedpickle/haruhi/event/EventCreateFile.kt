package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import java.io.File

/**
 * Called when a file is created
 */
object EventCreateFile : AbstractEvent<File>()