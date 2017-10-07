package com.tddp2.grupo2.linkup.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;

public abstract class AbstractLinkProfileActivity extends BroadcastActivity implements LinkProfileView, OnMapReadyCallback {
    @BindView(R.id.linkProfileCoordinatorLayout)
    CoordinatorLayout coordView;

    @BindView(R.id.linkProfileNameText)
    TextView textViewLinkName;

    @BindView(R.id.linkCommentCard)
    CardView cardViewCommentCard;

    @BindView(R.id.linkCommentText)
    TextView textViewLinkComment;

    @BindView(R.id.linkOccupationCard)
    CardView cardViewOccupationCard;

    @BindView(R.id.linkWorkText)
    TextView textViewLinkWork;

    @BindView(R.id.linkEducationCard)
    CardView cardViewEducationCard;

    @BindView(R.id.linkStudiesText)
    TextView textViewLinkStudies;

    @BindView(R.id.linkProfilePhoto)
    ImageView imageViewLinkProfilePhoto;

    @BindView(R.id.linkProfileImageProgress)
    ProgressBar progressBarImage;

    @BindView(R.id.linkLocationText)
    TextView textViewLinkDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_profile);

        ButterKnife.bind(this);
    }
}
