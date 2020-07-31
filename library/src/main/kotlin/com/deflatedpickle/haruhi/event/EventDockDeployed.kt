/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.event

import bibliothek.gui.dock.common.CGrid
import com.deflatedpickle.haruhi.api.event.AbstractEvent

/**
 * Called once a dock has been deployed
 */
@Suppress("unused")
object EventDockDeployed : AbstractEvent<CGrid>()
