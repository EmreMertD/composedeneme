import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PreferencesDataStoreManagerTest {

    private lateinit var storageManager: PreferencesDataStoreManager

    @Before
    fun setUp() {
        // Test context'i başlatıyoruz (mock ya da gerçek context kullanabilirsiniz)
        storageManager = PreferencesDataStoreManager(context = /* provide context here */)
    }

    // Float değer kaydetme ve okuma testi
    @Test
    fun testPutAndGetFloatValue() = runTest {
        val key = "float_key"
        val value = 3.14f

        // Değer kaydet
        storageManager.putValue(key, value)

        // Değer kontrolü
        val retrievedValue: Float? = storageManager.getValue(key, Float::class.java)
        assertEquals(value, retrievedValue)
    }

    // Long değer kaydetme ve okuma testi
    @Test
    fun testPutAndGetLongValue() = runTest {
        val key = "long_key"
        val value = 123456789L

        // Değer kaydet
        storageManager.putValue(key, value)

        // Değer kontrolü
        val retrievedValue: Long? = storageManager.getValue(key, Long::class.java)
        assertEquals(value, retrievedValue)
    }

    // Tüm preferences'ı temizleme (removePref) testi
    @Test
    fun testRemovePref() = runTest {
        val key1 = "key_1"
        val key2 = "key_2"

        // İki farklı değer kaydediyoruz
        storageManager.putValue(key1, "TestValue1")
        storageManager.putValue(key2, 123)

        // Preferences'ı temizliyoruz
        storageManager.removePref("app_preferences")

        // Kaydedilen değerlerin silindiğini kontrol ediyoruz
        val retrievedValue1: String? = storageManager.getValue(key1, String::class.java)
        val retrievedValue2: Int? = storageManager.getValue(key2, Int::class.java)

        assertNull(retrievedValue1)
        assertNull(retrievedValue2)
    }
}


import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PreferencesDataStoreManagerJavaTest {

    private PreferencesDataStoreManager storageManager;
    private Context context = ApplicationProvider.getApplicationContext();

    @Before
    public void setUp() {
        storageManager = new PreferencesDataStoreManager(context);
    }

    @Test
    public void testPutAndGetFloatValue() {
        String key = "float_key";
        Float value = 3.14f;

        // Float değer kaydetme
        storageManager.putValue(key, value);

        // Float değer okuma
        Float retrievedValue = storageManager.getValue(key, Float.class);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testPutAndGetLongValue() {
        String key = "long_key";
        Long value = 123456789L;

        // Long değer kaydetme
        storageManager.putValue(key, value);

        // Long değer okuma
        Long retrievedValue = storageManager.getValue(key, Long.class);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testRemovePref() {
        String key1 = "key_1";
        String key2 = "key_2";

        // İki farklı değer kaydet
        storageManager.putValue(key1, "TestValue1");
        storageManager.putValue(key2, 123);

        // Preferences'ı temizle
        storageManager.removePref("app_preferences");

        // Kaydedilen değerlerin silindiğini kontrol et
        String retrievedValue1 = storageManager.getValue(key1, String.class);
        Integer retrievedValue2 = storageManager.getValue(key2, Integer.class);

        assertNull(retrievedValue1);
        assertNull(retrievedValue2);
    }
}
