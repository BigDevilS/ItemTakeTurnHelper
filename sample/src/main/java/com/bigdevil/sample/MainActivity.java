package com.bigdevil.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bigdevil.taketurnhelper.TakeTurnHelperProvider;
import com.bigdevil.taketurnhelper.sample.R;


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
