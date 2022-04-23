package com.example.yash.guardian;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Main5Activity extends AppCompatActivity {

    ListView lv;
    GuardianDataBase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        lv = (ListView)findViewById(R.id.lv);
        mydb = new GuardianDataBase(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> namesForCA= mydb.NamesForCA();
        String[] namesforca = new String[namesForCA.size()];
        namesforca = namesForCA.toArray(namesforca);

        List<String> phonenoForCA= mydb.PhoneNoForCA();
        String[] phonenoforca = new String[phonenoForCA.size()];
        phonenoforca = phonenoForCA.toArray(phonenoforca);

        List<String> idsForCA= mydb.IdForCA();
        String[] idsforca = new String[idsForCA.size()];
        idsforca = idsForCA.toArray(idsforca);

        CustomLayout ca = new CustomLayout(this,namesforca,phonenoforca,idsforca);

        lv.setAdapter(ca);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idview = (TextView)view.findViewById(R.id.idview);
                if(idview.getText().toString().equalsIgnoreCase("no entered guardians")){}
                else
                {
                    String ID = idview.getText().toString();
                    Intent i = new Intent(Main5Activity.this, Main6Activity.class);
                    i.putExtra("ID", ID);
                    startActivity(i);
                }
            }
        });
    }
}
