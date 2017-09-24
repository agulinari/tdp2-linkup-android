package com.tddp2.grupo2.linkup.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.tddp2.grupo2.linkup.BaseView;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.controller.SettingsController;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;


public class SettingsFragment extends Fragment implements BaseView {

    private static final String TAG = "SettingsFragment";

    private TextView emptyView;
    private Context activity;

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

    @BindView(R.id.btn_update)
    Button buttonUpdate;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        // Toolbar:
        setHasOptionsMenu(true);
        Log.i("SETTINGS", "ON CREATE");
        // View:
        View mainView = inflater.inflate(R.layout.fragment_settings, container, false);
        mainView.setTag(TAG);


        ButterKnife.bind(this, mainView);

        controller = new SettingsController(this);

        initSettings();

        registerListeners();


        return mainView;
    }

    private void registerListeners() {
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

        buttonUpdate.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveProfile(v);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        //inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void initSettings() {
        Profile localProfile = ServiceFactory.getProfileService().getLocalProfile();
        Settings settings = new Settings();
        if (localProfile != null) {
            settings = localProfile.getSettings();
        }
        maxAge = settings.getMaxAge();
        minAge = settings.getMinAge();
        maxDistance = settings.getMaxDistance();
        onlyFriends = settings.isOnlyFriends();
        searchMales = settings.isSearchMales();
        searchFemales = settings.isSearchFemales();
        notifications = settings.isNotifications();
        invisible = settings.isInvisible();

        seekBarDistance.setMinValue(1).setMaxValue(100).setMinStartValue(maxDistance).apply();
        seekBarAge.setMinValue(18).setMaxValue(99).setMinStartValue(minAge).setMaxStartValue(maxAge).apply();
        switchFriends.setChecked(onlyFriends);
        switchFemales.setChecked(searchFemales);
        switchMales.setChecked(searchMales);
        switchInvisible.setChecked(invisible);
        switchNotifications.setChecked(notifications);
    }

    /* On Click button saveProfile */
    public void saveProfile(View view){
        if (!searchFemales && !searchMales && !onlyFriends) {
            showInvalidSearchParametersDialog();
        } else {
            Settings settings = new Settings();
            settings.setMaxDistance(maxDistance);
            settings.setMinAge(minAge);
            settings.setMaxAge(maxAge);
            settings.setOnlyFriends(onlyFriends);
            settings.setSearchFemales(searchFemales);
            settings.setSearchMales(searchMales);
            settings.setNotifications(notifications);
            settings.setInvisible(invisible);
            settings.setAccountType("Basic");
            controller.saveProfile(settings);
        }
    }

    private void showInvalidSearchParametersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.settings_wrong_search_parameters_title);
        builder.setMessage(getString(R.string.settings_wrong_search_parameters_description));
        builder.setCancelable(Boolean.FALSE);
        builder.setPositiveButton(getString(R.string.settings_wrong_search_parameters_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.saving_settings), true, false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showAfterSettingsSaveDialog(boolean success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int title = success ? R.string.save_settings_title_ok : R.string.save_settings_title_error;
        builder.setTitle(title);
        int text = success ? R.string.save_settings_text_ok : R.string.save_settings_text_error;
        builder.setMessage(getString(text));
        builder.setCancelable(Boolean.FALSE);
        builder.setPositiveButton(getString(R.string.save_settings_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void goToNext() {
        showAfterSettingsSaveDialog(true);
        //Intent intent = new Intent(this, LinksActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onError(String errorMsg) {
        showAfterSettingsSaveDialog(false);
        Toast.makeText(getActivity().getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}
