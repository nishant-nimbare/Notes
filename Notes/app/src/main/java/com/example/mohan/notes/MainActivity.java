package com.example.mohan.notes;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohan.notes.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
ImageButton addButton;
RecyclerView recyclerView;
RecyclerView.Adapter adapter;
ArrayList<Note> notes;
mDBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();

    }


    public void setUpUI(){

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        handler = new mDBHandler(this);
        notes=new ArrayList<Note>();



        notes= handler.getData(notes,getApplicationContext());


        adapter = new myAdapter(notes, this, new myAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note item) {
                Intent i = new Intent(getApplicationContext(),noteEditor.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",item.get_title());
                bundle.putString("description",item.get_description());
                bundle.putInt("id",item.get_id());
                i.putExtras(bundle);
                startActivityForResult(i,1);
//                  Toast.makeText(getApplicationContext(),"you clicked"+item.get_title(),Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);


    }

    public void printDatabase() {
        String dbString = handler.databaseToString();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            setUpUI();
        }else{
            Toast.makeText(getApplicationContext(),"error in request code ",Toast.LENGTH_SHORT).show();
        }
    }

    //TODO
    public  void addButtonClicked(View v) {
        Intent i = new Intent(this, noteEditor.class);
        startActivityForResult(i,1);
        Log.e("activityStarted","activityStarted");
    }

   /* public void onDeleteButtonClicked(View v){
        String inputText = title.getText().toString();
        handler.deleteNote(inputText);
        printDatabase();
    }*/
}
