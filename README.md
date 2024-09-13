override fun <T> putValue(key: String, value: T) {
    val dataStoreKey = when (value) {
        is String -> stringPreferencesKey(key)
        is Int -> intPreferencesKey(key)
        is Boolean -> booleanPreferencesKey(key)
        is Float -> floatPreferencesKey(key)
        is Long -> longPreferencesKey(key)
        is Set<*> -> {
            if (value.all { it is String }) {
                stringSetPreferencesKey(key)
            } else {
                throw IllegalArgumentException("Only Set<String> is supported")
            }
        }
        else -> throw IllegalArgumentException("Unsupported type")
    }

    // DataStore'a değer kaydetme
    runBlocking {
        context.dataStore.edit { preferences ->
            (preferences as MutablePreferences)[dataStoreKey as Preferences.Key<T>] = value
        }
    }
}
