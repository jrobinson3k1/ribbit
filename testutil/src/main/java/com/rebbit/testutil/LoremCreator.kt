package com.rebbit.testutil

import com.rebbit.data.model.Post
import com.thedeanda.lorem.LoremIpsum
import java.util.*

object LoremCreator {

    fun posts(size: Int): List<Post> {
        return arrayListOf<Post>().apply {
            LoremIpsum.getInstance().apply {
                repeat(size, {
                    val subreddit = getWords(1).toLowerCase()
                    add(ModelCreator.post(
                            subreddit = subreddit,
                            subredditNamePrefixed = "r/$subreddit",
                            author = firstName,
                            hint = Post.Hint.values().random(),
                            title = getTitle(2, 15),
                            createdUtc = currentTimeUtc() - Random().nextInt(86_400 * 7), // 7 days
                            thumbnail = "thumbnail",
                            numComments = Random().nextInt(2000)
                    ))
                })
            }
        }
    }

    private fun currentTimeUtc() = Calendar.getInstance(TimeZone.getTimeZone("utc")).timeInMillis / 1000

    private fun <T> Array<T>.random() = get(Random().nextInt(size))
}