package com.example.gratitude;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Eintrag.class}, version = 1, exportSchema = false)
/**
 * Abstrakte Klasse, die die Datenbank erstellt.
 * Quelle: https://developer.android.com/codelabs/android-room-with-a-view
 */
public abstract class EintragRoomDatabase extends RoomDatabase {

    public abstract EintragDao eintragDao();

    private static volatile EintragRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Singleton der verhindert, dass mehr als eine Instanz der Datanbank aktiv ist.
     * @param context
     * @return
     */
    static EintragRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (EintragRoomDatabase.class) {
                if(INSTANCE == null) {
                    // Methode: fallbackToDestructiveMigration() hinzugefügt, da bei mir die Datenbank sonst abgestürzt ist
                    // Quelle: https://stackoverflow.com/questions/44543608/room-cannot-verify-the-data-integrity-in-android
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EintragRoomDatabase.class, "gratitude_database").fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).build();

                }
            }
        }

        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.

            });
        }
    };

}
