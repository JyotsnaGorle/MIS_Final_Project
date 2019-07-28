package com.example.project_valhe;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

public class StartGame extends Fragment {

   private ImageView[] diceArray;
   private TextView touchButtons;

   private int[] leftArray = new int[10];
   private int leftIndex;

   private int[] rightArray = new int[10];
   private int rightIndex;

   public StartGame(){
      diceArray = new ImageView[6];
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_start_game, container, false);
      leftIndex = 0;
      rightIndex = 0;
      configureBack(view);
      configurePlay(view);
      configureDiceImage(view);
      return view;
   }

   private void configurePlay(View view){

      LinearLayout select = view.findViewById(R.id.selected);
      select.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            boolean left = false;
            int limit = leftArray.length;

            if(leftIndex != limit) {
               leftArray[leftIndex] = x;
               leftIndex = leftIndex + 1;
            }
            else{
               for(int i = 0; i < limit - 1; ++i){
                  if(leftArray[i] > leftArray[i + 1])
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
                  assert getFragmentManager() != null;
                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                  transaction.replace(R.id.fragment_container, new SelectedGame(), "START_GAME");
                  transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_right, R.anim.enter_from_right, R.anim.exit_from_right);
                  transaction.addToBackStack(null);
                  transaction.commit();
               }
               leftIndex = 0;
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
      TableLayout dices = view.findViewById(R.id.dices);
      touchButtons = view.findViewById(R.id.touchButtons);

      for(int i = 0; i < arrayLength; ++i) {
         int imageViewId = getResources().getIdentifier("dice" + (i+1), "id", "com.example.project_valhe");
         diceArray[i] = view.findViewById(imageViewId);
      }

      dices.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(touchButtons.getText() != "") {
               int y;
               for (y = 0; y < arrayLength; ++y) {
                  double random = Math.random();
                  random = random * 6;
                  int randomInt = (int) random + 1;
                  int imageViewId = getResources().getIdentifier("my_dice" + randomInt, "drawable", "com.example.project_valhe");
                  diceArray[y].setBackgroundResource(imageViewId);
               }
               touchButtons.setText("");
            }
         }
      });
   }
}