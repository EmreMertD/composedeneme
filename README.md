import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

class ArkParameterDistributionServiceTest {

    private ArkParameterDistributionService service;

    @BeforeEach
    void setUp() {
        service = new ArkParameterDistributionService();
    }

    @Test
    void testIsFeatureOpen_WithBlackListPolicy() throws ActionException {
        List<IArkParameterPolicy> policies = new ArrayList<>();
        ArkParameterPolicyType blackListPolicyType = ArkParameterPolicyType.BLACK_LIST;
        policies.add(new ArkParameterPolicy(blackListPolicyType));

        ArkParameterDistributionInput input = new ArkParameterDistributionInput();
        boolean result = service.isFeatureOpen(policies, input);

        assertFalse(result, "BlackList policy should return false");
    }

    @Test
    void testIsFeatureOpen_WithDistributionPolicy() throws ActionException {
        List<IArkParameterPolicy> policies = new ArrayList<>();
        ArkParameterPolicyType distributionPolicyType = ArkParameterPolicyType.DISTRIBUTION;
        policies.add(new ArkParameterPolicy(distributionPolicyType));

        ArkParameterDistributionInput input = new ArkParameterDistributionInput();
        boolean result = service.isFeatureOpen(policies, input);

        assertTrue(result, "Distribution policy should return true");
    }

    @Test
    void testIsFeatureOpen_EmptyPolicies() throws ActionException {
        List<IArkParameterPolicy> policies = new ArrayList<>();
        ArkParameterDistributionInput input = new ArkParameterDistributionInput();

        boolean result = service.isFeatureOpen(policies, input);
        assertFalse(result, "Empty policy list should return false");
    }
}




import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ArkParameterOperatingSystemTypeTest {

    @Test
    void testGetValueForAndroid() {
        ArkParameterOperatingSystemType osType = ArkParameterOperatingSystemType.AND;
        assertEquals("ANDROID", osType.getValue(), "Expected ANDROID for AND enum");
    }

    @Test
    void testGetValueForIOS() {
        ArkParameterOperatingSystemType osType = ArkParameterOperatingSystemType.IOS;
        assertEquals("IOS", osType.getValue(), "Expected IOS for IOS enum");
    }
}


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ArkParameterPolicyTypeTest {

    @Test
    void testGetTypeForBlackList() {
        ArkParameterPolicyType policyType = ArkParameterPolicyType.BLACK_LIST;
        assertEquals("B", policyType.getType(), "Expected 'B' for BLACK_LIST");
    }

    @Test
    void testGetTypeForDistribution() {
        ArkParameterPolicyType policyType = ArkParameterPolicyType.DISTRIBUTION;
        assertEquals("D", policyType.getType(), "Expected 'D' for DISTRIBUTION");
    }
}



