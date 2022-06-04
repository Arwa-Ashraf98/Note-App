package com.example.archexample.local;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.archexample.models.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();

    public static NoteDatabase noteInstance;
// that how i am working already and i memorized it so it fine to try new way but know the difference
// ------------------------------------------------------------------------------------------------------
//    public static synchronized void initRoom(Context context) {
//        if (noteInstance == null) {
//            noteInstance = Room.
//                    databaseBuilder(context, NoteDatabase.class, "Note_Database")
//                    .fallbackToDestructiveMigration()
//                    .build();
//        }
//    }
//
//    public static NoteDatabase getInstance() {
//        return noteInstance;
//    }

    public static synchronized NoteDatabase getInstance(Context context) {
        if (noteInstance == null) {
            noteInstance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomDatabaseCallBack)
                    .build();
        }
        return noteInstance;
    }

    private static RoomDatabase.Callback roomDatabaseCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDbAsyncTas(noteInstance).execute();
        }
    };

    private static class populateDbAsyncTas extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private populateDbAsyncTas(NoteDatabase noteDatabase) {
            noteDao = noteDatabase.getNoteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1 ", 1));
            noteDao.insert(new Note("Title 2", "Description 2 ", 2));
            noteDao.insert(new Note("Title 3", "Description 3 ", 3));
            return null;
        }
    }


}
