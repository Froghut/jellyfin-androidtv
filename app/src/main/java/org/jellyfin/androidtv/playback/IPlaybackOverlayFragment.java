package org.jellyfin.androidtv.playback;

import org.jellyfin.apiclient.model.dto.BaseItemDto;
import org.jellyfin.apiclient.model.mediainfo.SubtitleTrackInfo;

public interface IPlaybackOverlayFragment {
    void setCurrentTime(long time);
    void setSecondaryTime(long time);
    void setFadingEnabled(boolean value);
    void setPlayPauseActionState(int state);
    void updateDisplay();
    void finish();
    void addManualSubtitles(SubtitleTrackInfo info);
    void updateSubtitles(long posMs);
    void showSubLoadingMsg(boolean show);
    void showNextUp(String id);
}
