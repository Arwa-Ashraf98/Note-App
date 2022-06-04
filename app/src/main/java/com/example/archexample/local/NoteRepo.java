package com.example.archexample.local;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.archexample.local.NoteDao;
import com.example.archexample.local.NoteDatabase;
import com.example.archexample.models.Note;

import java.util.List;

public class NoteRepo {
    public MutableLiveData<String> successLiveData = new MutableLiveData<>();
    public MutableLiveData<String> insertLiveData = new MutableLiveData<>();
    public MutableLiveData<String> updateLiveData = new MutableLiveData<>();
    public MutableLiveData<String> deletedLiveData = new MutableLiveData<>();
    private NoteDao noteDao;
    private NoteDatabase noteDatabase;
    private LiveData<List<Note>> allNotes;

    public NoteRepo(Application application) {
        // inside it you can initialize database and call dao
        noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.getNoteDao();
        allNotes = noteDao.getAllNote();
    }

    public void insert(Note note) {
        // have instance of async task
        new insertNoteAsyncTask(noteDao).execute(note);
        insertLiveData.setValue("inserted");

    }

    public void delete(Note note) {
        new deleteNoteAsyncTask(noteDao).execute(note);
        deletedLiveData.setValue("deleted");

    }

    public void update(Note note) {
        new updateNoteAsyncTask(noteDao).execute(note);
        updateLiveData.setValue("updated");

    }

    public void deleteAllNotes() {
        new deleteAllNoteAsyncTask(noteDao).execute();

    }

    public LiveData<List<Note>> getAllNotes() {
        successLiveData.setValue("success");
        return allNotes;
    }
    // room work om background thread so you have to create multiple async task for each method that work on
    // ui thread

    private static class insertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        // has warning bec you have to override
        private NoteDao noteDao;

        private insertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class updateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private updateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class deleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private deleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private deleteAllNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAllNotes();
            return null;
        }
    }


}
