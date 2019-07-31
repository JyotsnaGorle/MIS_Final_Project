package com.example.project_valhe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class SelectedGame extends Fragment {

   private LinearLayout back;
   private LinearLayout done;
   private EditText calPoint;
   private Spinner spinner;
   private int[] rightArray = new int[10];
   private int rightIndex;
   private Date start;


   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_selected_game, container, false);
      configureBack(view);
      configureDone(view);
      start = null;
      return view;
   }

   @Override
   public void onResume() {
      super.onResume();
      getCurrentTimeStamp();
   }

   @Override
   public void onPause() {
      super.onPause();
      calculateDateDifference(TimeUnit.MILLISECONDS);


   }

   public void calculateDateDifference(TimeUnit timeUnit){
      Date end = new Date();
      long diffInMillies = end.getTime() - start.getTime();

      System.out.println("******************************************");
      System.out.println("End");
      System.out.println(end.getSeconds());
      System.out.println(timeUnit.convert(diffInMillies,TimeUnit.MINUTES));
   }


   public void getCurrentTimeStamp(){
      try {

         start = new Date();
         System.out.println("******************************************");
         System.out.println("Start");
         System.out.println(start.getSeconds());

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void configureDone(View view){

      done = view.findViewById(R.id.done);
      calPoint = view.findViewById(R.id.calPoints);
      spinner = view.findViewById(R.id.spinner);


      done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String sum = calPoint.getText().toString();
            String dicPoints = spinner.getSelectedItem().toString();

            System.out.println("go back");
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
         }
      });
   }

   private void configureBack(View view){

      back = view.findViewById(R.id.back);
      back.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            boolean left = false;
            int limit = rightArray.length;

            if(rightIndex != limit) {
               rightArray[rightIndex] = x;
               rightIndex = rightIndex + 1;
            }
            else{
               for(int i = 0; i < limit - 1; ++i){
                  if(rightArray[i] < rightArray[i + 1])
                  {
                     left = true;
                  }
                  else{
                     left = false;
                     break;
                  }
               }
               System.out.println(left);
               if(left == true)
               {
                  FragmentManager fm = getFragmentManager();
                  fm.popBackStack();
               }
               rightIndex = 0;
            }
            return true;
         }
      });
   }
}
