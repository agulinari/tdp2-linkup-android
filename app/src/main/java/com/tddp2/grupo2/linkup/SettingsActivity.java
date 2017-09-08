package com.tddp2.grupo2.linkup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.tddp2.grupo2.linkup.controller.SettingsController;
import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements BaseView {

    @BindView(R.id.seek_age)
    CrystalRangeSeekbar seekBarAge;

    @BindView(R.id.seek_distance)
    CrystalSeekbar seekBarDistance;

    @BindView(R.id.text_distance)
    TextView textViewDistance;

    @BindView(R.id.text_age)
    TextView textViewAge;

    @BindView(R.id.switch_friends)
    Switch switchFriends;

    @BindView(R.id.switch_males)
    Switch switchMales;

    @BindView(R.id.switch_females)
    Switch switchFemales;

    @BindView(R.id.switch_notifications)
    Switch switchNotifications;

    @BindView(R.id.switch_invisible)
    Switch switchInvisible;

    SettingsController controller;

    private ProgressDialog progressDialog;

    private int maxDistance = 0;
    private int minAge = 18;
    private int maxAge = 99;
    private boolean onlyFriends = false;
    private boolean searchMales = false;
    private boolean searchFemales = false;
    private boolean notifications = false;
    private boolean invisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        controller = new SettingsController(this);

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

        switchFriends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onlyFriends = isChecked;
            }
        });

        switchMales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                searchMales = isChecked;
            }
        });

        switchFemales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                searchFemales = isChecked;
            }
        });

        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notifications = isChecked;
            }
        });

        switchInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                invisible = isChecked;
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
            onlyFriends = settings.isOnlyFriends();
            searchMales = settings.isSearchMales();
            searchFemales = settings.isSearchFemales();
            notifications = settings.isNotifications();
            invisible = settings.isInvisible();

        }else{
            maxAge = 99;
            minAge = 0;
            maxDistance = 1;
            searchMales = false;
            searchFemales = false;
            onlyFriends = false;
            notifications = false;
            invisible = false;
        }
        seekBarDistance.setMinValue(1).setMaxValue(100).setMinStartValue(maxDistance).apply();
        seekBarAge.setMinValue(18).setMaxValue(99).setMinStartValue(minAge).setMaxStartValue(maxAge).apply();
        switchFriends.setChecked(onlyFriends);
        switchFemales.setChecked(searchFemales);
        switchMales.setChecked(searchMales);
        switchInvisible.setChecked(invisible);
        switchNotifications.setChecked(notifications);
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
        settings.setOnlyFriends(onlyFriends);
        settings.setSearchFemales(searchFemales);
        settings.setSearchMales(searchMales);
        settings.setNotifications(notifications);
        settings.setInvisible(invisible);
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
