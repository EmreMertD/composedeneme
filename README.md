import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SharedPrefStorageManagerTest {

    private lateinit var sharedPrefStorageManager: SharedPrefStorageManager

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val context = ApplicationProvider.getApplicationContext<Context>()

        Mockito.`when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putInt(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putFloat(Mockito.anyString(), Mockito.anyFloat())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putLong(Mockito.anyString(), Mockito.anyLong())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putStringSet(Mockito.anyString(), Mockito.anySet())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.remove(Mockito.anyString())).thenReturn(mockEditor)

        sharedPrefStorageManager = SharedPrefStorageManager(context)
    }

    @Test
    fun testPutAndGetStringValue() {
        val key = "test_key"
        val value = "test_value"

        sharedPrefStorageManager.putValue(key, value)
        Mockito.`when`(mockSharedPreferences.getString(key, "")).thenReturn(value)

        val result = sharedPrefStorageManager.getValue(key, "", String::class.java)
        assertEquals(value, result)
    }

    @Test
    fun testPutAndGetIntValue() {
        val key = "test_key"
        val value = 123

        sharedPrefStorageManager.putValue(key, value)
        Mockito.`when`(mockSharedPreferences.getInt(key, 0)).thenReturn(value)

        val result = sharedPrefStorageManager.getValue(key, 0, Int::class.java)
        assertEquals(value, result)
    }

    @Test
    fun testRemoveValue() {
        val key = "test_key"
        Mockito.`when`(mockSharedPreferences.contains(key)).thenReturn(true)

        sharedPrefStorageManager.removeValue(key)
        Mockito.verify(mockEditor).remove(key)
        Mockito.verify(mockEditor).apply()
    }
}
