package com.example.archexample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.archexample.R;
import com.example.archexample.adapter.NoteAdapter;
import com.example.archexample.databinding.ActivityMainBinding;
import com.example.archexample.helper.Cons;
import com.example.archexample.models.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private NoteViewModel noteViewModel;
    private final NoteAdapter adapter = new NoteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
//        binding.recyclerView.setHasFixedSize(true);
        getNote();
        onClicks();
        onItemHandle();
        binding.recyclerView.setAdapter(adapter);
        getSupportActionBar().setTitle(getString(R.string.note));

    }

    private void getNote() {
        noteViewModel
                .getAllNote()
                .observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        // update Recycler
                        adapter.submitList(notes);
                    }
                });
    }

    private void onClicks() {
        binding.buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, Cons.REQUEST_CODE_ADD_NOTE);
            }
        });
        adapter.setSetOnNoteItemListener(new NoteAdapter.setOnNoteItemListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(Cons.EXTRA_TITLE, note.getTitle());
                intent.putExtra(Cons.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(Cons.EXTRA_PRIORITY, note.getPriority());
                intent.putExtra(Cons.EXTRA_ID, note.getId());
                startActivityForResult(intent, Cons.REQUEST_CODE_EDIT_NOTE);

            }
        });
    }

    private void onItemHandle() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                noteViewModel.deleteLiveData.observe(MainActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Cons.REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {

            String title = data.getStringExtra(Cons.EXTRA_TITLE);
            String description = data.getStringExtra(Cons.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(Cons.EXTRA_PRIORITY, 1);
            insertNote(title, description, priority);


        } else if (requestCode == Cons.REQUEST_CODE_EDIT_NOTE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(Cons.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, getString(R.string.note_cannot_be_updated), Toast.LENGTH_SHORT).show();
                return;
            } else {
                String title = data.getStringExtra(Cons.EXTRA_TITLE);
                String description = data.getStringExtra(Cons.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(Cons.EXTRA_PRIORITY, 1);


                updateNote(title, description, priority, id);

                Toast.makeText(this, getString(R.string.note_updatef), Toast.LENGTH_SHORT).show();

            }


        } else {
            Toast.makeText(this, getString(R.string.NoteNotSaved), Toast.LENGTH_SHORT).show();
        }
    }


    private void insertNote(String title, String description, int priority) {
        Note note = new Note(title, description, priority);
        noteViewModel.insert(note);
        noteViewModel.insertLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNote(String title, String description, int priority, int id) {
        Note note = new Note(title, description, priority);
        note.setId(id);
        noteViewModel.update(note);
        noteViewModel.updateLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            noteViewModel.deleteAllNote();
            noteViewModel.deleteLiveData.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}