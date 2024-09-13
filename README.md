import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class PreferencesDataStoreManagerJavaTest {

    private PreferencesDataStoreManager storageManager;
    private Context context = ApplicationProvider.getApplicationContext();

    @Before
    public void setUp() {
        storageManager = new PreferencesDataStoreManager(context);
    }

    @Test
    public void testPutAndGetStringValue() {
        String key = "username";
        String value = "JohnDoe";

        // String değer kaydetme
        storageManager.putValue(key, value);

        // String değer okuma
        String retrievedValue = storageManager.getValue(key, String.class);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testPutAndGetIntValue() {
        String key = "age";
        int value = 30;

        // Integer değer kaydetme
        storageManager.putValue(key, value);

        // Integer değer okuma
        Integer retrievedValue = storageManager.getValue(key, Integer.class);
        assertEquals(Integer.valueOf(value), retrievedValue);
    }

    @Test
    public void testPutAndGetSetStringValue() {
        String key = "favorite_foods";
        Set<String> value = new HashSet<>();
        value.add("Pizza");
        value.add("Burger");
        value.add("Sushi");

        // Set<String> değer kaydetme
        storageManager.putValue(key, value);

        // Set<String> değer okuma
        Set<String> retrievedValue = (Set<String>) storageManager.getValue(key, Set.class);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testRemoveValue() {
        String key = "username";
        String value = "JohnDoe";

        // Değer kaydetme
        storageManager.putValue(key, value);

        // Değeri kaldırma
        storageManager.removeValue(key);

        // Değerin var olmadığını kontrol etme
        String retrievedValue = storageManager.getValue(key, String.class);
        assertNull(retrievedValue);
    }

    @Test
    public void testDefaultValue() {
        String key = "non_existent_key";
        String defaultValue = "DefaultUser";

        // Varsayılan değerle kontrol
        String retrievedValue = storageManager.getValue(key, defaultValue, String.class);
        assertEquals(defaultValue, retrievedValue);
    }
}
