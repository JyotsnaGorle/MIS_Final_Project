package com.example.project_valhe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class StartGame extends Fragment implements SensorEventListener{

   private ImageView[] diceArray;

   private int[] leftArray;
   private int leftIndex;

   private int[] rightArray;
   private int rightIndex;

   private int dicesValue[];
   private short round;
   private static short done = 12;
   private boolean playGame;
   private View v;

   private boolean[] diceTouch;
   private boolean[] diceLongTouch;
   private TextView[] pointsArray;
   private TextView[] textPointsArray;
   private int[] diceSelect;
   private boolean initlisedAccel;
   private float startX;
   private float startY;
   private float startZ;
   private static float border = 2.0f;
   private int shakedDices;
   private boolean shake[];
   private SensorManager sensorManager;
   private Sensor sensor;
   private int totalPoints = -1;
   private short amountSelect = 0;
   private boolean selected;


   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_start_game, container, false);
      leftIndex = 0;
      rightIndex = 0;
      round = 0;
      playGame = true;
      shakedDices = 0;
      shake = new boolean[5];
      diceTouch = new boolean[5];
      diceLongTouch = new boolean[5];
      dicesValue = new int [5];
      diceSelect = new int[5];
      diceArray = new ImageView[5];
      pointsArray = new TextView[11];
      textPointsArray = new TextView[10];
      leftArray = new int[10];
      rightArray = new int[10];
      initlisedAccel = false;
      selected = false;
      configurePointsArray(view);

      for(int i = 0; i < diceTouch.length; ++i)
      {
         diceTouch[i] = false;
         diceLongTouch[i] = false;
         shake[i] = false;
         diceSelect[i] = -1;
      }


      configureBack(view);
      configurePlay(view);
      configureDices(view);
      configureAcceleration();
      //configureDiceImage(view);

      v = view;
      return view;
   }

   @Override
   public void onPause() {
      super.onPause();
      sensorManager.unregisterListener(this);
   }

   public void onAccuracyChanged(Sensor sensor, int accuracy) {
   }

   private void configureAcceleration(){
      sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
      sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      sensorManager.registerListener(this, sensor , SensorManager.SENSOR_DELAY_NORMAL);

   }

   public void onSensorChanged(SensorEvent event){

      if(initlisedAccel == false){
         startX = event.values[0];
         startY = event.values[1];
         startZ = event.values[2];
         initlisedAccel = true;
      }
      else{
         if((event.values[0] > startX + border || event.values[0] < startX - border) &&
            (event.values[1] > startY + border || event.values[1] < startY - border) &&
            (shakedDices < 3))// &&
            //(event.values[2] < startZ + border && event.values[2] > startZ - border))
         {
            byte counter = 0;
            for(int i = 0; i < shake.length; ++i)
            {
               if(shake[i] == false){
                  counter = (byte)(counter + 1);
               }
            }
            if(counter == shake.length){
               for(int i = 0; i < diceTouch.length; ++i)
               {
                  diceTouch[i] = false;
                  diceSelect[i] = -1;
               }
               startX = event.values[0];
               startY = event.values[1];
               //startZ = event.values[2];
               shakeImages();
               shakedDices = shakedDices + 1;

               Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
               if (vibrator.hasVibrator()) {
                  vibrator.vibrate(200);
               }

               for(int i = 0; i < shake.length; ++i)
               {
                  if(i + 1 != shake.length)
                  {
                     shake[i] = shake[i + 1];
                  }
                  else{
                     shake[shake.length - 1] = true;
                  }
               }
            }
         }
         else{
            for(int i = 0; i < shake.length; ++i)
            {
               if(i + 1 != shake.length)
               {
                  shake[i] = shake[i + 1];
               }
               else{
                  shake[shake.length - 1] = false;
               }
            }
         }
      }
   }

   private void configurePointsArray(View view){

      pointsArray[0] = view.findViewById(R.id.select1);
      pointsArray[1] = view.findViewById(R.id.select2);
      pointsArray[2] = view.findViewById(R.id.select3);
      pointsArray[3] = view.findViewById(R.id.select4);
      pointsArray[4] = view.findViewById(R.id.select5);
      pointsArray[5] = view.findViewById(R.id.selectThreeKind);
      pointsArray[6] = view.findViewById(R.id.selectFourKind);
      pointsArray[7] = view.findViewById(R.id.selectLittleStraight);
      pointsArray[8] = view.findViewById(R.id.selectBigStraight);
      pointsArray[9] = view.findViewById(R.id.selectHouse);
      pointsArray[10] = view.findViewById(R.id.selectPayment);

      textPointsArray[0] = view.findViewById(R.id.ones);
      textPointsArray[1] = view.findViewById(R.id.twos);
      textPointsArray[2] = view.findViewById(R.id.threes);
      textPointsArray[3] = view.findViewById(R.id.fours);
      textPointsArray[4] = view.findViewById(R.id.fives);
      textPointsArray[5] = view.findViewById(R.id.threeOfAKind);
      textPointsArray[6] = view.findViewById(R.id.fourOfAKind);
      textPointsArray[7] = view.findViewById(R.id.littleStraight);
      textPointsArray[8] = view.findViewById(R.id.bigStraight);
      textPointsArray[9] = view.findViewById(R.id.house);
   }

   @Override
   public void onResume() {
      super.onResume();
      configureRounds(v);
   }

   @Override
   public void onStart() {
      super.onStart();
      configureRounds(v);
   }

   private void configureRounds(View view){
      if(round == done) {
         Context context = getContext();
         CharSequence text = "END";

         Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
         toast.show();
      }
   }

   private void configurePlay(View view){

      final RelativeLayout select = view.findViewById(R.id.selected);
      select.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if (playGame == true && selected!=false) {
               int x = (int) event.getX();
               boolean left = false;
               int limit = leftArray.length;

               if (leftIndex != limit) {
                  leftArray[leftIndex] = x;
                  leftIndex = leftIndex + 1;
               } else {
                  for (int i = 0; i < limit - 1; ++i) {
                     if (leftArray[i] > leftArray[i + 1]) {
                        left = true;
                     } else {
                        left = false;
                        break;
                     }
                  }
                  if (left == true) {
                     round = (short) (round + 1);
                     assert getFragmentManager() != null;
                     if (totalPoints >= 0) {
                        submitTruePoints(totalPoints);
                     }
                  }
                  leftIndex = 0;
               }
            }

            else {
               Context context = getContext();
               CharSequence text = "You forgot to select dices";

               Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
               toast.show();

            }
            return true;
         }
      });
   }

   private void configureBack(View view){

      RelativeLayout back = view.findViewById(R.id.back);
      back.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            boolean right = false;
            int limit = rightArray.length;

            if(rightIndex != limit) {
               rightArray[rightIndex] = x;
               rightIndex = rightIndex + 1;
            }
            else{
               for(int i = 0; i < limit - 1; ++i){
                  if(rightArray[i] < rightArray[i + 1])
                  {
                     right = true;
                  }
                  else{
                     right = false;
                     break;
                  }
               }
               if(right == true)
               {
                  FragmentManager fm = getFragmentManager();
                  assert fm != null;
                  fm.popBackStack();
               }
               rightIndex = 0;
            }
            return true;
         }
      });
   }

   private void shakeImages(){
      int arrayLen = diceArray.length;
      for (int y = 0; y < arrayLen; ++y) {
         double random = Math.floor(Math.random() * 5);
         int randomInt = (int) random;

         int imageViewId = getResources().getIdentifier("my_dice" + (randomInt + 1) , "drawable", "com.example.project_valhe");
         diceArray[y].setBackgroundResource(imageViewId);
         //diceArray[y].setTag(randomInt);
         configureDiceClick(diceArray[y], randomInt);
         configureDiceLongClick(diceArray[y], randomInt);
         dicesValue[y] = randomInt;
      }
   }

   private void configureDiceLongClick(ImageView v, final int number){
      v.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {

            for(int i = 0; i < diceTouch.length; ++i) {
               diceTouch[i] = false;
               diceSelect[i] = -1;
            }

            byte sum = 0;

            String tagString = String.valueOf(v.getTag());
            int tagInteger =  Integer.parseInt(tagString);

            if(diceLongTouch[tagInteger] == false)
            {
               for(int i = 0; i < diceLongTouch.length; ++i)
               {
                  int diceValue = dicesValue[i];

                  int imageViewId = getResources().getIdentifier("my_dice" + (diceValue + 1), "drawable", "com.example.project_valhe");
                  diceArray[i].setBackgroundResource(imageViewId);
                  diceLongTouch[i] = false;
                  if(number == diceValue)
                  {
                     imageViewId = getResources().getIdentifier("my_dice_select" + (number + 1), "drawable", "com.example.project_valhe");
                     diceArray[i].setBackgroundResource(imageViewId);
                     diceLongTouch[i] = true;
                     sum = (byte) (sum + (number + 1));
                  }
               }
               configurePoints(sum, number, -1);
            }
            else
            {
               for(int i = 0; i < diceLongTouch.length; ++i)
               {
                  int diceValue = dicesValue[i];

                  int imageViewId = getResources().getIdentifier("my_dice" + (diceValue + 1), "drawable", "com.example.project_valhe");
                  diceArray[i].setBackgroundResource(imageViewId);
                  diceLongTouch[i] = false;
               }
            }
            return true;
         }
      });
   }

   private void configureDiceClick(ImageView v, final int number){
      v.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            for(int i = 0; i < diceLongTouch.length; ++i) {
               diceLongTouch[i] = false;
            }
            String tagString = String.valueOf(v.getTag());
            int tagInteger =  Integer.parseInt(tagString);

            if(diceTouch[tagInteger] == false)
            {
               int imageViewId = getResources().getIdentifier("my_dice_select" + (number + 1), "drawable", "com.example.project_valhe");
               v.setBackgroundResource(imageViewId);
               diceTouch[tagInteger] = true;
               for(int i = 0; i < diceSelect.length; ++i)
               {
                  if(diceSelect[i] == -1) { diceSelect[i] = number + 1; break;}
               }
            }
            else
            {
               int imageViewId = getResources().getIdentifier("my_dice" + (number + 1) , "drawable", "com.example.project_valhe");
               v.setBackgroundResource(imageViewId);
               diceTouch[tagInteger] = false;
               for(int i = 0; i < diceSelect.length; ++i)
               {
                  if(diceSelect[i] == number + 1) { diceSelect[i] = -1; break;}
               }
            }

            short sum = 0;
            int option = -1;

            for(int i = 0; i < diceSelect.length; ++i)
            {
               if(diceSelect[i] != -1) { amountSelect = (short) (amountSelect + 1) ;}
            }

            if(amountSelect == 3 || amountSelect == 4 || amountSelect == 5) {
               Arrays.sort(diceSelect);
               int found = 0;
               int propablySum = 0;
               if (amountSelect == 3) {
                  for (int i = 0; i < diceSelect.length; ++i) {
                     int number = diceSelect[i];
                     found = 0;
                     for (int y = 0; y < diceSelect.length; ++y) {
                        int number2 = diceSelect[y];

                        if (number2 == number && number != -1) {
                           found = found + 1;
                        }
                     }
                     if (found == 3) {
                        sum = (short) (number + number + number);
                        option = 5;
                     }
                  }
               }
               else if (amountSelect == 4) {
                  boolean checkIn = false;
                  for (int i = 0; i < diceSelect.length; ++i) {
                     int number = diceSelect[i];
                     if (diceSelect.length != i + 1 &&  number != -1 && number + 1 == diceSelect[i + 1]) {
                        found = found + 1;
                        propablySum = (short) (propablySum + number);
                     }
                     else if(diceSelect.length == i + 1 && number != -1 && number - 1 == diceSelect[i - 1]) {
                        found = found + 1;
                        propablySum = (short) (propablySum + number);
                     }
                     else
                     {
                        found = 0;
                     }
                  }
                  if (found == 4) {
                     option = 7;
                     sum = (short) (propablySum);
                     checkIn = true;
                  }
                  if (checkIn == false){
                     for (int i = 0; i < diceSelect.length; ++i) {
                        int number = diceSelect[i];
                        found = 0;

                        for (int y = 0; y < diceSelect.length; ++y) {
                           int number2 = diceSelect[y];
                           if (number2 == number && number != -1) {
                              found = found + 1;
                           }

                        }
                        if (found == 4) {
                           sum = (short) (number + number + number + number);
                           option = 6;
                           checkIn = true;
                        }
                     }
                  }
                  if(checkIn == false){}
               }
               else if (amountSelect == 5) {
                  boolean checkIn = false;
                  for (int i = 0; i < diceSelect.length; ++i) {
                     int number = diceSelect[i];
                     if (diceSelect.length != i + 1 &&  number != -1 && number + 1 == diceSelect[i + 1]) {
                        found = found + 1;
                        propablySum = (short) (propablySum + number);
                     }
                     else if(diceSelect.length == i + 1 && number != -1 && number - 1 == diceSelect[i - 1]) {
                        found = found + 1;
                        propablySum = (short) (propablySum + number);
                     }
                     else
                     {
                        found = 0;
                     }
                  }
                  if (found == 5) {
                     option = 8;
                     sum = (short) (propablySum);
                     checkIn = true;
                  }
                  if (checkIn == false){
                     int probablySum1 = 0;
                     int probablySum2 = 0;
                     int storedValue;
                     for(int i = 0; i < diceSelect.length; ++i) {
                        int number = diceSelect[i];
                        found = 0;
                        probablySum1 = 0;
                        for (int y = 0; y < diceSelect.length; ++y) {
                           int number2 = diceSelect[y];
                           if(number2 == number && number != -1)
                           {
                              found =  found + 1;
                              probablySum1 = probablySum1 + number;
                           }
                           if(found == 2)
                           {
                              storedValue = number;
                              for (int z = 0; z < diceSelect.length; ++z) {
                                 int number3 = diceSelect[z];
                                 found = 0;
                                 probablySum2 = 0;
                                 for (int c = 0; c < diceSelect.length; ++c) {
                                    int number4 = diceSelect[c];
                                    if (number3 != storedValue)
                                    {
                                       if(number3 == number4 && number != -1) {
                                          found = found + 1;
                                          probablySum2 = probablySum2 + number3;
                                       }

                                       if(found == 3)
                                       {
                                          option = 9;
                                          checkIn = true;
                                          sum = (short) (probablySum2 + probablySum1);
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
                  if(checkIn == false){}
               }

            }

            amountSelect = 0;
            configurePoints(sum, number + 1, option );
         }
      });
   }

   private void configurePoints(short sum, final int number, int option){
      selected = true;

      short size = (byte) (pointsArray.length);

      for(byte i = 0; i < size; ++i)
      {
         pointsArray[i].setText("");

         if(option == -1 && number == i)
         {
            pointsArray[number].setText("" + sum);
         }
         else if(option != -1)
         {
            pointsArray[option].setText("" + sum);
         }
      }
      pointsArray[pointsArray.length -1].setText("" + sum); //Payment shit | sum of the points

      for(int i = 0; i < textPointsArray.length; ++i)
      {
         if(i == (number))
         {
            textPointsArray[number].setPaintFlags(textPointsArray[number].getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
         }
         else{
            textPointsArray[number].setPaintFlags(textPointsArray[number].getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
         }
      }
      totalPoints = sum;
   }

   private void submitTruePoints(int sum) {
      RequestParams params = new RequestParams();
      params.put("truePoints", sum);

      RestClient.get("storeTruth", params, new JsonHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("API CALL", "storeTruth success");
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new SelectedGame(), "START_GAME");
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_right, R.anim.enter_from_right, R.anim.exit_from_right);
            transaction.addToBackStack(null);
            transaction.commit();
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("API CALL", "storeTruth Failed");
         }
      });
   }

   private void configureDices(View view) {

      ImageView v = (ImageView)view.findViewById(R.id.dice1);
      String backgroundImageName = String.valueOf(v.getTag());
      int tagNumber = Integer.parseInt(backgroundImageName);
      diceArray[0] = v;
      configureDiceLongClick(v, tagNumber);
      configureDiceClick(v, tagNumber);

      v = (ImageView)view.findViewById(R.id.dice2);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      diceArray[1] = v;
      configureDiceLongClick(v, tagNumber);
      configureDiceClick(v, tagNumber);

      v = (ImageView)view.findViewById(R.id.dice3);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      diceArray[2] = v;
      configureDiceLongClick(v, tagNumber);
      configureDiceClick(v, tagNumber);

      v = (ImageView)view.findViewById(R.id.dice4);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      diceArray[3] = v;
      configureDiceLongClick(v, tagNumber);
      configureDiceClick(v, tagNumber);

      v = (ImageView)view.findViewById(R.id.dice5);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      diceArray[4] = v;
      configureDiceLongClick(v, tagNumber);
      configureDiceClick(v, tagNumber);

      shakeImages();
   }
}