package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.*
class MixDrop1: MixDrop() {
    override var mainUrl: String = "https://mixdrop.to"
}
class MixDropTo : MixDrop(){
    override var mainUrl = "https://mixdrop.to"
}

open class MixDrop : ExtractorApi() {
    override var name = "MixDrop"
    override var mainUrl = "https://mixdrop.co"
    private val srcRegex = Regex("""wurl.*?=.*?"(.*?)";""")
    override val requiresReferer = false

    override fun getExtractorUrl(id: String): String {
        return "$mainUrl/e/$id"
    }

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink>? {
        with(app.get(url)) {
            getAndUnpack(this.text).let { unpackedText ->
                srcRegex.find(unpackedText)?.groupValues?.get(1)?.let { link ->
                    return listOf(
                        ExtractorLink(
                            name,
                            name,
                            httpsify(link),
                            url,
                            Qualities.Unknown.value,
                        )
                    )
                }
            }
        }
        return null
    }
}