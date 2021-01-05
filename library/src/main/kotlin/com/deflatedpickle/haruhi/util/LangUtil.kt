/* Copyright (c) 2021 DeflatedPickle under the MIT license */

package com.deflatedpickle.haruhi.util

import com.deflatedpickle.haruhi.api.lang.Lang

@Suppress("unused")
object LangUtil {
    private val idToLang = mutableMapOf<String, Lang>()

    fun getLang(id: String): Lang = this.idToLang[id]!!

    fun addLang(id: String, lang: Lang) {
        this.idToLang[id] = lang
    }
}
