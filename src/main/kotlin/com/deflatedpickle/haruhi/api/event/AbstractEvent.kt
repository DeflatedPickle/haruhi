/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.api.event

import com.pploder.events.SimpleEvent
import java.util.function.Consumer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

abstract class AbstractEvent<T : Any> : SimpleEvent<T>() {
    @Suppress("MemberVisibilityCanBePrivate")
    protected val logger: Logger = LogManager.getLogger(this::class.simpleName)

    override fun trigger(t: T) {
        this.logger.debug("This event was triggered with ${t::class}")
        super.trigger(t)
    }

    // You know that new event you're looking for?
    // Well, listen to this!
    override fun addListener(listener: Consumer<T>) {
        this.logger.debug("A listener was attached to this event")
        super.addListener(listener)
    }
}
