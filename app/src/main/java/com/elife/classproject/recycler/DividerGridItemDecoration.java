package com.elife.classproject.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 
 * @author tzhang
 * 
 *         主要在getItemOffsets方法中，去判断如果是最后一行，则不需要绘制底部；如果是最后一列，则不需要绘制右边，
 *         整个判断也考虑到了StaggeredGridLayoutManager的横向和纵向
 *         ，所以稍稍有些复杂。最重要还是去理解，如何绘制什么的不重要。一般如果仅仅是希望有空隙，还是去设置item的margin方便
 * 
 *         如果我们有这么个需求，纵屏的时候显示为ListView，横屏的时候显示两列的GridView，我们RecyclerView可以轻松搞定
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {
	private static final int[] ATTRS = new int[] { android.R.attr.listDivider };
	private Drawable mDivider;

	public DividerGridItemDecoration(Context context) {
		final TypedArray a = context.obtainStyledAttributes(ATTRS);
		mDivider = a.getDrawable(0);
		a.recycle();
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, State state) {

		drawHorizontal(c, parent);
		drawVertical(c, parent);

	}

	private int getSpanCount(RecyclerView parent) {
		// 列数
		int spanCount = -1;
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {

			spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			spanCount = ((StaggeredGridLayoutManager) layoutManager)
					.getSpanCount();
		}
		return spanCount;
	}

	public void drawHorizontal(Canvas c, RecyclerView parent) {
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
					.getLayoutParams();
			final int left = child.getLeft() - params.leftMargin;
			final int right = child.getRight() + params.rightMargin
					+ mDivider.getIntrinsicWidth();
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	public void drawVertical(Canvas c, RecyclerView parent) {
		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);

			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
					.getLayoutParams();
			final int top = child.getTop() - params.topMargin;
			final int bottom = child.getBottom() + params.bottomMargin;
			final int left = child.getRight() + params.rightMargin;
			final int right = left + mDivider.getIntrinsicWidth();

			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
			int childCount) {
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
			{
				return true;
			}
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			int orientation = ((StaggeredGridLayoutManager) layoutManager)
					.getOrientation();
			if (orientation == StaggeredGridLayoutManager.VERTICAL) {
				if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
				{
					return true;
				}
			} else {
				childCount = childCount - childCount % spanCount;
				if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
					return true;
			}
		}
		return false;
	}

	private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
			int childCount) {
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			childCount = childCount - childCount % spanCount;
			if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
				return true;
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			int orientation = ((StaggeredGridLayoutManager) layoutManager)
					.getOrientation();
			// StaggeredGridLayoutManager 且纵向滚动
			if (orientation == StaggeredGridLayoutManager.VERTICAL) {
				childCount = childCount - childCount % spanCount;
				// 如果是最后一行，则不需要绘制底部
				if (pos >= childCount)
					return true;
			} else
			// StaggeredGridLayoutManager 且横向滚动
			{
				// 如果是最后一行，则不需要绘制底部
				if ((pos + 1) % spanCount == 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void getItemOffsets(Rect outRect, int itemPosition,
			RecyclerView parent) {
		int spanCount = getSpanCount(parent);
		int childCount = parent.getAdapter().getItemCount();
		if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
		{
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		} else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
		{
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
		} else {
			outRect.set(0, 0, mDivider.getIntrinsicWidth(),
					mDivider.getIntrinsicHeight());
		}
	}

}
