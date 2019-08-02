package com.example.project_valhe;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class SelectedGame extends Fragment implements SensorEventListener {

   private RelativeLayout back;
   private RelativeLayout done;
   private EditText calPoint;
   private Spinner spinner;
   private int[] rightArray;
   private int rightIndex;
   private Date start;

   private SensorManager sensorManagerPressure;
   private Sensor pressure;
   private boolean initPressure;
   private double initpPressureValue;

   private SensorManager sensorManagerAccel;
   private Sensor accel;
   private int xAxisTurns;
   private int yAxisTurns;
   private int zAxisTurns;
   private static float boarderAngle = 0.5f;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_selected_game, container, false);
      configureBack(view);
      configureDone(view);
      start = null;
      rightArray = new int[10];

      sensorManagerPressure = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
      pressure = sensorManagerPressure.getDefaultSensor(Sensor.TYPE_PRESSURE);
      initPressure = false;

      sensorManagerAccel = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
      accel = sensorManagerAccel.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

      return view;
   }

   @Override
   public final void onAccuracyChanged(Sensor sensor, int accuracy) {
      // Do something here if sensor accuracy changes.
   }

   @Override
   public final void onSensorChanged(SensorEvent event) {

      if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
         if (initPressure == false) {
            initpPressureValue = event.values[0];
            initPressure = true;
         } else {
            double pressure = event.values[0] - initpPressureValue;
            //System.out.println(pressure);
         }
      }

      if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
         float xAngle = event.values[0];
         float yAngle = event.values[1];
         float zAngle = event.values[2];

         System.out.println("xAngle: " + xAngle);
         System.out.println("yAngle: " + yAngle);
         System.out.println("zAngle: " + zAngle);

         //is that enough for you?
         if(xAngle > boarderAngle)
         {
            xAxisTurns = xAxisTurns + 1;
         }
         if(yAngle > boarderAngle)
         {
            yAxisTurns = yAxisTurns + 1;
         }
         if(xAngle > boarderAngle)
         {
            zAxisTurns = zAxisTurns + 1;
         }
      }

   }

   @Override
   public void onResume() {
      super.onResume();

      sensorManagerPressure.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
      sensorManagerAccel.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);

      getCurrentTimeStamp();
   }

   @Override
   public void onPause() {
      super.onPause();
      sensorManagerPressure.unregisterListener(this);
      sensorManagerAccel.unregisterListener(this);
      calculateDateDifference(TimeUnit.MILLISECONDS);


   }

   public void calculateDateDifference(TimeUnit timeUnit){
      Date end = new Date();
      long diffInMillies = end.getTime() - start.getTime();

      //System.out.println("******************************************");
      //System.out.println("End");
      //System.out.println(end.getSeconds());
      //System.out.println(timeUnit.convert(diffInMillies,TimeUnit.MINUTES));
   }

   public void getCurrentTimeStamp(){
      try {

         start = new Date();
         //System.out.println(start.getSeconds());

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void configureDone(View view){

      done = view.findViewById(R.id.done);
      calPoint = view.findViewById(R.id.calPoints);
      calPoint.setKeyListener(new DigitsKeyListener());

      spinner = view.findViewById(R.id.spinner);



      done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String sum = calPoint.getText().toString();
            String dicPoints = spinner.getSelectedItem().toString();

            //System.out.println("go back");
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
               //System.out.println(left);
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
