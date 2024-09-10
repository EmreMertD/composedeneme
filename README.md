import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OperatingSystemStagingPolicyTest {

    @InjectMocks
    private OperatingSystemStagingPolicy operatingSystemStagingPolicy;

    @Mock
    private IReadArkParameterDynamicService mockDynamicService;

    @Mock
    private ArkParameterDistributionInput mockInput;

    @Mock
    private ArkParameterListServiceOutput mockOutput;

    @Mock
    private ArkParameterServiceOutput mockServiceOutput;  // ArkParameterServiceOutput mocklandı

    @Before
    public void setUp() {
        operatingSystemStagingPolicy = new OperatingSystemStagingPolicy(mockDynamicService);
    }

    @Test
    public void testCheckOperatingSystemInStaging_NotInStagingList() throws ActionException {
        // Mock input ayarları
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.AND);  // Android işletim sistemi
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("androidFeatureControlParam");
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Mock DynamicService ayarları
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);

        // Staging listesine ekliyoruz fakat işletim sistemi staging listede değil
        List<ArkParameterServiceOutput> mockItems = Arrays.asList(mockServiceOutput);

        // getItems() metodu mockItems listesini döndürecek şekilde ayarlanıyor
        when(mockOutput.getItems()).thenReturn(mockItems);

        // Staging listede farklı bir işletim sistemi varmış gibi davranıyoruz
        when(mockServiceOutput.getPrmValue()).thenReturn("IOS");  // Staging listede olan iOS işletim sistemi

        // Test edilen metodu çağırıyoruz
        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        // Beklenen sonucu doğruluyoruz (Android işletim sistemi staging listede olmadığı için false)
        assertFalse(result);
    }
}
