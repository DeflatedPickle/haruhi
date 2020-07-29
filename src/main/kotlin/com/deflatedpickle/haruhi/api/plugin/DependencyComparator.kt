/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.api.plugin

@Suppress("unused")
object DependencyComparator : Comparator<Plugin> {
    override fun compare(o1: Plugin, o2: Plugin): Int {
        if (o1.dependencies.contains(o2.value) || o1.type != PluginType.CORE_API) {
            if (o2.dependencies.contains(o2.value)) {
                throw IllegalStateException("Circular dependency")
            }
            return 1
        }
        return 0
    }
}
