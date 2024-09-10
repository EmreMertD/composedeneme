import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class ArkParameterDistributionServiceTest {

    @InjectMocks
    private ArkParameterDistributionService service;

    @Mock
    private IArkParameterPolicy mockPolicy;

    @Before
    public void setUp() {
        service = new ArkParameterDistributionService();
    }

    @Test
    public void testIsFeatureOpen_WithBlackListPolicy() throws ActionException {
        List<IArkParameterPolicy> policies = new ArrayList<>();
        when(mockPolicy.getPolicyType()).thenReturn(ArkParameterPolicyType.BLACK_LIST);
        when(mockPolicy.validate(any())).thenReturn(false);
        policies.add(mockPolicy);

        ArkParameterDistributionInput input = new ArkParameterDistributionInput();
        boolean result = service.isFeatureOpen(policies, input);

        assertFalse(result);
        verify(mockPolicy).validate(input);
    }

    @Test
    public void testIsFeatureOpen_WithDistributionPolicy() throws ActionException {
        List<IArkParameterPolicy> policies = new ArrayList<>();
        when(mockPolicy.getPolicyType()).thenReturn(ArkParameterPolicyType.DISTRIBUTION);
        when(mockPolicy.validate(any())).thenReturn(true);
        policies.add(mockPolicy);

        ArkParameterDistributionInput input = new ArkParameterDistributionInput();
        boolean result = service.isFeatureOpen(policies, input);

        assertTrue(result);
        verify(mockPolicy).validate(input);
    }

    @Test
    public void testIsFeatureOpen_EmptyPolicies() throws ActionException {
        List<IArkParameterPolicy> policies = new ArrayList<>();

        ArkParameterDistributionInput input = new ArkParameterDistributionInput();
        boolean result = service.isFeatureOpen(policies, input);

        assertFalse(result);
    }
}




import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArkParameterOperatingSystemTypeTest {

    @Mock
    private ArkParameterOperatingSystemType mockOsType;

    @Test
    public void testGetValueForAndroid() {
        when(mockOsType.getValue()).thenReturn("ANDROID");
        assertEquals("ANDROID", mockOsType.getValue());
    }

    @Test
    public void testGetValueForIOS() {
        when(mockOsType.getValue()).thenReturn("IOS");
        assertEquals("IOS", mockOsType.getValue());
    }
}



import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArkParameterPolicyTypeTest {

    @Mock
    private ArkParameterPolicyType mockPolicyType;

    @Test
    public void testGetTypeForBlackList() {
        when(mockPolicyType.getType()).thenReturn("B");
        assertEquals("B", mockPolicyType.getType());
    }

    @Test
    public void testGetTypeForDistribution() {
        when(mockPolicyType.getType()).thenReturn("D");
        assertEquals("D", mockPolicyType.getType());
    }
}



