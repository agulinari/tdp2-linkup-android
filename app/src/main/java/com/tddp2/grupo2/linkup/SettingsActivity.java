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
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import com.tddp2.grupo2.linkup.adapter.GridViewAdapter;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.Profile;

import com.tddp2.grupo2.linkup.controller.SettingsController;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.ArrayList;

import static com.tddp2.grupo2.linkup.R.id.distance;

public class SettingsActivity extends AppCompatActivity implements BaseView {

    @BindView(R.id.seek_age)
    CrystalRangeSeekbar seekBarAge;

    @BindView(R.id.seek_distance)
    CrystalSeekbar seekBarDistance;

    @BindView(R.id.text_distance)
    TextView textViewDistance;

    @BindView(R.id.text_age)
    TextView textViewAge;

    @BindView(R.id.image_grid)
    GridView gridView;
    
    SettingsController controller;

    private ProgressDialog progressDialog;
    private GridViewAdapter gridAdapter;

    private int maxDistance = 0;
    private int minAge = 18;
    private int maxAge = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        controller = new SettingsController(this);
        gridAdapter = new GridViewAdapter(this, R.layout.image_item, getData());
        gridView.setAdapter(gridAdapter);
        initSettings();

        seekBarDistance.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                textViewDistance.setText(String.valueOf(minValue)+" KM");
                maxDistance = minValue.intValue();
            }
        });

        seekBarAge.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                textViewAge.setText(String.valueOf(minValue) +"-"+String.valueOf(maxValue)+" Años");
                minAge = minValue.intValue();
                maxAge = maxValue.intValue();
            }
        });
    }

    private void initSettings() {
        Profile localProfile = ServiceFactory.getProfileService().getLocalProfile();
        if (localProfile != null) {
            Settings settings = localProfile.getSettings();
            maxAge = settings.getMaxAge();
            minAge = settings.getMinAge();
            maxDistance = settings.getMaxDistance();

        }else{
            maxAge = 99;
            minAge = 0;
            maxDistance = 1;
        }
        seekBarDistance.setMinValue(1).apply();
        seekBarDistance.setMaxValue(100).apply();
        seekBarDistance.setMinStartValue(maxDistance).apply();
        seekBarAge.setMinValue(18).apply();
        seekBarAge.setMaxValue(99).apply();
        seekBarAge.setMinStartValue(minAge).apply();
        seekBarAge.setMaxStartValue(maxAge).apply();
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
        Settings settings = new Settings();
        settings.setMaxDistance(maxDistance);
        settings.setMinAge(minAge);
        settings.setMaxAge(maxAge);
        controller.saveProfile("1", "Juan", settings);
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
