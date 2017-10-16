package com.tddp2.grupo2.linkup.activity.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.tddp2.grupo2.linkup.R;

public class PictureSliderView extends BaseSliderView {
    public PictureSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.picture_slider_view, null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        byte[] byteArray = getBundle().getByteArray("image");
        Bitmap picture = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        target.setImageBitmap(picture);
        bindEventAndShow(v, target);
        return v;
    }
}
