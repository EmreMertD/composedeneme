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

    @Mock
    private ArkParameterListServiceOutput mockOutput;

    @Before
    public void setUp() {
        customerNumberBlackListPolicy = new CustomerNumberBlackListPolicy(mockDynamicService);
    }

    @Test
    public void testCheckCustomerNumberInBlackList_Success() throws ActionException {
        // Input mock ayarları
        when(mockInput.getCustomerNumberBlackListParameter()).thenReturn("blackListParam");
        when(mockInput.getCustomerNum()).thenReturn(BigDecimal.ONE);
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Mock DynamicService ayarları
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);
        when(mockOutput.getItems()).thenReturn(Arrays.asList("123", "456"));  // Blacklistteki itemlar

        // Test edilen metodu çağırıyoruz
        boolean result = customerNumberBlackListPolicy.checkCustomerNumberInBlackList(mockInput);

        // Beklenen sonucu doğruluyoruz
        assertTrue(result);
    }

    @Test
    public void testCheckCustomerNumberInBlackList_CustomerNotInBlackList() throws ActionException {
        // Input mock ayarları
        when(mockInput.getCustomerNumberBlackListParameter()).thenReturn("blackListParam");
        when(mockInput.getCustomerNum()).thenReturn(BigDecimal.TEN);
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Mock DynamicService ayarları
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);
        when(mockOutput.getItems()).thenReturn(Arrays.asList("123", "456"));  // Blacklistte bulunmayan itemlar

        // Test edilen metodu çağırıyoruz
        boolean result = customerNumberBlackListPolicy.checkCustomerNumberInBlackList(mockInput);

        // Beklenen sonucu doğruluyoruz
        assertFalse(result);  // Bu durumda müşteri blacklistte değil
    }
}
