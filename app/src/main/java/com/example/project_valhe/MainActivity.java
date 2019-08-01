package com.example.project_valhe;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements SecondScreen.OnFragmentInteractionListener{

   private RelativeLayout layout;
   private FrameLayout fragmentContainer;
   private int index;
   private int[] yPos;
   private boolean listenerActive;


   @Override
   protected void onCreate(Bundle savedInstanceState) {

      index = 0;
      yPos = new int[10];
      listenerActive = true;
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
           WindowManager.LayoutParams.FLAG_FULLSCREEN);

      fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
      configureGesture();

   }

   @Override
   protected void onResume() {
      super.onResume();
      listenerActive = true;
   }

   @Override
   protected void onPause() {
      super.onPause();
      listenerActive = false;
   }

   private View.OnTouchListener handleTouch = new View.OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

         if(listenerActive == true)
         {
            int y = (int) event.getY();
            boolean up = false;
            int limit = yPos.length;

            if(index != limit) {
               yPos[index] = y;
               index = index + 1;
            }
            else{
               for(int i = 0; i < limit - 1; ++i){
                  if(yPos[i] > yPos[i + 1])
                  {
                     up = true;
                  }
                  else{
                     up = false;
                     break;
                  }
               }
               System.out.println(up);
               if(up == true)
               {
                  openSecondScreen();
               }
               index = 0;
            }
         }
         return true;
      }
   };

   private void configureGesture(){
      layout = findViewById(R.id.app);
      layout.setOnTouchListener(handleTouch);
   }

   private void openSecondScreen() {
      SecondScreen fragment = SecondScreen.newInstance();
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction transaction = fragmentManager.beginTransaction();
      transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom);
      transaction.addToBackStack(null);
      transaction.add(R.id.fragment_container, fragment, "SECOND_SCREEN").commit();
   }

   @Override
   public void onFragmentInteraction(String test) {
   }
}