package com.example.smartmirror;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{

        private GpsTracker gpsTracker;
        private TransLocalPoint translator;
        private WeatherData getweather;
        private  double temp= 0;

        public static Socket socket;
        public static String ip = "172.16.17.242";
        private static final int GPS_ENABLE_REQUEST_CODE = 2001;
        private static final int PERMISSIONS_REQUEST_CODE = 100;
        String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        Button buttonInfo;
        Button buttonStyle;
        Button buttonCloset;
        Button buttonHowTo;
        Button buttonCamera;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            if (checkLocationServicesStatus()) {
                checkRunTimePermission();
                gpsTracker=new GpsTracker(MainActivity.this);
                double latitude = gpsTracker.latitude;
                double longitude = gpsTracker.longitude;
                Log.d("HERE","1");
                Log.e(">>Final latitude: ", String.valueOf(latitude));
                Log.e(">>Final longitude: ", String.valueOf(longitude));
                Toast.makeText(MainActivity.this, "???????????? \n?????? " + latitude + "\n?????? " + longitude, Toast.LENGTH_LONG).show();
                translator=new TransLocalPoint();
                TransLocalPoint.LatXLngY coord=translator.convertGRID_GPS(0,latitude,longitude);
                int x=(int)coord.x+1;
                int y=(int)coord.y+1;
                Log.e(">>Final X: ", String.valueOf(coord.x+1));
                Log.e(">>Final Y: ", String.valueOf(coord.y+1));

                getweather=new WeatherData();

                try {
                    getweather.getWeather(String.valueOf(x),String.valueOf(y));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                showDialogForLocationServiceSetting();
            }




            buttonInfo = (Button)findViewById(R.id.btn_info);
            buttonInfo.setOnClickListener(this);
            buttonStyle = (Button)findViewById(R.id.btn_styling);
            buttonStyle.setOnClickListener(this);
            buttonCloset = (Button)findViewById(R.id.btn_closet);
            buttonCloset.setOnClickListener(this);
            buttonHowTo = (Button)findViewById(R.id.btn_manual);
            buttonHowTo.setOnClickListener(this);
            buttonCamera=(Button)findViewById(R.id.btn_camera);
            buttonCamera.setOnClickListener(this);
        }

    //    @Override
    //    protected void onStart() {
    //        super.onStart();
    //    }
    //
    //    @Override
    //    protected void onRestart() {
    //        super.onRestart();
    //    }
    //
    //    @Override
    //    protected void onStop() {
    //        super.onStop();
    //    }
    //
    //    @Override
    //    protected void onDestroy() {
    //        super.onDestroy();
    //    }
    //
    //    @Override
    //    protected void onPause() {
    //        super.onPause();
    //    }
    //
    //    @Override
    //    protected void onResume() {
    //        super.onResume();
    //    }

        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_info:
                    Intent intent1 = new Intent(MainActivity.this, InfoActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btn_styling:
                    temp=getweather.weather;
                    Log.e("Temperature: ",String.valueOf(temp));
                    String section=String.valueOf(temp_section(temp));
                    Log.e(">>1. Temperature section: ",String.valueOf(section));
                    Intent intent2 = new Intent(MainActivity.this, StylingActivity.class);
                    intent2.putExtra("temp_section",section);
                    startActivity(intent2);
                    break;
                case R.id.btn_closet:
                    Intent intent3 = new Intent(MainActivity.this, ClosetActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.btn_manual:
                    Intent intent4 = new Intent(MainActivity.this, ManualActivity.class);
                    startActivity(intent4);
                    break;

                case R.id.btn_camera:
                    virtualfittingThread camera=new virtualfittingThread();
                    try {
                        String result=camera.execute("cam").get();
                        Log.e("[socket result]",result);
                        if(result.equals("camera"))
                        {
                            Toast.makeText(MainActivity.this, "?????? ??????! ClOSET?????? ??????????????????.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        }

        public int temp_section(double temperature){
            int section_num=0;
            temperature=(int)temperature;
            if(temperature>=27)
            {
                section_num=1;
            }
            else if(23<=temperature&&temperature<=26)
            {
                section_num=2;
            }
            else if(19<=temperature&&temperature<22)
            {
                section_num=3;
            }
            else if(15<=temperature&&temperature<=18)
            {
                section_num=4;
            }
            else if(8<=temperature&&temperature<=14)
            {
                section_num=5;
            }
            else if(5<=temperature&&temperature<=7)
            {
                section_num=6;
            }
            else if(temperature<=4)
            {
                section_num=7;
            }

            return section_num;
        }
    /*
     * ActivityCompat.requestPermissions??? ????????? ????????? ????????? ????????? ???????????? ??????????????????.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????

            boolean check_result = true;


            // ?????? ???????????? ??????????????? ???????????????.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //?????? ?????? ????????? ??? ??????
                ;
            } else {
                // ????????? ???????????? ????????? ?????? ????????? ??? ?????? ????????? ??????????????? ?????? ???????????????.2 ?????? ????????? ????????????.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }
    //??????????????? GPS ???????????? ?????? ????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS ????????? ?????????");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ???????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    public boolean checkLocationServicesStatus() {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    void checkRunTimePermission() {

            //????????? ????????? ??????
            // 1. ?????? ???????????? ????????? ????????? ???????????????.
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                // 2. ?????? ???????????? ????????? ?????????
                // ( ??????????????? 6.0 ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? ????????? ?????? ???????????????.)


                // 3.  ?????? ?????? ????????? ??? ??????


            } else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

                // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                    // 3-2. ????????? ???????????? ?????? ?????????????????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                    Toast.makeText(MainActivity.this, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                    // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                    ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);


                } else {
                    // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                    // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                    ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }

            }

    }



}