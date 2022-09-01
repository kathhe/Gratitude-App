package com.example.gratitude;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface EintragDao {
    /**
     * Speichert einen Eintrag in die Datenbank ab.
     */
    @Insert
    void insert(Eintrag eintrag);

    /**
     * Aktualisiert einen Eintrag in der Datenbank.
     */
    @Update
    void update(Eintrag eintrag);

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    @Query("DELETE FROM eintrag_table")
    void deleteAll();

    /**
     * Löscht einen Eintrag aus der Datenbank.
     */
    @Delete
    void delete(Eintrag eintrag);

    /**
     * Liefert eine alphabetizierte Liste aller Einträge.
     */
    @Query("SELECT * FROM eintrag_table ORDER BY datum ASC")
    LiveData<List<Eintrag>> getAlphabetizedEintraege();

    /**
     * Liefert eine chronologische Liste aller Einträge.
     */
    @Query("SELECT * FROM eintrag_table ORDER BY datum DESC")
    LiveData<List<Eintrag>> getChronologicalEintraege();

}
