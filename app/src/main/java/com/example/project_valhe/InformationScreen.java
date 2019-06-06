package com.example.project_valhe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

public class InformationScreen extends AppCompatActivity {

   private Switch back;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_information_screen);
      configureSwitch();
   }

   private void configureSwitch(){
      back = (Switch) findViewById(R.id.back);
      back.setChecked(true);

      back.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
               finish();
            }
         }
      });

   }

}


