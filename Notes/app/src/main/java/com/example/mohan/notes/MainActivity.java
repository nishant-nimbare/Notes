package com.example.mohan.notes;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

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


        //recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notes=new ArrayList<Note>();

        //handler
        handler = new mDBHandler(this);
        notes= handler.getData(notes,getApplicationContext());

        //adapter
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
        },notes.size());

        recyclerView.setAdapter(adapter);





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


    public  void addButtonClicked(View v) {
        Intent i = new Intent(this, noteEditor.class);
        startActivityForResult(i,1);

    }

public void searchButtonClicked(View view){

}
}
