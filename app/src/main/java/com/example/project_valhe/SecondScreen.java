package com.example.project_valhe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class SecondScreen extends Fragment{

   private OnFragmentInteractionListener mListener;
   private LinearLayout playGame;
   private LinearLayout showInformation;
   private int infoIndex;
   private int startGameIndex;
   private int[] infoArray = new int[10];
   private int[] startGameArray = new int[10];

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

      startGameIndex = 0;
      infoIndex = 0;
      configureGameLayout(view);
      configureInfoLayout(view);

      return view;
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

   private void configureGameLayout(View view)
   {
      playGame = (LinearLayout) view.findViewById(R.id.playGame);

      playGame.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            boolean left = false;
            int limit = startGameArray.length;

            if(startGameIndex != limit) {
               startGameArray[startGameIndex] = x;
               startGameIndex = startGameIndex + 1;
            }
            else{
               for(int i = 0; i < limit - 1; ++i){
                  if(startGameArray[i] > startGameArray[i + 1])
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
                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                  transaction.replace(R.id.fragment_container, new StartGame(), "Start_SCREEN");
                  transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_right, R.anim.enter_from_right, R.anim.enter_from_right);
                  transaction.addToBackStack(null);
                  transaction.commit();
               }
               startGameIndex = 0;
            }
            return true;
         }
      });
   }

   private void configureInfoLayout(View view)
   {
      showInformation = (LinearLayout) view.findViewById(R.id.information);

      showInformation.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            boolean left = false;
            int limit = infoArray.length;

            if(infoIndex != limit) {
               infoArray[infoIndex] = x;
               infoIndex = infoIndex + 1;
            }
            else{
               for(int i = 0; i < limit - 1; ++i){
                  if(infoArray[i] > infoArray[i + 1])
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
                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                  transaction.replace(R.id.fragment_container, new Information(), "INFORMATION_SCREEN");
                  transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_right, R.anim.enter_from_right, R.anim.enter_from_right);
                  transaction.addToBackStack(null);
                  transaction.commit();
               }
               infoIndex = 0;
            }
            return true;
         }
      });
   }
}
