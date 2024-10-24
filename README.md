    
    import android.graphics.Bitmap
    import android.util.LruCache
    import java.net.URL
    import java.util.concurrent.locks.ReentrantLock
    
    class ImageCache(private val config: Config = Config.defaultConfig()) {
    
        private val imageCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(config.memoryLimit) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                // Cache için kullanılan boyutu hesaplar (KB cinsinden)
                return value.byteCount / 1024
            }
        }
    
        private val lock = ReentrantLock()
    
        data class Config(val countLimit: Int, val memoryLimit: Int) {
            companion object {
                fun defaultConfig() = Config(countLimit = 100, memoryLimit = 1024 * 1024 * 50) // 50MB
            }
        }
    
        fun addImage(url: URL, image: Bitmap) {
            lock.lock()
            try {
                removeImage(url) // Aynı URL için varsa eski resmi kaldırır
                imageCache.put(url.toString(), image)
            } finally {
                lock.unlock()
            }
        }
    
        fun removeImage(url: URL) {
            lock.lock()
            try {
                imageCache.remove(url.toString())
            } finally {
                lock.unlock()
            }
        }
    
        fun getImage(url: URL): Bitmap? {
            lock.lock()
            return try {
                imageCache.get(url.toString())
            } finally {
                lock.unlock()
            }
        }
    
        operator fun get(url: URL): Bitmap? {
            return getImage(url)
        }
    
        operator fun set(url: URL, image: Bitmap) {
            addImage(url, image)
        }
    }


    import android.graphics.Bitmap
    import android.util.LruCache
    import java.net.URL
    
    class ImageCache(private val config: Config = Config.defaultConfig()) {
    
        private val imageCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(config.memoryLimit) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                // Cache için kullanılan boyutu hesaplar (KB cinsinden)
                return value.byteCount / 1024
            }
        }
    
        data class Config(val countLimit: Int, val memoryLimit: Int) {
            companion object {
                fun defaultConfig() = Config(countLimit = 100, memoryLimit = 1024 * 1024 * 50) // 50MB
            }
        }
    
        fun addImage(url: URL, image: Bitmap) {
            removeImage(url) // Aynı URL için varsa eski resmi kaldırır
            imageCache.put(url.toString(), image)
        }
    
        fun removeImage(url: URL) {
            imageCache.remove(url.toString())
        }
    
        fun getImage(url: URL): Bitmap? {
            return imageCache.get(url.toString())
        }
    
        operator fun get(url: URL): Bitmap? {
            return getImage(url)
        }
    
        operator fun set(url: URL, image: Bitmap) {
            addImage(url, image)
        }
    }

    
    import android.graphics.Bitmap
    import java.net.URL
    
    class ImageCache(private val config: Config = Config.defaultConfig()) {
    
        private val imageCache = mutableMapOf<String, Bitmap>()
    
        data class Config(val countLimit: Int, val memoryLimit: Int) {
            companion object {
                fun defaultConfig() = Config(countLimit = 100, memoryLimit = 1024 * 1024 * 50) // 50MB
            }
        }
    
        fun addImage(url: URL, image: Bitmap) {
            if (imageCache.size >= config.countLimit) {
                // En eski öğeyi kaldırmak için temel bir FIFO mantığı uygulayabiliriz
                val firstKey = imageCache.keys.firstOrNull()
                firstKey?.let { imageCache.remove(it) }
            }
            imageCache[url.toString()] = image
        }
    
        fun removeImage(url: URL) {
            imageCache.remove(url.toString())
        }
    
        fun getImage(url: URL): Bitmap? {
            return imageCache[url.toString()]
        }
    
        operator fun get(url: URL): Bitmap? {
            return getImage(url)
        }
    
        operator fun set(url: URL, image: Bitmap) {
            addImage(url, image)
        }
    }
