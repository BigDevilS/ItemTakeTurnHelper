package com.bigdevil.taketurnhelper;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static android.content.Context.WINDOW_SERVICE;

public class TakeTurnHelper {
    private RecyclerView mRecyclerView;

    private int mRecyclerViewWidth;

    private LinearLayoutManager mLayoutManager;

    private Point mWindowSize = new Point();

    private Rect mBounds = new Rect();

    private boolean mIsDoingAnimation;

    private int mLeft;

    private int mRight;

    private int mChildCount;

    private int mUnitOffset;

    private boolean mIsSupportLeft = true;

    private boolean mIsSupportRight;

    private boolean mIsSupportIn = true;

    private boolean mIsSupportOut;

    private boolean mIsEntering;

    private boolean mShouldDestroy;

    private int mLeftMargin;

    private int mRightMargin;

    private List<View> mChildList = new ArrayList<>();

    private ViewPager.OnPageChangeListener mViewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            doScrollAnimation();
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public enum ScrollDirection {
        LEFT, // 从右到左
        RIGHT, // 从左到又
        BOTH // 都支持
    }

    public enum Mode {
        IN, // 入场
        OUT, // 出场
        BOTH // 都支持
    }

    public TakeTurnHelper(Context context) {
        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(mWindowSize);
    }

    public void setParent(ViewPager parent) {
        parent.addOnPageChangeListener(mViewPagerListener);
    }

    private void doScrollAnimation() {
        // 如果被回收了，那么根布局的parent为null,否则为ViewRootImpl
        if (mRecyclerView.getRootView().getParent() == null) {
            return;
        }

        boolean isInScreen = mRecyclerView.getGlobalVisibleRect(mBounds);

        if (!isInScreen) {
            // 仅支持out动画时，完全移出屏幕需要重置位置
            if (mIsSupportOut && !mIsSupportIn && !mIsEntering) {
                resetChild();
            }
            mIsDoingAnimation = false;
            mIsEntering = true;
            destroyIfNeed();
            return;
        }

        getLeftAndRight();

        // RecyclerView完全进入屏幕
        if (mLeft >= 0 && mRight <= mWindowSize.x) {
            mIsDoingAnimation = false;
            mIsEntering = false;
            destroyIfNeed();
            return;
        }

        if (!shouldDoAnimation()) {
            return;
        }

        // 状态改变，重新获取子view
        if (!mIsDoingAnimation) {
            getChildList();
            if (mChildCount == 0) {
                return;
            }
            getMargin();
            mUnitOffset = (mRecyclerViewWidth - mLeftMargin - mRightMargin) / mChildCount;
            mIsDoingAnimation = true;
        }
        if (mLeft <= mWindowSize.x && mRight >= mWindowSize.x && mIsSupportLeft) {
            rightToLeft();
        } else if (mLeft <= 0 && mRight >= 0 && mIsSupportRight) {
            leftToRight();
        }
    }

    /**
     * 向左滑动
     */
    private void rightToLeft() {
        relayoutChild(mWindowSize.x - mLeft, true);
    }

    /**
     * 向右滑动
     */
    private void leftToRight() {
        relayoutChild(mRight, false);
    }

    private boolean shouldDoAnimation() {
        if (mIsDoingAnimation) {
            return true;
        }
        return mIsEntering ? mIsSupportIn : mIsSupportOut;
    }

    private void getChildList() {
        mChildList.clear();
        mChildCount = mLayoutManager.getChildCount();
        for (int i = 0; i < mChildCount; i++) {
            mChildList.add(mLayoutManager.getChildAt(i));
        }
    }

    private void relayoutChild(int dx, boolean isRightToLeft) {
        if (dx > mRecyclerViewWidth) {
            return;
        }

        float offset;

        // 让index为0的子View也有偏移量
        for (int i = 1; i <= mChildCount; i++) {
            // 稍微多偏移一点 不然太丑
            offset = (mUnitOffset * i - dx) * 1.5f;
            if (offset < mLeftMargin) {
                offset = mLeftMargin;
            }

            if (isRightToLeft) {
                mChildList.get(i - 1).setX(offset);
            } else {
                // 反向偏移，需要加上两个marginLeft
                mChildList.get(i - 1).setX(-offset + 2 * mLeftMargin);
            }
        }
        mRecyclerView.requestLayout();
    }

    private void resetChild() {
        for (int i = 0; i < mChildCount; i++) {
            mChildList.get(i).setX(mLeftMargin);
        }
        mRecyclerView.requestLayout();
    }


    /**
     * 计算出左右的坐标，getGlobalVisibleRect获取的是进入屏幕内的左右坐标
     */
    private void getLeftAndRight() {
        if (mBounds.left < mWindowSize.x && mBounds.right >= mWindowSize.x) {
            mLeft = mBounds.left;
            mRight = mLeft + mRecyclerViewWidth;
        } else if (mBounds.left <= 0 && mBounds.right > 0) {
            mRight = mBounds.right;
            mLeft = mRight - mRecyclerViewWidth;
        }
    }

    private void getMargin() {
        ViewGroup.LayoutParams params = mChildList.get(0).getLayoutParams();
        if (params instanceof ViewGroup.MarginLayoutParams) {
            mLeftMargin = ((ViewGroup.MarginLayoutParams) params).leftMargin;
            mRightMargin = ((ViewGroup.MarginLayoutParams) params).rightMargin;
        } else {
            mLeftMargin = 0;
            mRightMargin = 0;
        }
    }

    public void setSupportScrollDirection(ScrollDirection scrollDirection) {
        mIsSupportLeft = scrollDirection == ScrollDirection.LEFT || scrollDirection == ScrollDirection.BOTH;
        mIsSupportRight = scrollDirection == ScrollDirection.RIGHT || scrollDirection == ScrollDirection.BOTH;
    }

    public void setSupportMode(Mode mode) {
        mIsSupportIn = mode == Mode.IN || mode == Mode.BOTH;
        mIsSupportOut = mode == Mode.OUT || mode == Mode.BOTH;
    }

    /**
     * 设置需要设置入场动画的RecyclerView
     *
     * @param targetRecyclerView 目标RecyclerView
     */
    public void setTargetRecyclerView(RecyclerView targetRecyclerView) {
        mRecyclerView = targetRecyclerView;
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewWidth = mRecyclerView.getWidth();
            }
        });
        mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
    }

    private void destroyIfNeed() {
        if (mShouldDestroy) {
            destroy();
        }
    }

    private void destroy() {
        mRecyclerView = null;
        mLayoutManager = null;
    }

    public void onDestroy() {
        if (mIsDoingAnimation) {
            mShouldDestroy = true;
        } else {
            destroy();
        }
    }
}
