package com.example.car.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import static com.example.car.ConstantsUtility.Constants.MARQUE_KEY;
import static com.example.car.ConstantsUtility.Constants.MATRICULE_KEY;
import static com.example.car.ConstantsUtility.Constants.MODEL_KEY;

import com.example.car.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.

 */
public class formFragment3 extends Fragment {

    private Button button_annuler ;
    private Button button_confirmer;
    private ImageButton button_retour ;
    private TextInputEditText edtModel;
    private TextInputEditText edtMarque;
    private TextInputEditText edtMatricule;

    private formFragment bottomSheetFragment;
    private formFragment formFrag;

    public formFragment3(formFragment formFrag) {
        // Required empty public constructor
        this.formFrag = formFrag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recuperer l'objet de bottomSheetFragment pour pouvoir utiliser le bouton annuler.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bottomSheetFragment = bundle.getParcelable("this"); // Key
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form3, container, false);
        findViews(v);
        annulerButton(v);
        confirmerButton(v);
        retourButton(v);
        setPrevAttrForMod();



        return v;
    }

    private void setPrevAttrForMod() {
        if (formFrag.MARQUE_KEY_MOD != "DEFAULT") edtMarque.setText(formFrag.MARQUE_KEY_MOD);
        if (formFrag.MODEL_KEY_MOD != "DEFAULT") edtModel.setText(formFrag.MODEL_KEY_MOD);
        if (formFrag.MATRICULE_KEY_MOD != "DEFAULT") edtMatricule.setText(formFrag.MATRICULE_KEY_MOD);
    }


    public void annulerButton (View v) {
        button_annuler = v.findViewById(R.id.annuler);

        button_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetFragment.dismiss();

            }
        });
    }

    public void confirmerButton (View v) {
        button_confirmer = v.findViewById(R.id.confirmer);

        button_confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // put extra information
                formFrag.intent.putExtra(MARQUE_KEY,edtMarque.getText().toString());
                formFrag.intent.putExtra(MATRICULE_KEY,edtMatricule.getText().toString());
                formFrag.intent.putExtra(MODEL_KEY,edtModel.getText().toString());
                formFrag.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(formFrag.intent);
                bottomSheetFragment.dismiss();

            }
        });
    }

    public void retourButton (View v) {

        button_retour = v.findViewById(R.id.retour2);

        button_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetFragment.setFragment2(formFrag);


            }
        });
    }

    private void findViews(View v){
         edtModel = v.findViewById(R.id.model);
         edtMarque = v.findViewById(R.id.marque);
         edtMatricule = v.findViewById(R.id.matricule);
    }

}