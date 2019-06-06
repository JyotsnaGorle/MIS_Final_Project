package com.example.project_valhe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
public class SecondScreen extends AppCompatActivity {

   private Switch playGameSwitch;
   private Switch showInformation;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_second_screen);


      setupGameSwitch();
      setupInfoSwitch();

   }

   private void setupGameSwitch()
   {
      playGameSwitch = (Switch) findViewById(R.id.playGame);
      playGameSwitch.setChecked(false);

      playGameSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
               startActivity(new Intent(SecondScreen.this, StartGameScreen.class));
            }
         }
      });
   }

   private void setupInfoSwitch()
   {
      showInformation = (Switch) findViewById(R.id.information);
      showInformation.setChecked(false);

      showInformation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
               startActivity(new Intent(SecondScreen.this, InformationScreen.class));
            }
         }
      });
   }
}