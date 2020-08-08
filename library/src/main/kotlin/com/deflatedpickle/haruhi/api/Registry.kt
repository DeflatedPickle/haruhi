package com.deflatedpickle.haruhi.api

import com.deflatedpickle.haruhi.api.registry.Registry

open class Registry<K, V> : Registry<K, V> {
    private val items = mutableMapOf<K, V>()

    override fun register(key: K, value: V) {
        this.items[key] = value
    }

    override fun has(key: K): Boolean = this.items.containsKey(key)
    override fun get(key: K): V? = this.items[key]
    override fun getAll(): Map<K, V> = this.items
}