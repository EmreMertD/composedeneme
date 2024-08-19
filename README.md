import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit

class CircuitBreakerInterceptor(
    private val failureThreshold: Int = 10, // Belirlenen süre içinde olması gereken hata sayısı
    private val timeWindowMillis: Long = TimeUnit.MINUTES.toMillis(5), // Zaman dilimi (5 dakika)
    private val openDurationMillis: Long = TimeUnit.MINUTES.toMillis(1) // Circuit breaker'ın açık kalacağı süre (1 dakika)
) : Interceptor {

    private val failureTimestamps = ConcurrentLinkedQueue<Long>()
    private var circuitOpenedTime = 0L

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentTime = System.currentTimeMillis()

        // Eğer circuit breaker açıksa ve süre dolmadıysa, hata döndür
        if (isCircuitOpen(currentTime)) {
            throw IOException("Circuit breaker is open. Try again later.")
        }

        val response = try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            handleFailure(currentTime)
            throw IOException("Circuit breaker is open due to consecutive failures in the last $timeWindowMillis milliseconds.", e)
        }

        // HTTP 200 dışındaki tüm yanıtlar hata olarak kabul edilir
        if (!response.isSuccessful) {
            handleFailure(currentTime)
            if (failureTimestamps.size >= failureThreshold) {
                circuitOpenedTime = currentTime
                throw IOException("Circuit breaker is open due to consecutive failures in the last $timeWindowMillis milliseconds. Last response code: ${response.code}", null)
            }
        } else {
            clearOldFailures(currentTime)
            resetCircuitBreakerIfNecessary()
        }

        return response
    }

    private fun isCircuitOpen(currentTime: Long): Boolean {
        return circuitOpenedTime > 0 && (currentTime - circuitOpenedTime < openDurationMillis)
    }

    private fun handleFailure(currentTime: Long) {
        failureTimestamps.add(currentTime)
        clearOldFailures(currentTime)
    }

    private fun clearOldFailures(currentTime: Long) {
        // Zaman dilimini aşmış hataları temizle
        while (failureTimestamps.isNotEmpty() && currentTime - failureTimestamps.peek() > timeWindowMillis) {
            failureTimestamps.poll()
        }
    }

    private fun resetCircuitBreakerIfNecessary() {
        if (failureTimestamps.isEmpty()) {
            circuitOpenedTime = 0L
        }
    }
}
