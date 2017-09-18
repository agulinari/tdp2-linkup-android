package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.tddp2.grupo2.linkup.BaseView;
import com.tddp2.grupo2.linkup.ChatActivity;
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

    @BindView(R.id.linkCard)
    CardView linkCard;

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

    @BindView(R.id.progressImage)
    ProgressBar progressImage;

    @BindView(R.id.empty_view)
    TextView emptyView;

    @BindView(R.id.linlaHeaderProgress)
    LinearLayout progressView;

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

        controller.getLinks();

        return mainView;
    }

    private void registerListeners() {

        buttonYes.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressImage.setVisibility(View.VISIBLE);
                controller.nextLink();
            }
        });

        buttonNo.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                controller.rejectCurrentLink();
            }
        });

        reloadButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO: SUPERLINKS
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
        this.linkCard.setVisibility(View.GONE);
        this.emptyView.setVisibility(View.GONE);
        this.disableActions();
        this.progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        this.progressView.setVisibility(View.GONE);
    }


    @Override
    public void disableActions() {
        buttonYes.setEnabled(Boolean.FALSE);
        buttonNo.setEnabled(Boolean.FALSE);
    }

    @Override
    public void enableActions() {
        buttonYes.setEnabled(Boolean.TRUE);
        buttonNo.setEnabled(Boolean.TRUE);
    }


    @Override
    public void goToNext() {

    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLink(Profile profile) {
        linkCard.setVisibility(View.VISIBLE);
        this.enableActions();
        progressImage.setVisibility(View.INVISIBLE);
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
        linkCard.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        buttonYes.setEnabled(Boolean.FALSE);
        buttonNo.setEnabled(Boolean.FALSE);
    }


}
