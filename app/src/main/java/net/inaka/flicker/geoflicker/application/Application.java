package net.inaka.Flickr.geoFlickr.application;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

/**
 * Created by griveroa on 4/16/15.
 */
public class Application extends android.app.Application {
    static Application mApp;

    public Application(){
        mApp = this;
    }

    public static Application getInstance(){
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "geo_Flickr");
        } else {
            cacheDir = this.getCacheDir();
        }

        if(!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .build();

        ImageLoader.getInstance().init(config);
    }


}
