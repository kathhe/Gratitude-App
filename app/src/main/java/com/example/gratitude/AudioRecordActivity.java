package com.example.gratitude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class AudioRecordActivity extends AppCompatActivity {

    // Quelle: https://developer.android.com/guide/topics/media/mediarecorder
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

    private PlayButton   playButton = null;
    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    /**
     * Erfragt Erlaubnis zur Nutzung des Mikrofons
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    /**
     * Startet oder stoppt den Aufnahmevorgang.
     * @param start
     */
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    /**
     * Startet oder stoppt den Abspielvorgang.
     * @param start
     */
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    /**
     * Spielt die Audio-Datei ab.
     */
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    /**
     * Stoppt das Abspielen der Audio-Datei.
     */
    private void stopPlaying() {
        player.release();
        player = null;
    }

    /**
     * Startet das Aufnhemen der Audio-Datei.
     */
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    /**
     * Stppt das Aufnhemen der Audio-Datei.
     */
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }


    class RecordButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            /**
             * Clicklistener für den Aufnahmevorgang
             * @param v
             */
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        /**
         * Ändert den Text des Record-Buttons
         * @param ctx
         */
        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            /**
             * Clicklistener für den Abspielvorgang
             * @param v
             */
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        /**
         * Ändert den Text des Play-Buttons
         * @param ctx
         */
        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        LinearLayout ll = new LinearLayout(this);
        recordButton = new RecordButton(this);
        ll.addView(recordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        playButton = new PlayButton(this);
        ll.addView(playButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);
    }

    /**
     * Stoppt den Aufnahme Vorgang.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}