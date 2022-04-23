package com.example.yash.guardian;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main6Activity extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5;
    Button button,button2;
    GuardianDataBase mydb;

    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        mydb = new GuardianDataBase(this);


        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);


        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);

        tv4 = (TextView)findViewById(R.id.tv4);
        tv5 = (TextView)findViewById(R.id.tv5);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bun = getIntent().getExtras();
                    mydb.deletedata(bun.getString("ID"));
                    finish();
                }
            });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = getIntent().getExtras();
                Intent i = new Intent(Main6Activity.this,Main7Activity.class);
                i.putExtra("ID",bun.getString("ID"));
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bun = getIntent().getExtras();

        ID = Integer.parseInt(bun.getString("ID"));

        Cursor res = mydb.viewalldata();
        res.moveToFirst();
        while(ID != res.getInt(0))
        {
            res.moveToNext();
        }

        tv4.setText(res.getString(1));
        tv5.setText(res.getString(2));
    }
}
