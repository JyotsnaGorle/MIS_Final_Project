package com.example.project_valhe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
public class SecondScreen extends AppCompatActivity {

   private Switch playGameSwitch;
   private Switch showInformation;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_second_screen);

      playGameSwitch = (Switch) findViewById(R.id.playGame);
      showInformation = (Switch) findViewById(R.id.information);

      playGameSwitch.setChecked(false);
      showInformation.setChecked(false);
   }

}


/*





      playGameSwitch.setOnClickListener((View.OnClickListener) this);
      showInformation.setOnClickListener((View.OnClickListener) this);

 public void onClick(View view) {
      switch (view.getId()) {
         case R.id.playGame:
           showGame();
         case R.id.information:
           // showInformation();
      }
   }

   private void showGame(){
      //startActivity(new Intent(SecondActivity.this, StartGameScreen.class));
      finish();
   }

   private void showInformation(){
      //startActivity(new Intent(SecondActivity.this, InformationActivity.class));
      finish();
   }
 */