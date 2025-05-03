package com.example.moment_app.adapters;

import com.example.moment_app.models.request.FriendInviteRequest;

public interface OnFriendActionListener {

    void onSendFriendRequest(FriendInviteRequest account, int position);
    void onCancelFriend(FriendInviteRequest account, int position);
    void onAcceptFriend(FriendInviteRequest account, int position);
    void onRejectFriend(FriendInviteRequest account, int position);
    void onCancelRequest(FriendInviteRequest account, int position);
}
