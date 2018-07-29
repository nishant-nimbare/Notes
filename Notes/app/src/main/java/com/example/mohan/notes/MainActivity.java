package com.example.mohan.notes;



import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity{

RecyclerView recyclerView;
RecyclerView.Adapter adapter;
ArrayList<Note> notes;
mDBHandler handler;
Spinner spinner;
ArrayAdapter arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //spinner
        spinner=(Spinner)findViewById(R.id.spinner);

        //arrayAdapter
        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.spinner_arr,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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


    }


    public void setUpUI(String category){


        //recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
       recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notes=new ArrayList<Note>();

        //handler
        handler = new mDBHandler(this);
        notes= handler.getData(notes,getApplicationContext(),category);

        //adapter
        adapter = new myAdapter(notes, this, new myAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note item) {
                Intent i = new Intent(getApplicationContext(),noteEditor.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",item.get_title());
                bundle.putString("description",item.get_description());
                bundle.putInt("id",item.get_id());
                bundle.putString("category",item.get_category());
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


    public  void addButtonClicked(View v) {
        Intent i = new Intent(this, noteEditor.class);
        startActivityForResult(i,1);

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


/*    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),hour,min,0);

        //alarmManager
        AlarmManager am=(AlarmManager)getSystemService(this.ALARM_SERVICE);
        Intent i = new Intent(MainActivity.this,myAlarm.class);

        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);

        am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
        //    am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);

        Toast.makeText(this,"hours "+Integer.toString(hour)+" min "+Integer.toString(min),Toast.LENGTH_SHORT).show();


       // settingText.setText("alarm set for "+Integer.toString(hour)+":"+Integer.toString(min));

    }*/


}
