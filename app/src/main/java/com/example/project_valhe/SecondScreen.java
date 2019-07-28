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
   private int upIndex;
   private int leftIndex;
   private int[] upArray = new int[10];
   private int[] leftArray = new int[10];

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

      leftIndex = 0;
      upIndex = 0;
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
                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                  transaction.replace(R.id.fragment_container, new StartGame(), "Start_SCREEN");
                  transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_right, R.anim.enter_from_right, R.anim.enter_from_right);
                  transaction.addToBackStack(null);
                  transaction.commit();
               }
               leftIndex = 0;
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
            int y = (int) event.getY();
            boolean up = false;
            int limit = upArray.length;

            if(upIndex != limit) {
               upArray[upIndex] = y;
               upIndex = upIndex + 1;
            }
            else{
               for(int i = 0; i < limit - 1; ++i){
                  if(upArray[i] > upArray[i + 1])
                  {
                     up = true;
                  }
                  else{
                     up = false;
                     break;
                  }
               }
               System.out.println(up);
               if(up == true)
               {
                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                  transaction.replace(R.id.fragment_container, new Information(), "INFORMATION_SCREEN");
                  transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_bottom, R.anim.enter_from_bottom, R.anim.exit_from_bottom);
                  transaction.addToBackStack(null);
                  transaction.commit();
               }
               upIndex = 0;
            }
            return true;
         }
      });
   }
}
