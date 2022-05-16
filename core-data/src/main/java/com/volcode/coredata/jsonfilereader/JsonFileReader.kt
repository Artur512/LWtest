package com.volcode.coredata.jsonfilereader

import android.content.res.Resources


class JsonFileReader(private val resources: Resources) {

    fun readFile(fileId: Int): String {
        return this.resources.openRawResource(fileId)
            .bufferedReader().use { it.readText() }
    }
}