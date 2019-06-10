package com.example.project_valhe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SecondScreen extends Fragment {

   private OnFragmentInteractionListener mListener;


   public SecondScreen() {
   }

   // TODO: Rename and change types and number of parameters
   public static SecondScreen newInstance() {
      SecondScreen fragment = new SecondScreen();
      //Bundle args = new Bundle();
      //args.putString(TEXT, text);
      //fragment.setArguments(args);
      return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         //mText = getArguments().getString(TEXT);
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_second_screen, container, false);

      //buttonFragment = view.findViewById(R.id.button_fragment);
      //editTextFragment = view.findViewById(R.id.edittext_fragment);
      //editTextFragment.setText(mText);
      //editTextFragment.requestFocus();
/*
      buttonFragment.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String sendBackText = editTextFragment.getText().toString();
            sendBack(sendBackText);
         }
      });
*/
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

   /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
   public interface OnFragmentInteractionListener {
      // TODO: Update argument type and name
      void onFragmentInteraction(String sendBackText);
   }
}