package com.example.mohan.notes.fragments;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mohan.notes.R;
import com.example.mohan.notes.Util.myAlarm;

import java.util.Calendar;

public class settingsFragment extends DialogFragment {

    private static final String TAG = "settingsFragment";
    TimePicker mTimePicker;
    TextView settingText;
    SwitchCompat switchButton;
    Button setAlarm;
    int hour, min;
    boolean isAlarmSet=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.setting_layout,container,false);

        settingText=(TextView)view.findViewById(R.id.setting_text);
        mTimePicker=(TimePicker)view.findViewById(R.id.m_time_picker);
        setAlarm=(Button)view.findViewById(R.id.set_alarm);

        SharedPreferences sharedPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        isAlarmSet=sharedPref.getBoolean("isAlarmSet",isAlarmSet);

        if(!isAlarmSet){
            mTimePicker.setVisibility(View.GONE);
            setAlarm.setVisibility(View.GONE);
            settingText.setVisibility(View.GONE);
        }

        //slider switch
        switchButton=(SwitchCompat)view.findViewById(R.id.switch_button);
        switchButton.setChecked(isAlarmSet);

        switchButton.setOnCheckedChangeListener(

                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    Toast.makeText(getActivity(), "switch on", Toast.LENGTH_SHORT).show();
                    settingText.setVisibility(View.VISIBLE);
                    mTimePicker.setVisibility(View.VISIBLE);
                    setAlarm.setVisibility(View.VISIBLE);


                }else{
                    Toast.makeText(getActivity(), "switch off", Toast.LENGTH_SHORT).show();
                    mTimePicker.setVisibility(View.GONE);
                    setAlarm.setVisibility(View.GONE);
                    isAlarmSet=false;

                }
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 hour = mTimePicker.getCurrentHour();
                 min =mTimePicker.getCurrentMinute();

                Calendar c = Calendar.getInstance();
                c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),hour,min,0);

                //alarmManager
                AlarmManager am=(AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
                Intent i = new Intent(getActivity(),myAlarm.class);

                PendingIntent pi = PendingIntent.getBroadcast(getActivity(),0,i,0);

                am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
                //    am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);

                settingText.setText("alarm set for "+Integer.toString(hour)+":"+Integer.toString(min));

                isAlarmSet=true;

            }
        });
        //TimePicker

/*        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                Calendar c = Calendar.getInstance();
                c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),hour,min,0);

                //alarmManager
                AlarmManager am=(AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
                Intent i = new Intent(getActivity(),myAlarm.class);

                PendingIntent pi = PendingIntent.getBroadcast(getActivity(),0,i,0);

                am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
                //    am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);

              settingText.setText("alarm set for "+Integer.toString(hour)+":"+Integer.toString(min));
            }
        });*/


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }catch (NullPointerException e){
            Log.d(TAG, "onStart: nullPointerException");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sharedPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isAlarmSet",isAlarmSet);
        editor.commit();
    }
}
