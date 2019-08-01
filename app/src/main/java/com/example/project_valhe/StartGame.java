package com.example.project_valhe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class StartGame extends Fragment implements SensorEventListener{

   private ImageView[] diceArray;

   private int[] leftArray;
   private int leftIndex;

   private int[] rightArray;
   private int rightIndex;

   private short round;
   private static short done = 12;
   private boolean playGame;
   private View v;

   private boolean[] diceTouch;
   private TextView[] pointsArray;
   private TextView[] textPointsArray;
   private boolean initlisedAccel;
   private float startX;
   private float startY;
   private float startZ;
   private static float border = .15f;
   private boolean shaked;
   private int shakedDices;

   private SensorManager sensorManager;
   private Sensor sensor;


   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_start_game, container, false);
      leftIndex = 0;
      rightIndex = 0;
      round = 0;
      playGame = true;
      shakedDices = 0;

      diceTouch = new boolean[5];
      diceArray = new ImageView[5];
      pointsArray = new TextView[11];
      textPointsArray = new TextView[10];
      leftArray = new int[10];
      rightArray = new int[10];
      initlisedAccel = false;
      configurePointsArray(view);

      for(int i = 0; i < diceTouch.length; ++i)
      {
         diceTouch[i] = false;
      }


      configureBack(view);
      configurePlay(view);
      configureDices(view);
      configureAcceleration();
      configureDiceImage(view);

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
         //System.out.println(event.values[0]);
         //System.out.println(startX + border);
         //System.out.println(startX - border);

         if((event.values[0] < startX + border && event.values[0] > startX - border) &&
            (event.values[1] < startY + border && event.values[1] > startY - border) &&
            (event.values[2] < startZ + border && event.values[2] > startZ - border))
         {
            startX = event.values[0];
            startY = event.values[1];
            startZ = event.values[2];
            System.out.println("shaked");
            shakeImages();
         }
         else{
            shaked = false;
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
      if(round == done)
      {
         LinearLayout select = view.findViewById(R.id.selected);
         select.setBackgroundColor(Color.GREEN);

         TextView text = view.findViewById(R.id.selectedText);
         text.setText("Done! Fnd Results");

      }
   }

   private void configurePlay(View view){

      LinearLayout select = view.findViewById(R.id.selected);
      select.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if (playGame == true) {
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
                  //System.out.println(left);
                  if (left == true) {
                     round = (short) (round + 1);
                     assert getFragmentManager() != null;
                     FragmentTransaction transaction = getFragmentManager().beginTransaction();
                     transaction.replace(R.id.fragment_container, new SelectedGame(), "START_GAME");
                     transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_right, R.anim.enter_from_right, R.anim.exit_from_right);
                     transaction.addToBackStack(null);
                     transaction.commit();
                  }
                  leftIndex = 0;
               }
            }
            return true;
         }
      });
   }

   private void configureBack(View view){

      LinearLayout back = view.findViewById(R.id.back);
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
               //System.out.println(right);
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
      if(shakedDices < 3) {
         int arrayLen = diceArray.length;
         for (int y = 0; y < arrayLen; ++y) {
            double random = Math.floor(Math.random() * 5);
            int randomInt = (int) random;

            int imageViewId = getResources().getIdentifier("my_dice" + (randomInt + 1) , "drawable", "com.example.project_valhe");
            diceArray[y].setBackgroundResource(imageViewId);
            diceArray[y].setTag(randomInt);
            configureDiceClick(diceArray[y], randomInt, v);
            System.out.println("diceArray[y].getTag():" + diceArray[y].getTag());
            shakedDices = shakedDices + 1;
         }
      }
   }
   private void configureDiceImage(View view) {
      final int arrayLength = diceArray.length;

      for(int i = 0; i < arrayLength; ++i) {
         int imageViewId = getResources().getIdentifier("dice" + (i+1), "id", "com.example.project_valhe");
         diceArray[i] = view.findViewById(imageViewId);
         System.out.println("diceArray[i].getTag():" + diceArray[i].getTag());

      }
   }

   private void configureDiceLongClick(ImageView v, final int number, final View view){
      v.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
            //System.out.println("LONGCLICK");

            int touchedDice = number + 1;
            byte sum = 0;
            for(int i = 0; i < diceTouch.length; ++i)
            {
               String tag = String.valueOf(diceArray[i].getTag());
               int tagInt = Integer.parseInt(tag);

               if(tagInt == number)
               {
                  int imageViewId = getResources().getIdentifier("my_dice_select" + (number + 1) + "", "drawable", "com.example.project_valhe");
                  diceArray[number].setBackgroundResource(imageViewId);
                  diceTouch[number] = true;
                  sum = (byte)(sum + touchedDice);
               }
               else
               {
                  int imageViewId = getResources().getIdentifier("my_dice" + (i + 1) , "drawable", "com.example.project_valhe");
                  diceArray[i].setBackgroundResource(imageViewId);
                  diceTouch[number] = false;
               }
            }
            configurePoints(sum, number, -1);
            return true;
         }
      });
   }

   private void configureDiceClick(ImageView v, final int number, final View view){
      v.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            //System.out.println("CLICK");

            if(diceTouch[number] == false)
            {
               int imageViewId = getResources().getIdentifier("my_dice_select" + (number + 1), "drawable", "com.example.project_valhe");
               v.setBackgroundResource(imageViewId);
               diceTouch[number] = true;
            }
            else
            {
               int imageViewId = getResources().getIdentifier("my_dice" + (number + 1) , "drawable", "com.example.project_valhe");
               v.setBackgroundResource(imageViewId);
               diceTouch[number] = false;
            }

            short amountSelect = 0;
            short sum = 0;
            int option = -1;

            for(int i = 0; i < diceTouch.length; ++i)
            {
               if(diceTouch[i] == true)
               {
                  amountSelect = (short) (amountSelect + 1);
               }
            }
            int[] tags = new int[amountSelect];

            if(amountSelect == 3|| amountSelect == 4 || amountSelect == 5) {
               for (int i = 0; i < amountSelect; ++i) {
                  String tag = String.valueOf(diceArray[i].getTag());
                  int tagInt = Integer.parseInt(tag);
                  tags[i] = tagInt;
               }
               Arrays.sort(tags);

               if (amountSelect == 3){
                  byte found = 0;
                  for (int i = 0; i < amountSelect; ++i) {
                     if (i + 1 == amountSelect && tags[i] == tags[i - 1]) {
                        found = (byte) (found + 1);
                        sum = (short) (sum + (tags[i]));
                     } else if (i + 1 != amountSelect && tags[i] == tags[i + 1]) {
                        found = (byte) (found + 1);
                        sum = (short) (sum + (tags[i]));
                     }
                  }
                  if (found == 3)
                  {
                     //System.out.println("found 3 of the same kind");
                     option = 5;
                  } else if (found == 4)
                  {
                     //System.out.println("found 4 of the same kind");
                     option = 6;
                  }
               }
               else if (amountSelect == 4){
                  int sumFourOfAKind = 0;
                  int sumSmallStraight = 0;
                  boolean fourOfAKind = true;
                  boolean smallStraight = true;
                  byte found = 0;

                  if(smallStraight == true) {
                        for (int i = 0; i < amountSelect; ++i) {
                           if (i + 1 == amountSelect && tags[i] - 1 == tags[i - 1]) {
                              sumSmallStraight = (short) (sumSmallStraight + (tags[i] + 1));
                              found = (byte) (found + 1);
                           } else if (tags[i] + 1 == tags[i + 1]) {
                              sumSmallStraight = (short) (sumSmallStraight + (tags[i] + 1));
                              found = (byte) (found + 1);
                           } else {
                              smallStraight = false;
                           }
                        }
                     }
                  if(fourOfAKind == true) {
                        for (int i = 0; i < amountSelect; ++i) {
                           if (i + 1 == amountSelect && tags[i] == tags[i - 1]) {
                              found = (byte) (found + 1);
                              sumFourOfAKind = (short) (sumFourOfAKind + (tags[i]));
                           } else if (i + 1 != amountSelect && tags[i] == tags[i + 1]) {
                              found = (byte) (found + 1);
                              sumFourOfAKind = (short) (sumFourOfAKind + (tags[i]));
                           } else {
                              fourOfAKind = false;
                           }
                        }
                     }


                  if (fourOfAKind == true) {
                     //System.out.println("4 of a kind");
                     sum = (short) sumFourOfAKind;
                     option = 6;
                  }
                  else if (smallStraight == true) {
                     //System.out.println("small street");
                     sum = (short) sumSmallStraight;
                     option = 7;
                  }
               }
               else if (amountSelect == 5) {
                  boolean bigStraight = true;
                  int sumBigStraight = 0;
                  boolean house = true;
                  int sumHouse = 0;

                  byte found = 0;
                  byte found1 = 0;
                  byte found2 = 0;
                  boolean alreadyUsed = false;
                  int[] oldTags = new int[amountSelect];

                  if(bigStraight == true) {
                     for (int i = 0; i < amountSelect; ++i) {
                        if (i + 1 == amountSelect && tags[i] - 1 == tags[i - 1]) {
                           sumBigStraight = (short) (sumBigStraight + (tags[i] + 1));
                           found = (byte) (found + 1);
                        } else if (tags[i] + 1 == tags[i + 1]) {
                           sumBigStraight = (short) (sumBigStraight + (tags[i] + 1));
                           found = (byte) (found + 1);
                        } else {
                           bigStraight = false;
                        }
                     }
                  }
                  if(house == true) {
                     for(int x = 0; x < oldTags.length; ++x) {
                        String tag = String.valueOf(diceArray[x].getTag());
                        int tagInt = Integer.parseInt(tag);
                        for (int x2 = 0; x2 < oldTags.length; ++x2) {
                           if (tagInt == oldTags[x2]) {
                              alreadyUsed = true;
                           }
                        }
                        if (alreadyUsed == false) {
                           for (int x3 = 0; x3 < oldTags.length; ++x3) {
                              if (found1 != 3) {
                                 found1 = (byte) (found1 + 1);
                                 sumHouse = (short) (sumHouse + (oldTags[x3] + 1));
                              } else {
                                 found2 = (byte) (found2 + 1);
                                 sumHouse = (short) (sumHouse + (oldTags[x3] + 1));
                              }
                           }
                        }
                        oldTags[x] = tagInt;
                        if (found1 != 3 && found2 != 2) {
                           house = false;
                        }
                     }
                  }
                  if(bigStraight == true) {
                     option = 8;
                     sum = (short) sumBigStraight;
                  }
                  if(house == true) {
                     option = 9;
                     sum = (short) sumHouse;
                  }
               }
            }

            configurePoints(sum, number + 1, option );
         }
      });
   }

   private void configurePoints(short sum, final int number, int option){

      int diceNumber = number - 1;
      short size = (byte) (pointsArray.length);
      System.out.println(size);

      for(byte i = 0; i < size; ++i)
      {
         pointsArray[i].setText("");

         if(option == -1 && diceNumber == i)
         {
            pointsArray[diceNumber].setText("" + sum);
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
   }

   private void configureDices(View view) {

      ImageView v = (ImageView)view.findViewById(R.id.dice1);
      String backgroundImageName = String.valueOf(v.getTag());
      int tagNumber = Integer.parseInt(backgroundImageName);
      configureDiceLongClick(v, tagNumber, view);
      configureDiceClick(v, tagNumber, view);
      diceArray[0] = v;

      v = (ImageView)view.findViewById(R.id.dice2);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      configureDiceLongClick(v, tagNumber, view);
      configureDiceClick(v, tagNumber, view);
      diceArray[1] = v;

      v = (ImageView)view.findViewById(R.id.dice3);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      configureDiceLongClick(v, tagNumber, view);
      configureDiceClick(v, tagNumber, view);
      diceArray[2] = v;

      v = (ImageView)view.findViewById(R.id.dice4);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      configureDiceLongClick(v, tagNumber, view);
      configureDiceClick(v, tagNumber, view);
      diceArray[3] = v;

      v = (ImageView)view.findViewById(R.id.dice5);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      configureDiceLongClick(v, tagNumber, view);
      configureDiceClick(v, tagNumber, view);
      diceArray[4] = v;
   }
}