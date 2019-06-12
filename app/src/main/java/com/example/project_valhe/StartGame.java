package com.example.project_valhe;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

public class StartGame extends Fragment {

   private ImageView[] diceArray;
   private TextView touchButtons;

   public StartGame(){
      diceArray = new ImageView[6];
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_start_game, container, false);

      configureSwitch(view);
      configureButton(view);
      configureDicesImage(view);
      return view;
   }

   private void configureButton(View view){

      Button select = view.findViewById(R.id.selected);

      select.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
         assert getFragmentManager() != null;
         FragmentTransaction transaction = getFragmentManager().beginTransaction();
         transaction.replace(R.id.fragment_container, new SelectedGame(), "START_GAME");
         transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_right, R.anim.enter_from_right, R.anim.exit_from_right);
         transaction.addToBackStack(null);
         transaction.commit();
      }
    });
   }

   private void configureSwitch(View view){

      Switch back = view.findViewById(R.id.back);
      back.setChecked(true);

      back.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
               FragmentManager fm = getFragmentManager();
               assert fm != null;
               fm.popBackStack();
            }
         }
      });
   }

   private void configureDicesImage(View view) {
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