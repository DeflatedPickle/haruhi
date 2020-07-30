package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.api.util.Document

/**
 * Called when a window is shown
 */
object EventCreateDocument : AbstractEvent<Document>()