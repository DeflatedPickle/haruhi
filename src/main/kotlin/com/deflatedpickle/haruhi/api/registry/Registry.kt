/* Copyright (c) 2021 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.api.registry

interface Registry<K, V> {
    fun register(key: K, value: V)
    fun has(key: K): Boolean
    fun get(key: K): V?
    fun getAll(): Map<K, V>
}
