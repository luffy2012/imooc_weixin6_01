package com.imooc.weixin6_0.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.imooc.weixin6_0.R;


import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshListView extends ListView implements OnScrollListener {
	View header;// 顶部布局文件；
	int headerHeight;// 顶部布局文件的高度；
	int firstVisibleItem;// 当前第一个可见的item的位置；
	int scrollState;// listview 当前滚动状态；
	boolean isRemark;// 标记，当前是在listview最顶端摁下的；
	int startY;// 摁下时的Y值；
	int state;// 当前的状态；
	final int NONE = 0;// 正常状态；
	final int PULL = 1;// 提示下拉状态；
	final int RELESE = 2;// 提示释放状态；
	final int REFLASHING = 3;// 刷新状态；

	private static final int TAP_TO_LOADMORE = 5;			// 未加载更多
	private static final int LOADING = 6;					// 正在加载

	IReflashListener iReflashListener;//刷新数据的接口


	//底部布局加载更多
	private int mLoadState;									// 加载状态
	private RelativeLayout mLoadMoreFooterView;				// 加载更多
	private TextView mLoadMoreText;							// 提示文本
	private ProgressBar mLoadMoreProgress;					// 加载更多进度条


	public RefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 初始化界面，添加顶部布局文件到 listview
	 *
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.header_layout, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		Log.i("tag", "headerHeight = " + headerHeight);
		topPadding(-headerHeight);
		this.addHeaderView(header);
		this.setOnScrollListener(this);

		//底部布局
		mLoadMoreFooterView = (RelativeLayout) inflater.inflate(
				R.layout.loadmore_footer, this, false);
		mLoadMoreText = (TextView) mLoadMoreFooterView.findViewById(R.id.loadmore_text);
		mLoadMoreProgress = (ProgressBar) mLoadMoreFooterView.findViewById(R.id.loadmore_progress);
		mLoadMoreFooterView.setOnClickListener(new OnClickLoadMoreListener());
		mLoadState = TAP_TO_LOADMORE;
		addFooterView(mLoadMoreFooterView);			// 增加尾部视图


	}


	/**
	 *
	 * @author wwj
	 * 加载更多
	 */
	private class OnClickLoadMoreListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(mLoadState != LOADING) {
				prepareForLoadMore();
				OnLoadMore();
			}
		}
	}

	/**为加载更多做准备**/
	public void prepareForLoadMore() {
		mLoadMoreProgress.setVisibility(View.VISIBLE);
		mLoadMoreText.setText(R.string.loading_label);
		mLoadState = LOADING;
	}

	public void OnLoadMore() {
		if(iReflashListener != null) {
			this.setSelection(this.getBottom());
			iReflashListener.onLoadMore();
		}
	}


	/**
	 * 通知父布局，占用的宽，高；
	 *
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	/**
	 * 设置header 布局 上边距；
	 *
	 * @param topPadding
	 */
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding,
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.firstVisibleItem = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		this.scrollState = scrollState;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstVisibleItem == 0) {
					isRemark = true;
					startY = (int) ev.getY();
				}
				break;

			case MotionEvent.ACTION_MOVE:
				onMove(ev);
				break;
			case MotionEvent.ACTION_UP:
				if (state == RELESE) {
					state = REFLASHING;
					// 加载最新数据；
					reflashViewByState();
					iReflashListener.onRefresh();
				} else if (state == PULL) {
					state = NONE;
					isRemark = false;
					reflashViewByState();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 判断移动过程操作；
	 *
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY;
		int topPadding = space - headerHeight;
		switch (state) {
			case NONE:
				if (space > 0) {
					state = PULL;
					reflashViewByState();
				}
				break;
			case PULL:
				topPadding(topPadding);
				if (space > headerHeight + 15
						&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
					state = RELESE;
					reflashViewByState();
				}
				break;
			case RELESE:
				topPadding(topPadding);
				if (space < headerHeight + 15) {
					state = PULL;
					reflashViewByState();
				} else if (space <= 0) {
					state = NONE;
					isRemark = false;
					reflashViewByState();
				}
				break;
		}
	}

	/**
	 * 根据当前状态，改变界面显示；
	 */
	private void reflashViewByState() {
		TextView tip = (TextView) header.findViewById(R.id.tip);
		ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
		ProgressBar progress = (ProgressBar) header.findViewById(R.id.progress);
		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch (state) {
			case NONE:
				arrow.clearAnimation();
				topPadding(-headerHeight);
				break;

			case PULL:
				arrow.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				tip.setText("下拉可以刷新！");
				arrow.clearAnimation();
				arrow.setAnimation(anim1);
				break;
			case RELESE:
				arrow.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				tip.setText("松开可以刷新！");
				arrow.clearAnimation();
				arrow.setAnimation(anim);
				break;
			case REFLASHING:
				topPadding(50);
				arrow.setVisibility(View.GONE);
				progress.setVisibility(View.VISIBLE);
				tip.setText("正在刷新...");
				arrow.clearAnimation();
				break;
		}
	}

	/**
	 * 获取完数据；
	 */
	public void reflashComplete() {
		state = NONE;
		isRemark = false;
		reflashViewByState();
		TextView lastupdatetime = (TextView) header
				.findViewById(R.id.lastupdate_time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastupdatetime.setText(time);
	}


	public void onLoadMoreComplete() {//加载完成
		resetFooter();
	}

	/**
	 * 重设ListView尾部视图为初始状态
	 */
	private void resetFooter() {
		if(mLoadState != TAP_TO_LOADMORE) {
			mLoadState = TAP_TO_LOADMORE;

			// 进度条设置为不可见
			mLoadMoreProgress.setVisibility(View.GONE);
			// 按钮的文本替换为“加载更多”
			mLoadMoreText.setText(R.string.loadmore_label);
		}

	}

	public void setInterface(IReflashListener iReflashListener){
		this.iReflashListener = iReflashListener;
	}
	/**
	 * 刷新数据接口
	 * @author Administrator
	 */
	public interface IReflashListener{
		public void onRefresh();
		public void onLoadMore();
	}
}
