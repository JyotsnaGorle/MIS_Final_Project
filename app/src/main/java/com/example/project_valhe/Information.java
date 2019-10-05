package com.example.project_valhe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Information extends Fragment {

   private RelativeLayout back;
   private int[] downArray;
   private int downIndex;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_information, container, false);
      downIndex = 0;
      downArray = new int[10];
      configureBack(view);

      return view;
   }

   private void configureBack(View view){

      back = (RelativeLayout) view.findViewById(R.id.information_back);
      back.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            boolean left = false;
            int limit = downArray.length;

            if(downIndex != limit) {
               downArray[downIndex] = x;
               downIndex = downIndex + 1;
            }
            else{
               for(int i = 0; i < limit - 1; ++i){
                  if(downArray[i] < downArray[i + 1])
                  {
                     left = true;
                  }
                  else{
                     left = false;
                     break;
                  }
               }
               if(left == true)
               {
                  FragmentManager fm = getFragmentManager();
                  fm.popBackStack();
               }
               downIndex = 0;
            }
            return true;
         }
      });
   }
}
