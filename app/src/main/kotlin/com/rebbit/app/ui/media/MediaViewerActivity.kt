package com.rebbit.app.ui.media

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rebbit.app.R
import com.rebbit.app.databinding.ActivityMediaViewerBinding
import com.rebbit.app.di.Injector
import com.rebbit.app.util.OnImageLoadedListener
import com.rebbit.app.util.isGif
import com.rebbit.app.util.isSupportedImageUrl
import javax.inject.Inject

class MediaViewerActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MediaViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get()
                .mediaViewerActivityBuilder()
                .activity(this)
                .mediaUrl(intent.getStringExtra(ARG_MEDIA_URL))
                .build()
                .inject(this)

        DataBindingUtil.setContentView<ActivityMediaViewerBinding>(this, R.layout.activity_media_viewer).apply {
            imageView.transitionName = intent.getStringExtra(ARG_TRANSITION_NAME)
            viewModel = this@MediaViewerActivity.viewModel
            onLoadedListener = object : OnImageLoadedListener {
                override fun onLoadFailed() {
                    supportStartPostponedEnterTransition()
                }

                override fun onLoadSuccess() {
                    supportStartPostponedEnterTransition()
                }
            }
        }

        supportPostponeEnterTransition()
    }

    companion object {
        private const val ARG_MEDIA_URL = "media_url"
        private const val ARG_TRANSITION_NAME = "transition_name"

        fun getStartIntent(context: Context, mediaUrl: String, transitionName: String): Intent {
            return Intent(context, MediaViewerActivity::class.java)
                    .putExtra(ARG_MEDIA_URL, mediaUrl)
                    .putExtra(ARG_TRANSITION_NAME, transitionName)
        }

        fun isEmbeddableMedia(mediaUrl: String) = mediaUrl.isSupportedImageUrl() || mediaUrl.isGif()
    }
}