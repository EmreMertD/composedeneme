
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
    private var lastFailureTime = 0L
    private var circuitOpenedTime = 0L

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentTime = System.currentTimeMillis()

        // Eğer circuit breaker açıksa ve süre dolmadıysa, hata döndür
        if (isCircuitOpen(currentTime)) {
            throw IOException("Circuit breaker is open. Try again later.")
        }

        return try {
            val response = chain.proceed(chain.request())
            
            // Sadece başarılı bir seri istek olursa hataları temizle
            if (response.isSuccessful) {
                clearOldFailures(currentTime)
                if (failureTimestamps.isEmpty()) {
                    lastFailureTime = 0L
                    circuitOpenedTime = 0L
                }
            }

            response
        } catch (e: Exception) {
            // Hata durumunda failureTimestamps'e zaman damgası ekle
            failureTimestamps.add(currentTime)
            lastFailureTime = currentTime

            // Eski hataları temizle
            clearOldFailures(currentTime)

            // Belirlenen süre içinde hatalar eşiği aşarsa circuit breaker açılır
            if (failureTimestamps.size >= failureThreshold) {
                circuitOpenedTime = currentTime
                throw IOException("Circuit breaker is open due to consecutive failures in the last $timeWindowMillis milliseconds.", e)
            } else {
                throw e
            }
        }
    }

    private fun isCircuitOpen(currentTime: Long): Boolean {
        return circuitOpenedTime > 0 && (currentTime - circuitOpenedTime < openDurationMillis)
    }

    private fun clearOldFailures(currentTime: Long) {
        // Zaman dilimini aşmış hataları temizle
        while (failureTimestamps.isNotEmpty() && currentTime - failureTimestamps.peek() > timeWindowMillis) {
            failureTimestamps.poll()
        }
    }
}
