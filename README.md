import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class CustomerNumberBlackListPolicyTest {

    @InjectMocks
    private CustomerNumberBlackListPolicy customerNumberBlackListPolicy;

    @Mock
    private IReadArkParameterDynamicService mockDynamicService;

    @Mock
    private ArkParameterDistributionInput mockInput;

    @Before
    public void setUp() {
        customerNumberBlackListPolicy = new CustomerNumberBlackListPolicy(mockDynamicService);
    }

    @Test
    public void testGetPolicyType() {
        assertEquals(ArkParameterPolicyType.BLACK_LIST, customerNumberBlackListPolicy.getPolicyType());
    }

    @Test
    public void testValidate_CustomerNumberInBlackList() throws ActionException {
        when(mockInput.getCustomerNumberBlackListParameter()).thenReturn("SomeParam");
        when(mockInput.getCustomerNum()).thenReturn(BigDecimal.ONE);
        
        ArkParameterListServiceOutput mockOutput = mock(ArkParameterListServiceOutput.class);
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);
        when(mockOutput.getItems()).thenReturn(List.of(mock(ArkParameterListServiceItem.class)));

        boolean result = customerNumberBlackListPolicy.validate(mockInput);
        
        assertTrue(result);
        verify(mockDynamicService).getArkParameterListWithCodeName(any());
    }

    @Test(expected = ActionException.class)
    public void testValidate_ThrowsActionExceptionOnInvalidInput() throws ActionException {
        when(mockInput.getCustomerNumberBlackListParameter()).thenReturn(null);
        customerNumberBlackListPolicy.validate(mockInput);
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
import java.math.BigDecimal;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CustomerNumberStagingPolicyTest {

    @InjectMocks
    private CustomerNumberStagingPolicy customerNumberStagingPolicy;

    @Mock
    private IReadArkParameterDynamicService mockDynamicService;

    @Mock
    private ArkParameterDistributionInput mockInput;

    @Before
    public void setUp() {
        customerNumberStagingPolicy = new CustomerNumberStagingPolicy(mockDynamicService);
    }

    @Test
    public void testGetPolicyType() {
        assertEquals(ArkParameterPolicyType.DISTRIBUTION, customerNumberStagingPolicy.getPolicyType());
    }

    @Test
    public void testValidate_CustomerNumberInStagingDistribution() throws ActionException {
        when(mockInput.getCustomerDistributionControlParameter()).thenReturn("SomeParam");
        when(mockInput.getCustomerNum()).thenReturn(BigDecimal.ONE);

        ArkParameterListServiceOutput mockOutput = mock(ArkParameterListServiceOutput.class);
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);
        when(mockOutput.getItems()).thenReturn(List.of(mock(ArkParameterListServiceItem.class)));

        boolean result = customerNumberStagingPolicy.validate(mockInput);

        assertTrue(result);
        verify(mockDynamicService).getArkParameterListWithCodeName(any());
    }

    @Test(expected = ActionException.class)
    public void testValidate_ThrowsActionExceptionOnInvalidInput() throws ActionException {
        when(mockInput.getCustomerDistributionControlParameter()).thenReturn(null);
        customerNumberStagingPolicy.validate(mockInput);
    }
}
