package net.inaka_test.flickrinaka;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import net.inaka_test.flickrinaka.Application.AppController;
import net.inaka_test.flickrinaka.Connections.Connection;
import net.inaka_test.flickrinaka.Models.Photo;

import org.w3c.dom.Text;

public class PhotoDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        Photo photo = (Photo) bundle.getSerializable("photo");

        TextView tvPhotoTitle = (TextView)findViewById(R.id.tvPhotoTitle);
        tvPhotoTitle.setText(photo.getTitle());

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView imPhoto = (NetworkImageView) findViewById(R.id.imPhoto);
        imPhoto.setImageUrl(String.format(Connection.PHOTO_URL, photo.getFarm(), photo.getServer(),
                photo.getId(), photo.getSecret(),"z"), imageLoader);
    }

}
