/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import com.deflatedpickle.haruhi.api.event.AbstractEvent
import java.io.File

/**
 * Called when a file is created
 */
@Suppress("unused")
object EventCreateFile : AbstractEvent<File>()
