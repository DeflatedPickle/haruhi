/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.api.plugin

@Suppress("unused")
object ClassPluginDependencyComparator : Comparator<HaruhiPlugin> {
    override fun compare(o1: HaruhiPlugin, o2: HaruhiPlugin): Int {
        if (o1.dependencies.contains(o2.getName()) || o1.type != PluginType.CORE_API) {
            if (o2.dependencies.contains(o2.getName())) {
                throw IllegalStateException("Circular dependency")
            }
            return 1
        }
        return 0
    }
}
