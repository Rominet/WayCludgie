package com.cryocrystal.waytocludgie.rxutils

import android.content.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.Observable
import java.io.File
import java.io.IOException


fun fileExists(context: Context, fileName: String): Boolean {
    val reminderFile = File(context.filesDir.toString() + "/" + fileName)
    return reminderFile.exists()
}


fun <T> loadListFromCache(context: Context, fileName: String, classContent: Class<*>): List<T>? {
    var cacheData: List<T>? = null
    if (fileExists(context, fileName)) {
        try {
            val fis = context.openFileInput(fileName)
            val mapper = jacksonObjectMapper()
            val type = mapper.typeFactory.constructCollectionType(List::class.java, classContent)
            cacheData = mapper.readValue<List<T>>(fis, type)
            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    return cacheData
}


fun <T> Observable<T>.saveToCache(context: Context, fileName: String): Observable<T> {
    return this.doOnNext {
        try {
            val fos = context.openFileOutput(fileName, android.content.Context.MODE_PRIVATE)
            jacksonObjectMapper().writeValue(fos, it)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}