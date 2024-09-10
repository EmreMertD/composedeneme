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
public class CustomerNumberStagingPolicyTest {

    @InjectMocks
    private CustomerNumberStagingPolicy customerNumberStagingPolicy;

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
        customerNumberStagingPolicy = new CustomerNumberStagingPolicy(mockDynamicService);
    }

    @Test
    public void testCheckCustomerNumberInStaging_CustomerNotInStagingList() throws ActionException {
        // Mock input ayarları
        when(mockInput.getCustomerDistributionControlParameter()).thenReturn("stagingControlParam");
        when(mockInput.getCustomerNum()).thenReturn(BigDecimal.TEN);  // Staging listede olmayan müşteri numarası
        when(mockInput.getGroupName()).thenReturn("groupName");
        when(mockInput.getAttributeName()).thenReturn("attributeName");
        when(mockInput.getCache()).thenReturn("cache");
        when(mockInput.getDialect()).thenReturn("dialect");

        // Mock DynamicService ayarları
        when(mockDynamicService.getArkParameterListWithCodeName(any())).thenReturn(mockOutput);

        // Staging listesine ekliyoruz fakat müşteri numarası staging listede değil
        List<ArkParameterServiceOutput> mockItems = Arrays.asList(mockServiceOutput);

        // getItems() metodu mockItems listesini döndürecek şekilde ayarlanıyor
        when(mockOutput.getItems()).thenReturn(mockItems);

        // Staging listede farklı bir müşteri numarası varmış gibi davranıyoruz
        when(mockServiceOutput.getPrmValue()).thenReturn("123");  // Staging listede olan başka bir müşteri numarası

        // Test edilen metodu çağırıyoruz
        boolean result = customerNumberStagingPolicy.validate(mockInput);

        // Beklenen sonucu doğruluyoruz (müşteri staging listede olmadığı için false)
        assertFalse(result);
    }
}
