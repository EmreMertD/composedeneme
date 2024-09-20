import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// StorageManager Interface
interface StorageManager {
    fun <T> putValue(key: String, value: T)
    fun <T> getValueFlow(key: String, returnType: Class<T>): Flow<T?>
    fun <T> getValueFlow(key: String, defaultValue: T?, returnType: Class<T>): Flow<T?>
    fun removeValue(key: String)
    fun removePref(preferences: String)
}

// DataStoreStorageManager class implementing StorageManager
@JvmSynthetic
class DataStoreStorageManager(private val context: Context) : StorageManager {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    // DataStore key generation method
    private fun <T> getKey(key: String, returnType: Class<T>): Preferences.Key<T> {
        return when (returnType) {
            String::class.java -> stringPreferencesKey(key) as Preferences.Key<T>
            Int::class.java -> intPreferencesKey(key) as Preferences.Key<T>
            Boolean::class.java -> booleanPreferencesKey(key) as Preferences.Key<T>
            Float::class.java -> floatPreferencesKey(key) as Preferences.Key<T>
            Long::class.java -> longPreferencesKey(key) as Preferences.Key<T>
            else -> throw IllegalArgumentException("Unsupported type: ${returnType.name}")
        }
    }

    // Kotlin Flow implementation for getValue
    override fun <T> getValueFlow(key: String, returnType: Class<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[getKey(key, returnType)]
        }
    }

    // Kotlin Flow with default value
    override fun <T> getValueFlow(key: String, defaultValue: T?, returnType: Class<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[getKey(key, returnType)] ?: defaultValue
        }
    }

    // Put value method for both Kotlin and Java
    override fun <T> putValue(key: String, value: T) {
        GlobalScope.launch(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[getKey(key, value!!::class.java) as Preferences.Key<T>] = value
            }
        }
    }

    // Remove value method
    override fun removeValue(key: String) {
        GlobalScope.launch(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
            }
        }
    }

    // Remove all preferences
    override fun removePref(preferences: String) {
        GlobalScope.launch(Dispatchers.IO) {
            context.dataStore.edit {
                it.clear()
            }
        }
    }
}
