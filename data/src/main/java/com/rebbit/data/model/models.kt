package com.rebbit.data.model

data class AccessTokenResponse(val accessToken: String, val tokenType: String, val deviceId: String, val expiresIn: Long, val scope: String)

data class Thing<T>(val id: String, val name: String, val kind: String, val data: T) {

    enum class Kind(private val value: String) {
        Comment("t1"),
        Account("t2"),
        Link("t3"),
        Message("t4"),
        Subreddit("t5"),
        Award("t6"),
        Listing("Listing");

        companion object {
            fun fromValue(value: String) = values().firstOrNull { it.value == value }
        }
    }
}

data class Listing(val modhash: String, val dist: Int, val children: List<Thing<Link>>, val after: String?, val before: String?)

data class Link(val isCrosspostable: Boolean, val subredditId: String, val approvedAtUtc: String?, val wls: Int, val modReasonBy: String?, val bannedBy: String?, val numReports: Int?,
                val removalReason: String?, val thumbnailWidth: Int?, val subreddit: String, val selftextHtml: String, val authorFlairTemplateId: String?, val selftext: String?, val likes: Int?,
                val suggestedSort: String?, val userReports: List<String>, val isRedditMediaDomain: Boolean, val saved: Boolean, val id: String, val bannedAtUtc: String?,
                val modReasonTitle: String?, val viewCount: Int?, val archived: Boolean, val clicked: Boolean, val noFollow: Boolean, val author: String, val numCrossposts: Int, val linkFlairText: String,
                val canModPost: Boolean, val sendReplies: Boolean, val pinned: Boolean, val score: Int, val approvedBy: String?, val over18: Boolean, val reportReasons: String?, val domain: String,
                val hidden: Boolean, val preview: Preview, val pwls: Int, val thumbnail: String, val edited: Int?, val linkFlairCssClass: String?, val authorFlairCssClass: String?, val contentMode: Boolean,
                val gilded: Int, val locked: Boolean, val downs: Int, val modReports: List<String>, val subredditSubscribers: Int, val postHint: PostHint, val stickied: Boolean, val visited: Boolean,
                val canGild: Boolean, val thumbnailHeight: Int?, val name: String, val spoiler: Boolean, val permalink: String, val subredditType: String, val parentWhitelistStatus: String, val hideScore: Boolean,
                val created: Long, val url: String, val authorFlairText: String?, val quarantine: Boolean, val title: String, val createdUtc: Long, val subredditNamePrefixed: String, val ups: Int,
                val numComments: Int, val isSelf: Boolean, val whitelistStatus: String?, val modNote: String?, val isVideo: Boolean, val distinguished: String?, val postCategories: String?) {

    enum class PostHint(private vararg val value: String) {
        Unknown(""),
        Link("link"),
        Image("image"),
        Video("rich:video", "hosted:video"),
        Self("self");

        companion object {
            fun fromValue(value: String) = values().firstOrNull { it.value.contains(value) } ?: Unknown
        }
    }
}

// Left out "secure_media" and "media", it is either null or of format:
// "secure_media": {"reddit_video": {"fallback_url": "https://v.redd.it/f2vgtvochax01/DASH_9_6_M", "height": 1080, "width": 608, "scrubber_media_url": "https://v.redd.it/f2vgtvochax01/DASH_600_K", "dash_url": "https://v.redd.it/f2vgtvochax01/DASHPlaylist.mpd", "duration": 126, "hls_url": "https://v.redd.it/f2vgtvochax01/HLSPlaylist.m3u8", "is_gif": false, "transcoding_status": "completed"}}
// "media": {"reddit_video": {"fallback_url": "https://v.redd.it/f2vgtvochax01/DASH_9_6_M", "height": 1080, "width": 608, "scrubber_media_url": "https://v.redd.it/f2vgtvochax01/DASH_600_K", "dash_url": "https://v.redd.it/f2vgtvochax01/DASHPlaylist.mpd", "duration": 126, "hls_url": "https://v.redd.it/f2vgtvochax01/HLSPlaylist.m3u8", "is_gif": false, "transcoding_status": "completed"}}

data class Preview(val images: List<Image>, val enabled: Boolean)

data class Image(val source: Data, val resolutions: List<Data>, val id: String) {

    data class Data(val url: String, val width: Int, val height: Int)
}