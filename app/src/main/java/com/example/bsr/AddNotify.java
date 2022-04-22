package com.example.bsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.Calendar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.allyants.notifyme.NotifyMe;

public class AddNotify extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

     Calendar now = Calendar.getInstance();
     TimePickerDialog tpd;
     DatePickerDialog dpd;
     EditText et_title;
     ImageView arrow;

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

     @Override
     protected void onCreate(Bundle saveInstanceState){
         super.onCreate(saveInstanceState);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         setContentView(R.layout.activity_add_notify);
         Button notify = findViewById(R.id.btn_addNewNotify);
         et_title = findViewById(R.id.et_title);
       //  et_content = findViewById(R.id.et_content);

         arrow = findViewById(R.id.image_back);

         arrow.setOnClickListener(view -> {
             Intent intent = new Intent(AddNotify.this, AddBills.class);
             startActivity(intent);
         });

     notify.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             dpd.show(getFragmentManager(), "Datepickerdialog");
         }
     });
         dpd = DatePickerDialog.newInstance(
                 AddNotify.this,
                 now.get(Calendar.YEAR),
                 now.get(Calendar.MONTH),
                 now.get(Calendar.DATE)

         );

         tpd = TimePickerDialog.newInstance(
                 AddNotify.this,
                 now.get(Calendar.HOUR_OF_DAY),
                 now.get(Calendar.MINUTE),
                 now.get(Calendar.SECOND),
                 false
         );

     }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second){
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);


        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title(et_title.getText().toString())
                .color(255,0,0,255)
                .led_color(255,255,255,255)
                .time(now)
                .addAction(new Intent(),"Snooze",false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(new Intent(), "Done")
                .build();


    }


}