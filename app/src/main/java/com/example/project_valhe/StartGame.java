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

public class StartGame extends Fragment implements SensorEventListener{

   private ImageView[] diceArray;
   private TextView touchButtons;

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
   private static float border = .1f;
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
      pointsArray = new TextView[12];
      textPointsArray = new TextView[11];
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
      //configureDiceImage(view);
      configureDicesClick(view);

      configureAcceleration();

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





   private  void configureAcceleration(){
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
         System.out.println(event.values[0]);
         System.out.println(startX + border);
         System.out.println(startX - border);

         if((event.values[0] < startX + border && event.values[0] > startX - border) &&
            (event.values[1] < startY + border && event.values[1] > startY - border) &&
            (event.values[2] < startZ + border && event.values[2] > startZ - border))
         {
            startX = event.values[0];
            startY = event.values[1];
            startZ = event.values[2];
            System.out.println("shaked");
            configureDiceImage(v);
            shakedDices = shakedDices + 1;
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
      pointsArray[10] = view.findViewById(R.id.selectChance);
      pointsArray[11] = view.findViewById(R.id.selectPayment);

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
      textPointsArray[10] = view.findViewById(R.id.chance);
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
                  System.out.println(left);
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
               System.out.println(right);
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

   private void configureDiceImage(View view) {
      final int arrayLength = diceArray.length;

      for(int i = 0; i < arrayLength; ++i) {
         int imageViewId = getResources().getIdentifier("dice" + (i+1), "id", "com.example.project_valhe");
         diceArray[i] = view.findViewById(imageViewId);
      }

      if(shakedDices != 3) {
         int y;
         for (y = 0; y < arrayLength; ++y) {
            double random = Math.random();
            random = random * 6;
            int randomInt = (int) random + 1;
            int imageViewId = getResources().getIdentifier("my_dice" + randomInt, "drawable", "com.example.project_valhe");
            diceArray[y].setBackgroundResource(imageViewId);
            diceArray[y].setTag("" + randomInt);
         }
      }
   }

   private void configureDice(ImageView v, final int number, final View view){
      v.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if(diceTouch[number] == true)
            {
               int imageViewId = getResources().getIdentifier("ic_dice_" + (number + 1) + "_select", "drawable", "com.example.project_valhe");
               v.setBackgroundResource(imageViewId);
               diceTouch[number] = false;
            }
            else
            {
               int imageViewId = getResources().getIdentifier("my_dice" + (number + 1) , "drawable", "com.example.project_valhe");
               v.setBackgroundResource(imageViewId);
               diceTouch[number] = true;
            }
            short counter = 0;
            int touchedDice = number + 1;
            for(int i = 0; i < diceTouch.length; ++i)
            {
               String tag = String.valueOf(diceArray[i].getTag());
               int tagInt = Integer.parseInt(tag);
               tagInt = tagInt + 1;

               if(tagInt == touchedDice){
                  counter = (short) (counter + 1);
               };
            }
            short sum = (short) (counter * touchedDice);

            configurePoints(sum, number + 1);
            return true;
         }
      });
   }

   private void configurePoints(short sum, final int number){

      ImageView image = (ImageView)v.findViewById(R.id.dice1);
      System.out.println("**************************");
      int diceNumber = number - 1;
      short size = (short) (pointsArray.length);

      for(short i = 0; i < size; ++i)
      {
         if(diceNumber == i)
         {
            pointsArray[i].setText("" + (sum));
         }
         else{
            pointsArray[i].setText("");
         }
      }
      pointsArray[pointsArray.length -1].setText("" + (sum));

      for(int i = 0; i < textPointsArray.length; ++i)
      {
         if(i == (number -1))
         {
            textPointsArray[number -1].setPaintFlags(textPointsArray[number -1].getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
         }
         else{
            textPointsArray[number -1].setPaintFlags(textPointsArray[number -1].getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
         }
      }
   }

   //TODO ---

   private void configureDicesClick(View view)
   {
      ImageView v = (ImageView)view.findViewById(R.id.dice1);
      String backgroundImageName = String.valueOf(v.getTag());
      int tagNumber = Integer.parseInt(backgroundImageName);
      configureDice(v, tagNumber, view);

      v = (ImageView)view.findViewById(R.id.dice2);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      configureDice(v, tagNumber, view);

      v = (ImageView)view.findViewById(R.id.dice3);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      configureDice(v, tagNumber, view);

      v = (ImageView)view.findViewById(R.id.dice4);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      configureDice(v, tagNumber, view);

      v = (ImageView)view.findViewById(R.id.dice5);
      backgroundImageName = String.valueOf(v.getTag());
      tagNumber = Integer.parseInt(backgroundImageName);
      configureDice(v, tagNumber, view);
   }
}