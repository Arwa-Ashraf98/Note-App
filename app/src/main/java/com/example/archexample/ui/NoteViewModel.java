package com.example.archexample.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.archexample.local.NoteRepo;
import com.example.archexample.models.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    public NoteRepo repo;
    public LiveData<List<Note>> allNote;
    public MutableLiveData<String> insertLiveData;
    public MutableLiveData<String> updateLiveData;
    public MutableLiveData<String> deleteLiveData;
    public MutableLiveData<String> successLiveData;


    public NoteViewModel(@NonNull Application application) {
        super(application);
        repo = new NoteRepo(application);
        this.allNote = repo.getAllNotes();
        this.insertLiveData = repo.insertLiveData;
        this.updateLiveData = repo.updateLiveData;
        this.deleteLiveData = repo.deletedLiveData;
        this.successLiveData = repo.successLiveData;
    }

    public void insert(Note note) {
        repo.insert(note);

    }

    public void update(Note note) {
        repo.update(note);
    }

    public void delete(Note note) {
        repo.delete(note);
    }

    public void deleteAllNote() {
        repo.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNote() {
        return allNote;
    }

}
