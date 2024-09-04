import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPrefStorageManagerInstrumentationTest {

    private lateinit var sharedPrefStorageManager: SharedPrefStorageManager
    private lateinit var sharedPreferences: SharedPreferences
    private val testKey = "test_key"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sharedPrefStorageManager = SharedPrefStorageManager(context)
        sharedPreferences = context.getSharedPreferences(CommonParameters.PREF_NAME, Context.MODE_PRIVATE)

        // Teste temiz bir başlangıç yapmak için SharedPreferences'i temizliyoruz.
        sharedPreferences.edit().clear().commit()
    }

    @After
    fun tearDown() {
        // Her testten sonra temizleme işlemi
        sharedPreferences.edit().clear().commit()
    }

    @Test
    fun testPutAndGetStringValue() {
        val value = "test_value"

        sharedPrefStorageManager.putValue(testKey, value)
        val result = sharedPrefStorageManager.getValue(testKey, "", String::class.java)

        assertEquals(value, result)
    }

    @Test
    fun testPutAndGetIntValue() {
        val value = 123

        sharedPrefStorageManager.putValue(testKey, value)
        val result = sharedPrefStorageManager.getValue(testKey, 0, Int::class.java)

        assertEquals(value, result)
    }

    @Test
    fun testPutAndGetBooleanValue() {
        val value = true

        sharedPrefStorageManager.putValue(testKey, value)
        val result = sharedPrefStorageManager.getValue(testKey, false, Boolean::class.java)

        assertEquals(value, result)
    }

    @Test
    fun testRemoveValue() {
        val value = "to_be_removed"

        sharedPrefStorageManager.putValue(testKey, value)
        sharedPrefStorageManager.removeValue(testKey)
        val result = sharedPrefStorageManager.getValue(testKey, null, String::class.java)

        assertEquals(null, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPutInvalidType() {
        val invalidValue = listOf(1, 2, 3) // Geçersiz bir değer tipi
        sharedPrefStorageManager.putValue(testKey, invalidValue)
    }
}
