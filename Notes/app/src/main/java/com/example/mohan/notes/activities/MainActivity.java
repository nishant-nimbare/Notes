package com.example.mohan.notes.activities;



import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mohan.notes.Util.*;
import com.example.mohan.notes.model.Note;
import com.example.mohan.notes.R;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements onItemClickListener {
    private static final String TAG = "MainActivity";
RecyclerView recyclerView;
RecyclerView.Adapter adapter;
ArrayList<Note> notes;
mDBHandler handler;
Spinner spinner;
ArrayAdapter arrayAdapter;
ActionMode mActionMode;
myActionModeCallback callback;
ArrayList<Integer> toBeDeletedPositions;
StaggeredGridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notes=new ArrayList<Note>();
        toBeDeletedPositions=new ArrayList<Integer>();

        //recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //adapter
        adapter = new myAdapter(notes, this,this);
        recyclerView.setAdapter(adapter);
        Log.e(TAG, "onCreate: adapter set");

        //handler
        handler = new mDBHandler(this);

        //spinner
        spinner=(Spinner)findViewById(R.id.spinner);

        //arrayAdapter for spinner
        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.spinner_arr,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = spinner.getSelectedItem().toString();
                if(item.equals("ALL")){
                    setUpUI("");
                }else{
                    setUpUI(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        callback = new myActionModeCallback(new contextMenuListener() {
            @Override
            public void deleteMultipleClicked() {
                ProgressDialog pd =new ProgressDialog(MainActivity.this);
                pd.setMessage("deleting notes");
                pd.show();
                for(int i=0;i<toBeDeletedPositions.size();i++){

                    if(Build.VERSION.SDK_INT >= 23) {
                        
                        layoutManager.findViewByPosition(toBeDeletedPositions.get(i)).setForeground(null);
                    }
                    
                    Note item = notes.get(toBeDeletedPositions.get(i));
                    Log.e(TAG, "deleteMultipleClicked: item  "+item.get_title() + " id "+ item.get_id());
                    handler.deleteNote(item.get_id());

                }

                toBeDeletedPositions.removeAll(toBeDeletedPositions);
                pd.dismiss();
                Toast.makeText(MainActivity.this,"items deleted", Toast.LENGTH_SHORT).show();
                mActionMode.finish();
                setUpUI("");
            }

            @Override
            public void clearToBeDeleted() {

                //delete multiple canceled

                //removing foreground
                for(int i=0;i<toBeDeletedPositions.size();i++) {

                    if (Build.VERSION.SDK_INT >= 23) {

                        layoutManager.findViewByPosition(toBeDeletedPositions.get(i)).setForeground(null);
                    }
                }

                toBeDeletedPositions.removeAll(toBeDeletedPositions);
            }


        });

    }


    public void setUpUI(String category){
        //remove old data
        notes.removeAll(notes);

        //getting data
        notes= handler.getData(notes,getApplicationContext(),category);
        adapter.notifyDataSetChanged();

        Log.e(TAG, "setUpUI: data set changed  "+notes.size());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_action,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

//used for debugging
    public void printDatabase() {
        String dbString = handler.databaseToString();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            setUpUI("");
            spinner.setSelection(0);
        }else{
            Toast.makeText(getApplicationContext(),"error in request code ",Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("RestrictedApi")
    public  void addButtonClicked(View v) {
        Intent i = new Intent(this, noteEditor.class);
        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= 21)
            bundle =ActivityOptions.makeSceneTransitionAnimation(this).toBundle();

        startActivityForResult(i,1,bundle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//TODO
           case R.id.setting:
               Toast.makeText(this, "settings will be available soon", Toast.LENGTH_SHORT).show();
                /*DialogFragment setting= new settingsFragment();
                setting.show(getFragmentManager(),"Settings");*/


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {

        Note item = notes.get(position);
        if (!callback.isVisible()) {

        //user wants to edit the clicked note
        //starting the note editor activity

        Intent i = new Intent(getApplicationContext(), noteEditor.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", item.get_title());
        bundle.putString("description", item.get_description());
        bundle.putInt("id", item.get_id());
        bundle.putString("category", item.get_category());
        i.putExtras(bundle);
        startActivityForResult(i, 1);
//                  Toast.makeText(getApplicationContext(),"you clicked"+item.get_title(),Toast.LENGTH_SHORT).show();

    }else if (callback.isVisible()){

        //user is deleting multiple notes

            //setting foreground of the selected item to differentiate
            if(Build.VERSION.SDK_INT >= 23) {
                layoutManager.findViewByPosition(position).setForeground(new ColorDrawable(0x55999999));
            }


        toBeDeletedPositions.add(/*item.get_id()*/position);
        mActionMode.setTitle(toBeDeletedPositions.size()+" items Selected");

    }
    }

    @Override
    public void onItemLongClick(int position) {

        if (!callback.isVisible()) {
            //starting action bar
            mActionMode = startSupportActionMode(callback);

            Toast.makeText(this, "you long Clicked " + notes.get(position).get_title(), Toast.LENGTH_SHORT).show();
        }

        //setting foreground of the selected item to differentiate
        if(Build.VERSION.SDK_INT >= 23) {
            layoutManager.findViewByPosition(position).setForeground(new ColorDrawable(0x55999999));
        }
        toBeDeletedPositions.add(position);      //todo
        mActionMode.setTitle(toBeDeletedPositions.size()+" items Selected");
    }




}
