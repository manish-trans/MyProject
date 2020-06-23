package com.e.cartapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class LocationActivity extends AppCompatActivity {

    AutoCompleteTextView edt_location;
    Button btn_save;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        edt_location=findViewById(R.id.edt_location);
        btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location_txt=edt_location.getText().toString();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LocationActivity.this);

                SharedPreferences.Editor editor = settings.edit();

                editor.putString("location_id",location_txt);
                editor.apply();


            }
        });
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LocationActivity.this);
        String locationtxt = settings.getString("location_id", "");
        if(!locationtxt.equalsIgnoreCase(""))
        {
            Intent i=new Intent(LocationActivity.this,MainActivity.class);
            startActivity(i);;
        }

      /*  edt_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                callExistingDistributorApi();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                edt_location.showDropDown();

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });*/

    }
}
