package com.deflatedpickle.haruhi.util

import com.deflatedpickle.haruhi.Haruhi
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.plugin.PluginType
import com.github.zafarkhaja.semver.Version
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.apache.logging.log4j.LogManager

object ValidateUtil {
    private val logger = LogManager.getLogger()

    /**
     * Checks [plugin]'s version matches [versionRegex]
     */
    fun validateVersion(plugin: Plugin): Boolean {
        if (Haruhi.versionRegex.containsMatchIn(plugin.version)) {
            return true
        }

        this.logger.warn("The plugin ${plugin.value} doesn't have a valid version. Please use a semantic version")
        return false
    }

    /**
     * Checks [plugin]'s description includes a line break
     */
    fun validateDescription(plugin: Plugin): Boolean {
        if (plugin.description.contains("<br>")) {
            return true
        }

        this.logger.warn("The plugin ${plugin.value} doesn't contain a break tag")
        return false
    }

    /**
     * Checks [plugin]'s type has the proper field
     */
    fun validateType(plugin: Plugin): Boolean =
        when (plugin.type) {
            PluginType.CORE_API,
            PluginType.LAUNCHER,
            PluginType.API,
            PluginType.MENU_COMMAND,
            PluginType.DIALOG,
            PluginType.OTHER -> true

            PluginType.SETTING -> plugin.settings != Nothing::class
            PluginType.COMPONENT -> plugin.component != Nothing::class
        }

    /**
     * Checks [plugin]'s dependency slug's matches [slugRegex]
     */
    fun validateDependencySlug(plugin: Plugin): Boolean {
        for (dep in plugin.dependencies) {
            if (!Haruhi.slugRegex.matches(dep)) {
                return false
            }
        }

        return true
    }

    /**
     * Checks [plugin]'s dependencies exist
     */
    fun validateDependencyExistence(plugin: Plugin): Boolean {
        val suggestions = mutableMapOf<String, MutableList<String>>()

        for (dep in plugin.dependencies) {
            // The version might be a symver,
            // in which case it wouldn't exist in the discovered plugins,
            // so we test for it first
            for (plug in PluginUtil.discoveredPlugins) {
                val author = dep.substringBefore("@")
                val value = dep.substringAfter("@").substringBefore("#")
                val version = dep.substringAfter("#")

                if (plug.author.toLowerCase() == author && plug.value == value) {
                    val symVer = Version.valueOf(plug.version)

                    if (symVer.satisfies(version)) {
                        return true
                    }
                }
            }

            if (dep !in PluginUtil.discoveredPlugins.map { PluginUtil.pluginToSlug(it) }) {
                suggestions.putIfAbsent(dep, mutableListOf())

                for (checkDep in PluginUtil.pluginMap.keys) {
                    if (FuzzySearch.partialRatio(dep, checkDep.value) > 60) {
                        suggestions[dep]!!.add(checkDep.value)
                    }
                }
            }
        }

        if (suggestions.isEmpty()) {
            return true
        }

        for ((k, v) in suggestions) {
            this.logger.warn("The plugin ID \"$k\" wasn't found. Did you mean one of these? $v")
        }

        return false
    }
}