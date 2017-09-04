package com.tddp2.grupo2.linkup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import com.tddp2.grupo2.linkup.adapter.GridViewAdapter;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.Profile;

import com.tddp2.grupo2.linkup.controller.SettingsController;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements BaseView {

    @BindView(R.id.seek_age)
    SeekBar seekBarAge;

    @BindView(R.id.seek_distance)
    SeekBar seekBarDistance;

    @BindView(R.id.image_grid)
    GridView gridView;
    
    SettingsController controller;

    private ProgressDialog progressDialog;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        ServiceFactory.init();

        controller = new SettingsController(this);
        gridAdapter = new GridViewAdapter(this, R.layout.image_item, getData());
        gridView.setAdapter(gridAdapter);
    }

    // Prepare some dummy data for gridview
    private ArrayList<Image> getData() {
        final ArrayList<Image> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i,-1));
            Image image = new Image();
            image.setImage(bitmap);
            imageItems.add(image);
        }
        return imageItems;

    }

    /* On Click button saveProfile */
    public void saveProfile(View view){
        controller.saveProfile();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.saving_profile), true, false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void goToNext() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}
