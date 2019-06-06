package com.example.project_valhe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SelectedGameScreen extends AppCompatActivity {

   private Button done;
   private Switch back;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_selected_game_screen);

      configureSwitch();
      configureButton();
   }

   private void configureSwitch(){
      back = (Switch) findViewById(R.id.back);
      back.setChecked(true);
      back.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked)
            {
               finish();
            }
         }
      });
   }

   private void configureButton(){
      done = (Button) findViewById(R.id.done);
      done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(SelectedGameScreen.this, SecondScreen.class));
         }
      });
   }
}
