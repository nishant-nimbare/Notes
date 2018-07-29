package com.example.mohan.notes;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class noteEditor extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
mDBHandler handler;
EditText newTitle;
EditText newDescription;
android.support.v7.app.AlertDialog categoryPicker;
RelativeLayout layout;
boolean hasExtra;
int id,checkedItem=-1;
String category="";
String[] categories={"TODO","WORK","PERSONAL","OTHER"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        //layout
        layout=(RelativeLayout)findViewById(R.id.relative_layout);

        //getting user clicks on the whole layout
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newDescription.requestFocus();
            }
        });


       //geeting widgets
        handler = new mDBHandler(this);
        newDescription=findViewById(R.id.edit_description);

        //showing keyBoard on focus
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(newDescription, InputMethodManager.SHOW_IMPLICIT);
        newTitle=findViewById(R.id.edit_title);

        //actionbar
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        //getting bundle data
        Bundle bundle=getIntent().getExtras();
        if(bundle !=null){
            newTitle.setText(bundle.getString("title"));
            newDescription.setText(bundle.getString("description"));
            category=bundle.getString("category");
            id=bundle.getInt("id");
            hasExtra=true;
        }else{
            hasExtra=false;
        }

        //getting selected radioButton
        if (hasExtra) {

            switch (category) {
                case "TODO":
                   checkedItem=0;
                    break;
                case "WORK":
                    checkedItem=1;
                    break;
                case "PERSONAL":
                    checkedItem=2;
                    break;
                case "OTHER":
                    checkedItem=3;
                    break;
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.note_editior_action_bar,menu);


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
            case R.id.categoryPicker:
                categoryPickerClicked();
                return true;
            case R.id.notification_noteEditior:
                DialogFragment timePicker = new timePickerFragment();
                timePicker.show(getSupportFragmentManager(),"Time Picker");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void editDoneButtonClicked(){


        if(hasExtra){
            handler.updateNote(id,newTitle.getText().toString(),newDescription.getText().toString(),category);
            Toast.makeText(getApplicationContext(), "note edited ", Toast.LENGTH_SHORT).show();

        }else {
            Note note=new Note(newTitle.getText().toString(),newDescription.getText().toString(),category);
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

    //category picker alert box
    public void categoryPickerClicked(){
        android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(noteEditor.this);
        builder.setTitle("choose category");

        builder.setSingleChoiceItems(categories, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        category = "TODO";
                        break;
                    case 1:
                        category = "WORK";
                        break;
                    case 2:
                        category = "PERSONAL";
                        break;
                    case 3:
                        category = "OTHER";
                        break;
                }
                categoryPicker.dismiss();
            }
        });
        categoryPicker=builder.create();
        categoryPicker.show();
    }

    //timePicker listener
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
//TODO
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),hour,min,0);

        //alarmManager
        AlarmManager am=(AlarmManager)getSystemService(this.ALARM_SERVICE);
        Intent i = new Intent(noteEditor.this,myAlarm.class);
        i.putExtra("Title",newTitle.getText().toString());
        i.putExtra("desc",newDescription.getText().toString());

        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);

        am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
    //    am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);

        Toast.makeText(this,"hours "+Integer.toString(hour)+" min "+Integer.toString(min),Toast.LENGTH_SHORT).show();
    }
}
