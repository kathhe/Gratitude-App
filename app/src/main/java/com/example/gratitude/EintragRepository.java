package com.example.gratitude;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EintragRepository {

    private EintragDao mEintragDao;
    private LiveData<List<Eintrag>> mAllEintraege;
    private LiveData<List<Eintrag>> mChronologicalEintraege;
    private final String R_TAG = "Repository";

    EintragRepository(Application application) {
        // Datenbank instanzieren
        EintragRoomDatabase db = EintragRoomDatabase.getDatabase(application);

        // Auf mEintragDao zugreifen
        mEintragDao = db.eintragDao();

        // Auf DAO-Methoden zugreifen
        mAllEintraege = mEintragDao.getAlphabetizedEintraege();
        mChronologicalEintraege = mEintragDao.getChronologicalEintraege();
    }

    /**
     * Methode, die, mit Hilfe der DAO, alle Einträge aus der Datenbank ausliest.
     */
    LiveData<List<Eintrag>> getallEintraege(){
        return mAllEintraege;
    }

    /**
     * Methode, die, mit Hilfe der DAO, alle Einträge aus der Datenbank chronologisch ausliest.
     */
    LiveData<List<Eintrag>> getChronologicalEntraege(){
        return mChronologicalEintraege;
    }

    /**
     * Schreibt mit Hilfe des DAO, das Objekts in die Datenbank
     */
    void insert(Eintrag eintrag){
        EintragRoomDatabase.databaseWriteExecutor.execute(() -> {
            mEintragDao.insert(eintrag);
        });
        Log.d(R_TAG, "Repository: insert executed.");
    }

    /**
    * Aktualisiert mit Hilfe des DAO, das Objekts in der Datenbank
    */
    void update(Eintrag eintrag){
        EintragRoomDatabase.databaseWriteExecutor.execute(() -> {
            mEintragDao.update(eintrag);
        });
        Log.d(R_TAG, "Repository: update executed.");
    }

    /**
     * Löscht mit Hilfe der DAO, das Objekts aus der Datenbank
     */
    void delete(Eintrag eintrag){
        EintragRoomDatabase.databaseWriteExecutor.execute(() -> {
            mEintragDao.delete(eintrag);
        });
        Log.d(R_TAG, "Repository: delete executed.");
    }

}
