package com.example.archexample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.archexample.R;
import com.example.archexample.helper.Cons;

public class AddEditNoteActivity extends AppCompatActivity {
    //    private ActivityAddNoteBinding binding;
    private NumberPicker numberPickerPriority;
    private EditText editTextTitle, editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_add_note);

        initView();

        String title = getIntent().getStringExtra(Cons.EXTRA_TITLE);
        String description = getIntent().getStringExtra(Cons.EXTRA_DESCRIPTION);
        int priority = getIntent().getIntExtra(Cons.EXTRA_PRIORITY, 1);

        // handle number picker
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(15);

        // handle action bar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        handleActionBar(title, description, priority);

    }

    private void initView() {
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextTitle = findViewById(R.id.edit_text_title);
    }

    private void handleActionBar(String title, String description, int priority) {
        Intent intent = getIntent();
        if (intent.hasExtra(Cons.EXTRA_ID)) {
            setTitle(getString(R.string.edit_note));
            editTextTitle.setText(title);
            editTextDescription.setText(description);
            numberPickerPriority.setValue(priority);
        } else
            setTitle(getString(R.string.add_note));

    }

    private void saveNote() {
        getData();
    }

    private void getData() {
        // get data to be saved in db
        String title = editTextTitle.getEditableText().toString().trim();
        String description = editTextDescription.getEditableText().toString().trim();
        int priority = numberPickerPriority.getValue();

        // validate the data
        Validation(title, description, priority);
    }

    private void Validation(String title, String description, int priority) {
        if (title.trim().isEmpty()) {
            editTextTitle.setError(getString(R.string.required));
        } else if (description.trim().isEmpty()) {
            editTextDescription.setError(getString(R.string.required));
        } else {
            insertUpdateNote(title, description, priority);
        }
    }

    private void insertUpdateNote(String title, String description, int priority) {
        Intent intent = new Intent();
        intent.putExtra(Cons.EXTRA_TITLE, title);
        intent.putExtra(Cons.EXTRA_DESCRIPTION, description);
        intent.putExtra(Cons.EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(Cons.EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(Cons.EXTRA_ID, id);
        }
        setResult(RESULT_OK, intent);
        finish();


    }

    // inflate the Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_nte_menu, menu);
        return true;
    }

    // handle Clicks of menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_note);
//
//        editTextTitle = findViewById(R.id.edit_text_title);
//        editTextDescription = findViewById(R.id.edit_text_description);
//         numberPickerPriority = findViewById(R.id.number_picker_priority);
//
//        numberPickerPriority.setMinValue(1);
//        numberPickerPriority.setMaxValue(10);
//
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
//
//        Intent intent = getIntent();
//
//        if (intent.hasExtra(Cons.EXTRA_ID)) {
//            setTitle("Edit Note");
//            editTextTitle.setText(intent.getStringExtra(Cons.EXTRA_TITLE));
//            editTextDescription.setText(intent.getStringExtra(Cons.EXTRA_DESCRIPTION));
//            numberPickerPriority.setValue(intent.getIntExtra(Cons.EXTRA_PRIORITY, 1));
//        } else {
//            setTitle("Add Note");
//        }
//  }

//    private void saveNote() {
//        String title = editTextTitle.getText().toString();
//        String description = editTextDescription.getText().toString();
//        int priority = numberPickerPriority.getValue();
//
//        if (title.trim().isEmpty() || description.trim().isEmpty()) {
//            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Intent data = new Intent();
//        data.putExtra(Cons.EXTRA_TITLE, title);
//        data.putExtra(Cons.EXTRA_DESCRIPTION, description);
//        data.putExtra(Cons.EXTRA_PRIORITY, priority);
//
//        int id = getIntent().getIntExtra(Cons.EXTRA_ID, -1);
//        if (id != -1) {
//            data.putExtra(Cons.EXTRA_ID, id);
//        }
//
//        setResult(RESULT_OK, data);
//        finish();
//    }


}