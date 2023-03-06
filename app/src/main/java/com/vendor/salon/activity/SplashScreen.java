package com.vendor.salon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.vendor.salon.R;

public class SplashScreen extends AppCompatActivity {
        boolean tappedOne = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!com.vendor.salon.utilityMethod.loginResponsePref.getInstance(getApplicationContext()).isLogIN()) {
                    Intent intent=new Intent(SplashScreen.this, Login.class);
//                Intent intent=new Intent(SplashScreen.this, Home.class);
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent =new Intent(SplashScreen.this, Home.class);
                    startActivity(intent);
                    finish();
                }

            }
        },5000);


    }

    @Override
    public void onBackPressed() {
        if(tappedOne) {
            Toast.makeText(this, " Press again to exist from app. ", Toast.LENGTH_SHORT).show();
            tappedOne = false ;
        }
        else {
            super.onBackPressed();
        }
    }
}