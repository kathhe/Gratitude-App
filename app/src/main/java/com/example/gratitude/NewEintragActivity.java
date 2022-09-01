package com.example.gratitude;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewEintragActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.gratitude.ID";
    public static final String EXTRA_DATUM = "com.example.gratitude.DATUM";
    public static final String EXTRA_TITEL = "com.example.gratitude.TITEL";
    public static final String EXTRA_STIMMUNG = "com.example.gratitude.STIMMUNG";
    public static final String EXTRA_TEXT = "com.example.gratitude.TEXT";
    public static final String EXTRA_BILD = "com.example.gratitude.BILD";
    public static final String EXTRA_AUDIO = "com.example.gratitude.AUDIO";
    public static final String EXTRA_DELETE_EINTRAG = "com.example.gratitude.DELETE_EINTRAG";
    public static final String TAG = "NewEintragLogs";

    private static final int TAKE_PIC = 100;
    static final int REQUEST_IMAGE_CAPTURE = 99;

    private EditText mEditTitelView;
    private EditText mEditStimmungView;
    private EditText mEditTextView;
    private EditText mEditBildView;
    private EditText mEditAudioView;

    private TextView mTextDatumView;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;


    private ImageView imageView;
    private ImageButton photo;
    private Uri photoFromDB;

    // Aufgenommenes Image
    private String currentPhotoPath;
    private Uri currentPhotoUri;

    ImageButton buttonBack;
    private ImageButton buttonSaveEintrag;
    private ImageButton buttonDeleteEintrag;
    private ImageButton buttonVoiceMemo;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_eintrag);



        // Neues Bild aufnehmen
        imageView = findViewById(R.id.eintragImageView);
        photo = findViewById(R.id.ibutton_eintrag_add_bild);

        // App Erlaubnis zum Fotografieren
        if(ContextCompat.checkSelfPermission(NewEintragActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NewEintragActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, TAKE_PIC);
        }

        // Button zum Öffnen der Kamera.
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        mEditTitelView = findViewById(R.id.edit_eintrag_titel);
        mEditStimmungView = findViewById(R.id.edit_eintrag_stimmung);
        mEditTextView = findViewById(R.id.edit_eintrag_text);
        mEditBildView = findViewById(R.id.edit_eintrag_bild);
        mEditAudioView = findViewById(R.id.edit_eintrag_audio);

        buttonSaveEintrag = findViewById(R.id.ibutton_eintrag_save);
        buttonDeleteEintrag = findViewById(R.id.ibutton_eintrag_delete);
        buttonVoiceMemo = findViewById(R.id.ibutton_eintrag_add_audio);

        // Datum darstellen
        mTextDatumView = (TextView)findViewById(R.id.text_eintrag_datum);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("d/M/Y H:m");

        date = dateFormat.format(calendar.getTime());



        Intent intent = getIntent();
        // Hat der Eintrag bereits eine ID, dann sollen alle Daten aus dem Objekt ausgelesen werden.
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Eintrag bearbeiten");
            mTextDatumView.setText(intent.getStringExtra(EXTRA_DATUM));
            mEditTitelView.setText(intent.getStringExtra(EXTRA_TITEL));
            mEditStimmungView.setText(intent.getStringExtra(EXTRA_STIMMUNG));
            mEditTextView.setText(intent.getStringExtra(EXTRA_TEXT));
            mEditBildView.setText(intent.getStringExtra(EXTRA_BILD));
            mEditAudioView.setText(intent.getStringExtra(EXTRA_AUDIO));

            // Bild (aus der DB) darstellen
            photoFromDB = Uri.parse(intent.getStringExtra(EXTRA_BILD));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoFromDB);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Gibt es noch keine ID, dann soll eine neuer Eintrag erstellt werden.
            setTitle("Neue Eintrag hinzufügen");
            mTextDatumView.setText(date);
        }

        //Load mood values
        onRadioButtonViewCreate();

        // VoiceMemo Intent
        buttonVoiceMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insantieren des Intent
                Intent voicememointent = new Intent(NewEintragActivity.this, AudioRecordActivity.class);
                startActivity(voicememointent);
            }
        });

        // Listener zum speichern der Daten
        buttonSaveEintrag.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if (TextUtils.isEmpty(mEditTitelView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);

            } else {
                String datum = mTextDatumView.getText().toString();
                String titel = mEditTitelView.getText().toString();
                String stimmung = mEditStimmungView.getText().toString();
                String text = mEditTextView.getText().toString();
                String bild = mEditBildView.getText().toString();
                String audio = mEditAudioView.getText().toString();

                replyIntent.putExtra(EXTRA_DATUM, datum);
                replyIntent.putExtra(EXTRA_TITEL, titel);
                replyIntent.putExtra(EXTRA_STIMMUNG, stimmung);
                replyIntent.putExtra(EXTRA_TEXT, text);
                replyIntent.putExtra(EXTRA_BILD, bild);
                replyIntent.putExtra(EXTRA_AUDIO, audio);

                int id = getIntent().getIntExtra(EXTRA_ID, -1);

                if(id != -1){
                    replyIntent.putExtra(EXTRA_ID, id);
                }

                Bundle bundle = replyIntent.getExtras();
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        Log.d(TAG, key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                    }
                }

                // Intent zum Speichern wird mit Request code an MainActivity gesendet.
                setResult(RESULT_OK, replyIntent);

            }
            finish();
        });


        // Button zum Löschen des Eintrags
        buttonDeleteEintrag = findViewById(R.id.ibutton_eintrag_delete);

        buttonDeleteEintrag.setOnClickListener(view -> {

            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            Intent eintragIntentDelete = new Intent();

            // Eintrag ID wird dem Intent hinzugefügt.
            if(id != -1){
                eintragIntentDelete.putExtra(EXTRA_ID, id);
            }

            String datum = mTextDatumView.getText().toString();
            String titel = mEditTitelView.getText().toString();
            String stimmung = mEditStimmungView.getText().toString();
            String text = mEditTextView.getText().toString();
            String bild = mEditBildView.getText().toString();
            String audio = mEditAudioView.getText().toString();

            eintragIntentDelete.putExtra(EXTRA_DATUM, datum);
            eintragIntentDelete.putExtra(EXTRA_TITEL, titel);
            eintragIntentDelete.putExtra(EXTRA_STIMMUNG, stimmung);
            eintragIntentDelete.putExtra(EXTRA_TEXT, text);
            eintragIntentDelete.putExtra(EXTRA_BILD, bild);
            eintragIntentDelete.putExtra(EXTRA_AUDIO, audio);
            eintragIntentDelete.putExtra(EXTRA_DELETE_EINTRAG, true);

            // Intent zum Löschen wird mit Request code an MainActivity gesendet.
            setResult(RESULT_OK, eintragIntentDelete);

            finish();
        });


        // Button zum Zurücknavigieren
        buttonBack = (ImageButton) findViewById(R.id.new_eintrag_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }



    //Kamerafunktion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(currentPhotoUri);
            }
        }
    }


    // Quelle: https://developer.android.com/training/camera/photobasics
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Quelle: https://developer.android.com/training/camera/photobasics
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                String ello;
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);


                currentPhotoUri = photoURI;
                mEditBildView.setText("" + currentPhotoUri);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Log.d(TAG, "Kamera Intent läuft nicht.");
        }
    }

    public void onRadioButtonClicked(View view) {
        mEditStimmungView = findViewById(R.id.edit_eintrag_stimmung);

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.stim_vhappy_radio:
                if (checked)
                    mEditStimmungView.setText("Very happy");
                    break;
            case R.id.stim_happy_radio:
                if (checked)
                    mEditStimmungView.setText("Happy");
                    break;
            case R.id.stim_content_radio:
                if (checked)
                    mEditStimmungView.setText("Content");
                    break;
            case R.id.stim_dissa_radio:
                if (checked)
                    mEditStimmungView.setText("Dissatisfied");
                    break;
            case R.id.stim_sad_radio:
                if (checked)
                    mEditStimmungView.setText("Sad");
                    break;
        }
    }

    public void onRadioButtonViewCreate() {
        mEditStimmungView = findViewById(R.id.edit_eintrag_stimmung);
        String sMEditStimmungView = mEditStimmungView.getText().toString();

        RadioButton vhappy = findViewById(R.id.stim_vhappy_radio);
        RadioButton happy = findViewById(R.id.stim_happy_radio);
        RadioButton content = findViewById(R.id.stim_content_radio);
        RadioButton dissa = findViewById(R.id.stim_dissa_radio);
        RadioButton sad = findViewById(R.id.stim_sad_radio);

        // Check which radio button was clicked
        switch( sMEditStimmungView ) {
            case "Very happy":
                vhappy.setChecked(true);
                break;
            case "Happy":
                happy.setChecked(true);
                break;
            case "Content":
                content.setChecked(true);
                break;
            case "Dissatisfied":
                dissa.setChecked(true);
                break;
            case "Sad":
                sad.setChecked(true);
                break;
        }
    }
}
