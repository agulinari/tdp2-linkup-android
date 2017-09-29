package com.tddp2.grupo2.linkup;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.controller.LinkProfileController;

public class LinkProfileActivity extends AppCompatActivity implements LinkProfileView {

    private LinkProfileController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_profile);

        ButterKnife.bind(this);

        controller = new LinkProfileController(this);
    }

    public void showProgress() {}

    public void hideProgress() {}

    public void goToNext() {}

    public Context getContext() {
        return this.getContext();
    }

    public void onError(String errorMsg) {}
}
