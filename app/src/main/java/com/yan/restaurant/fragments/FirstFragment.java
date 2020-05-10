package com.yan.restaurant.fragments;

import android.os.Bundle;
import com.yan.restaurant.R;
import com.yan.restaurant.Views.ListContainer;
import com.yan.restaurant.activity.HomeActivity;
import com.yan.restaurant.adapters.FoodAdapter;
import com.yan.restaurant.adapters.TypeAdapter;
import com.shizhefei.fragment.LazyFragment;

public class FirstFragment extends LazyFragment {

	private ListContainer listContainer;

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_first);
		listContainer = (ListContainer) findViewById(R.id.listcontainer);
		listContainer.setAddClick((HomeActivity) getActivity());
	}

	public FoodAdapter getFoodAdapter() {
		return listContainer.foodAdapter;
	}

	public TypeAdapter getTypeAdapter() {
		return listContainer.typeAdapter;
	}

}
