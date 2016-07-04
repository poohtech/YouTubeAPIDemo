package com.esp.testvideo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.esp.testvideo.R;
import com.esp.testvideo.Utils.Config;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by admin on 4/2/16.
 */
public class PlayingVideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayerView playerView;
    private static final int RECOVERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);
        System.out.println("Videos IDDDSSSSS" + getIntent().getStringExtra("videoId"));
        playerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        // Initializing video player with developer key
        playerView.initialize(Config.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

        if (!wasRestored) {
            // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automaticall
            youTubePlayer.loadVideo(getIntent().getStringExtra("videoId"));

            // youTubePlayer.loadVideo("cBz-1lEVS9M"); //loadVideo will play video automatically
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            System.out.println("----------onActivityResult--------");
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }
}
