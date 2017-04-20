package com.hyena.dd.core;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzc on 17/4/19.
 */

public class DragAndDropLayout extends RelativeLayout {

    private static final int STATUS_IDLE = 1;
    private static final int STATUS_DRAG = 2;

    private static int mMinDistance;
    private float mLastX, mLastY;
    private int mStatus = STATUS_IDLE;

    private List<View> mDroppableViews = new ArrayList<View>();

    private IDraggable mDraggable;
    private IDraggableGhost mGhostDraggable;
    private IDroppable mDroppable;

    public DragAndDropLayout(Context context) {
        super(context);
        init();
    }

    public DragAndDropLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragAndDropLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinDistance = configuration.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                this.mLastX = ev.getX();
                this.mLastY = ev.getY();
                //获得触摸到的可拖拽对象
                mDraggable = findTopChildUnder(this, ev.getX(), ev.getY());
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float xDis = (ev.getX() - mLastX);
                float yDis = (ev.getY() - mLastY);
                if (mDraggable != null && xDis * xDis + yDis * yDis > mMinDistance * mMinDistance) {
                    mGhostDraggable = mDraggable.toGhost();//当前可拖拽对象转化成幽灵状态
                    makeGhostPosition(mGhostDraggable);
                    reCollectDroppables();
                    mStatus = STATUS_DRAG;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mStatus = STATUS_IDLE;
                break;
            }
        }
        return mStatus == STATUS_DRAG;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();
                onTouchMove(x, y);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                onTouchUp();
                mStatus = STATUS_IDLE;
                break;
            }
        }
        return mStatus == STATUS_DRAG;
    }

    private void onTouchMove(float x, float y) {
        float disX = x - mLastX;
        float disY = y - mLastY;
        if (mGhostDraggable != null && mGhostDraggable instanceof View) {
            View view = (View) mGhostDraggable;
            RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
            setGhostPosition(params.leftMargin + disX, params.topMargin + disY);
            IDroppable droppable = checkHitDroppable();
            if (droppable != mDroppable) {
                //捕获到可放置对象
                onCaptureDroppable(droppable);
                this.mDroppable = droppable;
            }
        }
        this.mLastX = x;
        this.mLastY = y;
    }

    private void onTouchUp() {
        if (mGhostDraggable == null)
            return;

        if (mDroppable != null) {
            IDraggable captured = mDroppable.getCapturedDraggable();
            if (captured != null) {
                captured.reset();//状态还原
            }
            mDroppable.captureDraggable(mGhostDraggable.getNatureDraggable());
            removeGhostView();
        } else {
            ghostFlyBack();
        }
    }

    private void ghostFlyBack() {
        final int startX = ((View)mGhostDraggable).getLeft();
        final int startY = ((View)mGhostDraggable).getTop();
        int xy[] = getLocationInThis((View) mGhostDraggable.getNatureDraggable());
        final int endX = xy[0];
        final int endY = xy[1];


        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                setGhostPosition((endX - startX) * value + startX, (endY - startY) * value + startY);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                removeGhostView();
                if (mGhostDraggable != null) {
                    mGhostDraggable.getNatureDraggable().reset();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private void setGhostPosition(float x, float y) {
        if (mGhostDraggable != null && mGhostDraggable instanceof View) {
            View view = (View) mGhostDraggable;
            RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.leftMargin = (int) Math.min(Math.max(0, x), getWidth() - view.getWidth());
            params.topMargin = (int) Math.min(Math.max(0, y), getWidth() - view.getWidth());
            requestLayout();
        }
    }

    private void onCaptureDroppable(IDroppable droppable) {
        if (mDroppable != null) {
            mDroppable.setCapturing(false);
        }
        this.mDroppable = droppable;
        if (mDroppable != null) {
            mDroppable.setCapturing(true);
        }
    }

    private IDroppable checkHitDroppable() {
        if (mGhostDraggable != null && mDroppableViews != null
                && !mDroppableViews.isEmpty()) {
            View ghostView = (View) mGhostDraggable;

            Rect ghostRect = new Rect(ghostView.getLeft(), ghostView.getTop(),
                    ghostView.getLeft() + ghostView.getWidth(),
                    ghostView.getTop() + ghostView.getHeight());
            for (int i = 0; i < mDroppableViews.size(); i++) {
                View droppable = mDroppableViews.get(i);
                int xy[] = getLocationInThis(droppable);
                Rect rect = new Rect(xy[0], xy[1], xy[0] + droppable.getWidth(),
                        xy[1] + droppable.getHeight());
                if (rect.intersect(ghostRect)) {
                    return (IDroppable) droppable;
                }
            }
        }
        return null;
    }

    private void reCollectDroppables() {
        mDroppableViews.clear();
        collectDroppables(mDroppableViews, this);
    }

    private void collectDroppables(List<View> droppableViews, View view) {
        if (view instanceof IDroppable) {
            droppableViews.add(view);
        }
        if (view instanceof ViewGroup) {
            final int childCount = ((ViewGroup) view).getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = ((ViewGroup) view).getChildAt(i);
                collectDroppables(droppableViews, child);
            }
        }
    }

    protected void makeGhostPosition(IDraggableGhost ghostView) {
        if (!(ghostView instanceof View))
            return;

        if (ghostView.getNatureDraggable() != null
                && ghostView.getNatureDraggable() instanceof View) {
            View natureView = (View) ghostView.getNatureDraggable();
            int xy[] = getLocationInThis(natureView);
            //添加ghost
            addGhostView((View) ghostView, xy, natureView.getWidth(), natureView.getHeight());
        }
    }

    private void addGhostView(View view, int xy[], int width, int height) {
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = xy[0];
        params.topMargin = xy[1];
        addView(view, params);
    }

    public void flyBack(IDroppable droppable) {
        if (droppable != null && droppable.getCapturedDraggable() != null) {
            int xy[] = getLocationInThis((View) droppable);
            View ghostView = (View) droppable.getCapturedDraggable().toGhost();
            View draggableView = (View) droppable.getCapturedDraggable();
            addGhostView(ghostView, xy, draggableView.getWidth(), draggableView.getHeight());
            ghostFlyBack();
            droppable.captureDraggable(null);
        }
    }

    private void removeGhostView() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof IDraggableGhost) {
                removeView(child);
                break;
            }
        }
    }

    public IDraggable findTopChildUnder(View view, float x, float y) {
        if (view instanceof IDraggable) {
            int xy[] = getLocationInThis(view);
            if (x >= xy[0] && x < xy[0] + view.getWidth()
                    && y >= xy[1] && y < xy[1] + view.getHeight()) {
                return (IDraggable) view;
            }
        }

        if (view instanceof ViewGroup) {
            final int childCount = ((ViewGroup) view).getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = ((ViewGroup) view).getChildAt(i);
                IDraggable result = findTopChildUnder(child, x, y);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private int[] getLocationInThis(View view) {
        int location[] = new int[2];
        View v = view;
        do {
            location[0] += v.getLeft();
            location[1] += v.getTop();
            v = (View) v.getParent();
        } while (v != this);
        return location;
    }

}
