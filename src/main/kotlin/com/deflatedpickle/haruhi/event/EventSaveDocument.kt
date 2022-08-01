/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.api.util.Document
import java.io.File

/**
 * Called when a window is shown
 */
@Suppress("unused")
object EventSaveDocument : AbstractEvent<Pair<Document, File>>()
