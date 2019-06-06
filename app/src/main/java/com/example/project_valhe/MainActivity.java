package com.example.project_valhe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

   private FrameLayout layout;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      configureGesture();
   }

   private void configureGesture(){
      layout = (FrameLayout) findViewById(R.id.app);

      layout.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
               case MotionEvent.ACTION_DOWN:
                  Toast.makeText(getBaseContext(), "down", Toast.LENGTH_SHORT).show();
               case MotionEvent.ACTION_UP:
                  startActivity(new Intent(MainActivity.this, SecondScreen.class));
               case MotionEvent.ACTION_MOVE:
                  Toast.makeText(getBaseContext(), "test" , Toast.LENGTH_SHORT).show();
            }
            return true;
         }
      });
   }
}
