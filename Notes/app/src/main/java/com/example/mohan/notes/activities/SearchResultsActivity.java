package com.example.mohan.notes.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohan.notes.Util.onItemClickListener;
import com.example.mohan.notes.model.Note;
import com.example.mohan.notes.R;
import com.example.mohan.notes.Util.mDBHandler;
import com.example.mohan.notes.Util.myAdapter;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    private static final String TAG = "SearchResultsActivity";

private TextView searchQuery;
ArrayList<Note> notes;
RecyclerView recyclerView;
myAdapter adapter;
mDBHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchQuery=(TextView) findViewById(R.id.search_query);

        notes = new ArrayList<Note>();

        recyclerView =(RecyclerView)findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adapter
        adapter = new myAdapter(notes, this, new onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Note item = notes.get(position);

                Intent i = new Intent(getApplicationContext(),noteEditor.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",item.get_title());
                bundle.putString("description",item.get_description());
                bundle.putString("category",item.get_category());
                bundle.putInt("id",item.get_id());
                i.putExtras(bundle);
                startActivityForResult(i,1);
                Log.e(TAG, "onItemClick: position "+position+" category" +item.get_category()  );
            }

            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(SearchResultsActivity.this, "yor long Clicked "+notes.get(position).get_title(), Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView.setAdapter(adapter);

        handler=new mDBHandler(this);

        handleIntent(getIntent());
    }

    public void handleIntent(Intent intent) {


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            searchQuery.setText("the query is "+query);

            //getting the results
            if(notes.size()!=0) {
                notes.removeAll(notes);
            }
            notes=handler.getSearchResult(query,notes);
            adapter.notifyDataSetChanged();

            Log.e(TAG, "handleIntent: data set changed "+notes.size() );


         //  String s=handler.resultsToString(query,this);



        }
    }



}
