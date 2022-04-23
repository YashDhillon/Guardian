package com.example.yash.guardian;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.UUID;

public class OnGuard extends Service {

    BluetoothAdapter mybtadapt = null;
    BluetoothSocket mybtsock = null;
    final UUID myuuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    boolean isconnected = false;

    boolean  turnNotificationON = false;

    boolean isConnectionAlive = true;

    boolean isThisFirstConnectionSession = true;

    boolean isConnectionAliveFOR1 = false;

    static long UID;

    static long PhoneNumber;

    final String[] profile = {"profile_name", "profile_phoneNumber"};

    String address;

    String sentstatus = "sentstatus";

    static final String localhost = "192.168.43.113"; // quasar localhost : 10.16.105.122  quasar + localhost : 192.168.43.113   quasar_5GHZ localhost = 10.16.105.144

    GuardianDataBase mydb;

    Notification notification;

    LocationManager mLocationManger;

    LocationListener mLocationListener;

    RequestQueue requestQueue;

    @SuppressLint("HandlerLeak")
    Handler bthandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int) msg.arg1;
            int end = (int) msg.arg2;
            switch (msg.what) {
                case 1: {
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
                    if (writeMessage.equals("1")) {
                        turnNotificationON = true;
                        isConnectionAliveFOR1 = true;
                        changeAlertFileStatusToOne();
                        Intent broadcastIntent = new Intent();
                        deployGaurdians();
                        broadcastIntent.setAction(MainActivity.Alert_ON);
                        sendBroadcast(broadcastIntent);
                    } else if (writeMessage.equals("0")) {
                        turnNotificationON = false;
                        isConnectionAlive = true;
                        isConnectionAliveFOR1 = false;
                        changeAlertFileStatusToZero();
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(MainActivity.Alert_OFF);
                        sendBroadcast(broadcastIntent);
                    } else if (writeMessage.equals("2")) {
                        isConnectionAlive = true;
                    }/*else if (writeMessage.equals("3")) {
                        changeAlertFileStatusToZero();
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(MainActivity.ALERT_SEMI);
                        sendBroadcast(broadcastIntent);
                    }*/
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManger = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            FileInputStream fis = openFileInput(profile[1]);
            String temp = "";
            int c;
            while ((c = fis.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            PhoneNumber = Long.parseLong(temp);
            UID = (long) (PhoneNumber * Math.random());
        } catch (Exception e) {
        }
        requestQueue = Volley.newRequestQueue(OnGuard.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Boolean doItOnce = true;
                if (turnNotificationON) {
                    if (isThisFirstConnectionSession) {
                        String JRequestString = FunctionURLs.getPOSTrequestBody(UID, PhoneNumber, location.getLongitude(), location.getLatitude());

                        JSONObject jobject = null;
                        try {
                            jobject = new JSONObject(JRequestString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://" + localhost + ":8080/GuardianWS/gws/location/", jobject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(OnGuard.this, "works", Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(OnGuard.this, "POST", Toast.LENGTH_SHORT).show();
                                }
                            });
                            isThisFirstConnectionSession = false;
                            requestQueue.add(request);
                        } else {
                            String JRequestString = FunctionURLs.getPUTrequestBody(UID, PhoneNumber, location.getLongitude(), location.getLatitude());

                            JSONObject jobject = null;
                            try {
                                jobject = new JSONObject(JRequestString);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, "http://" + localhost + ":8080/GuardianWS/gws/location/", jobject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            if (response == null) {
                                                Toast.makeText(OnGuard.this, "works", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(OnGuard.this, "PUT", Toast.LENGTH_SHORT).show();
                                }
                            });
                            isThisFirstConnectionSession = false;
                            requestQueue.add(request);
                        }
                    }



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //noinspection MissingPermission
        mLocationManger.requestLocationUpdates("gps", 1000, 0, mLocationListener);

        mydb = new GuardianDataBase(this);
        NotificationCompat.Builder notif  = new NotificationCompat.Builder(this);
        notif.setContentTitle("Guardian Systems Online");
        notif.setContentText("Guardian status: On Guard");
        notif.setSmallIcon(R.drawable.gaurdian_symbol_sqaure);
        notif.setSmallIcon(R.drawable.gaurdian_symbol_sqaure,0);
        notif.setOngoing(true);
        notification = notif.build();
        startForeground(101,notification);
        Bundle bun = intent.getExtras();
        address = bun.getString("address");
        connectbt(address);
        return START_REDELIVER_INTENT;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        String DeleteURL = FunctionURLs.getDELETErequestBody(PhoneNumber);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, DeleteURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(request);

        mLocationManger.removeUpdates(mLocationListener);

        changeAlertFileStatusToeight();
        if(isConnectionAlive == false)
        {
            Toast.makeText(this, "Connection with device lost.", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Guardian services offline", Toast.LENGTH_SHORT).show();
        if(mybtsock!=null)
        {
            try {
                mybtsock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ALL_OFF);
        sendBroadcast(broadcastIntent);
    }

    public void connectbt(String str1) {

        final String address = str1;

        class ConnectBT extends AsyncTask<Void, Void, Void> {
            private boolean ConnectSuccess = true;

            @Override
            protected void onPreExecute() {
                Toast.makeText(OnGuard.this, "Connecting...", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Void doInBackground(Void... devices) {
                try {
                    if (mybtsock == null || !isconnected) {
                        mybtadapt = BluetoothAdapter.getDefaultAdapter();
                        BluetoothDevice dispositivo = mybtadapt.getRemoteDevice(address);
                        mybtsock = dispositivo.createInsecureRfcommSocketToServiceRecord(myuuid);
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        mybtsock.connect();
                    }
                } catch (IOException e) {
                    ConnectSuccess = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                if (!ConnectSuccess) {
                    Toast.makeText(OnGuard.this, "Connection Failed.Try again.", Toast.LENGTH_SHORT).show();
                    stopForeground(true);
                    stopSelf();
                } else {
                    Toast.makeText(OnGuard.this, "Connected.", Toast.LENGTH_SHORT).show();
                    isconnected = true;
                    dynamicConnectionStatus mdynamicConnectionStatus = new dynamicConnectionStatus();
                    mdynamicConnectionStatus.start();
                    connectedThread mConnectedThread = new connectedThread(mybtsock);
                    mConnectedThread.start();

                    Toast.makeText(OnGuard.this, "Please switch on GPS services", Toast.LENGTH_SHORT).show();

                    if(!mLocationManger.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }

                }

                if (isconnected) {}

            }

        }

        new ConnectBT().execute();

    }

    private class connectedThread extends Thread{
        private final BluetoothSocket mysocket;
        private final InputStream minstr;
        private final OutputStream moutstr;

        public connectedThread (BluetoothSocket mysocket)
        {
            this.mysocket = mysocket;
            InputStream tempin = null;
            OutputStream tempout = null;
            try{
                tempin = mysocket.getInputStream();
                tempout = mysocket.getOutputStream();
            }catch (IOException e){}
            minstr = tempin;
            moutstr=tempout;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                try {
                    bytes += minstr.read(buffer, bytes, buffer.length - bytes);
                    for(int i = begin; i < bytes; i++) {
                        if(buffer[i] == "#".getBytes()[0]) {
                            bthandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }

            }
        }
    }


    private class dynamicConnectionStatus extends Thread{
        public dynamicConnectionStatus ()
        {}

        public void run() {


            while (true) {

                try {
                    Thread.sleep(40000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(isConnectionAliveFOR1 == false) {

                    if (isConnectionAlive == true) {
                        isConnectionAlive = false;
                    } else if (isConnectionAlive == false) {
                        stopForeground(true);
                        stopSelf();
                    }
                }
            }
        }
    }


    protected void deployGaurdians() {

        Cursor res = mydb.viewalldata();
        res.moveToFirst();

        String sent = "Message Sent";
        String delivered = "Message Delivered";

        if(res.getCount()>0) {
            do {
                String alertMessage = res.getString(1).concat(" i am in danger. Track my location here: http://"+localhost+":8080/GuardianWS/mapmarker?uid="+UID+"&pnum="+PhoneNumber+" .Send help");

                PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(sent), 0);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(delivered), 0);

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(context, "Generic Error", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new IntentFilter(sent));

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(context, "Message Deliviered", Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(context, "Message not deliviered", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new IntentFilter(delivered));

                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(res.getString(2), null, alertMessage, sentPI, deliveredPI);

            }while(res.moveToNext());
        }
        else
        {
            Toast.makeText(this, "No entered Guardians", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeAlertFileStatusToOne()
    {
        try{
            FileOutputStream fos = openFileOutput(sentstatus,MODE_PRIVATE);
            fos.write("1".getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void changeAlertFileStatusToZero()
    {
        try{
            FileOutputStream fos = openFileOutput(sentstatus,MODE_PRIVATE);
            fos.write("0".getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void changeAlertFileStatusToeight()
    {
        try{
            FileOutputStream fos = openFileOutput(sentstatus,MODE_PRIVATE);
            fos.write("8".getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
