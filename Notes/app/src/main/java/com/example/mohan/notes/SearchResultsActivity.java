package com.example.mohan.notes;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

private EditText searchQuery;
ArrayList<Note> notes;
RecyclerView recyclerView;
myAdapter adapter;
TextView database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchQuery=(EditText)findViewById(R.id.search_query);
        handleIntent(getIntent());


    }

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            searchQuery.setText("the query is "+query);
/*
            //getting the results
            notes = new ArrayList<Note>();
            mDBHandler handler=new mDBHandler(this);
          notes =  handler.getSearchResult(query,notes);

          database.findViewById(R.id.database);

          String s="";
          for (int i=0;i<notes.size();i++){
              s=s+notes.get(i).get_title()+" \n";
          }

          database.setText(s);*/


/*          //showing the results
            recyclerView =(RecyclerView)findViewById(R.id.search_recycler_view);
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
            });

            recyclerView.setAdapter(adapter);*/
        }
    }
}
