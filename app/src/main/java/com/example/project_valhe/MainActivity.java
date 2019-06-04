package com.example.project_valhe;

import android.app.Activity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
   private FrameLayout layout;
   private int startDraggingX = 0;
   private int stopDraggingX = 0;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      layout = (FrameLayout) findViewById(R.id.app);


      layout.setOnDragListener(new View.OnDragListener()
      {
         @Override
         public boolean onDrag(View v, DragEvent event)
         {
            if (event.getAction() == event.ACTION_DRAG_STARTED)
            {
               startDraggingX = (int) event.getX();
            }
            else if(event.getAction() == event.ACTION_DROP)
            {
               stopDraggingX = (int) event.getX();

               if((startDraggingX - stopDraggingX) >= 50)
               {
               }
            }
            return true;
         }
      });


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
      });
   }
}
