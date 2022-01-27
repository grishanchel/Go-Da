package com.example.afinal;

import android.content.Context;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;

public class CustomDialogFragmentHome extends DialogFragment {

    private static final String TAG = "MyCustomDialog";

    private AutoCompleteTextView mInput;

    private TextView mActionOk, mActionCancel;

    String[] ntypes = {"Бар", "Кафе", "Ресторан", "Кофейня", "Фастфуд", "Столовая"};
    ArrayAdapter<String> types;

    public interface OnInputSelected{

        void sendInput(String input);

    }

    public OnInputSelected mOnInputSelected;

    @Nullable

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_dialog_custom_home, container, false);

        mActionOk = view.findViewById(R.id.action_ok);

        mActionCancel = view.findViewById(R.id.action_cancel);

        mInput = view.findViewById(R.id.Ntypehome);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, ntypes);
        mInput.setText(arrayAdapter.getItem(0).toString(),false);
        mInput.setAdapter(arrayAdapter);

        mActionCancel.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Log.d(TAG, "onClick: closing dialog");

                getDialog().dismiss();

            }

        });

        mActionOk.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Log.d(TAG, "onClick: capturing input.");

                String input = mInput.getText().toString();

                if(!input.equals("")){

                    mOnInputSelected.sendInput(input);

                }

                getDialog().dismiss();

            }

        });

        return view;

    }

    @Override

    public void onAttach(Context context) {

        super.onAttach(context);

        try{

            mOnInputSelected = (OnInputSelected) getTargetFragment();

        }catch (ClassCastException e){

            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );

        }

    }

}

