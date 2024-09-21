'''
    import android.content.Context
    import androidx.datastore.preferences.core.*
    import androidx.datastore.preferences.preferencesDataStore
    import kotlinx.coroutines.*
    import kotlinx.coroutines.flow.*
    
    // StorageManager Interface
    interface StorageManager {
        fun <T> putValue(key: String, value: T)
        fun putValues(values: Map<String, Any>)
        fun <T> getValueFlow(key: String, returnType: Class<T>): Flow<T?>
        fun <T> getValueFlow(key: String, defaultValue: T?, returnType: Class<T>): Flow<T?>
        fun removeValue(key: String)
        fun removeValues(keys: List<String>)
        fun removePref()
    }
    
    // DataStoreStorageManager class implementing StorageManager
    @JvmSynthetic
    class DataStoreStorageManager(
        private val context: Context,
        private val prefName: String, // Kullanıcı tarafından belirlenen DataStore adı
        private val scope: CoroutineScope // Dışarıdan geçirilen CoroutineScope
    ) : StorageManager {

    // Context uzantısı üzerinden prefName ile dinamik olarak dataStore'a erişim
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = prefName)

    // DataStore referansı
    private val dataStore = context.dataStore

    // Type-safe key generation methodu
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

    // Asenkron veri alma işlemi, Flow döndürür
    override fun <T> getValueFlow(key: String, returnType: Class<T>): Flow<T?> {
        return dataStore.data
            .map { preferences -> 
                preferences[getKey(key, returnType)]
            }
            .catch { e -> 
                logError(e)
                emit(null) // Hata durumunda null değeri yayıyoruz
            }
            .flowOn(Dispatchers.IO) // Yukarı akış işlemlerinin IO dispatcher'da yapılmasını sağlar
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
            .flowOn(Dispatchers.IO) // Yukarı akış işlemlerinin IO dispatcher'da yapılmasını sağlar
    }

    // Tekli veri yazma işlemi
    override fun <T> putValue(key: String, value: T) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[getKey(key, value!!::class.java) as Preferences.Key<T>] = value
            }
        }.invokeOnCompletion { exception ->
            exception?.let { logError(it) } // Hata varsa loglama
        }
    }

    // Toplu veri yazma işlemi
    override fun putValues(values: Map<String, Any>) {
        scope.launch {
            dataStore.edit { preferences ->
                for ((key, value) in values) {
                    try {
                        // getKey metodu ile key türünü belirleyerek ekleme yapıyoruz
                        val preferencesKey = getKey(key, value::class.java)
                        @Suppress("UNCHECKED_CAST")
                        preferences[preferencesKey as Preferences.Key<Any>] = value
                    } catch (e: IllegalArgumentException) {
                        logError(e)
                        // Desteklenmeyen tür hatası varsa, hatayı işleyin
                    }
                }
            }
        }.invokeOnCompletion { exception ->
            exception?.let { logError(it) } // Hata varsa loglama
        }
    }

    // Tekli veri silme işlemi
    override fun removeValue(key: String) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
            }
        }.invokeOnCompletion { exception ->
            exception?.let { logError(it) } // Hata varsa loglama
        }
    }

    // Toplu veri silme işlemi
    override fun removeValues(keys: List<String>) {
        scope.launch {
            dataStore.edit { preferences ->
                keys.forEach { key ->
                    preferences.remove(stringPreferencesKey(key))
                }
            }
        }.invokeOnCompletion { exception ->
            exception?.let { logError(it) } // Hata varsa loglama
        }
    }

    // Tüm verileri arka planda temizleme işlemi
    override fun removePref() {
        scope.launch {
            dataStore.edit {
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
}





'''
