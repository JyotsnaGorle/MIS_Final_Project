package com.example.project_valhe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SecondScreen extends Fragment{

   private OnFragmentInteractionListener mListener;
   private Switch playGame;
   private Switch showInformation;

   public SecondScreen() {
   }

   // TODO: Rename and change types and number of parameters
   public static SecondScreen newInstance() {
      SecondScreen fragment = new SecondScreen();

      return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_second_screen, container, false);


      configureGameSwitch(view);
      configureInfoSwitch(view);

      return view;
   }

   // TODO: Rename method, update argument and hook method into UI event
   public void sendBack(String sendBackText) {
      if (mListener != null) {
         mListener.onFragmentInteraction(sendBackText);
      }
   }

   @Override
   public void onAttach(Context context) {
      super.onAttach(context);
      if (context instanceof OnFragmentInteractionListener) {
         mListener = (OnFragmentInteractionListener) context;
      } else {
         throw new RuntimeException(context.toString()
              + " must implement OnFragmentInteractionListener");
      }
   }

   @Override
   public void onDetach() {
      super.onDetach();
      mListener = null;
   }

   public interface OnFragmentInteractionListener {
      void onFragmentInteraction(String sendBackText);
   }

   private void configureGameSwitch(View view)
   {
      playGame = (Switch) view.findViewById(R.id.playGame);
      playGame.setChecked(false);

      playGame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
               FragmentTransaction transaction = getFragmentManager().beginTransaction();
               transaction.replace(R.id.fragment_container, new StartGame(), "START_GAME");
               transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_right, R.anim.enter_from_right, R.anim.exit_from_right);
               transaction.addToBackStack(null);
               transaction.commit();
            }
         }
      });
   }

   private void configureInfoSwitch(View view)
   {
      showInformation = (Switch) view.findViewById(R.id.information);
      showInformation.setChecked(false);

      showInformation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
               FragmentTransaction transaction = getFragmentManager().beginTransaction();
               transaction.replace(R.id.fragment_container, new Information(), "INFORMATION_SCREEN");
               transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom);
               transaction.addToBackStack(null);
               transaction.commit();
            }
         }
      });
   }
}
