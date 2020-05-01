package com.ismael.baulcomponent

import android.util.Log
import java.io.*

class Baul(projectFilesPath: String) {

    private var data: HashMap<String, BaseDataSource> = HashMap()
    private val CACHE_FILENAME = "$projectFilesPath/preloadData.baul"

    init {
        Log.i("baulClass: ", "init");
        createCacheIfNotExists()
    }

    fun setDataSources(vararg dataSource: Pair<String, BaseDataSource>) {
        Log.i("baulClass: ", "setting data sources");
        dataSource.forEach { pair ->
            data.put(pair.first, pair.second)
        }
    }

    fun loadFromCache() {
        Log.i("baulClass: ", "reading from cache");
        val cacheFile = File(CACHE_FILENAME)
        if (cacheFile.length() > 0) {
            val editFile = ObjectInputStream(FileInputStream(cacheFile))
            editFile.readObject()?.run {
                if (this is HashMap<*, *>) {
                    this.forEach { pair ->
                        data[pair.key]!!.fromCacheData(pair.value as HashMap<String, Object>)
                    }
                }

            }
            editFile.close()
        }
    }

    fun saveOnCache() {
        Log.i("baulClass: ", "saving on cache");
        resetCache()
        val editFile = ObjectOutputStream(FileOutputStream(File(CACHE_FILENAME)))

        val hashToSafe = hashMapOf<String, HashMap<String, Object>>()

        data.forEach { pair ->
            hashToSafe.put(pair.key, pair.value.toCache())
        }

        editFile.writeObject(hashToSafe)
        editFile.close()
    }

    fun getDataSource(dataKey: String): BaseDataSource? = data[dataKey]

    private fun clearDataCache() {
        val cacheFile = File(CACHE_FILENAME)
        if (cacheFile.exists()) cacheFile.delete()
    }

    private fun createCacheIfNotExists() {
        val cacheFile = File(CACHE_FILENAME)
        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
            cacheFile.setWritable(true, true)
        }
    }

    fun resetCache() {
        Log.i("baulClass: ", "removing baul");
        clearDataCache()
        createCacheIfNotExists()
    }
}