package com.example.car.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.car.R;


public class SetupFragment extends Fragment {
    private View thisFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisFragment =  inflater.inflate(R.layout.fragment_setup, container, false);

        return thisFragment;
    }

}