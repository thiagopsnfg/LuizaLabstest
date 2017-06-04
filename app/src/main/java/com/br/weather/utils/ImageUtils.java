package com.br.weather.utils;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtils {

    public static void loadImage(@NonNull ImageView view, @NonNull String url, int width, int height, @DrawableRes int placeholder, boolean adjustHeight, boolean toScale) {

        if (toScale) {
            final float scale = view.getContext().getResources().getDisplayMetrics().density;
            width = (int) ((width * scale) + 0.5f);
            height = (int) ((height * scale) + 0.5f);
        }

        view.getLayoutParams().width = width;

        if (adjustHeight) {
            view.getLayoutParams().height = height;
        }

        try {
            Picasso
                    .with(view.getContext())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .resize(width, height)
                    .centerCrop()
                    .into(view);
        } catch (Exception ex) {
            Log.e("LOG", "Falha ao carregar icone");
        }
    }

}
