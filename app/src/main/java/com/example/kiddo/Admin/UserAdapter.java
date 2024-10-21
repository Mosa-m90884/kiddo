package com.example.kiddo.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiddo.Model.UserInAdminPanel;
import com.example.kiddo.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserInAdminPanelViewHolder> {

    private List<UserInAdminPanel> userList;
    private OnUserActionListener actionListener;

    public UserAdapter(List<UserInAdminPanel> userList, OnUserActionListener actionListener) {
        this.userList = userList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public UserInAdminPanelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserInAdminPanelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserInAdminPanelViewHolder holder, int position) {
        UserInAdminPanel user = userList.get(position);
        holder.nameTextView.setText(user.getUsername());
        holder.emailTextView.setText(user.getEmail());

        holder.editButton.setOnClickListener(v -> actionListener.onEdit(user));
        holder.deleteButton.setOnClickListener(v -> actionListener.onDelete(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserInAdminPanelViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;
        public Button editButton;
        public Button deleteButton;

        public UserInAdminPanelViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_username);
            emailTextView = itemView.findViewById(R.id.text_email);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }

    public interface OnUserActionListener {
        void onEdit(UserInAdminPanel user);
        void onDelete(UserInAdminPanel user);
    }
}