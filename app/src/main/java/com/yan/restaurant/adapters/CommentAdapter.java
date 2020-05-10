package com.yan.restaurant.adapters;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yan.restaurant.R;
import com.yan.restaurant.bean.CommentBean;

import java.util.List;

public class CommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

	public CommentAdapter(@Nullable List<CommentBean> data) {
		super(R.layout.item_comment, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, CommentBean item) {

	}
}
