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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;


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
   private double initPressureValue;

   private SensorManager sensorManagerAccel;
   private Sensor accel;
   private float xAxisTurns;

   private static float boarderAngle = 0.15f;
   private double finalPressure;
   private ArrayList<Double> finalPressureArray;
   private ArrayList<Double> finalXaccelerationArray;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_selected_game, container, false);
      configureBack(view);
      configureDone(view);
      start = null;
      rightArray = new int[10];
      xAxisTurns = 0;


      sensorManagerPressure = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
      pressure = sensorManagerPressure.getDefaultSensor(Sensor.TYPE_PRESSURE);
      initPressure = false;
      finalPressureArray = new ArrayList<>();

      sensorManagerAccel = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
      accel = sensorManagerAccel.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

      return view;
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
   }

   public long calculateDateDifference(TimeUnit timeUnit){
      Date end = new Date();
      long diffInMillies = end.getTime() - start.getTime();

      //System.out.println("******************************************");
      //System.out.println("End");
      //System.out.println(end.getSeconds());
      //System.out.println(timeUnit.convert(diffInMillies,TimeUnit.MINUTES));
      return diffInMillies;
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

            long inputTimeDiff = calculateDateDifference(TimeUnit.MILLISECONDS);

            //System.out.println("go back");
            submitLiePoints(sum, finalPressureArray.toString(), xAxisTurns, inputTimeDiff);
         }
      });

      calPoint.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            System.out.println(finalPressure);
            finalPressureArray.add(finalPressure);
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
      });
   }

   private void submitLiePoints(String sum, String pressureArray, float xAxisTurns, long inputTimeDiff) {
      RequestParams params = new RequestParams();
      params.put("liePoints", sum);
      params.put("pressure", pressureArray);
      params.put("accelerationX", xAxisTurns);
      params.put("inputTimeDiff", inputTimeDiff);

      RestClient.get("storeLie", params, new JsonHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("API CALL", "storeResult success");
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("API CALL", "storeResult Failed");
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

   @Override
   public final void onAccuracyChanged(Sensor sensor, int accuracy) {
      // Do something here if sensor accuracy changes.
   }



   @Override
   public final void onSensorChanged(SensorEvent event) {

      if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
         if (initPressure == false) {
            initPressureValue = event.values[0];
            initPressure = true;
         } else {
            finalPressure = event.values[0] - initPressureValue;
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
            xAxisTurns =  xAngle;
         }
      }

   }

}
