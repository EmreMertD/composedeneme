import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OperatingSystemStagingPolicyTest {

    @InjectMocks
    private OperatingSystemStagingPolicy operatingSystemStagingPolicy;

    @Mock
    private IReadArkParameterDynamicService mockDynamicService;

    @Mock
    private ArkParameterDistributionInput mockInput;

    @Mock
    private ArkParameterServiceOutput mockServiceOutput;

    @Before
    public void setUp() {
        operatingSystemStagingPolicy = new OperatingSystemStagingPolicy(mockDynamicService);
    }

    @Test
    public void testValidate_Android_Success() throws ActionException {
        // Mock input ayarları: Android işletim sistemi
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.AND);
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("androidFeatureControlParam");
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Output mock ayarları (Android için 'Y' dönmeli)
        Optional<ArkParameterServiceOutput> mockOptionalOutput = Optional.of(mockServiceOutput);
        when(mockDynamicService.getArkParameterOutputWithCodeName(any())).thenReturn(mockOptionalOutput);
        when(mockServiceOutput.getPrmValue()).thenReturn("Y");

        // Test edilen metodu çağırıyoruz
        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        // Beklenen sonucu doğruluyoruz (Android OS staging disable edilmiş olmalı, true dönmeli)
        assertTrue(result);
    }

    @Test
    public void testValidate_IOS_Success() throws ActionException {
        // Mock input ayarları: IOS işletim sistemi
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.IOS);
        when(mockInput.getIosFeatureControlParameter()).thenReturn("iosFeatureControlParam");
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Output mock ayarları (iOS için 'Y' dönmeli)
        Optional<ArkParameterServiceOutput> mockOptionalOutput = Optional.of(mockServiceOutput);
        when(mockDynamicService.getArkParameterOutputWithCodeName(any())).thenReturn(mockOptionalOutput);
        when(mockServiceOutput.getPrmValue()).thenReturn("Y");

        // Test edilen metodu çağırıyoruz
        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        // Beklenen sonucu doğruluyoruz (iOS OS staging disable edilmiş olmalı, true dönmeli)
        assertTrue(result);
    }

    @Test(expected = ActionException.class)
    public void testValidate_ThrowsExceptionForInvalidInput() throws ActionException {
        // Boş Android ve IOS parametreleri geçildiğinde istisna fırlatılmalı
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn(null);
        when(mockInput.getIosFeatureControlParameter()).thenReturn(null);

        operatingSystemStagingPolicy.validate(mockInput);
    }

    @Test
    public void testValidate_OperatingSystemNotInStagingList() throws ActionException {
        // Mock input ayarları: Android işletim sistemi
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.AND);
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("androidFeatureControlParam");
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Output mock ayarları (Android için 'N' dönmeli)
        Optional<ArkParameterServiceOutput> mockOptionalOutput = Optional.of(mockServiceOutput);
        when(mockDynamicService.getArkParameterOutputWithCodeName(any())).thenReturn(mockOptionalOutput);
        when(mockServiceOutput.getPrmValue()).thenReturn("N");

        // Test edilen metodu çağırıyoruz
        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        // Beklenen sonucu doğruluyoruz (Android OS staging listede değil, false dönmeli)
        assertFalse(result);
    }
}
