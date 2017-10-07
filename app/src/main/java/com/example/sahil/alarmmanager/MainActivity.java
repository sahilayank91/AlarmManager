package com.example.sahil.alarmmanager;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private TextView time;
    private Switch aSwitch;
    private TimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intent;
    MainActivity inst;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = getIntent().getStringExtra("status");

        timePicker = (TimePicker)findViewById(R.id.timePicker2);
        aSwitch = (Switch) findViewById(R.id.timepicker);
        time = (TextView)findViewById(R.id.settime);
        timePicker.setOnClickListener(this);
        aSwitch.setOnCheckedChangeListener(this);


        alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
        calendar  = Calendar.getInstance();
        intent = new Intent(MainActivity.this,AlarmReciever.class);

        if(status!="on"){
            aSwitch.setChecked(true);
        }else{
            aSwitch.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
       if(isChecked){
           calendar.add(Calendar.SECOND, 3);

           int hour = timePicker.getCurrentHour();
           int min = timePicker.getCurrentMinute();
           boolean flag = false;
           Log.e("MyActivity", "In the receiver with " + hour + " and " + min);
           calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
           calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());


           intent.putExtra("extra","yes");
           pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

           alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

           if(hour>12) flag=true;

           time.setText(String.valueOf(hour)+":"+String.valueOf(min) +String.valueOf(flag?" PM":" AM"));
           String text = time.getText().toString();
           Toast.makeText(MainActivity.this,"Alarm has been set to: " + text,Toast.LENGTH_SHORT).show();
       }else{



           intent.putExtra("extra","no");
           sendBroadcast(intent);
           alarmManager.cancel(pendingIntent);
           time.setText("No Alarm set");
           Toast.makeText(MainActivity.this,"Alarm has been turn Off",Toast.LENGTH_SHORT).show();
       }
    }
}
