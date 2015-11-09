package net.inaka_test.flickrinaka.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import net.inaka_test.flickrinaka.Application.AppController;
import net.inaka_test.flickrinaka.Connections.Connection;
import net.inaka_test.flickrinaka.Models.Photo;
import net.inaka_test.flickrinaka.R;

import java.util.List;

/**
 * Created by Jonathan Garcia on 11/7/2015.
 */
public class PhotosAdapter extends BaseAdapter {

    private Context context;
    private List<Photo> photoList;

    public PhotosAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.photo_item, null);

        Photo photo = (Photo) getItem(position);
        TextView tvPhotoTitle = (TextView) view.findViewById(R.id.tvPhotoTitle);
        tvPhotoTitle.setText(!photo.getTitle().equalsIgnoreCase("") ? photo.getTitle(): context.getResources().getString(R.string.no_title));

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView imPhoto = (NetworkImageView)view.findViewById(R.id.imPhoto);
        imPhoto.setImageUrl(String.format(Connection.PHOTO_URL, photo.getFarm(), photo.getServer(),
                photo.getId(), photo.getSecret(),"m"), imageLoader);

        return view;
    }
}