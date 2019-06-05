package com.example.project_valhe;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {

   public MyFrameLayout(Context context) {
      super(context);
   }

   @Override
   public boolean onTouchEvent(MotionEvent event) {
      int action = event.getActionMasked();
      
      return super.onTouchEvent(event);
   }
}
