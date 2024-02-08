package com.datnguyen.movie_dev.ui.fragment.trailer_view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.databinding.DialogTrailerViewBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class TrailerViewDialog : DialogFragment() {
    private var _binding: DialogTrailerViewBinding? = null

    // This property is only valid between onCreateDialog and
    // onDestroyView.
    private val binding get() = _binding!!

    var videoId: String? = null

    companion object {
        const val KEY_KEY_ID = "KEY_KEY_ID"

        fun createIntent(keyId: String): TrailerViewDialog {
            val dialog = TrailerViewDialog()
            // Supply num input as an argument.
            val args = Bundle()
            args.putString(KEY_KEY_ID, keyId)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity(), R.style.DialogFullScreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        window?.let {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val root = RelativeLayout(activity)
            root.layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            window.setContentView(root)
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            videoId = it.getString(KEY_KEY_ID)
        }

        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_trailer_view, container, false)
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoId?.let { videoId ->
            lifecycle.addObserver(binding.youtubePlayerView)
            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}