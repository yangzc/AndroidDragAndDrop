package com.hyena.dd.core;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by yangzc on 17/4/19.
 */

public class DragAndDropLayout extends RelativeLayout {

    private ViewDragHelper mDragHelper;

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

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, mCallBack);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void onDrop() {
        if (mDragHelper.getCapturedView() != null
                && mDragHelper.getCapturedView() instanceof IDraggable) {
            IDroppable droppable = findDroppable();
            if (droppable != null) {
                droppable.drop((IDraggable) mDragHelper.getCapturedView());
            }
        }
    }

    private IDroppable findDroppable() {
        if (mDragHelper.getCapturedView() != null) {

        }
        return null;
    }

    private ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.min(Math.max(0, left), getWidth() - child.getWidth());
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return Math.min(Math.max(0, top), getHeight() - child.getHeight());
        }
    };

}
