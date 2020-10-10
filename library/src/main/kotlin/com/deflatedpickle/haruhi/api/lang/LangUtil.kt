package com.deflatedpickle.haruhi.api.lang

@Suppress("unused")
object LangUtil {
    private val idToLang = mutableMapOf<String, Lang>()

    fun getLang(id: String): Lang = this.idToLang[id]!!

    fun addLang(id: String, lang: Lang) {
        this.idToLang[id] = lang
    }
}