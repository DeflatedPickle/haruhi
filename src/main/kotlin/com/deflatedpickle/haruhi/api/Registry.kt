/* Copyright (c) 2021 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.api

import com.deflatedpickle.haruhi.api.registry.Registry

open class Registry<K, V> : Registry<K, V> {
    private val items = mutableMapOf<K, V>()

    override fun register(key: K, value: V): V {
        this.items[key] = value
        return value
    }

    override fun has(key: K): Boolean = this.items.containsKey(key)
    override fun get(key: K): V? = this.items[key]
    override fun getAll(): Map<K, V> = this.items

    override fun getOrRegister(key: K, value: V): V? {
        if (!this.has(key)) {
            this.register(key, value)
        }
        return this.get(key)
    }

    fun getOrRegister(key: K, value: () -> V): V? = getOrRegister(key, value())
}
