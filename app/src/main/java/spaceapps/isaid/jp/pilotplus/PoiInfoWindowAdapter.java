package spaceapps.isaid.jp.pilotplus;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import spaceapps.isaid.jp.pilotplus.databinding.ImageInfoWindowBinding;


public class PoiInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "poiInfo";
    @NonNull
    private final Context mContext;

    PoiInfoWindowAdapter(Context context) {
        mContext = Objects.requireNonNull(context);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * initiates loading the info window and makes sure the new image is used in case it changed
     */

    @Override
    public View getInfoContents(final Marker marker) {
        Log.d(TAG, "getInfoContents:" + marker.hashCode());

        Object o = marker.getTag();
        Log.d(TAG, "o:" + o);
        if (o == null) {
            return null;
        }

        if (!(o instanceof PoiData)) {
            return null;
        }

        Log.d(TAG, "o:" + o.getClass());
        PoiData poi = (PoiData) o;
        ImageInfoWindowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.image_info_window, null, false);
        final ImageView image = binding.image;
        final TextView name = binding.name;
        name.setText(poi.getName());

        Glide.with(mContext)
                .load(poi.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //TODO ローディング画像にしたい
//                        .placeholder(R.drawable.dummy)
                .error(R.drawable.dummy)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> glideDrawableTarget, boolean b) {
                        Log.d(TAG, "Error in Glide listener");
                        Log.d(TAG, s);
                        if (e != null) {
                            Log.d(TAG, e.getMessage());
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> glideDrawableTarget, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (!isFromMemoryCache) {
                            marker.showInfoWindow();
                        }
                        return false;
                    }
                })
                .into(image);
        return binding.getRoot();
    }
}
