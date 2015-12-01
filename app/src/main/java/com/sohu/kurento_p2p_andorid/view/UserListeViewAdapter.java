package com.sohu.kurento_p2p_andorid.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.kurento_p2p_andorid.R;
import com.sohu.kurento_p2p_andorid.model.bean.UserBean;

import java.util.ArrayList;

/**
 * Created by jingbiaowang on 2015/11/18.
 */
public class UserListeViewAdapter extends RecyclerView.Adapter<UserListeViewAdapter
		.UserViewHolder> implements View.OnClickListener {
	private Context context;
	private ArrayList<UserBean> users = new ArrayList<>();
	private UserItemClickListener listener;

	public UserListeViewAdapter(Context context) {

		this.context = context;
	}

	@Override
	public void onClick(View v) {
		UserBean userBean = (UserBean) v.getTag();
		if (listener != null)
			listener.onItemClickListener(userBean);
	}

	public interface UserItemClickListener {

		public void onItemClickListener(UserBean userBean);

	}

	public void notifyDataOnDataChanged(ArrayList<UserBean> users) {

		if (users != null) {
			this.users.clear();
			this.users.addAll(users);
		}

		notifyDataSetChanged();
	}

	public void notifyDataOnAddUser(UserBean user) {

		users.add(user);
		notifyItemInserted(users.size() - 1);
	}


	public void clearData() {
		users.clear();
		notifyDataSetChanged();
	}

	public ArrayList<UserBean> getUsers() {
		return users;
	}


	public void notifyDataOnRemoveUser(UserBean userBean) {
		int position = 0;
		boolean contained = false;
		for (UserBean user :
				users) {
			if (user.getName().equals(userBean.getName())) {
				contained = true;
				break;
			} else {
				position++;
			}


		}

		if (contained) {
			this.users.remove(position);
			notifyItemRemoved(position);
		}
	}

	public void setUsers(ArrayList<UserBean> users) {
		this.users = users;
	}

	@Override
	public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		UserViewHolder viewHolder = new UserViewHolder(LayoutInflater.from(context).inflate(R
				.layout.user_list_item, parent, false));
		viewHolder.itemView.setOnClickListener(this);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(UserViewHolder holder, int position) {
		holder.nameTv.setText(users.get(position).getName());
		holder.itemView.setTag(users.get(position));
	}

	@Override
	public int getItemCount() {
		return users.size();
	}


	public class UserViewHolder extends RecyclerView.ViewHolder {

		TextView nameTv;

		public UserViewHolder(View itemView) {
			super(itemView);
			nameTv = (TextView) itemView.findViewById(R.id.item_name_tv);
		}

	}

	public UserItemClickListener getListener() {
		return listener;
	}

	public void setListener(UserItemClickListener listener) {
		this.listener = listener;
	}
}
