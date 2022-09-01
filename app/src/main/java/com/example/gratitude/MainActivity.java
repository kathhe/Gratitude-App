package com.example.gratitude;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Request codes für die Intents
    public static final int NEW_EINTRAG_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_EINTRAG_ACTIVITY_REQUEST_CODE = 2;
    private final String MA_TAG = "MainActivity";

    private EintragViewModel mEintragViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView für die Einträge wird instanziert und dem Layout hinzugefügt.
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // RecyclerAdapter für die Einträge.
        EintragRecyclerAdapter adapter = new EintragRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModel wird instanziert, alle Einträge werden dargestellt und beobachtet.
        mEintragViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(EintragViewModel.class);
        //mEintragViewModel.getmAllEintraege().observe(this, eintraege -> {
        mEintragViewModel.getmChronologicalEintraege().observe(this, eintraege -> {

            // Kopie aller Einträge werden im Adapter aktualisiert.
            adapter.setEintraege(eintraege);
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Button zum Hinzufügen eines neuen Eintrags. Ein Intent mit dem dazugehören Requestcode wird abgefeurt.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, NewEintragActivity.class);
            startActivityForResult(intent, NEW_EINTRAG_ACTIVITY_REQUEST_CODE);
        });


        // Klickt man auf einen Eintrag im Recycler, werden dessen Daten mit Hilfe eines Intents zum Bearbeiten dargestellt.
        adapter.setOnEintragClickListener( new EintragRecyclerAdapter.OnEintragClickListener() {
            @Override
            public void onEintragClick(Eintrag eintrag) {

                Intent intent = new Intent(MainActivity.this, NewEintragActivity.class);
                intent.putExtra(NewEintragActivity.EXTRA_ID, eintrag.getId());
                intent.putExtra(NewEintragActivity.EXTRA_DATUM, eintrag.getmDatum());
                intent.putExtra(NewEintragActivity.EXTRA_TITEL, eintrag.getmTitel());
                intent.putExtra(NewEintragActivity.EXTRA_STIMMUNG, eintrag.getmStimmung());
                intent.putExtra(NewEintragActivity.EXTRA_TEXT, eintrag.getmText());
                intent.putExtra(NewEintragActivity.EXTRA_BILD, eintrag.getmBild());
                intent.putExtra(NewEintragActivity.EXTRA_AUDIO, eintrag.getmAudio());

                startActivityForResult(intent, EDIT_EINTRAG_ACTIVITY_REQUEST_CODE);

            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Nach dem bearbeiten eines Eintrags, werden die Daten an den ViewModel weitergeleitet, je nach dem welche CRUD-Operation ausgeführt wird.
        if (requestCode == NEW_EINTRAG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String datum = data.getStringExtra(NewEintragActivity.EXTRA_DATUM);
            String titel = data.getStringExtra(NewEintragActivity.EXTRA_TITEL);
            String stimmung = data.getStringExtra(NewEintragActivity.EXTRA_STIMMUNG);
            String text = data.getStringExtra(NewEintragActivity.EXTRA_TEXT);
            String bild = data.getStringExtra(NewEintragActivity.EXTRA_BILD);
            String audio = data.getStringExtra(NewEintragActivity.EXTRA_AUDIO);

            //

            // Ein neues Objekt, mit den Werten aus dem Intent wird instanziert und dem ViewModel übergeben.
            Eintrag eintrag = new Eintrag(datum, stimmung, titel, text, bild, audio);

            Log.d(MA_TAG, eintrag.getAllValues());
            Log.d(MA_TAG, "Main Activity: created new.");
            mEintragViewModel.insert(eintrag);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.eintrag_saved,
                    Toast.LENGTH_LONG).show();

        } else if(requestCode == EDIT_EINTRAG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            if(data.getBooleanExtra(NewEintragActivity.EXTRA_DELETE_EINTRAG, false)) {
                int id = data.getIntExtra(NewEintragActivity.EXTRA_ID, -1);

                // Ein neues, leeres Objekt wird instanziert und mit einer ID an das Model übergeben.
                Eintrag eintrag = new Eintrag("", "", "", "", "", "");

                eintrag.setId(id);

                Log.d(MA_TAG, eintrag.getAllValues());
                Log.d(MA_TAG, "Main Activity: deleted.");
                mEintragViewModel.delete(eintrag);

                Toast.makeText(
                        getApplicationContext(),
                        R.string.eintrag_deleted,
                        Toast.LENGTH_LONG).show();
            } else {
                int id = data.getIntExtra(NewEintragActivity.EXTRA_ID, -1);

                if(id == -1){
                    // Falls beim Aktualsieren etwas falsch läuft.
                    Toast.makeText(this,R.string.eintrag_edit_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                String datum = data.getStringExtra(NewEintragActivity.EXTRA_DATUM);
                String stimmung = data.getStringExtra(NewEintragActivity.EXTRA_STIMMUNG);
                String titel = data.getStringExtra(NewEintragActivity.EXTRA_TITEL);
                String text = data.getStringExtra(NewEintragActivity.EXTRA_TEXT);
                String bild = data.getStringExtra(NewEintragActivity.EXTRA_BILD);
                String audio = data.getStringExtra(NewEintragActivity.EXTRA_AUDIO);

                // Ein neues Object mit aktualierten Werten aus dem Intent wird instanziert und dem ViewModel übergeben.
                Eintrag eintrag = new Eintrag(datum, stimmung, titel, text, bild, audio);

                // *** Id wird hier gesetzt um update (Dao macht vergleich der Ids) zu ermöglichen.
                eintrag.setId(id);

                Log.d(MA_TAG, eintrag.getAllValues());
                Log.d(MA_TAG, "Main Activity: edited & saved.");

                mEintragViewModel.update(eintrag);

                Toast.makeText(
                        getApplicationContext(),
                        R.string.eintrag_updated,
                        Toast.LENGTH_LONG).show();
            }
        } else {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}