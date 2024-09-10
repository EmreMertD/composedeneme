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

    @Mock
    private ArkParameterServiceOutput mockServiceOutput;  // ArkParameterServiceOutput mocklandı

    @Before
    public void setUp() {
        customerNumberBlackListPolicy = new CustomerNumberBlackListPolicy(mockDynamicService);
    }

    @Test
    public void testCheckCustomerNumberInBlackList_CustomerNotInBlackList() throws ActionException {
        // Mock input ayarları
        when(mockInput.getCustomerNumberBlackListParameter()).thenReturn("blackListParam");
        when(mockInput.getCustomerNum()).thenReturn(BigDecimal.TEN);  // Kara listede olmayan müşteri numarası
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Mock DynamicService ayarları
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);

        // Mock edilen ArkParameterServiceOutput öğesini listeye ekliyoruz (listede olmayan müşteri)
        List<ArkParameterServiceOutput> mockItems = Arrays.asList(mockServiceOutput);

        // getItems() metodu mockItems listesini döndürecek şekilde ayarlanıyor
        when(mockOutput.getItems()).thenReturn(mockItems);

        // Test edilen metodu çağırıyoruz
        boolean result = customerNumberBlackListPolicy.checkCustomerNumberInBlackList(mockInput);

        // Beklenen sonucu doğruluyoruz (müşteri kara listede olmadığı için false)
        assertFalse(result);
    }
}
