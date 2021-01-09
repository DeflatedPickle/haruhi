package com.deflatedpickle.haruhi.api.plugin

import com.deflatedpickle.haruhi.util.PluginUtil

class PluginCollection(
    val annotationPluginList: MutableList<Plugin> = mutableListOf(),
    val classPluginList: MutableList<HaruhiPlugin> = mutableListOf()
) : Collection<Any> {
    override val size: Int
        get() = this.annotationPluginList.size + this.classPluginList.size

    override fun contains(element: Any) = when(element) {
        is Plugin -> this.annotationPluginList.contains(element)
        is HaruhiPlugin -> this.classPluginList.contains(element)
        else -> throw IllegalArgumentException("Argument must be a Plugin or HaruhiPlugin")
    }

    override fun containsAll(elements: Collection<Any>): Boolean {
        var containsAll = true

        for(element in elements) {
            containsAll = containsAll && when(element) {
                is Plugin -> this.annotationPluginList.contains(element)
                is HaruhiPlugin -> this.classPluginList.contains(element)
                else -> throw IllegalArgumentException("Argument must be a Plugin or HaruhiPlugin")
            }
        }

        return containsAll
    }

    override fun isEmpty() = this.annotationPluginList.isEmpty() && this.classPluginList.isEmpty()

    fun add(element: Any) = when(element) {
        is Plugin -> this.annotationPluginList.add(element)
        is HaruhiPlugin -> this.classPluginList.add(element)
        else -> throw IllegalArgumentException("Argument must be a Plugin or HaruhiPlugin")
    }

    fun addAll(elements: Collection<Any>): Boolean {
        var addAll = true

        for(element in elements) {
            addAll = addAll && when(element) {
                is Plugin -> this.annotationPluginList.add(element)
                is HaruhiPlugin -> this.classPluginList.add(element)
                else -> throw IllegalArgumentException("Argument must be a Plugin or HaruhiPlugin")
            }
        }

        return addAll
    }

    override fun iterator(): Iterator<Any> = listOf(
        *this.annotationPluginList.toTypedArray(),
        *this.classPluginList.toTypedArray()
    ).listIterator()

    fun getSlugList(): List<String> {
        val list = mutableListOf<String>()

        for (i in this) {
            list.add(when(i) {
                is Plugin -> PluginUtil.pluginToSlug(i)
                is HaruhiPlugin -> i.getSlug()
                else -> throw IllegalArgumentException("Argument must be a Plugin or HaruhiPlugin")
            })
        }

        return list
    }
}