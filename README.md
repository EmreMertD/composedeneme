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
public class DeviceInfoBlackListPolicyTest {

    @InjectMocks
    private DeviceInfoBlackListPolicy deviceInfoBlackListPolicy;

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
        deviceInfoBlackListPolicy = new DeviceInfoBlackListPolicy(mockDynamicService);
    }

    @Test
    public void testCheckDeviceInfoInBlackList_DeviceNotInBlackList() throws ActionException {
        // Mock input ayarları
        when(mockInput.getDeviceInfoBlackListParameter()).thenReturn("deviceBlackListParam");
        when(mockInput.getDeviceInfo()).thenReturn("Device_456");  // Kara listede olmayan cihaz bilgisi
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Mock DynamicService ayarları
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);

        // Kara listeye ekliyoruz fakat cihaz kara listede değil
        List<ArkParameterServiceOutput> mockItems = Arrays.asList(mockServiceOutput);

        // getItems() metodu mockItems listesini döndürecek şekilde ayarlanıyor
        when(mockOutput.getItems()).thenReturn(mockItems);

        // Kara listede farklı bir cihaz varmış gibi davranıyoruz
        when(mockServiceOutput.getPrmValue()).thenReturn("Device_123");  // Kara listede olan başka bir cihaz

        // Test edilen metodu çağırıyoruz
        boolean result = deviceInfoBlackListPolicy.validate(mockInput);

        // Beklenen sonucu doğruluyoruz (cihaz kara listede olmadığı için false)
        assertFalse(result);
    }
}
