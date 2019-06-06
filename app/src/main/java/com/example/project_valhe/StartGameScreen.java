package com.example.project_valhe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class StartGameScreen extends AppCompatActivity {

   private Button select;
   private Switch back;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_start_game_screen);

      configureButton();
      configureSwitch();
   }

   private void configureButton(){
      select = (Button) findViewById(R.id.selected);

      select.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(StartGameScreen.this, SelectedGameScreen.class));
         }
      });

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
