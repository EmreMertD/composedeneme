import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesDataStoreManagerTest {

    private lateinit var storageManager: PreferencesDataStoreManager
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        storageManager = PreferencesDataStoreManager(context)
    }

    @Test
    fun testPutAndGetStringValue() = runTest {
        val key = "username"
        val value = "JohnDoe"

        // Değer kaydet
        storageManager.putValue(key, value)

        // Değer kontrolü
        val retrievedValue: String? = storageManager.getValue(key, String::class.java)
        assertEquals(value, retrievedValue)
    }

    @Test
    fun testPutAndGetIntValue() = runTest {
        val key = "age"
        val value = 30

        // Değer kaydet
        storageManager.putValue(key, value)

        // Değer kontrolü
        val retrievedValue: Int? = storageManager.getValue(key, Int::class.java)
        assertEquals(value, retrievedValue)
    }

    @Test
    fun testPutAndGetSetStringValue() = runTest {
        val key = "favorite_foods"
        val value = setOf("Pizza", "Burger", "Sushi")

        // Değer kaydet
        storageManager.putValue(key, value)

        // Değer kontrolü
        val retrievedValue: Set<String>? = storageManager.getValue(key, Set::class.java) as Set<String>?
        assertEquals(value, retrievedValue)
    }

    @Test
    fun testRemoveValue() = runTest {
        val key = "username"
        val value = "JohnDoe"

        // Değer kaydet
        storageManager.putValue(key, value)

        // Değeri kaldır
        storageManager.removeValue(key)

        // Değerin var olmadığını kontrol et
        val retrievedValue: String? = storageManager.getValue(key, String::class.java)
        assertNull(retrievedValue)
    }

    @Test
    fun testDefaultValue() = runTest {
        val key = "non_existent_key"
        val defaultValue = "DefaultUser"

        // Varsayılan değerle kontrol
        val retrievedValue: String? = storageManager.getValue(key, defaultValue, String::class.java)
        assertEquals(defaultValue, retrievedValue)
    }
}
