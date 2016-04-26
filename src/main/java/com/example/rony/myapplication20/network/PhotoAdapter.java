package com.example.rony.myapplication20.network;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rony.myapplication20.R;
import com.example.rony.myapplication20.activity.MainActivity;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rony on 25/04/2016.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<ObjectFlickr.Photo> photoList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView photo;
        public TextView text;

        public MyViewHolder(View view) {
            super(view);
            photo = (ImageView) view.findViewById(R.id.iv_photo);
            text = (TextView) view.findViewById(R.id.tv_text);
        }
    }

    public PhotoAdapter(Context context, List<ObjectFlickr.Photo> moviesList) {
        this.context = context;
        this.photoList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ObjectFlickr.Photo photo = photoList.get(position);

        String imageUrl = String.format("https://farm%s.staticflickr.com/%s/%s_%s_m.jpg",
                photo.getFarm(),
                photo.getServer(),
                photo.getId(),
                photo.getSecret());


        if (photo.getTitle().length() > 15) {
            holder.text.setText(photo.getTitle().substring(1, 10) + "...");
        } else {
            holder.text.setText(photo.getTitle());
        }

        Picasso.with(context).load(imageUrl).error(R.drawable.flickricon).placeholder(R.drawable.flickricon).into(holder.photo, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });


        holder.photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) context).goto_photo_detail(photo);
            }
        });

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }


}
