package com.deflatedpickle.haruhi.api.event

import com.deflatedpickle.haruhi.api.plugin.Plugin

interface Packet {
    val time: Long
    val source: Plugin
}