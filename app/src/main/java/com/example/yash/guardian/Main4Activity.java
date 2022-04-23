package com.example.yash.guardian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main4Activity extends AppCompatActivity {

    EditText et1, et2;
    GuardianDataBase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        mydb = new GuardianDataBase(this);

    }

    public void click(View v) {
        if (et1.getText().toString().equals("")) {
            Toast.makeText(this, "Details insufficient", Toast.LENGTH_SHORT).show();
        } else if (et2.getText().toString().equals("")) {
            Toast.makeText(this, "Details insufficient", Toast.LENGTH_SHORT).show();
        } else {
            if (et2.getText().toString().length() == 10) {
                String name = et1.getText().toString();
                String phonno = et2.getText().toString();
                addguard(name, phonno);
            } else {
                Toast.makeText(this, "Wrong mobile number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addguard(String name, String phonno) {
        mydb.insertdata(name,phonno);
        finish();
    }
}