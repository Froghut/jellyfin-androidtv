package org.jellyfin.androidtv.playback.nextup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.leanback.app.BackgroundManager
import kotlinx.android.synthetic.main.fragment_next_up.*
import kotlinx.android.synthetic.main.fragment_next_up.view.*
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.playback.PlaybackOverlayActivity
import org.jellyfin.androidtv.util.toHtmlSpanned

class NextUpFragment(private val data: NextUpItemData) : Fragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_next_up, container, false).apply {
			BackgroundManager.getInstance(activity).setBitmap(data.backdrop)

			logo.setImageBitmap(data.logo)
			image.setImageBitmap(data.thumbnail)
			title.text = data.title
			description.text = data.description?.toHtmlSpanned()

			fragment_next_up_buttons.apply {
				setPlayNextListener {
					startActivity(Intent(activity, PlaybackOverlayActivity::class.java))
					activity?.finish()
				}
				setCancelListener {
					activity?.finish()
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()

		fragment_next_up_buttons.startTimer()
	}

	override fun onPause() {
		super.onPause()

		fragment_next_up_buttons.stopTimer()
	}
}
