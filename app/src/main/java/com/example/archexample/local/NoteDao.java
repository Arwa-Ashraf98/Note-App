package com.example.archexample.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.archexample.models.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("select * from note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNote();

    @Query("DELETE FROM note_table")
    void deleteAllNotes();


}
