package com.example.ratenews;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.ratenews.tabs.PageTabsAdapter;

public class MainActivity extends AppCompatActivity {

    public static String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmail = getIntent().getStringExtra("email");
        Log.e("username", userEmail);

        //Fabric.with(this, new Crashlytics());

        //initializing tabs
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dobrodošli");
        toolbar.setSubtitle(userEmail);
        ViewPager viewPager = findViewById(R.id.viewPager);
        PageTabsAdapter myPagerAdapter = new PageTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        final TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    //toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    //        R.color.colorInformer));
                    //tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    //        R.color.colorInformer));
                } else if (tab.getPosition() == 2) {
                    //toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    //        R.color.colorN1));
                    //tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    //        R.color.colorN1));
                } else if (tab.getPosition() == 3) {
                    //toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    //        R.color.colorN1));
                    //tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    //        R.color.colorN1));
                }
                else {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
//                            R.color.colorBlic));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
//                            R.color.colorBlic));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}