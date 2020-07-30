package com.deflatedpickle.haruhi.event

import bibliothek.gui.dock.common.CGrid
import com.deflatedpickle.haruhi.api.event.AbstractEvent

/**
 * Called once a dock has been deployed
 */
object EventDockDeployed : AbstractEvent<CGrid>()