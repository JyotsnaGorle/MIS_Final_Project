package com.example.project_valhe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Information extends Fragment {

   private Switch back;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_information, container, false);

      configureSwitch(view);

      return view;
   }

   private void configureSwitch(View view){

      back = view.findViewById(R.id.information_back);
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
