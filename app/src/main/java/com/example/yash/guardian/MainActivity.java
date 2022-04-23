package com.example.yash.guardian;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    //permission code for sms permission
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;

    //the database of all entered guardians
    GuardianDataBase mydb;

    //for the service broadcast reciever
    private IntentFilter mIntentFilter;

    //for updating the UI
    String sentstatus = "sentstatus";

    //checking wether OnGuard service is on
    public boolean ServiceStatus = false;

    //all the register recievers
    public static final String Alert_ON = "AlertON";
    public static final String Alert_OFF = "AlertOFF";
    public static final String ALL_OFF = "ALL_OFF";
    public static final String ALERT_SEMI = "ALERT_SEMI";

    //initializing the reciever
    private BroadcastReceiver mReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Alert_ON)) {
                ChangeToRed();
            } else if(intent.getAction().equals(Alert_OFF)) {
               ChangeToGreen();
            }else if(intent.getAction().equals(ALL_OFF)) {
                Shutdown();
            }//else if(intent.getAction().equals(ALERT_SEMI)) {
               // ChangeToOrange();
           // }
        }
    };

    final String[] profile = {"profile_name","profile_phoneNumber"};

    boolean doesProfileNameExist = false;
    boolean doesProfilePhoneNumber = false;

    Button b4, b5,b6,b7;

    ImageView iv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.drawable.ic_gaurdian_symbol_sqaure);
        getSupportActionBar().setIcon(R.drawable.ic_gaurdian_symbol_sqaure);

        getSupportActionBar().setTitle("Guardian");


        try{
            FileInputStream fis = openFileInput(profile[0]);
            String temp= "";
            int c;
            while((c = fis.read())!=-1)
            {
                temp = temp + Character.toString((char)c);
            }
            doesProfileNameExist = true;
        }catch (Exception e){
        }

        try{
            FileInputStream fis = openFileInput(profile[1]);
            String temp= "";
            int c;
            while((c = fis.read())!=-1)
            {
                temp = temp + Character.toString((char)c);
            }
            doesProfilePhoneNumber = true;
        }catch (Exception e){
        }

        if(!doesProfileNameExist && !doesProfilePhoneNumber){
            Intent intent = new Intent(MainActivity.this,Main0Activity.class);
            startActivityForResult(intent,1212);
        }


        mydb = new GuardianDataBase(this);


        iv1 = (ImageView) findViewById(R.id.iv1);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Alert_OFF);
        mIntentFilter.addAction(Alert_ON);
        mIntentFilter.addAction(ALL_OFF);
        //mIntentFilter.addAction(ALERT_SEMI);


        if (checkPermission(Manifest.permission.SEND_SMS) && checkPermission(Manifest.permission.ACCESS_FINE_LOCATION ) &&
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && checkPermission(Manifest.permission.INTERNET)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},
                    SEND_SMS_PERMISSION_REQUEST_CODE);
        }


        b5 = (Button) findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iv1.setImageDrawable(getDrawable(R.drawable.alertunavailableguardianbackground));
                Intent service = new Intent(MainActivity.this,OnGuard.class);
                stopService(service);
            }
        });

        b6 = (Button) findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Main4Activity.class);
                startActivity(i);
            }
        });

        b7 = (Button) findViewById(R.id.button7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Main5Activity.class);
                startActivity(i);
            }
        });

        b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceStatus = true;
                Intent i = new Intent(MainActivity.this, Main8Activity.class);
                startActivityForResult(i, 7);
            }
        });
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mReceiver, mIntentFilter);

        int i = -1;

        try{
            FileInputStream fis = openFileInput(sentstatus);
            String temp = "";
            int c;
            while((c=fis.read())!=-1)
            {
                temp = temp +Character.toString((char)c);
            }
            i = Integer.parseInt(temp);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        switch(i)
        {
            case 1 :
                ChangeToRed();
                break;
            case 0:
                ChangeToGreen();
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().toString().equalsIgnoreCase("settings"))
        {
            Intent i = new Intent(this, Main2Activity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void ChangeToRed()
    {
        iv1.setImageDrawable(getDrawable(R.drawable.alertonguardianbackgroundanimationstagefour));
        /*alertonanimation malertonanimation = new alertonanimation();
        malertonanimation.start();*/
    }

    public void ChangeToGreen()
    {
        iv1.setImageDrawable(getDrawable(R.drawable.alertoffguardianbackgroundanimationstagefour));
        /*alertoffanimation malertoffanimation = new alertoffanimation();
        malertoffanimation.start();*/
    }

    public void Shutdown()
    {
        Intent i = new Intent(MainActivity.this,OnGuard.class);
        stopService(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 7)
        {
            String address;
            Bundle bun = data.getExtras();

            address = bun.getString("address");

            if (address.equals("")) {
                Toast.makeText(this, "Device info insufficient", Toast.LENGTH_SHORT).show();
            } else {
                Intent service = new Intent(this,OnGuard.class);
                service.putExtra("address",address);
                startService(service);
            }
        }else if(requestCode == 1212){
            Bundle bun = data.getExtras();

            String phoneNumber = bun.getString("PhoneNumber");
            String name = bun.getString("Name");
            try{
                FileOutputStream fos = openFileOutput(profile[0],MODE_PRIVATE);
                fos.write(name.getBytes());
                fos.close();
            }catch (Exception e) {
            }

            try{
                FileOutputStream fos = openFileOutput(profile[1],MODE_PRIVATE);
                fos.write(phoneNumber.getBytes());
                fos.close();
            }catch (Exception e) {
            }
        }
    }


    /*private class alertoffanimation extends Thread{
        public alertoffanimation ()
        {}

        public void run() {

            boolean flag = true;

            while (flag) {
                int i = 0;
                for(i=0;i<4;i++)
                {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    if(i==0)
                    {
                       alertoffanimationhandler.post(new Runnable() {
                           @Override
                           public void run() {
                               iv1.setImageDrawable(getDrawable(R.drawable.universalstageone));
                           }
                       }) ;
                    } else if(i==1)
                    {
                        alertoffanimationhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iv1.setImageDrawable(getDrawable(R.drawable.alertoffguardianbackgroundanimationstagetwo));
                            }
                        }) ;
                    }else if(i==2)
                    {
                        alertoffanimationhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iv1.setImageDrawable(getDrawable(R.drawable.alertoffguardianbackgroundanimationstagethree));
                            }
                        }) ;
                    }else if(i==3)
                    {
                        alertoffanimationhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iv1.setImageDrawable(getDrawable(R.drawable.alertoffguardianbackgroundanimationstagefour));
                            }
                        }) ;
                        flag = false;

                    }
                }

            }
        }
    }

    Handler alertoffanimationhandler = new Handler();



    private class alertonanimation extends Thread{
        public alertonanimation ()
        {}

        public void run() {

            boolean flag = true;

            while (flag) {
                int i = 0;
                for(i=0;i<4;i++)
                {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    if(i==0)
                    {
                        alertonanimationhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iv1.setImageDrawable(getDrawable(R.drawable.universalstageone));
                            }
                        }) ;
                    } else if(i==1)
                    {
                        alertonanimationhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iv1.setImageDrawable(getDrawable(R.drawable.alertonguardianbackgroundanimationstagetwo));
                            }
                        }) ;
                    }else if(i==2)
                    {
                        alertonanimationhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iv1.setImageDrawable(getDrawable(R.drawable.alertonguardianbackgroundanimationstagethree));
                            }
                        }) ;
                    }else if(i==3)
                    {
                        alertonanimationhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iv1.setImageDrawable(getDrawable(R.drawable.alertonguardianbackgroundanimationstagefour));
                            }
                        }) ;
                        flag = false;
                    }
                }

            }
        }
    }

    Handler alertonanimationhandler = new Handler();*/

}