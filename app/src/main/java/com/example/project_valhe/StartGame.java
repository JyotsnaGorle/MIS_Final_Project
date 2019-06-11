package com.example.project_valhe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class StartGame extends Fragment {


   private Switch back;
   private Button select;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_start_game, container, false);

      configureSwitch(view);
      configureButton(view);
      return view;
   }

   private void configureButton(View view){

      select = view.findViewById(R.id.selected);

      select.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
         FragmentTransaction transaction = getFragmentManager().beginTransaction();
         transaction.replace(R.id.fragment_container, new SelectedGame(), "START_GAME");
         transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom);
         transaction.addToBackStack(null);
         transaction.commit();
      }
    });
   }

   private void configureSwitch(View view){

      back = view.findViewById(R.id.back);
      back.setChecked(true);

      back.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
               FragmentManager fm = getFragmentManager();
               fm.popBackStack();
            }
         }
      });
   }
}
