package com.example.project_valhe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class SelectedGame extends Fragment {

   private Switch back;
   private Button done;
   private EditText calPoint;
   private Spinner spinner;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_selected_game, container, false);

      configureSwitch(view);
      configureButton(view);
      return view;
   }

   private void configureButton(View view){

      done = view.findViewById(R.id.done);
      calPoint = view.findViewById(R.id.calPoints);
      spinner = view.findViewById(R.id.spinner);

      done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String sum = calPoint.getText().toString();
            String dicPoints = spinner.getSelectedItem().toString();

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
