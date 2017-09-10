package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.tddp2.grupo2.linkup.BaseView;
import com.tddp2.grupo2.linkup.LinksView;
import com.tddp2.grupo2.linkup.LoginActivity;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.controller.LinksController;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.utils.DateUtils;
import com.tddp2.grupo2.linkup.utils.ImageUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinksFragment extends Fragment implements LinksView{

    private static final String TAG = "LinksFragment";

    @BindView(R.id.linkImage)
    ImageView imageViewLinkImage;

    @BindView(R.id.linkName)
    TextView textViewLinkName;

    @BindView(R.id.linkAge)
    TextView textViewLinkAge;

    @BindView(R.id.linkInterests)
    TextView textViewLinkInterests;

    @BindView(R.id.yesButton)
    ImageButton buttonYes;

    @BindView(R.id.noButton)
    ImageButton buttonNo;

    @BindView(R.id.reloadButton)
    ImageButton reloadButton;

    private Context activity;
    private LinksController controller;
    private Links links;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        controller = new LinksController(this);
        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        View mainView = inflater.inflate(R.layout.fragment_links, container, false);
        mainView.setTag(TAG);

        ButterKnife.bind(this, mainView);

        registerListeners();
        // ListView
        //emptyView = (TextView) mainView.findViewById(R.id.empty_view);
        controller.getLinks();

        return mainView;
    }

    private void registerListeners() {

        buttonYes.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                controller.nextLink();
            }
        });

        buttonNo.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                controller.previousLink();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
     //   inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void goToNext() {

    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void showLink(Profile profile) {
        String image = profile.getImages().get(0).getImage();
        Bitmap bitmap = ImageUtils.base64ToBitmap(image);
        imageViewLinkImage.setImageBitmap(bitmap);
        textViewLinkName.setText(profile.getFirstName());
        String age = "";
        try {
            age = String.valueOf(DateUtils.getAgeFromBirthday(profile.getBirthday()));
        } catch (MissingAgeException e) {
        }
        textViewLinkAge.setText(age);
        textViewLinkInterests.setText(profile.getComments());
    }

    @Override
    public void showEmptyLinks() {
        //emptyView = (TextView) mainView.findViewById(R.id.empty_view);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_chat:
                goToChat();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


}
