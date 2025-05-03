package com.example.moment_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moment_app.R;
import com.example.moment_app.models.request.FriendInviteRequest;
import com.example.moment_app.models.response.AccountResponse;
import com.example.moment_app.models.response.PhotoResponse;

import java.util.List;

import lombok.Getter;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    @Getter
    private List<AccountResponse> accountList;
    private Context context;

    private OnFriendActionListener listener;

    public FriendAdapter(Context context, List<AccountResponse> accountList, OnFriendActionListener listener) {
        this.context = context;
        this.accountList = accountList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account, parent, false);
        return new FriendAdapter.FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        AccountResponse accountResponse = accountList.get(position);
        holder.textViewUserNameFr.setText(accountResponse.getName());

        String urlAvt = "https://res.cloudinary.com/moment-images/image/upload/" + accountResponse.getUrlPhoto();
        Glide.with(context).load(urlAvt).circleCrop().into(holder.imageViewAvatarFr);

        FriendInviteRequest friendInviteRequest = new FriendInviteRequest();
        friendInviteRequest.setAccountFriendId(accountResponse.getId());

        if (accountResponse.getFriendStatus().equals("me")) {
            holder.button1.setVisibility(View.GONE);
            holder.button2.setVisibility(View.GONE);
        }

        if (accountResponse.getFriendStatus().equals("none")) {

            holder.button1.setText("Kết bạn");
            holder.button2.setVisibility(View.GONE);
            holder.button1.setOnClickListener(v -> {
                // Gửi lời mời kết bạn
//                listener.onSendFriendRequest(friendInviteRequest, position); // truyền qua interface
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onSendFriendRequest(friendInviteRequest, pos);
                }


            });
        } else if (accountResponse.getFriendStatus().equals("accepted")) {
            holder.button1.setText("Hủy kết bạn");

            holder.button2.setVisibility(View.GONE);
            holder.button1.setOnClickListener(v -> {
                // Hủy kết bạn
//                listener.onCancelFriend(friendInviteRequest);
                friendInviteRequest.setStatus("deleted");
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onCancelFriend(friendInviteRequest, pos);
                }
            });
        } else if (accountResponse.getFriendStatus().equals("received")) {
            holder.button1.setText("Chấp nhận");
            holder.button2.setText("Từ chối");
            holder.button1.setOnClickListener(v -> {
//                listener.onAcceptFriend(friendInviteRequest);
                friendInviteRequest.setStatus("accepted");
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onAcceptFriend(friendInviteRequest, pos);
                }
            });

            holder.button2.setOnClickListener(v -> {
//                listener.onRejectFriend(friendInviteRequest);
                friendInviteRequest.setStatus("deleted");
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onRejectFriend(friendInviteRequest, pos);
                }
            });

        } else if (accountResponse.getFriendStatus().equals("sent")) {
            holder.button1.setText("Hủy lời mời");
            holder.button2.setVisibility(View.GONE);
            holder.button1.setOnClickListener(v -> {
//                listener.onCancelRequest(friendInviteRequest);
                friendInviteRequest.setStatus("deleted");
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onCancelRequest(friendInviteRequest, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public void addData(List<AccountResponse> moreData) {
        this.accountList.addAll(moreData);
        notifyDataSetChanged();
    }


    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatarFr;
        TextView textViewUserNameFr;
        Button button1;
        Button button2;




        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatarFr = itemView.findViewById(R.id.imageViewAvatarFr);
            textViewUserNameFr = itemView.findViewById(R.id.textViewUserNameFr);
            button1 = itemView.findViewById(R.id.button1);
            button2 =  itemView.findViewById(R.id.button2);
        }
    }
}
