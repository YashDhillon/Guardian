package com.example.yash.guardian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main0Activity extends AppCompatActivity {

    final String[] profile = {"profile_name","profile_phoneNumber"};

    boolean doesProfileNameExist = false;
    boolean doesProfilePhoneNumber = false;

    Button button8;

    EditText nEt,pnEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);

        nEt =(EditText)findViewById(R.id.nEt);
        pnEt = (EditText)findViewById(R.id.pnEt);
        button8 = (Button)findViewById(R.id.button8);

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pnEt.getText().toString().length() <10 || nEt.getText().length()<0){
                    Toast.makeText(Main0Activity.this, "Incorrect phone number", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("PhoneNumber",pnEt.getText().toString());
                    intent.putExtra("Name",nEt.getText().toString());
                    setResult(1212,intent);
                    finish();
                }
            }
        });

    }
}
