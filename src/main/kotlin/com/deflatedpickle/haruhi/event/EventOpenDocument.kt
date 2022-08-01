/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import com.deflatedpickle.haruhi.api.util.Document
import java.io.File

/**
 * Called when a document is opened from a file
 */
@Suppress("unused")
object EventOpenDocument : AbstractEvent<Pair<Document, File>>()
