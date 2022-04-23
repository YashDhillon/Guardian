package com.example.yash.guardian;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Main7Activity extends AppCompatActivity {

    TextView tv1,tv2,tv3;
    Button button;
    EditText et1,et2;
    GuardianDataBase mydb;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        tv1 = (TextView)findViewById(R.id.textView7);
        tv2 = (TextView)findViewById(R.id.textView8);
        tv3 = (TextView)findViewById(R.id.textView9);

        button = (Button)findViewById(R.id.button);

        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);

        mydb = new GuardianDataBase(this);

        Bundle bun = getIntent().getExtras();

        ID = Integer.parseInt(bun.getString("ID"));

        Cursor res = mydb.viewalldata();
        res.moveToFirst();
        while(ID != res.getInt(0))
        {
            res.moveToNext();
        }

        et1.setText(res.getString(1));
        et2.setText(res.getString(2));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = getIntent().getExtras();
                mydb.updatedata(bun.getString("ID"),et1.getText().toString(),et2.getText().toString());
                finish();
            }
        });
    }
}
