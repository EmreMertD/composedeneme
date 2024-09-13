@Suppress("UNCHECKED_CAST")
private fun <T> getKey(key: String, returnType: Class<T>): Preferences.Key<T> {
    return when {
        returnType.isAssignableFrom(String::class.java) -> stringPreferencesKey(key) as Preferences.Key<T>
        returnType.isAssignableFrom(Int::class.java) || returnType.isAssignableFrom(Integer::class.java) -> intPreferencesKey(key) as Preferences.Key<T>
        returnType.isAssignableFrom(Boolean::class.java) -> booleanPreferencesKey(key) as Preferences.Key<T>
        returnType.isAssignableFrom(Float::class.java) -> floatPreferencesKey(key) as Preferences.Key<T>
        returnType.isAssignableFrom(Long::class.java) -> longPreferencesKey(key) as Preferences.Key<T>
        returnType.isAssignableFrom(Set::class.java) -> stringSetPreferencesKey(key) as Preferences.Key<T>
        else -> throw IllegalArgumentException("Unsupported type: ${returnType.name}")
    }
}
