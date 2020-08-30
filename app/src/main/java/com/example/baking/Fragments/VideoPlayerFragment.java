package com.example.baking.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baking.Classes.Step;
import com.example.baking.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.Serializable;

public class VideoPlayerFragment extends Fragment {

    private static final String LOG_TAG = VideoPlayerFragment.class.getSimpleName();

    private Step step;
    private PlayerView playerView;
    private SimpleExoPlayer mPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private Button mPreviousButton;
    private Button mNextButton;

    OnPreviousButtonClickListener mPreviousButtonClickListener;
    OnNextButtonClickListener mNextButtonClickListener;

    public interface OnPreviousButtonClickListener {
        void onPreviousButtonClicked(Step step);
    }

    public interface OnNextButtonClickListener {
        void onNextButtonClicked(Step step);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mPreviousButtonClickListener = (OnPreviousButtonClickListener) context;
            mNextButtonClickListener = (OnNextButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                + " must implement each onClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video_player, container , false);

        if (savedInstanceState == null) {
            fetchBundle();

        } else {
            step = (Step) savedInstanceState.getSerializable("step");
            playbackPosition = savedInstanceState.getLong("videoPosition");
            currentWindow = savedInstanceState.getInt("videoWindow");
        }

        TextView stepDescriptionTextView = rootView.findViewById(R.id.tv_step_description);
        playerView = rootView.findViewById(R.id.pv_video);

        stepDescriptionTextView.setText(step.getDescription());

        mPreviousButton = rootView.findViewById(R.id.button_previous_step);
        mNextButton = rootView.findViewById(R.id.button_next_step);

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Previous button clicked");
                mPreviousButtonClickListener.onPreviousButtonClicked(step);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Next button clicked");
                mNextButtonClickListener.onNextButtonClicked(step);
            }
        });

        return rootView;
    }

    private void fetchBundle() {
        Bundle bundle = this.getArguments();

        assert bundle != null;
        step = (Step) bundle.getSerializable("videoPlayer");
        Log.d(LOG_TAG, step.getDescription());
    }

    private void initializePlayer() {
        mPlayer = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(mPlayer);

        Uri uri = Uri.parse(step.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.setPlayWhenReady(playWhenReady);
        mPlayer.seekTo(currentWindow, playbackPosition);
        mPlayer.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                getContext(), "BakingApp");

        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            playWhenReady = mPlayer.getPlayWhenReady();
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!step.getVideoURL().isEmpty()) {
            if (Util.SDK_INT >= 24) {
                playerView.setVisibility(View.VISIBLE);
                initializePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!step.getVideoURL().isEmpty()) {
            if (Util.SDK_INT < 24 || mPlayer == null) {
                playerView.setVisibility(View.VISIBLE);
                initializePlayer();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("step", step);
        outState.putLong("videoPosition", playbackPosition);
        outState.putInt("videoWindow", currentWindow);
    }
}
