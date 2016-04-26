package com.example.rony.myapplication20.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rony.myapplication20.R;
import com.example.rony.myapplication20.aplication.VolleySingleton;
import com.example.rony.myapplication20.network.DataCallback;
import com.example.rony.myapplication20.network.ObjectFlickr;
import com.example.rony.myapplication20.network.PhotoAdapter;
import com.example.rony.myapplication20.network.PhotoSize;
import com.example.rony.myapplication20.network.ServiceImageFlickr;
import com.example.rony.myapplication20.network.Sizes;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class PhotoActivity extends AppCompatActivity {

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.photo_view);

        String text = getIntent().getStringExtra("text");
        String id = getIntent().getStringExtra("id");

        PhotoView photoView = (PhotoView) findViewById(R.id.photo);

        final PhotoView photoViewFinal = photoView;

        final Activity _activity = this;

        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        ServiceImageFlickr service = new ServiceImageFlickr(id);
        service.callFlickrPhotoSise(new DataCallback() {

            @Override
            public void onSuccess(List<ObjectFlickr.Photo> photos) {
                dialog.dismiss();
            }

            @Override
            public void onSuccessPhoto(Sizes sizes) {


                for (PhotoSize ps : sizes.getSize()) {

                    if (ps.getLabel().equalsIgnoreCase("Medium")) {
                        Picasso.with(_activity).load(ps.getSource()).error(R.drawable.flickricon).placeholder(R.drawable.flickricon).into(photoViewFinal, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                                dialog.dismiss();
                            }

                            @Override
                            public void onError() {


                                Toast toast = Toast.makeText(_activity, "Image Error.", Toast.LENGTH_SHORT);
                                toast.show();

                                dialog.dismiss();
                                _activity.finish();
                            }
                        });

                    }
                }


            }

            @Override
            public void onError(String error) {
                Toast toast = Toast.makeText(_activity, "Image Error.", Toast.LENGTH_SHORT);
                toast.show();
                dialog.dismiss();
                _activity.finish();
            }
        });


        TextView textView = (TextView) findViewById(R.id.tv_text);
        textView.setText(text);

    }
}
