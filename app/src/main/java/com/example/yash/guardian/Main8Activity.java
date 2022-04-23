package com.example.yash.guardian;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class Main8Activity extends AppCompatActivity {

    BluetoothAdapter mybtadapt = null;
    Set<BluetoothDevice> paireddevices;
    ListView lvpaired;

    Button b3;

    TextView tv13;
    TextView tv14;

    String lastdevicename = "lastdevicename";
    String lastdeviceaddress = "lastdeviceaddress";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        lvpaired = (ListView) findViewById(R.id.lvpaired);

        tv13 = (TextView)findViewById(R.id.textView13);
        tv14 = (TextView)findViewById(R.id.textView14);

        b3 = (Button)findViewById(R.id.button3);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = tv14.getText().toString();

                Intent i = new Intent();
                i.putExtra("address",address);
                setResult(7,i);
                finish();

            }
        });

        mybtadapt = BluetoothAdapter.getDefaultAdapter();
        if (mybtadapt == null)
        {
            Toast.makeText(this, "The device does not have BlueTooth services", Toast.LENGTH_SHORT).show();
        }
        else if (!mybtadapt.isEnabled())
        {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, 1);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            FileInputStream fis = openFileInput(lastdevicename);
            String temp = "";
            int c;
            while((c = fis.read())!=-1)
            {
                temp = temp + Character.toString((char)c);
            }

            tv13.setText(temp);
        }catch (IOException e)
        {
        }

        try{
            FileInputStream fis = openFileInput(lastdeviceaddress);
            String temp = "";
            int c;
            while((c = fis.read())!=-1)
            {
                temp = temp + Character.toString((char)c);
            }

            tv14.setText(temp);
        }catch (IOException e)
        {
        }

        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> address = new ArrayList<String>();


        paireddevices = mybtadapt.getBondedDevices();

        if (paireddevices.size() > 0)
        {
            for (BluetoothDevice bd : paireddevices)
            {
                names.add(bd.getName());
                address.add(bd.getAddress());
            }

            String[] namearr = new String[names.size()];
            namearr = names.toArray(namearr);

            String[] addressarr = new String[address.size()];
            addressarr = address.toArray(addressarr);

            String[] blankarr = new String[names.size()];

            CustomLayout ca = new CustomLayout(Main8Activity.this, namearr, addressarr,blankarr);


            lvpaired.setAdapter(ca);


            lvpaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv1 = (TextView) view.findViewById(R.id.nameview);
                    TextView tv2 = (TextView)view.findViewById(R.id.phonenoview);
                    String name = tv1.getText().toString();
                    String address = tv2.getText().toString();

                    try{
                        FileOutputStream fos = openFileOutput(lastdevicename,MODE_PRIVATE);
                        fos.write(name.getBytes());
                        fos.close();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    try{
                        FileOutputStream fos = openFileOutput(lastdeviceaddress,MODE_PRIVATE);
                        fos.write(address.getBytes());
                        fos.close();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                    tv13.setText(name);
                    tv14.setText(address);


                    Intent i = new Intent();
                    i.putExtra("name",name);
                    i.putExtra("address",address);
                    setResult(7,i);
                    finish();


                }
            });

        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent();
        i.putExtra("name","");
        i.putExtra("address","");
        setResult(7,i);
        finish();
    }
}

