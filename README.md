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

    @Before
    public void setUp() {
        operatingSystemStagingPolicy = new OperatingSystemStagingPolicy(mockDynamicService);
    }

    @Test
    public void testGetPolicyType() {
        assertEquals(ArkParameterPolicyType.DISTRIBUTION, operatingSystemStagingPolicy.getPolicyType());
    }

    @Test
    public void testValidate_AndroidOperatingSystem() throws ActionException {
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.AND);
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn("AndroidParam");

        ArkParameterDynamicServiceInput dynamicInput = mock(ArkParameterDynamicServiceInput.class);
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(Optional.of(mock(ArkParameterServiceOutput.class)));

        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        assertTrue(result);
        verify(mockDynamicService).getArkParameterListWithCodeName(any());
    }

    @Test
    public void testValidate_IosOperatingSystem() throws ActionException {
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.IOS);
        when(mockInput.getIosFeatureControlParameter()).thenReturn("IosParam");

        ArkParameterDynamicServiceInput dynamicInput = mock(ArkParameterDynamicServiceInput.class);
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(Optional.of(mock(ArkParameterServiceOutput.class)));

        boolean result = operatingSystemStagingPolicy.validate(mockInput);

        assertTrue(result);
        verify(mockDynamicService).getArkParameterListWithCodeName(any());
    }

    @Test(expected = ActionException.class)
    public void testValidate_ThrowsActionExceptionOnInvalidInput() throws ActionException {
        when(mockInput.getOperatingSystem()).thenReturn(ArkParameterOperatingSystemType.AND);
        when(mockInput.getAndroidFeatureControlParameter()).thenReturn(null);
        
        operatingSystemStagingPolicy.validate(mockInput);
    }
}










import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DeviceInfoBlackListPolicyTest {

    @InjectMocks
    private DeviceInfoBlackListPolicy deviceInfoBlackListPolicy;

    @Mock
    private IReadArkParameterDynamicService mockDynamicService;

    @Mock
    private ArkParameterDistributionInput mockInput;

    @Before
    public void setUp() {
        deviceInfoBlackListPolicy = new DeviceInfoBlackListPolicy(mockDynamicService);
    }

    @Test
    public void testGetPolicyType() {
        assertEquals(ArkParameterPolicyType.BLACK_LIST, deviceInfoBlackListPolicy.getPolicyType());
    }

    @Test
    public void testValidate_DeviceInBlackList() throws ActionException {
        when(mockInput.getDeviceInfoBlackListParameter()).thenReturn("DeviceParam");
        when(mockInput.getDeviceInfo()).thenReturn("Device123");

        ArkParameterListServiceOutput mockOutput = mock(ArkParameterListServiceOutput.class);
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);
        when(mockOutput.getItems()).thenReturn(List.of(mock(ArkParameterListServiceItem.class)));

        boolean result = deviceInfoBlackListPolicy.validate(mockInput);

        assertTrue(result);
        verify(mockDynamicService).getArkParameterListWithCodeName(any());
    }

    @Test(expected = ActionException.class)
    public void testValidate_ThrowsActionExceptionOnInvalidInput() throws ActionException {
        when(mockInput.getDeviceInfoBlackListParameter()).thenReturn(null);
        
        deviceInfoBlackListPolicy.validate(mockInput);
    }
}




