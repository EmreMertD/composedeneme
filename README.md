import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.Before
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainActivityViewModelTest {

    @get:Rule
    var instantExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainActivityViewModel
    private val apiInvoker: DigitalApiInvoker = mockk(relaxed = true)
    private val endpoint = "test_endpoint"
    private val header = hashMapOf("key" to "value")
    private val queryMap = hashMapOf("key" to "value")
    private val body = hashMapOf("key" to "value")

    @Before
    fun setUp() {
        viewModel = MainActivityViewModel(apiInvoker)
    }

    @Test
    fun `callExampleServicePost should call apiInvoker post`() = runBlockingTest {
        val callbackSlot = slot<ApiCallback<CallCenterCallResult>>()

        coEvery {
            apiInvoker.post(any(), capture(callbackSlot))
        } answers {
            callbackSlot.captured.onStarted()
            callbackSlot.captured.onSuccess(mockk {
                every { data } returns "Success"
            })
        }

        viewModel.callExampleServicePost(endpoint, header, queryMap, body)

        coVerify {
            apiInvoker.post(any(), any())
        }
    }
}
