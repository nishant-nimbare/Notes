package com.example.mohan.notes;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class noteEditor extends AppCompatActivity {
mDBHandler handler;
EditText newTitle;
EditText newDescription;
boolean hasExtra;
int id;
//TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        handler = new mDBHandler(this);
        newDescription=findViewById(R.id.edit_description);
        newTitle=findViewById(R.id.edit_title);
        //textView=(TextView)findViewById(R.id.databaase);



        //actionbar
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        Bundle bundle=getIntent().getExtras();
        if(bundle !=null){
            newTitle.setText(bundle.getString("title"));
            newDescription.setText(bundle.getString("description"));
            id=bundle.getInt("id");
            hasExtra=true;
        }else{
            hasExtra=false;
        }

        //textView.setText(handler.databaseToString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.note_editior_action_bar,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        //hiding the delete button  when creating new note
        if(!hasExtra){
            MenuItem delete = menu.findItem(R.id.edit_delete_button);
            delete.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.edit_delete_button:
                editDeleteButtonClicked();
               return true;
            case R.id.edit_done_button:
                editDoneButtonClicked();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void editDoneButtonClicked(){


        if(hasExtra){
            handler.updateNote(id,newTitle.getText().toString(),newDescription.getText().toString());
            Toast.makeText(getApplicationContext(), "note edited ", Toast.LENGTH_SHORT).show();

        }else {
            Note note=new Note(newTitle.getText().toString(),newDescription.getText().toString());
            handler.addNote(note, getApplicationContext());
            Toast.makeText(getApplicationContext(), "note added ", Toast.LENGTH_SHORT).show();
        }

        setResult(1);
        finish();
    }
    public void editDeleteButtonClicked(){
        handler.deleteNote(newTitle.getText().toString());
        Toast.makeText(getApplicationContext(),"note deleted ",Toast.LENGTH_SHORT).show();


        setResult(1);
        finish();
    }
}
