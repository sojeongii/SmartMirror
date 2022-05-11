package com.example.smartmirror;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StylingActivity extends AppCompatActivity {

    private CategoryFragment fragment;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_styling);

        fragment = new CategoryFragment();

        Intent receive_intent=getIntent();
        String section=receive_intent.getStringExtra("temp_section");
        Log.e(">>2. Temperature section: ",section);
        Bundle bundle=new Bundle();
        bundle.putString("temp_section",section);

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainFragment, fragment).commitAllowingStateLoss();

    }

}