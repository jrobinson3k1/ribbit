package com.rebbit.app

import com.rebbit.data.model.Image
import com.rebbit.data.model.Post
import com.rebbit.data.model.Preview

object ModelCreator {

    fun post(isCrosspostable: Boolean = false, subredditId: String = "", approvedAtUtc: String? = null, wls: Int = 0, modReasonBy: String? = null, bannedBy: String? = null, numReports: Int? = null,
             removalReason: String? = null, thumbnailWidth: Int? = null, subreddit: String = "", selftextHtml: String = "", authorFlairTemplateId: String? = null, selftext: String? = null, likes: Int? = null,
             suggestedSort: String? = null, userReports: List<String> = listOf(), isRedditMediaDomain: Boolean = false, saved: Boolean = false, id: String = "", bannedAtUtc: String? = null,
             modReasonTitle: String? = null, viewCount: Int? = null, archived: Boolean = false, clicked: Boolean = false, noFollow: Boolean = false, author: String = "", numCrossposts: Int = 0, linkFlairText: String = "",
             canModPost: Boolean = false, sendReplies: Boolean = false, pinned: Boolean = false, score: Int = 0, approvedBy: String? = null, over18: Boolean = false, reportReasons: String? = null, domain: String = "",
             hidden: Boolean = false, preview: Preview = ModelCreator.preview(), pwls: Int = 0, thumbnail: String = "", edited: Int? = null, linkFlairCssClass: String? = null, authorFlairCssClass: String? = null, contentMode: Boolean = false,
             gilded: Int = 0, locked: Boolean = false, downs: Int = 0, modReports: List<String> = listOf(), subredditSubscribers: Int = 0, hint: Post.Hint = Post.Hint.Image, stickied: Boolean = false, visited: Boolean = false,
             canGild: Boolean = false, thumbnailHeight: Int? = null, name: String = "", spoiler: Boolean = false, permalink: String = "", subredditType: String = "", parentWhitelistStatus: String = "", hideScore: Boolean = false,
             created: Long = 0, url: String = "", authorFlairText: String? = null, quarantine: Boolean = false, title: String = "", createdUtc: Long = 0, subredditNamePrefixed: String = "", ups: Int = 0,
             numComments: Int = 0, isSelf: Boolean = false, whitelistStatus: String? = null, modNote: String? = null, isVideo: Boolean = false, distinguished: String? = null, postCategories: String? = null): Post {
        return Post(isCrosspostable, subredditId, approvedAtUtc, wls, modReasonBy, bannedBy, numReports, removalReason, thumbnailWidth, subreddit, selftextHtml, authorFlairTemplateId, selftext, likes,
                suggestedSort, userReports, isRedditMediaDomain, saved, id, bannedAtUtc, modReasonTitle, viewCount, archived, clicked, noFollow, author, numCrossposts, linkFlairText, canModPost,
                sendReplies, pinned, score, approvedBy, over18, reportReasons, domain, hidden, preview, pwls, thumbnail, edited, linkFlairCssClass, authorFlairCssClass, contentMode, gilded,
                locked, downs, modReports, subredditSubscribers, hint, stickied, visited, canGild, thumbnailHeight, name, spoiler, permalink, subredditType, parentWhitelistStatus, hideScore,
                created, url, authorFlairText, quarantine, title, createdUtc, subredditNamePrefixed, ups, numComments, isSelf, whitelistStatus, modNote, isVideo, distinguished, postCategories)
    }

    fun preview(images: List<Image> = listOf(), enabled: Boolean = false): Preview {
        return Preview(images, enabled)
    }
}