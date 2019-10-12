package com.bigdevil.taketurnhelper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.pv);
        viewPager.setAdapter(new ViewPagerAdapter(viewPager));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TakeTurnHelperProvider.onParentDestroy(viewPager);
    }
}
