package org.jellyfin.androidtv.preferences.ui

import android.content.Context
import android.graphics.Rect
import android.text.method.TransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.preference.PreferenceViewHolder
import androidx.preference.SeekBarPreference
import org.jellyfin.androidtv.R

class DurationSeekBarPreference(
	context: Context,
	attrs: AttributeSet? = null
) : SeekBarPreference(context, attrs) {
	init {
		// Explicitly set the layout or it will crash
		layoutResource = androidx.preference.R.layout.preference_widget_seekbar_material
	}

	override fun onBindViewHolder(view: PreferenceViewHolder) {
		super.onBindViewHolder(view)

		val textView = view.findViewById(R.id.seekbar_value) as TextView
		textView.transformationMethod = object : TransformationMethod {
			override fun onFocusChanged(view: View, sourceText: CharSequence?, focused: Boolean, direction: Int, previouslyFocusedRect: Rect) {}

			override fun getTransformation(source: CharSequence?, view: View): CharSequence? {
				val numberValue = source?.toString()?.toIntOrNull() ?: return source
				return (numberValue / 1000).toString()
			}
		}
	}
}
