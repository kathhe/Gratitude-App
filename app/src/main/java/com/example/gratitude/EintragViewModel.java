package com.example.gratitude;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EintragViewModel extends AndroidViewModel {

    private EintragRepository mRepository;
    private final LiveData<List<Eintrag>> mAllEintraege;
    private final LiveData<List<Eintrag>> mChronologicalEintraege;

    private final String VM_TAG = "ViewModel";

    public EintragViewModel(@NonNull Application application) {
        super(application);

        // Neue Repository wird instanziert
        mRepository = new EintragRepository(application);

        // Auf Repository-Methoden zugreifen
        mAllEintraege = mRepository.getallEintraege();
        mChronologicalEintraege = mRepository.getChronologicalEntraege();
    }

    /**
     * Methode, die, mit Hilfe der Repository, alle Einträge aus der Datenbank ausliest
     */
    LiveData<List<Eintrag>> getmAllEintraege() {
        return mAllEintraege;
    }

    /**
     * Methode, die, mit Hilfe der Repository, alle Einträge chronologisch aus der Datenbank ausliest
     */
    LiveData<List<Eintrag>> getmChronologicalEintraege() {
        return mChronologicalEintraege;
    }

    /***
    * Schreibt mit Hilfe des Repositoy, das Objekts in die Datenbank
    */
    public void insert(Eintrag eintrag) {
        mRepository.insert(eintrag);

        Log.d(VM_TAG, eintrag.getAllValues());
        Log.d(VM_TAG, "View model: insert executed.");
    }

    /**
     * Aktualisiert mit Hilfe des Repository, das Objekts in der Datenbank
     */
    public void update(Eintrag eintrag){
        mRepository.update(eintrag);

        Log.d(VM_TAG, eintrag.getAllValues());
        Log.d(VM_TAG, "View model: update executed.");

    }

    /**
     * Löscht mit Hilfe der Repositoy, das Objekts aus der Datenbank
     */
    public void delete(Eintrag eintrag){
        mRepository.delete(eintrag);

        Log.d(VM_TAG, eintrag.getAllValues());
        Log.d(VM_TAG, "View model: delete executed.");
    }
}
