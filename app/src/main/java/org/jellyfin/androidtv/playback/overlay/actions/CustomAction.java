package org.jellyfin.androidtv.playback.overlay.actions;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.leanback.widget.PlaybackControlsRow;

import org.jellyfin.androidtv.playback.PlaybackController;
import org.jellyfin.androidtv.playback.overlay.CustomPlaybackTransportControlGlue;
import org.jellyfin.androidtv.playback.overlay.LeanbackOverlayFragment;

public abstract class CustomAction extends PlaybackControlsRow.MultiAction {

    private Context context;
    private CustomPlaybackTransportControlGlue customPlaybackTransportControlGlue;

    public CustomAction(Context context, CustomPlaybackTransportControlGlue customPlaybackTransportControlGlue) {
        super(0);
        this.context = context;
        this.customPlaybackTransportControlGlue = customPlaybackTransportControlGlue;
    }

    public void onCustomActionClicked(View view) { // We need a custom onClicked implementation for showing the popup
        customPlaybackTransportControlGlue.onCustomActionClicked(this, view);
    }

    void initializeWithIcon(int resourceId) {
        Drawable drawable = context.getDrawable(resourceId);
        setIcon(drawable);
        setDrawables(new Drawable[]{drawable});
    }

    public void handleClickAction(PlaybackController playbackController, LeanbackOverlayFragment leanbackOverlayFragment, Context context, View view) {

    }
}
