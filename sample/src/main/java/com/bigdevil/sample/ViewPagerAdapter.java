package com.bigdevil.sample;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bigdevil.taketurnhelper.TakeTurnHelper;
import com.bigdevil.taketurnhelper.TakeTurnHelperProvider;
import com.bigdevil.taketurnhelper.sample.R;

public class ViewPagerAdapter extends PagerAdapter {
    private ViewPager viewPager;

    private TakeTurnHelper helper;

    public ViewPagerAdapter(ViewPager viewPager) {
        this.viewPager = viewPager;
        helper = new TakeTurnHelper(viewPager.getContext());
    }

    @Override
    public int getCount() {
        return 11;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv);
        TestAdapter adapter = new TestAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        switch (position) {
            case 1:
                adapter.setColor(Color.GRAY);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.IN);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.RIGHT);
                helper.setTargetRecyclerView(recyclerView);
                break;
            case 2:
                adapter.setColor(Color.BLUE);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.IN);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.LEFT);
                helper.setTargetRecyclerView(recyclerView);
                break;
            case 3:
                adapter.setColor(Color.BLACK);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.IN);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.BOTH);
                helper.setTargetRecyclerView(recyclerView);
                break;
            case 4:
                adapter.setColor(Color.RED);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.OUT);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.RIGHT);
                helper.setTargetRecyclerView(recyclerView);
                break;
            case 5:
                adapter.setColor(Color.GREEN);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.OUT);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.LEFT);
                helper.setTargetRecyclerView(recyclerView);
                break;
            case 6:
                adapter.setColor(Color.YELLOW);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.OUT);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.BOTH);
                helper.setTargetRecyclerView(recyclerView);
                break;
            case 7:
                adapter.setColor(Color.CYAN);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.BOTH);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.RIGHT);
                helper.setTargetRecyclerView(recyclerView);
                break;
            case 8:
                adapter.setColor(Color.MAGENTA);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.BOTH);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.LEFT);
                helper.setTargetRecyclerView(recyclerView);
                break;
            case 9:
                adapter.setColor(Color.DKGRAY);
                helper = TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position);
                helper.setSupportMode(TakeTurnHelper.Mode.BOTH);
                helper.setSupportScrollDirection(TakeTurnHelper.ScrollDirection.BOTH);
                helper.setTargetRecyclerView(recyclerView);
                break;
            default:
                break;

        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        TakeTurnHelperProvider.getHelper(container.getContext(), viewPager, position).onDestroy();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
