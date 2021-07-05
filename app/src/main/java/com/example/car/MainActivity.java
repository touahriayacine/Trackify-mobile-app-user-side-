package com.example.car;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.car.Fragments.CarInfoFragment;
import com.example.car.Fragments.SetupFragment;
import com.example.car.Fragments.formFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.car.ConstantsUtility.Constants.DEFAULT_TEXT;
import static com.example.car.ConstantsUtility.Constants.FILE_NAME;
import static com.example.car.ConstantsUtility.Constants.MARQUE_KEY;
import static com.example.car.ConstantsUtility.Constants.MATRICULE_KEY;
import static com.example.car.ConstantsUtility.Constants.MODEL_KEY;
import static com.example.car.ConstantsUtility.Constants.PHONE_KEY_WITHOUT_PLUS;
import static com.example.car.ConstantsUtility.Constants.PHONE_KEY_WITH_PLUS;
import static com.example.car.ConstantsUtility.Constants.PRIVATE_CODE_KEY;
import static com.example.car.ConstantsUtility.Constants.REQUEST_CHECK_SETTING;

public class MainActivity extends AppCompatActivity {


    private HashMap<String ,String> carDataMap = new HashMap<String, String>();
    private LocationRequest locationRequest;
    FloatingActionButton configBtn;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        setFragment();
        checkingForPermissions();
        gpsState(locationRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromIntet();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData(this.carDataMap);
    }
    private void getDataFromIntet() {
            Intent intent = getIntent();
            if(intent.getExtras() != null){
                carDataMap.put(PHONE_KEY_WITHOUT_PLUS,intent.getExtras().getString(PHONE_KEY_WITHOUT_PLUS));
                carDataMap.put(PHONE_KEY_WITH_PLUS,intent.getExtras().getString(PHONE_KEY_WITH_PLUS));
                carDataMap.put(PRIVATE_CODE_KEY,intent.getExtras().getString(PRIVATE_CODE_KEY));
                carDataMap.put(MARQUE_KEY,intent.getExtras().getString(MARQUE_KEY));
                carDataMap.put(MODEL_KEY,intent.getExtras().getString(MODEL_KEY));
                carDataMap.put(MATRICULE_KEY,intent.getExtras().getString(MATRICULE_KEY));
                saveData(carDataMap);
                setFragment();
            }
    }
    public Boolean loadData(){
        String privateCode = sharedPreferences.getString(PRIVATE_CODE_KEY , DEFAULT_TEXT);
        String phoneNumberPlus = sharedPreferences.getString(PHONE_KEY_WITH_PLUS , DEFAULT_TEXT);
        String phoneNumber = sharedPreferences.getString(PHONE_KEY_WITHOUT_PLUS , DEFAULT_TEXT);
        String marque = sharedPreferences.getString(MARQUE_KEY , DEFAULT_TEXT);
        String matricule = sharedPreferences.getString(MATRICULE_KEY , DEFAULT_TEXT);
        String model = sharedPreferences.getString(MODEL_KEY , DEFAULT_TEXT);
        String[] data = {marque,model,matricule,privateCode,phoneNumber};
        //if there is at least one field which is not set, the loading process fails
        for (String d:data){
            if (d == DEFAULT_TEXT) return false;
        }
        this.carDataMap.put(PRIVATE_CODE_KEY, privateCode);
        this.carDataMap.put(PHONE_KEY_WITHOUT_PLUS, phoneNumber);
        this.carDataMap.put(PHONE_KEY_WITH_PLUS, phoneNumberPlus);
        this.carDataMap.put(MARQUE_KEY, marque);
        this.carDataMap.put(MATRICULE_KEY, matricule);
        this.carDataMap.put(MODEL_KEY, model);
        return true;
    }
    public void saveData(HashMap<String,String> dataMap){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PHONE_KEY_WITHOUT_PLUS, dataMap.get(PHONE_KEY_WITHOUT_PLUS));
        editor.putString(PHONE_KEY_WITH_PLUS, dataMap.get(PHONE_KEY_WITH_PLUS));
        editor.putString(PRIVATE_CODE_KEY, dataMap.get(PRIVATE_CODE_KEY));
        editor.putString(MATRICULE_KEY, dataMap.get(MATRICULE_KEY));
        editor.putString(MODEL_KEY, dataMap.get(MODEL_KEY));
        editor.putString(MARQUE_KEY, dataMap.get(MARQUE_KEY));
        editor.commit();
    }
    public void setFragment(){
        if(loadData()){
            CarInfoFragment carInfoFragment = new CarInfoFragment(carDataMap);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, carInfoFragment).commit();
            hideConfigBtn();
        }else {
            SetupFragment setupFragment = new SetupFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, setupFragment).commit();
            setConfigBtn();
        }
    }
    void setConfigBtn(){
        configBtn = (FloatingActionButton) findViewById(R.id.configure_btn);
        configBtn.setVisibility(View.VISIBLE);
        configBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formFragment bottomSheetDialog = new formFragment();
                bottomSheetDialog.show( getSupportFragmentManager(), null);
            }
        });
    }
    void hideConfigBtn(){
        configBtn = (FloatingActionButton) findViewById(R.id.configure_btn);
        configBtn.setVisibility(View.INVISIBLE);
    }

//************************************************************************************************//
//**************************************** GPS STATE *********************************************//
    public void gpsState(LocationRequest locationRequest) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is ON", Toast.LENGTH_LONG).show();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTING);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHECK_SETTING)
        {
            switch (resultCode)
            {
                case Activity.RESULT_OK:
                    Toast.makeText(this, "GPS is turned on", Toast.LENGTH_LONG).show();
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS is required to be turned ON", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
    //********************************************************************************************//
    //************************* ASCKING AND CHECKING FOR REQUIRED PERMISSIONS ********************//
    public void checkingForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}
                    , com.example.car.ConstantsUtility.Constants.PERMISSION_CODE);
        }
    }
    public void onRequestPermissionResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults){
        if(requestCode == com.example.car.ConstantsUtility.Constants.PERMISSION_CODE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED)
            {
                // if the user give permissions to the application
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    //********************************************************************************************//
    //******************************* FINDING XML COMPONENT OF ACTIVITY **************************//

}