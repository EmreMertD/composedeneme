import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// StorageManager Interface
interface StorageManager {
    fun <T> putValue(key: String, value: T)
    fun <T> getValueFlow(key: String, returnType: Class<T>): Flow<T?>  // Kotlin için Flow
    fun <T> getValueFlow(key: String, defaultValue: T?, returnType: Class<T>): Flow<T?>  // Kotlin için Flow
    fun <T> getValueCompletableFuture(key: String, returnType: Class<T>): CompletableFuture<Flow<T?>>  // Java için CompletableFuture
    fun <T> getValueCompletableFuture(key: String, defaultValue: T?, returnType: Class<T>): CompletableFuture<Flow<T?>>  // Java için CompletableFuture
    fun removeValue(key: String)  // Kotlin ve Java için senkron ve asenkron çalışabilir
    fun removePref(preferences: String)  // Kotlin ve Java için senkron ve asenkron çalışabilir
}

// DataStoreStorageManager class implementing StorageManager
class DataStoreStorageManager(private val context: Context) : StorageManager {

    // Java'da asenkron işlemler için ExecutorService
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    // DataStore'daki anahtarları almak için bir yardımcı metot
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

    // Kotlin'de asenkron removeValue metodu
    override fun removeValue(key: String) {
        if (isKotlinEnvironment()) {
            // Kotlin ortamında coroutine kullanımı
            GlobalScope.launch(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences.remove(stringPreferencesKey(key))
                }
            }
        } else {
            // Java ortamında ExecutorService ile kullanım
            executorService.execute {
                context.dataStore.edit { preferences ->
                    preferences.remove(stringPreferencesKey(key))
                }
            }
        }
    }

    // Kotlin'de asenkron removePref metodu
    override fun removePref(preferences: String) {
        if (isKotlinEnvironment()) {
            // Kotlin ortamında coroutine kullanımı
            GlobalScope.launch(Dispatchers.IO) {
                context.dataStore.edit {
                    it.clear()
                }
            }
        } else {
            // Java ortamında ExecutorService ile kullanım
            executorService.execute {
                context.dataStore.edit {
                    it.clear()
                }
            }
        }
    }

    // Kotlin ve Java için putValue metodu
    override fun <T> putValue(key: String, value: T) {
        if (isKotlinEnvironment()) {
            // Kotlin ortamında coroutine kullanımı
            GlobalScope.launch(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[getKey(key, value!!::class.java) as Preferences.Key<T>] = value
                }
            }
        } else {
            // Java ortamında ExecutorService ile kullanım
            executorService.execute {
                context.dataStore.edit { preferences ->
                    preferences[getKey(key, value!!::class.java) as Preferences.Key<T>] = value
                }
            }
        }
    }

    // Kotlin Flow için getValue metodu
    override fun <T> getValueFlow(key: String, returnType: Class<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[getKey(key, returnType)]
        }
    }

    // Kotlin Flow için getValue metodu (default değer ile)
    override fun <T> getValueFlow(key: String, defaultValue: T?, returnType: Class<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[getKey(key, returnType)] ?: defaultValue
        }
    }

    // Java için CompletableFuture metodu (Flow döndürme)
    override fun <T> getValueCompletableFuture(key: String, returnType: Class<T>): CompletableFuture<Flow<T?>> {
        return CompletableFuture.supplyAsync {
            context.dataStore.data.map { preferences ->
                preferences[getKey(key, returnType)]
            }
        }
    }

    // Java için CompletableFuture metodu (Flow döndürme, default değer ile)
    override fun <T> getValueCompletableFuture(key: String, defaultValue: T?, returnType: Class<T>): CompletableFuture<Flow<T?>> {
        return CompletableFuture.supplyAsync {
            context.dataStore.data.map { preferences ->
                preferences[getKey(key, returnType)] ?: defaultValue
            }
        }
    }

    // Ortamın Kotlin olup olmadığını kontrol eden yardımcı metod
    private fun isKotlinEnvironment(): Boolean {
        return try {
            Class.forName("kotlin.Unit")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}
