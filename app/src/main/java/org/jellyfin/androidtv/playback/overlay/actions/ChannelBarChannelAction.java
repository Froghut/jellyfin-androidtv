package org.jellyfin.androidtv.playback.overlay.actions;

import android.content.Context;

import org.jellyfin.androidtv.R;
import org.jellyfin.androidtv.playback.overlay.CustomPlaybackTransportControlGlue;

public class ChannelBarChannelAction extends CustomAction {

    public ChannelBarChannelAction(Context context, CustomPlaybackTransportControlGlue customPlaybackTransportControlGlue) {
        super(context, customPlaybackTransportControlGlue);
        super.initializeWithIcon(R.drawable.ic_channel_bar);
    }
}
