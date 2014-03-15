package com.mdareports.ui.drawer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdareports.R;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.FontTypeFaceManager;
import com.mdareports.utils.FontTypeFaceManager.CustomFonts;

public class DrawerItemsAdapter extends BaseAdapter {

	static class ViewHolder {
		ImageView imgDrawerItemIcon;
		TextView tvDrawerItemDescription;
	}

	private List<DrawerItem> drawerItemsList;
	private LayoutInflater inflater;
	private int selectedIndex;

	public DrawerItemsAdapter(Context context, List<DrawerItem> drawerItems) {
		this.drawerItemsList = (ArrayList<DrawerItem>) drawerItems;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.selectedIndex = 0;
	}

	public DrawerItemsAdapter(Context context) {
		this(context, new ArrayList<DrawerItem>());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_drawer_menu,
					parent, false);

			// bind the view holder to the proper widgets
			holder = new ViewHolder();
			holder.imgDrawerItemIcon = (ImageView) convertView
					.findViewById(R.id.imgDrawerItemIcon);
			holder.tvDrawerItemDescription = (TextView) convertView
					.findViewById(R.id.tvDrawerItemDescription);

			// set custom fonts for non-hebrew version
			if (!DeviceInfoUtils.isCurrentLanguageHebrew(convertView.getContext())){				
				FontTypeFaceManager.getInstance(convertView.getContext()).setFont(holder.tvDrawerItemDescription, CustomFonts.RobotoThin);				
			}
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
	
		
		// populate the item with values
		DrawerItem item = (DrawerItem) getItem(position);
		holder.imgDrawerItemIcon.setImageResource(item.getIconResourceId());
		holder.tvDrawerItemDescription.setText(item.getTitleResourceId());

		notifyDataSetChanged();

		return convertView;
	}

	public int add(DrawerItem newDrawerItem, int position) {
		// Set the id and the position of this DrawerItem
		// newDrawerItem.setId(drawersItemsId);
		newDrawerItem.setPosition(position);

		// Add the DrawerItem to the list of this adapter
		drawerItemsList.add(position, newDrawerItem);

		// Update all the positions of the DrawerItems that after the new item
		// that was added
		for (int i = position; i < drawerItemsList.size(); i++) {
			drawerItemsList.get(i).setPosition(i);
		}

		// Notify the dataset has changed
		notifyDataSetChanged();

		return 0;
	}

	public void setItems(List<DrawerItem> items) {
		drawerItemsList = items;
	}

	public int add(DrawerItem newDrawerItem) {
		return add(newDrawerItem, drawerItemsList.size());
	}

	public void remove(DrawerItem drawerItemToRemove) {
		this.drawerItemsList.remove(drawerItemToRemove);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.drawerItemsList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.drawerItemsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	public void setSelected(int index) {
		int previousSelected = this.selectedIndex;
		this.selectedIndex = index;

		// change only the relevant items
		getView(previousSelected, null, null);
		getView(selectedIndex, null, null);
	}

}
