import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// DataStore instance'ı
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

interface StorageManager {
    fun <T> putValue(key: String, value: T)
    fun <T> getValue(key: String, returnType: Class<T>): T?
    fun <T> getValue(key: String, defaultValue: T, returnType: Class<T>): T?
    fun removeValue(key: String)
    fun removePref(preferences: String)
}

class PreferencesDataStoreManager(private val context: Context) : StorageManager {

    // Değer tipine göre ilgili DataStore anahtarını oluşturur
    private fun <T> getKey(key: String, returnType: Class<T>): Preferences.Key<T> {
        // returnType parametresine göre ilgili DataStore anahtarını döner
        return when (returnType) {
            String::class.java -> stringPreferencesKey(key) as Preferences.Key<T>
            Int::class.java -> intPreferencesKey(key) as Preferences.Key<T>
            Boolean::class.java -> booleanPreferencesKey(key) as Preferences.Key<T>
            Float::class.java -> floatPreferencesKey(key) as Preferences.Key<T>
            Long::class.java -> longPreferencesKey(key) as Preferences.Key<T>
            else -> throw IllegalArgumentException("Unsupported type: ${returnType.name}")
        }
    }

    // Değer depolama
    override fun <T> putValue(key: String, value: T) {
        val dataStoreKey = getKey(key, value!!::class.java)
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[dataStoreKey] = value
            }
        }
    }

    // Değer okuma (varsayılan değer olmadan)
    override fun <T> getValue(key: String, returnType: Class<T>): T? {
        val dataStoreKey = getKey(key, returnType)
        return runBlocking {
            context.dataStore.data
                .map { preferences ->
                    preferences[dataStoreKey]
                }.first()
        }
    }

    // Değer okuma (varsayılan değer ile)
    override fun <T> getValue(key: String, defaultValue: T, returnType: Class<T>): T? {
        val dataStoreKey = getKey(key, returnType)
        return runBlocking {
            context.dataStore.data
                .map { preferences ->
                    preferences[dataStoreKey] ?: defaultValue
                }.first()
        }
    }

    // Değer silme
    override fun removeValue(key: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
            }
        }
    }

    // Belirli bir DataStore'u (preferences) kaldırma
    override fun removePref(preferences: String) {
        runBlocking {
            context.dataStore.edit {
                it.clear()
            }
        }
    }
}
