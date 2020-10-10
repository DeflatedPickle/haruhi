package com.deflatedpickle.haruhi.api.lang

import java.util.*

open class Lang(
    prefix: String,
    lang: String? = null,
    classLoader: ClassLoader? = null
) {
    private val locale = Locale(lang ?: Locale.getDefault().language)
    private val bundle: ResourceBundle

    init {
        bundle = if (classLoader == null) {
            ResourceBundle.getBundle(
                "lang/${prefix}",
                locale
            )
        } else {
            ResourceBundle.getBundle(
                "lang/${prefix}",
                locale,
                classLoader
            )
        }
    }

    /**
     * Translate a key from this bundle
     */
    @Suppress("unused")
    fun trans/*rights*/(key: String): String = this.bundle.getString(key)
}