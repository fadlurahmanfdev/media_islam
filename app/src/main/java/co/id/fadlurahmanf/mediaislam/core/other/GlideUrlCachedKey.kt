package co.id.fadlurahmanf.mediaislam.core.other

import com.bumptech.glide.load.model.GlideUrl

class GlideUrlCachedKey(url: String, private val customKey: String?) : GlideUrl(url) {
    override fun getCacheKey(): String {
        return customKey ?: System.currentTimeMillis().toString()
    }
}