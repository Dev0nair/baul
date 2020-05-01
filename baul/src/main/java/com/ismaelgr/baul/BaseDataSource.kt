package com.ismael.baulcomponent

abstract class BaseDataSource {
    open var sync: Boolean = false

    abstract fun refreshData()
    abstract fun <T> refreshData(clazz: Class<T>)
    abstract fun toCache(): HashMap<String, Object>
    abstract fun fromCacheData(data: HashMap<String, Object>)
}