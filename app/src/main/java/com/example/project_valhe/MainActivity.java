package com.example.project_valhe;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements SecondScreen.OnFragmentInteractionListener {

   private RelativeLayout layout;
   private FrameLayout fragmentContainer;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
           WindowManager.LayoutParams.FLAG_FULLSCREEN);

      fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
      configureGesture();
   }

   private void configureGesture(){
      layout = findViewById(R.id.app);

      layout.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
               case MotionEvent.ACTION_UP:
                  openSecondScreen();
                  //startActivity(new Intent(MainActivity.this, SecondScreen.class));
            }
            return true;
         }
      });
   }


   public void openSecondScreen() {
      SecondScreen fragment = SecondScreen.newInstance();
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction transaction = fragmentManager.beginTransaction();
      transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom);
      transaction.addToBackStack(null);
      transaction.add(R.id.fragment_container, fragment, "SECOND_SCREEN").commit();
   }

   @Override
   public void onFragmentInteraction(String sendBackText) {
      //editText.setText(sendBackText);
      //onBackPressed();
   }
}