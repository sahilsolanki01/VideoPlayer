package com.solanki.sahil.test_player.ui.player;

import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Clock;

public class PlayerTrackSelector implements TrackSelection.Factory {
    private BandwidthMeter bandwidthMeter;

    public PlayerTrackSelector(BandwidthMeter bandwidthMeter) {
        this.bandwidthMeter = bandwidthMeter;
    }

    @Override
    public TrackSelection createTrackSelection(TrackGroup group, int... tracks) {
        return new AdaptiveTrackSelection(group,tracks,bandwidthMeter,
                AdaptiveTrackSelection.DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS,
                AdaptiveTrackSelection.DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS,
                AdaptiveTrackSelection.DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS,
                AdaptiveTrackSelection.DEFAULT_BANDWIDTH_FRACTION,
                AdaptiveTrackSelection.DEFAULT_BUFFERED_FRACTION_TO_LIVE_EDGE_FOR_QUALITY_INCREASE,
                AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS,
                Clock.DEFAULT);
    }
}
