package com.rebbit.app.ui.media

import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rebbit.app.R
import com.rebbit.app.databinding.FragmentMediaViewerBinding
import com.rebbit.app.di.Injector
import com.rebbit.app.util.isGif
import com.rebbit.app.util.isSupportedImageUrl
import javax.inject.Inject

class MediaViewerDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModel: MediaViewerViewModel

    private lateinit var binding: FragmentMediaViewerBinding

    override fun onAttach(context: Context?) {
        Injector.get()
                .mediaViewerFragmentBuilder()
                .fragment(this)
                .mediaUrl(arguments!!.getString(ARG_MEDIA_URL))
                .build()
                .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentMediaViewerBinding>(inflater, R.layout.fragment_media_viewer, container, false).apply {
            viewModel = this@MediaViewerDialogFragment.viewModel
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dummyView.setOnClickListener { dismissAllowingStateLoss() }
    }

    companion object {
        private const val ARG_MEDIA_URL = "media_url"

        fun newInstance(mediaUrl: String): MediaViewerDialogFragment {
            return MediaViewerDialogFragment().apply {
                arguments = Bundle().apply { putString(ARG_MEDIA_URL, mediaUrl) }
            }
        }

        fun isEmbeddableMedia(mediaUrl: String) = mediaUrl.isSupportedImageUrl() || mediaUrl.isGif()
    }
}