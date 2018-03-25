package com.example.mohan.notes;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

private TextView searchQuery;
ArrayList<Note> notes;
RecyclerView recyclerView;
myAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchQuery=(TextView) findViewById(R.id.search_query);

        handleIntent(getIntent());

        notes = new ArrayList<Note>();
    }

    public void handleIntent(Intent intent) {
        recyclerView =(RecyclerView)findViewById(R.id.search_recycler_view);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            searchQuery.setText("the query is "+query);

            //getting the results
            notes = new ArrayList<Note>();
            mDBHandler handler=new mDBHandler(this);
            notes=handler.getSearchResult(query,notes);



         //  String s=handler.resultsToString(query,this);

            try {
                recyclerView.hasFixedSize();
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

            }catch (NullPointerException e){
                Toast.makeText(this,notes.get(0).get_title(),Toast.LENGTH_SHORT).show();
            }


        }
    }


}
