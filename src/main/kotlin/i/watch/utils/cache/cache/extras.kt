package i.watch.utils.cache.cache

inline fun <reified T : Any> IDataCacheManager.IDataCache.cache(variable: String, noinline calc: () -> T): T {
    return cache(variable, T::class, calc)
}
