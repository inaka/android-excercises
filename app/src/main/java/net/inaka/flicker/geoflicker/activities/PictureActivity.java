package net.inaka.Flickr.geoFlickr.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.inaka.Flickr.geoFlickr.R;

import uk.co.senab.photoview.PhotoView;

public class PictureActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_picture);

        String url = getIntent().getStringExtra("url");
        PhotoView photoView = (PhotoView) findViewById(R.id.photo);

        ImageLoader.getInstance().displayImage(url, photoView, new ImageLoadingListener() {
            ProgressDialog dialog = new ProgressDialog(PictureActivity.this);
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                dialog.setMessage(getString(R.string.text_status));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                dialog.dismiss();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                dialog.dismiss();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                dialog.dismiss();
            }
        });
    }
}
