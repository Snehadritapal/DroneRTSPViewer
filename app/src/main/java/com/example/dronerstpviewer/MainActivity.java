package com.example.dronerstpviewer;

import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LibVLC libVLC;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerRecorder;
    private VLCVideoLayout videoLayout;

    private EditText urlEditText;
    private Button playButton, recordButton, btnEnterPip;

    private boolean isRecording = false;
    private String outputPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI references
        urlEditText = findViewById(R.id.rtspUrlEditText);
        playButton = findViewById(R.id.playButton);
        recordButton = findViewById(R.id.recordButton);
        btnEnterPip = findViewById(R.id.btnEnterPip);
        videoLayout = findViewById(R.id.vlc_video_layout);

        // VLC options
        ArrayList<String> options = new ArrayList<>();
        options.add("--network-caching=150");
        options.add("--rtsp-tcp");

        libVLC = new LibVLC(this, options);
        mediaPlayer = new MediaPlayer(libVLC);               // for playback
        mediaPlayerRecorder = new MediaPlayer(libVLC);       // for recording

        mediaPlayer.attachViews(videoLayout, null, false, false);

        // Play Button
        playButton.setOnClickListener(v -> {
            String rtspUrl = urlEditText.getText().toString().trim();
            if (rtspUrl.isEmpty()) {
                Toast.makeText(this, "Please enter a valid RTSP URL", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mediaPlayer.isPlaying()) mediaPlayer.stop();

            Media media = new Media(libVLC, Uri.parse(rtspUrl));
            media.setHWDecoderEnabled(true, false);

            mediaPlayer.setMedia(media);
            mediaPlayer.play();
        });

        // Record Button
        recordButton.setOnClickListener(v -> {
            String rtspUrl = urlEditText.getText().toString().trim();
            if (rtspUrl.isEmpty()) {
                Toast.makeText(this, "Please enter a valid RTSP URL", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isRecording) {
                // Start Recording
                File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "RTSP_Recordings");
                if (!outputDir.exists()) outputDir.mkdirs();

                outputPath = new File(outputDir, "recorded_" + System.currentTimeMillis() + ".mp4").getAbsolutePath();

                Media media = new Media(libVLC, Uri.parse(rtspUrl));
                media.setHWDecoderEnabled(false, false); // Disable video for recorder
                media.addOption(":sout=#file{dst=" + outputPath + "}");
                media.addOption(":sout-keep");

                mediaPlayerRecorder.setMedia(media);
                mediaPlayerRecorder.play();

                Log.d("Recording", "Started recording to: " + outputPath);
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
                recordButton.setText("Stop Recording");
                isRecording = true;

            } else {
                // Stop Recording
                if (mediaPlayerRecorder.isPlaying()) {
                    mediaPlayerRecorder.stop();
                }

                Toast.makeText(this, "Recording saved to:\n" + outputPath, Toast.LENGTH_LONG).show();
                recordButton.setText("Start Recording");
                isRecording = false;
            }
        });

        // PiP button
        btnEnterPip.setOnClickListener(v -> enterPipModeManually());
    }

    private void enterPipModeManually() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Rational aspectRatio = new Rational(16, 9);
            PictureInPictureParams pipBuilder = new PictureInPictureParams.Builder()
                    .setAspectRatio(aspectRatio)
                    .build();
            enterPictureInPictureMode(pipBuilder);
        }
    }

    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        enterPipModeManually();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        int visibility = isInPictureInPictureMode ? View.GONE : View.VISIBLE;

        urlEditText.setVisibility(visibility);
        playButton.setVisibility(visibility);
        recordButton.setVisibility(visibility);
        btnEnterPip.setVisibility(visibility);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (mediaPlayerRecorder != null) {
            mediaPlayerRecorder.stop();
            mediaPlayerRecorder.release();
        }
        if (libVLC != null) libVLC.release();

        super.onDestroy();
    }
}
