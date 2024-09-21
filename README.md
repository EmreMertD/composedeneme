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

    import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
class DataStoreStorageManager(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()) // Default olarak IO dispatcher ve SupervisorJob kullanıyoruz
) : StorageManager {

    // DataStore referansını doğrudan saklıyoruz
    private val dataStore = context.dataStore

    // Type-safe key generation
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

    // Asenkron veri alma işlemi
    override fun <T> getValueFlow(key: String, returnType: Class<T>): Flow<T?> {
        return dataStore.data
            .map { preferences -> 
                preferences[getKey(key, returnType)]
            }
            .catch { e -> 
                logError(e)
                emit(null) // Hata durumunda null değeri yayıyoruz
            }
    }

    // Asenkron veri alma işlemi, default değeri de işleyerek
    override fun <T> getValueFlow(key: String, defaultValue: T?, returnType: Class<T>): Flow<T?> {
        return dataStore.data
            .map { preferences -> 
                preferences[getKey(key, returnType)] ?: defaultValue
            }
            .catch { e -> 
                logError(e)
                emit(defaultValue) // Hata durumunda varsayılan değeri yayıyoruz
            }
    }

    // Veriyi arka planda kaydetmek için sadece scope.launch kullanıyoruz
    override fun <T> putValue(key: String, value: T) {
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[getKey(key, value!!::class.java) as Preferences.Key<T>] = value
            }
        }.invokeOnCompletion { exception ->
            exception?.let { logError(it) } // Hata varsa loglama
        }
    }

    // Veriyi arka planda silme işlemi
    override fun removeValue(key: String) {
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
            }
        }.invokeOnCompletion { exception ->
            exception?.let { logError(it) } // Hata varsa loglama
        }
    }

    // Tüm verileri arka planda temizleme işlemi
    override fun removePref(preferences: String) {
        scope.launch {
            context.dataStore.edit {
                it.clear()
            }
        }.invokeOnCompletion { exception ->
            exception?.let { logError(it) } // Hata varsa loglama
        }
    }

    // Hata loglama işlemi
    private fun logError(e: Throwable) {
        println("DataStore error: ${e.message}")
        e.printStackTrace()
    }

    // CoroutineScope'u manuel olarak kapatma metodu
    fun close() {
        scope.cancel()
    }
}

}
