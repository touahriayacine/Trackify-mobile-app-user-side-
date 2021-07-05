package com.example.car.Fragments;

import android.os.Bundle;

import static com.example.car.ConstantsUtility.Constants.MARQUE_KEY;
import static com.example.car.ConstantsUtility.Constants.MATRICULE_KEY;
import static com.example.car.ConstantsUtility.Constants.MODEL_KEY;
import static com.example.car.ConstantsUtility.Constants.PHONE_KEY_WITHOUT_PLUS;
import static com.example.car.ConstantsUtility.Constants.PRIVATE_CODE_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CarInfoFragment extends Fragment {


    private String data;
    View thisFragment;
    TextView phone;
    TextView matricule;
    TextView privateCode;
    TextView carName;
    Button modifierBtn;
    Button showQrBtn;
    private HashMap<String ,String> carDataMap;


    public CarInfoFragment(HashMap<String,String> data) {
        this.carDataMap = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisFragment = inflater.inflate(R.layout.fragment_car_info, container, false);
        findViews(thisFragment);
        setTextAttributes();
        return thisFragment;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modifierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on bottom sheet dialog for modify
                formFragment bottomSheetDialog = new formFragment(carDataMap);
                bottomSheetDialog.show(getParentFragmentManager(), null);
            }
        });
        showQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QrCodeFragment qrCodeFragment = new QrCodeFragment(carDataMap);
                qrCodeFragment.show(getActivity().getSupportFragmentManager(),null);
            }
        });
    }
    private void findViews(View v){
        phone = (TextView)  v.findViewById(R.id.value_num_tele);
        matricule = (TextView)  v.findViewById(R.id.value_matricule);
        privateCode = (TextView)  v.findViewById(R.id.value_code_secret);
        carName = (TextView)  v.findViewById(R.id.value_car_name);
        modifierBtn = (Button) v.findViewById(R.id.modifier_btn);
        showQrBtn = (Button) v.findViewById(R.id.show_qr_btn);
    }
    private void setTextAttributes(){
        phone.setText(carDataMap.get(PHONE_KEY_WITHOUT_PLUS));
        matricule.setText(carDataMap.get(MATRICULE_KEY));
        privateCode.setText(carDataMap.get(PRIVATE_CODE_KEY));
        carName.setText(carDataMap.get(MARQUE_KEY) +" "+ carDataMap.get(MODEL_KEY));
    }
}