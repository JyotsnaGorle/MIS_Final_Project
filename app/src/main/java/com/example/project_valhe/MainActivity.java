package com.example.project_valhe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

   private FrameLayout layout;
   private TextView test;
   private int startDraggingX = 0;
   private int stopDraggingX = 0;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      test = findViewById(R.id.test);

      layout = (FrameLayout) findViewById(R.id.app);
      layout.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            test.setText((int)event.getX());
            return true;
         }
      });


      /*
      layout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            test.setText("Hallo");

         }
      });*/


      /*
      layout.setOnDragListener(new View.OnDragListener()
      {
         @Override
         public boolean onDrag(View v, DragEvent event)
         {
            test.setText("Hallo");

            if (event.getAction() == event.ACTION_DRAG_STARTED)
            {
               startDraggingX = (int) event.getX();
               test.setText(startDraggingX);
            }
            else if(event.getAction() == event.ACTION_DROP)
            {
               stopDraggingX = (int) event.getX();

               if((startDraggingX - stopDraggingX) >= 50)
               {
                  startActivity(new Intent(MainActivity.this, SecondScreen.class));
               }
               test.setText(startDraggingX - stopDraggingX);
            }
            return true;
         }
      });
*/
      /*
      layout.setOnTouchListener(new View.OnTouchListener()
      {
         @Override
         public boolean onTouch(View v, MotionEvent event)
         {
            startDraggingX = (int) event.getX();

            if(event.getAction() == MotionEvent.ACTION_UP)
            {
               return true;
            }
            else
            {

            }
            return false;
         }
      });*/
   }
}
