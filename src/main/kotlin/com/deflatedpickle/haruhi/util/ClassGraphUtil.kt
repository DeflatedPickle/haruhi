/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.util

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import java.io.File
import java.net.URLClassLoader
import org.apache.logging.log4j.LogManager

object ClassGraphUtil {
    private val logger = LogManager.getLogger(this::class.simpleName)

    lateinit var scanResults: ScanResult
        private set

    private val jars = File("plugins").listFiles { _, name ->
        name.endsWith(".jar")
    }?.map { it.toURI().toURL() } ?: listOf()

    @Suppress("MemberVisibilityCanBePrivate")
    val classLoader = URLClassLoader(jars.toTypedArray())

    init {
        if (!PluginUtil.isInDev) {
            if (jars.isEmpty()) {
                this.logger.warn("Found no plugin JARs")
            } else {
                this.logger.info("Found the plugin JARs: ${
                jars.map { it.file.split("/", "\\").last() }
                }")
            }
        }
    }

    fun refresh() {
        this.scanResults = if (PluginUtil.isInDev) {
            ClassGraph().enableAllInfo().scan()
        } else {
            ClassGraph()
                .enableAllInfo()
                .overrideClassLoaders(this.classLoader)
                .scan()
        }
    }
}
