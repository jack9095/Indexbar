package com.fei.sidebardemo.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * URecyclerView with OverScroll effective and you can set overscroll enable of any direction;
 */
public class URecyclerView extends RecyclerView {
    private static final String TAG = "URecyclerView";

    private static final boolean DEBUG = false;

    /**
     * This interpolator is same with androidx.widget.RecyclerView for OverScroll;
     * you should not change it;
     */
    static final Interpolator sQuinticInterpolator = t -> {
        t -= 1.0f;
        return t * t * t * t * t + 1.0f;
    };

    private static final float SPRING_STIFFNESS = 500.0f;

    private boolean mOverScrollEnable = true;

    private boolean mTopOverScrollEnable = true;

    private boolean mBtmOverScrollEnable = true;

    private boolean mLeftOverScrollEnable = true;

    private boolean mRightOverScrollEnable = true;

    private int mPointerIndex;

    private float mLastTouchX;

    private float mLastTouchY;

    private List<OverScrollListener> mOverScrollListeners;

    private final ViewFlinger mViewFlinger;

    // the max fling velocity
    private int mMaxFlingVelocity;

    // the min fling velocity
    private int mMinFlingVelocity;

    private SpringAnimation mSpringAnimation;

    /**
     * An OverScrollListener can be added to a URecyclerView to receive messages
     * when a OverScrolling event has occurred on that URecyclerView.
     *
     * @see #addOnOverScrollListener(OverScrollListener)
     * @see #removeOverScrollListener(OverScrollListener)
     * @see #clearOnOverScrollListener()
     */
    public interface OverScrollListener {
        /**
         * Callback method to be invoked when URecyclerView's OverScroll changes(TranslationX or TranslationY)
         *
         * @param translationX when canScrollVertically, the translationX will be callback. otherwise 0;
         * @param translationY when canScrollHorizontally, the translationY will be callback. otherwise 0;
         */
        void onOverScroll(float translationX, float translationY);
    }

    /**
     * Construct a new URecyclerView with default styling.
     *
     * @param context The Context that will determine this widget's theming.
     */
    public URecyclerView(@NonNull Context context) {
        this(context, null);
    }

    /**
     * Construct a new URecyclerView with default styling, overriding specific style
     * attributes as requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from default styling.
     */
    public URecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Construct a new URecyclerView with a default style determined by the given theme attribute,
     * overriding specific style attributes as requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from the default styling.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     */
    public URecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVelocity(context);
        mViewFlinger = new ViewFlinger();
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void initVelocity(Context context) {
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
    }

    /**
     * Set overScroll enable. If false, RecyclerView cannot overScroll in any edge
     *
     * @param enable true is can overscroll, otherwise not
     */
    public void setOverScrollEnable(boolean enable) {
        mOverScrollEnable = enable;
    }

    /**
     * Whether overscroll is enable
     *
     * @return true is enable otherwise not
     */
    public boolean isOverScrollEnable() {
        return mOverScrollEnable;
    }

    /**
     * Set top edge overscroll enable, if {@link #isOverScrollEnable()} is false, this cannot effect
     *
     * @param enable true is enable otherwise not
     */
    public void setTopOverScrollEnable(boolean enable) {
        mTopOverScrollEnable = enable;
    }

    /**
     * Whether top overscroll enable
     *
     * @return true is enable otherwise not
     */
    public boolean isTopOverScrollEnable() {
        return mTopOverScrollEnable;
    }

    /**
     * Set bottom edge overscroll enable, if {@link #isOverScrollEnable()} is false, this cannot effect
     *
     * @param enable true is enable otherwise not
     */
    public void setBottomOverScrollEnable(boolean enable) {
        mBtmOverScrollEnable = enable;
    }

    /**
     * Whether bottom overscroll enable
     *
     * @return true is enable otherwise not
     */
    public boolean isBottomOverScrollEnable() {
        return mBtmOverScrollEnable;
    }

    /**
     * Set left edge overscroll enable, if {@link #isOverScrollEnable()} is false, this cannot effect
     *
     * @param enable true is enable otherwise not
     */
    public void setLeftOverScrollEnable(boolean enable) {
        mLeftOverScrollEnable = enable;
    }

    /**
     * Whether left overscroll enable
     *
     * @return true is enable otherwise not
     */
    public boolean isLeftOverScrollEnable() {
        return mLeftOverScrollEnable;
    }

    /**
     * Set right edge overscroll enable, if {@link #isOverScrollEnable()} is false, this cannot effect
     *
     * @param enable true is enable otherwise not
     */
    public void setRightOverScrollEnable(boolean enable) {
        mRightOverScrollEnable = enable;
    }

    /**
     * Whether right overscroll enable
     *
     * @return true is enable otherwise not
     */
    public boolean isRightOverScrollEnable() {
        return mRightOverScrollEnable;
    }

    /**
     * Add a listener that will be notified of any changes in OverScroll state or position.
     * Components that add a listener should take care to remove it when finished,please call
     * @see #removeOverScrollListener
     *
     * Other components that take ownership of a view may call
     * @see #clearOnOverScrollListener() to remove all attached listeners.
     * Params: listener – listener to set
     */
    public void addOnOverScrollListener(@NonNull OverScrollListener overScrollListener) {
        if (mOverScrollListeners == null) {
            mOverScrollListeners = new ArrayList<>();
        }
        mOverScrollListeners.add(overScrollListener);
    }

    /**
     * Remove a listener that was notified of any changes in OverScroll state or position.
     *
     * @param overScrollListener listener to set or null to clear
     */
    public void removeOverScrollListener(@NonNull OverScrollListener overScrollListener) {
        if (mOverScrollListeners != null) {
            mOverScrollListeners.remove(overScrollListener);
        }
    }

    /**
     * Remove all secondary listener that were notified of any changes in OverScroll state or position.
     */
    public void clearOnOverScrollListener() {
        if (mOverScrollListeners != null) {
            mOverScrollListeners.clear();
        }
    }

    private void dispatchOverScroll(float translationX, float translationY) {
        if (mOverScrollListeners != null) {
            for (OverScrollListener listener : mOverScrollListeners) {
                listener.onOverScroll(translationX, translationY);
            }
        }
    }

    boolean canScrollVertically() {
        LayoutManager manager = getLayoutManager();
        return manager != null && manager.canScrollVertically();
    }

    boolean canScrollHorizontally() {
        LayoutManager manager = getLayoutManager();
        return manager != null && manager.canScrollHorizontally();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPointerIndex = ev.getPointerId(0);
                onActionDown();
                resetTouchPoint(ev, 0);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;
            default:break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPointerIndex = event.getPointerId(0);
                onActionDown();
                resetTouchPoint(event, 0);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onActionUp();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointerIndex = event.getPointerId(actionIndex);
                onActionDown();
                resetTouchPoint(event, actionIndex);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                // when an item touched, and event dispatched to item so cannot receive touch_down action
                if (mLastTouchX == 0 && mLastTouchY == 0) {
                    resetTouchPoint(event, actionIndex);
                    return super.onTouchEvent(event);
                }
                onActionMove(event);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void resetTouchPoint(MotionEvent event, int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLastTouchX = event.getRawX(index);
            mLastTouchY = event.getRawY(index);
        } else {
            mLastTouchX = event.getRawX();
            mLastTouchY = event.getRawY();
        }
    }

    private void onPointerUp(MotionEvent event) {
        final int actionIndex = event.getActionIndex();
        if (event.getPointerId(actionIndex) == mPointerIndex) {
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mPointerIndex = event.getPointerId(newIndex);
            mLastTouchX = event.getX(newIndex);
            mLastTouchY = event.getY(newIndex);
        }
    }

    private void onActionDown() {
        if (mViewFlinger != null) {
            mViewFlinger.abortAnimation();
        }

        abortSpringAnimation();
    }

    private void onActionMove(MotionEvent event) {
        if (!mOverScrollEnable) {
            return;
        }

        abortSpringAnimation();
        if (mViewFlinger != null) {
            mViewFlinger.abortAnimation();
        }

        final int index = event.findPointerIndex(mPointerIndex);
        if (index < 0) {
            Log.e(TAG, "Error processing scroll, pointer index for id " + mPointerIndex
                    + " not found. Did any MotionEvent get skipped?");
            return;
        }

        if (canScrollVertically()) {
            handleOverScrollVertically(event, index);
        } else if (canScrollHorizontally()) {
            handleOverScrollHorizontally(event, index);
        }
        resetTouchPoint(event, index);
    }

    private void handleOverScrollVertically(MotionEvent event, int index) {
        float curY = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            curY = event.getRawY(index);
        } else {
            curY = event.getRawY();
        }
        int direction = Float.compare(curY, mLastTouchY) > 0 ? -1 : 1;
        boolean canScrollDown = canScrollVertically(-1);
        boolean canScrollUp = canScrollVertically(1);

        float deltaDistance = (curY - mLastTouchY) * 0.5f;
        float translationY = getTranslationY();

        // when overScroll but dragging to reverse direction
        // in this case, the translation should be equal move distance.
        boolean isOverScrollReverseDragging =
                (direction == 1 && translationY > 0) || (direction == -1 && translationY < 0);

        if (DEBUG) {
            Log.d(TAG, "reverse:" + isOverScrollReverseDragging + ",direction:"
                    + direction + ",canScrollUP:" + canScrollUp + ",canScrollDown:"
                    + canScrollDown + ",mLastTouchY:" + mLastTouchY + ",curY:" + curY);
        }

        if (isOverScrollReverseDragging) {
            float newTranslationY = translationY + curY - mLastTouchY;
            // translationY should >= 0;cannot < 0;
            if (direction == 1) {
                newTranslationY = Math.max(0, newTranslationY);
            } else {
                newTranslationY = Math.min(0, newTranslationY);
            }
            setTranslationYAndDispatch(newTranslationY);
        }
        if (direction == -1 && !canScrollDown && mTopOverScrollEnable) {
            setTranslationYAndDispatch(translationY + deltaDistance);
        } else if (direction == 1 && !canScrollUp && mBtmOverScrollEnable) {
            setTranslationYAndDispatch(translationY + deltaDistance);
        }
    }

    private void handleOverScrollHorizontally(MotionEvent event, int index) {
        float curX = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            curX = event.getRawX(index);
        } else {
            curX = event.getRawX();
        }
        int direction = Float.compare(curX, mLastTouchX) > 0 ? -1 : 1;
        boolean canScrollRight = canScrollHorizontally(-1);
        boolean canScrollLeft = canScrollHorizontally(1);
        float deltaDistance = (curX - mLastTouchX) * 0.5f;
        float translationX = getTranslationX();

        // when overScroll but dragging to reverse direction
        // in this case, the translation should be equal move distance.
        boolean isOverScrollReverseDragging =
                (direction == 1 && translationX >0) || (direction == -1 && translationX < 0);
        if (isOverScrollReverseDragging) {
            float newTranslationX = translationX + curX - mLastTouchX;
            if (direction == 1) {
                newTranslationX = Math.max(0, newTranslationX);
            } else {
                newTranslationX = Math.min(0, newTranslationX);
            }
            setTranslationXAndDispatch(newTranslationX);
        }
        if (direction == -1 && !canScrollRight && mLeftOverScrollEnable) {
            setTranslationXAndDispatch(translationX + deltaDistance);
        } else if (direction == 1 && !canScrollLeft && mRightOverScrollEnable) {
            setTranslationXAndDispatch(translationX + deltaDistance);
        }
    }

    private void onActionUp() {
        if (getTranslationY() != 0.0f) {
            createSpringAnimation().start();
        } else if (getTranslationX() != 0.0f) {
            createSpringAnimation().start();
        }
        mLastTouchX = mLastTouchY = 0;
    }

    private void setTranslationXAndDispatch(float translationX) {
        setTranslationX(translationX);
        dispatchOverScroll(translationX, getTranslationY());
    }

    private void setTranslationYAndDispatch(float translationY) {
        setTranslationY(translationY);
        dispatchOverScroll(getTranslationX(), translationY);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        boolean canScroll = canScrollVertically() || canScrollHorizontally();
        if ((velocityY < 0 && !mTopOverScrollEnable) || (velocityY > 0 && !mBtmOverScrollEnable)) {
            return super.fling(velocityX, velocityY);
        }
        if ((velocityX < 0 && !mLeftOverScrollEnable) || (velocityX > 0 && !mRightOverScrollEnable)) {
            return super.fling(velocityX, velocityY);
        }
        if (!canScrollHorizontally() || Math.abs(velocityX) < mMinFlingVelocity) {
            velocityX = 0;
        }

        if (!canScrollVertically() || Math.abs(velocityY) < mMinFlingVelocity) {
            velocityY = 0;
        }

        if ((velocityX !=0 || velocityY !=0) && canScroll) {
            int newVelocityX = Math.max(-mMaxFlingVelocity, Math.min(velocityX, mMaxFlingVelocity));
            int newVelocityY = Math.max(-mMaxFlingVelocity, Math.min(velocityY, mMaxFlingVelocity));
            mViewFlinger.fling(newVelocityX, newVelocityY);
        }
        return super.fling(velocityX, velocityY);
    }

    private SpringAnimation createSpringAnimation() {
        if (mSpringAnimation != null) {
            return mSpringAnimation;
        }
        SpringForce springForce = new SpringForce(0)
                .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
                .setStiffness(SPRING_STIFFNESS);

        mSpringAnimation = new SpringAnimation(this,
                canScrollVertically() ? SpringAnimation.TRANSLATION_Y : SpringAnimation.TRANSLATION_X)
                .setSpring(springForce);
        mSpringAnimation.addUpdateListener((animation, value, velocity)
                -> dispatchOverScroll(getTranslationX(), getTranslationY()));
        return mSpringAnimation;
    }

    private void abortSpringAnimation() {
        if (mSpringAnimation != null && mSpringAnimation.isRunning()) {
            mSpringAnimation.cancel();
        }
    }

    private void notifyOverScroll(float velocity) {
        if (!mOverScrollEnable) {
            return;
        }
        if (Math.abs(velocity) < mMinFlingVelocity) {
            Log.w(TAG, "velocity minus minVelocity:" + mMinFlingVelocity);
            return;
        }
        createSpringAnimation().setStartVelocity(velocity).start();
    }

    /**
     * This Flinger copy from {@link RecyclerView}, Imitate the fling scroll
     *
     * and get the velocity when stop scroll;
     */
    class ViewFlinger implements Runnable {
        private final OverScroller mOverScroller;
        private boolean mHasAddStateChangeListener;

        private final OnScrollListener mListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (canScrollVertically()) {
                        scrollVerticalStateChanged();
                    } else if (canScrollHorizontally()) {
                        scrollHorizontalStateChanged();
                    } else {
                        // do nothing
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };

        ViewFlinger() {
            mOverScroller = new OverScroller(getContext(), sQuinticInterpolator);
        }

        private void scrollVerticalStateChanged() {
            if (!mOverScroller.isFinished()) {
                int startY = mOverScroller.getStartY();
                int currY = mOverScroller.getCurrY();
                boolean scrollDown = startY > currY;
                float velocity = mOverScroller.getCurrVelocity();
                notifyOverScroll(scrollDown ? velocity : -1 * velocity);
            }
        }

        private void scrollHorizontalStateChanged() {
            int startX = mOverScroller.getStartX();
            int currX = mOverScroller.getCurrX();
            boolean scrollRight = startX > currX;
            float velocity = mOverScroller.getCurrVelocity();
            notifyOverScroll(scrollRight ? velocity : -1 * velocity);
        }

        @Override
        public void run() {
            final OverScroller scroller = mOverScroller;
            if (scroller.computeScrollOffset()) {
                postOnAnimation();
            }
        }

        void fling(int velocityX, int velocityY) {
            if (mOverScroller.computeScrollOffset() || !mOverScroller.isFinished()) {
                mOverScroller.abortAnimation();
            }
            mOverScroller.fling(0, 0, velocityX, velocityY,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
            if (!mHasAddStateChangeListener) {
                URecyclerView.this.addOnScrollListener(mListener);
                mHasAddStateChangeListener = true;
            }
        }

        void postOnAnimation() {
            removeCallbacks(this);
            URecyclerView.this.postOnAnimation(this);
        }

        void abortAnimation() {
            URecyclerView.this.removeOnScrollListener(mListener);
            mHasAddStateChangeListener = false;
            if (!mOverScroller.isFinished()) {
                mOverScroller.abortAnimation();
            }
        }
    }
}
