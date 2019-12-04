package com.solanki.sahil.test_player.ui.player;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.AlertDialog;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.solanki.sahil.test_player.R;
import com.solanki.sahil.test_player.data.provider.CustomSharedPreference;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private DefaultDataSourceFactory dataSourceFactory;
    private String url;
    private ProgressBar progressBar;
    private static final String TAG = "PlayerActivity";
    private ImageView btn_settings, btn_full_screen, btn_playback;
    private DefaultTrackSelector trackSelector;
    private BandwidthMeter bandwidthMeter;
    private PlaybackParams param;
    private boolean mPlayVideoWhenReady;
    private int currentWindow;
    private long mLastPosition;
    private boolean check_state, check_fullscreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        check_fullscreen = true;
        check_state = false;

        progressBar = findViewById(R.id.progressBar);

        playerView = findViewById(R.id.player_view);
        playerView.setOnTouchListener(this);
        Log.e(TAG, "onCreate: ");
        btn_settings = findViewById(R.id.btn_settings);

        btn_settings.setOnClickListener(this);
        btn_settings.setEnabled(false);

        btn_full_screen = findViewById(R.id.btn_full_screen);
        btn_full_screen.setOnClickListener(this);

        btn_playback = findViewById(R.id.btn_playback);
        btn_playback.setOnClickListener(this);


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            init_landscape();
        }

        url = getIntent().getStringExtra("url");
        url = url.replace("\\", "");
        Log.e("ejgwlieg&^%%", "onCreate: " + url);

        hide_buttons();

    }

    @Override
    protected void onStart() {
        super.onStart();

        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory factory = new PlayerTrackSelector(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(factory);
        trackSelector = new DefaultTrackSelector();

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        playerView.setPlayer(player);


        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exo-test"));

        MediaSource mediaSource = build_media_source(Uri.parse(url));

        player.prepare(mediaSource);

        param = new PlaybackParams();

        player.setPlayWhenReady(true);

        if(CustomSharedPreference.getPreferences(this).contains(url)){
            player.seekTo(CustomSharedPreference.getLastPosition(this, url));

        }

        player_listerner();
    }


    @Override
    public void onClick(View v) {
        int i1 = v.getId();

        switch (i1) {
            case R.id.btn_playback:
                PopupMenu popup = new PopupMenu(this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.items, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.one:
                                param.setSpeed(0.5f);
                                player.setPlaybackParams(param);
                                return true;
                            case R.id.two:
                                param.setSpeed(1f);
                                player.setPlaybackParams(param);
                                return true;
                            case R.id.three:
                                param.setSpeed(1.5f);
                                player.setPlaybackParams(param);
                                return true;
                            case R.id.four:
                                param.setSpeed(2f);
                                player.setPlaybackParams(param);
                                return true;
                            case R.id.five:
                                param.setSpeed(2.5f);
                                player.setPlaybackParams(param);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                break;


            case R.id.btn_settings:
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    int rendererIndex = 0;
                    int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
                    boolean allowAdaptiveSelections =
                            rendererType == C.TRACK_TYPE_VIDEO
                                    || (rendererType == C.TRACK_TYPE_AUDIO
                                    && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);
                    Pair<AlertDialog, TrackSelectionView> dialogPair =
                            TrackSelectionView.getDialog(PlayerActivity.this, "Track Selector", trackSelector, rendererIndex);
                    dialogPair.second.setShowDisableOption(true);
                    dialogPair.second.setAllowAdaptiveSelections(allowAdaptiveSelections);
                    dialogPair.first.show();
                }
                break;

            case R.id.btn_full_screen:
                if (check_fullscreen){
                    check_fullscreen = false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                } else{
                    check_fullscreen = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
                break;


            default:
                return;

        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(playerView.isControllerVisible()){
            btn_settings.setVisibility(View.VISIBLE);
            btn_full_screen.setVisibility(View.VISIBLE);
            btn_playback.setVisibility(View.VISIBLE);
            hide_buttons();
            Log.e(TAG, "onPlayerClick: visible");
        }else {
            btn_settings.setVisibility(View.INVISIBLE);
            btn_full_screen.setVisibility(View.INVISIBLE);
            btn_playback.setVisibility(View.INVISIBLE);
            Log.e(TAG, "onPlayerClick: invisible");
        }
        return false;
    }


    public void hide_buttons(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // hide your button here
                btn_settings.setVisibility(View.INVISIBLE);
                btn_full_screen.setVisibility(View.INVISIBLE);
                btn_playback.setVisibility(View.INVISIBLE);

            }
        }, playerView.getControllerShowTimeoutMs());
    }


    private MediaSource build_media_source(Uri uri) {

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            return new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
        } else if (uri.getLastPathSegment().contains("m3u8")) {
            return new HlsMediaSource(uri, dataSourceFactory,
                    null, null);
        } else if (uri.getLastPathSegment().contains("mpd")){
            return new DashMediaSource(uri, dataSourceFactory,
                    new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);
        }

        return null;
    }


    private void player_listerner() {

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.");
                        if (progressBar != null) {
                            progressBar.setVisibility(VISIBLE);
                        }

                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: Video ended.");
                        player.seekTo(0);
                        break;
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_READY:
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.");
                        if (progressBar != null) {
                            progressBar.setVisibility(GONE);
                        }
                        if(playerView.isControllerVisible()){
                            btn_settings.setVisibility(View.VISIBLE);
                            btn_full_screen.setVisibility(View.VISIBLE);
                            btn_playback.setVisibility(View.VISIBLE);
                            Log.e(TAG, "onPlayerStateChanged: visible");
                        }else {
                            btn_settings.setVisibility(View.INVISIBLE);
                            btn_full_screen.setVisibility(View.INVISIBLE);
                            btn_playback.setVisibility(View.INVISIBLE);
                            Log.e(TAG, "onPlayerStateChanged: invisible");
                        }
                        btn_settings.setEnabled(true);

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            init_landscape();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            init_portrait();
        }
    }

    private void init_portrait() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
        params.width = params.MATCH_PARENT;
        params.height = params.WRAP_CONTENT;
        playerView.setLayoutParams(params);

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }

        PlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
    }

    private void init_landscape() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
        params.width = params.MATCH_PARENT;
        params.height = params.MATCH_PARENT;
        playerView.setLayoutParams(params);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        PlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (check_state) {

            player.setPlayWhenReady(true);
            player.seekTo(currentWindow, mLastPosition);

            // Put the player into the last state we were in.
            player.setPlayWhenReady(mPlayVideoWhenReady);

            Log.e(TAG, "onResume: " + currentWindow + " " + mLastPosition + " " + mPlayVideoWhenReady);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        mPlayVideoWhenReady = player.getPlayWhenReady();

        // Store off the last position our player was in before we paused it.
        mLastPosition = player.getCurrentPosition();
        CustomSharedPreference.setLastPosition(this, url, mLastPosition);

        currentWindow = player.getCurrentWindowIndex();

        player.setPlayWhenReady(false);

        check_state = true;

        Log.e(TAG, "onPause: " + currentWindow + " " + mLastPosition + " " + mPlayVideoWhenReady);
    }

    @Override
    protected void onStop() {
        super.onStop();

        playerView.setPlayer(null);
        player.release();
        player = null;


    }
}


