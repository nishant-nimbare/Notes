package com.example.mohan.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class noteEditor extends AppCompatActivity {
mDBHandler handler;
EditText newTitle;
EditText newDescription;
//TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        handler = new mDBHandler(this);
        newDescription=findViewById(R.id.edit_description);
        newTitle=findViewById(R.id.edit_title);
        //textView=(TextView)findViewById(R.id.databaase);



        Bundle bundle=getIntent().getExtras();
        if(bundle !=null){
            newTitle.setText(bundle.getString("title"));
            newDescription.setText(bundle.getString("description"));
        }

        //textView.setText(handler.databaseToString());

    }

    public void editAddButtonClicked(View v){
        Note note=new Note(newTitle.getText().toString(),newDescription.getText().toString());

        handler.addNote(note,getApplicationContext());
        Toast.makeText(getApplicationContext(),"note added ",Toast.LENGTH_SHORT).show();

        Intent i = new Intent(noteEditor.this,MainActivity.class);
        startActivity(i);
    }
    public void editDeleteButtonClicked(View v){
        handler.deleteNote(newTitle.getText().toString());
        Toast.makeText(getApplicationContext(),"note deleted ",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(noteEditor.this,MainActivity.class);
        startActivity(i);
    }
}
