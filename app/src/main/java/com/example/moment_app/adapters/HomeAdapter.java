package com.example.moment_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moment_app.R;
import com.example.moment_app.models.request.FriendInviteRequest;
import com.example.moment_app.models.response.PhotoResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.PhotoViewHolder> {
    private List<PhotoResponse> photoList;
    private Context context;

    public HomeAdapter(Context context, List<PhotoResponse> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoResponse photo = photoList.get(position);
        holder.textViewCaption.setText(photo.getCaption());
        holder.textViewName.setText(photo.getName());

        // Định dạng ban đầu
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        // Định dạng mong muốn
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm 'Ngày' dd/MM/yyyy");

        try {
            // Chuyển đổi chuỗi thành Date
            Date date = inputFormat.parse(photo.getCreatedAt());
            // Định dạng lại thành chuỗi mong muốn
            String formattedDate = outputFormat.format(date);
            holder.textViewTime.setText(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String urlAvt = "https://res.cloudinary.com/moment-images/image/upload/" + photo.getUrlAvt();
        Glide.with(context)
                .load(urlAvt)
                .circleCrop()
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(holder.imageViewAvt);

        String urlPhoto = "https://res.cloudinary.com/moment-images/image/upload/" + photo.getUrlPhoto();
        Glide.with(context).load(urlPhoto).into(holder.imageViewPhoto);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void addData(List<PhotoResponse> moreData) {
        this.photoList.addAll(moreData);
        notifyDataSetChanged();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto;
        ImageView imageViewAvt;
        TextView textViewCaption;
        TextView textViewName;

        TextView textViewTime;


        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewCaption = itemView.findViewById(R.id.textViewCaption);
            imageViewAvt = itemView.findViewById(R.id.imageViewAvt);
            textViewName =  itemView.findViewById(R.id.textViewName);
            textViewTime = itemView.findViewById(R.id.textViewTime);


        }
    }
}
