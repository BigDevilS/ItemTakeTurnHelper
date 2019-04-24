package com.bigdevil.taketurnhelper;

import android.content.Context;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

import androidx.viewpager.widget.ViewPager;

public class TakeTurnHelperProvider {
    private static Map<Object, SparseArray<TakeTurnHelper>> mHelpers = new HashMap<>();

    /**
     * 获取TakeTurnHelper实例
     *
     * @param context  上下文
     * @param parent   父容器，支持ViewPager和RecyclerView
     * @param position 目标RecyclerView在父容器的中的位置
     * @return TakeTurnHelper
     */
    public static TakeTurnHelper getHelper(Context context, ViewPager parent, int position) {
        SparseArray<TakeTurnHelper> helpers = mHelpers.get(parent);
        if (helpers == null) {
            helpers = new SparseArray<>();
            mHelpers.put(parent, helpers);
        }
        TakeTurnHelper helper = helpers.get(position);
        if (helper == null) {
            helper = new TakeTurnHelper(context);
            helpers.put(position, helper);
        }
        helper.setParent(parent);
        return helper;
    }

    public static void onParentDestroy(ViewPager parent) {
        SparseArray<TakeTurnHelper> helpers = mHelpers.get(parent);
        if (helpers != null) {
            int size = helpers.size();
            for (int i = 0; i < size; i++) {
                helpers.valueAt(i).onDestroy();
            }
            helpers.clear();
            mHelpers.remove(parent);
        }
    }
}
