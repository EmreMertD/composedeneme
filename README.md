'''
    import android.content.Context
    import androidx.datastore.preferences.core.*
    import androidx.datastore.preferences.preferencesDataStore
    import kotlinx.coroutines.*
    import kotlinx.coroutines.flow.*
    
    import android.content.Context
    import androidx.datastore.preferences.core.*
    import androidx.datastore.preferences.preferencesDataStore
    import androidx.test.core.app.ApplicationProvider
    import androidx.test.ext.junit.runners.AndroidJUnit4
    import com.google.common.truth.Truth.assertThat
    import kotlinx.coroutines.*
    import kotlinx.coroutines.flow.first
    import kotlinx.coroutines.flow.firstOrNull
    import kotlinx.coroutines.test.*
    import org.junit.After
    import org.junit.Before
    import org.junit.Test
    import org.junit.runner.RunWith
    
    @RunWith(AndroidJUnit4::class)
    class DataStoreStorageManagerTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var context: Context
    private lateinit var dataStoreManager: DataStoreStorageManager

    // Context uzantısı ile TestDataStore tanımı
    private val Context.testDataStore: DataStore<Preferences> by preferencesDataStore(name = "test_settings")

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        dataStoreManager = DataStoreStorageManager(context, testScope)
    }

    @After
    fun tearDown() {
        // Test scope temizliği
        testScope.cancel()
    }

    @Test
    fun `test putValue and getValueFlow`() = runTest {
        // putValue kullanarak veri ekleme
        dataStoreManager.putValue("test_key", "test_value")

        // getValueFlow kullanarak veri okuma
        val result = dataStoreManager.getValueFlow("test_key", String::class.java).first()

        // Beklenen sonucu kontrol etme
        assertThat(result).isEqualTo("test_value")
    }

    @Test
    fun `test getValueFlow with default value`() = runTest {
        // Varsayılan değer ile veri okuma (test_key henüz eklenmedi)
        val result = dataStoreManager.getValueFlow("test_key", "default_value", String::class.java).first()

        // Varsayılan değeri kontrol etme
        assertThat(result).isEqualTo("default_value")
    }

    @Test
    fun `test removeValue`() = runTest {
        // putValue kullanarak veri ekleme
        dataStoreManager.putValue("test_key", "test_value")

        // Değeri silme
        dataStoreManager.removeValue("test_key")

        // Değeri okuma (null bekleniyor çünkü silindi)
        val result = dataStoreManager.getValueFlow("test_key", String::class.java).firstOrNull()

        // Beklenen sonucu kontrol etme
        assertThat(result).isNull()
    }

    @Test
    fun `test putValues and getValueFlow`() = runTest {
        // Toplu veri ekleme
        val values = mapOf(
            "key1" to "value1",
            "key2" to 123,
            "key3" to true
        )
        dataStoreManager.putValues(values)

        // Eklenen verileri okuma ve kontrol etme
        val result1 = dataStoreManager.getValueFlow("key1", String::class.java).first()
        val result2 = dataStoreManager.getValueFlow("key2", Int::class.java).first()
        val result3 = dataStoreManager.getValueFlow("key3", Boolean::class.java).first()

        assertThat(result1).isEqualTo("value1")
        assertThat(result2).isEqualTo(123)
        assertThat(result3).isEqualTo(true)
    }

    @Test
    fun `test removeValues`() = runTest {
        // Toplu veri ekleme
        val values = mapOf(
            "key1" to "value1",
            "key2" to 123,
            "key3" to true
        )
        dataStoreManager.putValues(values)

        // Toplu veri silme
        dataStoreManager.removeValues(listOf("key1", "key2"))

        // Silinen anahtarların kontrolü
        val result1 = dataStoreManager.getValueFlow("key1", String::class.java).firstOrNull()
        val result2 = dataStoreManager.getValueFlow("key2", Int::class.java).firstOrNull()
        val result3 = dataStoreManager.getValueFlow("key3", Boolean::class.java).first()

        assertThat(result1).isNull()
        assertThat(result2).isNull()
        assertThat(result3).isEqualTo(true) // key3 silinmediği için true dönecektir
    }

    @Test
    fun `test removePref`() = runTest {
        // Toplu veri ekleme
        val values = mapOf(
            "key1" to "value1",
            "key2" to 123,
            "key3" to true
        )
        dataStoreManager.putValues(values)

        // Tüm verileri silme
        dataStoreManager.removePref()

        // Tüm anahtarların kontrolü (hepsi null dönmeli)
        val result1 = dataStoreManager.getValueFlow("key1", String::class.java).firstOrNull()
        val result2 = dataStoreManager.getValueFlow("key2", Int::class.java).firstOrNull()
        val result3 = dataStoreManager.getValueFlow("key3", Boolean::class.java).firstOrNull()

        assertThat(result1).isNull()
        assertThat(result2).isNull()
        assertThat(result3).isNull()
    }

}







'''
