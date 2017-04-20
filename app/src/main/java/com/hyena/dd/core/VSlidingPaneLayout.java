//package com.baidu.music.ui.base;
//
//import android.content.Context;
//import android.support.v4.view.ViewCompat;
//import android.support.v4.widget.ViewDragHelper;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import com.baidu.music.framework.clientlog.LogUtil;
//import com.baidu.music.ui.base.HSlidingPaneLayout.PanelSlideListener;
//
///**
// * 上下滑动
// * @author yangzc
// *
// */
//public class VSlidingPaneLayout extends ViewGroup {
//
//    private static final String TAG = "VSlidingPaneLayout";
//
//    private float mMinVelocity = 0;
//
//    private View mHandleView;
//    private ViewDragHelper mDragHelper;
//	private float mSlideOffset = 0;//偏移量
//	private boolean mDragable = true;
//
//	private SlideState mSlideState = SlideState.OPENED;
//	public static enum SlideState {
//        OPENED,
//        CLOSED,
//    }
//
//	public VSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	public VSlidingPaneLayout(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public VSlidingPaneLayout(Context context) {
//		super(context);
//		init();
//	}
//
//	/**
//	 * 设置拖拽手柄
//	 * @param handleView
//	 */
//	public void setHandleView(View handleView){
//		this.mHandleView = handleView;
//		DisplayMetrics metrics = getResources().getDisplayMetrics();
//		mHandleView.offsetTopAndBottom(metrics.heightPixels);
//	}
//
//	/**
//	 * 初始化Layout
//	 */
//	private void init(){
//		mDragHelper = ViewDragHelper.create(this, 0.5f, new DragCallBack());
//		final float density = getResources().getDisplayMetrics().density;
//		mDragHelper.setMinVelocity(400 * density);
//		mMinVelocity = density * 1500;
//	}
//
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		LogUtil.v(TAG, "onLayout");
//		final int childCount = getChildCount();
//		for(int i=0; i< childCount; i++){
//			final View child = getChildAt(i);
//			if (child.getVisibility() == View.GONE){
//				continue;
//			}
//			child.layout(0, 0, getWidth(), getHeight());
//		}
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int measuredWidth = resolveSize(widthMeasureSpec, widthMeasureSpec);
//		int measuredHeight = resolveSize(heightMeasureSpec, heightMeasureSpec);
//		measureChildren(widthMeasureSpec, heightMeasureSpec);
//		setMeasuredDimension(measuredWidth, measuredHeight);
//	}
//
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		if(!dragable()){
//			return super.onInterceptTouchEvent(ev);
//		}
//		try{
//			return mDragHelper.shouldInterceptTouchEvent(ev);
//		}catch(Throwable e){
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		if(!dragable()){
//			return super.onTouchEvent(ev);
//		}
//		mDragHelper.processTouchEvent(ev);
//		return true;
//	}
//
//	@Override
//	public void computeScroll() {
//		try {
//			if(mDragHelper.continueSettling(true)){
//				ViewCompat.postInvalidateOnAnimation(this);
//			}
//		} catch (Exception e) {
//			LogUtil.e(TAG, "", e);
//		}
//	}
//
//	/**
//	 * 是否可以拖拽
//	 * @return
//	 */
//	public boolean dragable(){
//		return mHandleView != null && mDragable;
//	}
//
//	/**
//	 * 设置是否可以拖拽
//	 * @param dragable
//	 */
//	public void setDragable(boolean dragable){
//		this.mDragable = dragable;
//	}
//
//	/**
//	 * 发生拖拽事件
//	 * @param changedView
//	 * @param top
//	 */
//	private void onPanelDragged(View changedView, int top){
//		mSlideOffset = 1.0f - Math.abs(top - getHeight() + 0.0f)/getHeight();
////		Log.v(TAG, "onPanelDragged, mSlideOffset: " + mSlideOffset);
//		if(mPanelSlideListener != null){
//			mPanelSlideListener.onPanelSlide(mHandleView, mSlideOffset);
//		}
//	}
//
//	private PanelSlideListener mPanelSlideListener;
//
//	/**
//	 * 设置滑动监听器
//	 * @param panelSlideListener
//	 */
//	public void setPanelSlideListener(PanelSlideListener panelSlideListener){
//		this.mPanelSlideListener = panelSlideListener;
//	}
//
//	/**
//	 * 打开主场景
//	 */
//	public void openPane(){
//		mDragHelper.smoothSlideViewTo(mHandleView, mHandleView.getLeft(), getHeight());
//		invalidate();
//		mSlideState = SlideState.OPENED;
//	}
//
//	/**
//	 * 关闭主场景
//	 */
//	public void closePane(){
//		mDragHelper.smoothSlideViewTo(mHandleView, mHandleView.getLeft(), 0);
//		invalidate();
//		mSlideState = SlideState.CLOSED;
//	}
//
//	/**
//	 * 是否已经打开主场景
//	 * @return
//	 */
//	public boolean isOpened(){
//		return mSlideState == SlideState.OPENED;
//	}
//
//	/**
//	 * 获得状态
//	 * @return
//	 */
//	public SlideState getSlideState(){
//		return mSlideState;
//	}
//
//	/**
//	 * 设置状态
//	 * @param state
//	 */
//	public void setSlideState(SlideState state){
//		this.mSlideState = state;
//	}
//
//	/**
//	 * 拖拽状态回调
//	 * @author yangzc
//	 */
//	private class DragCallBack extends ViewDragHelper.Callback {
//
//		@Override
//		public boolean tryCaptureView(View child, int pointerId) {
//			return mHandleView == child;
//		}
//
//		@Override
//		public void onViewDragStateChanged(int state) {
//			LogUtil.v(TAG, "onViewDragStateChanged, state: " + state + ", mSlideOffset: " + mSlideOffset);
//			//空闲状态时
//			if(mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE){
//				if(mSlideOffset < 0.5f){
//					if (mPanelSlideListener != null) {
//			            mPanelSlideListener.onPanelClosed(mHandleView);
//			        }
//					mSlideState = SlideState.CLOSED;
//					if(mSlideOffset != 0){
//						closePane();
//						mSlideOffset = 0;
//					}
//				}else{
//					if (mPanelSlideListener != null) {
//			            mPanelSlideListener.onPanelOpened(mHandleView);
//			        }
//					mSlideState = SlideState.OPENED;
//					if(mSlideOffset != 1){
//						openPane();
//						mSlideOffset = 1;
//					}
//				}
//			}
//		}
//
//		@Override
//		public void onViewPositionChanged(View changedView, int left, int top,
//				int dx, int dy) {
//			Log.v(TAG, "onViewPositionChanged, top: " + top);
//			onPanelDragged(changedView,top);
//			invalidate();
//		}
//
//		@Override
//		public void onViewReleased(View releasedChild, float xvel, float yvel) {
//			Log.v(TAG, "onViewReleased, xvel: " + xvel + ", yvel: " + yvel);
//			int top = 0;
//			if(yvel > mMinVelocity){//向下
//				top = getHeight();
//			}else if(yvel < -mMinVelocity){
//				top = 0;
//			}else if(mSlideOffset > 0.5f){
//				top = getHeight();
//			}
//			mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
//			invalidate();
//		}
//
//		@Override
//		public int getViewVerticalDragRange(View child) {
//			return getHeight();
//		}
//
//		@Override
//		public int clampViewPositionVertical(View child, int top, int dy) {
//			return Math.min(Math.max(top, 0), getHeight());
//		}
//	}
//}
