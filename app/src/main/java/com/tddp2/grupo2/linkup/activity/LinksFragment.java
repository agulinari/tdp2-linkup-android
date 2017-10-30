package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.LinksView;
import com.tddp2.grupo2.linkup.controller.LinksController;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.Link;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.utils.DateUtils;
import com.tddp2.grupo2.linkup.utils.MapUtils;
import com.tddp2.grupo2.linkup.utils.OnSwipeTouchListener;

import static com.tddp2.grupo2.linkup.R.id.reloadButton;

public class LinksFragment extends BroadcastFragment implements LinksView {

    private static final String TAG = "LinksFragment";

    @BindView(R.id.linksCoordinatorLayout)
    CoordinatorLayout coordView;

    @BindView(R.id.linkCard)
    CardView linkCard;

    @BindView(R.id.linkImageNameAge)
    CardView linkImageCard;

    @BindView(R.id.linkImage)
    ImageView imageViewLinkImage;

    @BindView(R.id.linkImageProgress)
    ProgressBar progressBarImage;

    @BindView(R.id.linkName)
    TextView textViewLinkName;

    @BindView(R.id.linkDistance)
    TextView textViewLinkDistance;

    @BindView(R.id.yesButton)
    ImageButton buttonYes;

    @BindView(R.id.noButton)
    ImageButton buttonNo;

    @BindView(reloadButton)
    ImageButton superlinkButton;

    @BindView(R.id.progressImage)
    ProgressBar progressImage;

    @BindView(R.id.empty_view)
    TextView emptyView;

    @BindView(R.id.linlaHeaderProgress)
    LinearLayout progressView;

    @BindView(R.id.goToCandidateProfile)
    Button buttonGoToCandidateProfile;

    @BindView(R.id.goToAdvertisementSite)
    Button buttonGoToAdvertisementSite;

    private Context activity;
    private LinksController controller;
    private Links links;
    private int currentLinkIndex;

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

        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerListeners();
        controller.getLinks();
    }

    private void registerListeners() {

        buttonYes.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                controller.acceptCurrentLink("Link");
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

        superlinkButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                controller.acceptCurrentLink("Superlink");
            }
        });

        buttonGoToCandidateProfile.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), LinkProfileActivity.class);
                Bundle b = new Bundle();
                Link currentLink = controller.getCurrentLink();
                b.putString("LINK_USER_ID", currentLink.getFbid());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        registerNextAndPreviousListeners();
    }

    @Override
    public void registerNextAndPreviousListeners() {
        linkImageCard.setOnTouchListener(new OnSwipeTouchListener(activity) {
            public void onSwipeRight() {
                controller.previousLink();
            }
            public void onSwipeLeft() {
                controller.nextLink();
            }
        });
    }

    @Override
    public void blockCandidatesNavigation() {
        /*imageViewLinkImage.setOnClickListener(null);*/
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
        superlinkButton.setEnabled(Boolean.FALSE);
    }

    @Override
    public void enableActions() {
        buttonYes.setEnabled(Boolean.TRUE);
        buttonNo.setEnabled(Boolean.TRUE);
        superlinkButton.setEnabled(Boolean.TRUE);
    }


    @Override
    public void goToNext() {

    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
        public void showLink(Profile profile, int currentLinkIndex) {
        showElements();
        this.currentLinkIndex = currentLinkIndex;
        linkCard.setVisibility(View.VISIBLE);
        this.enableActions();
        progressImage.setVisibility(View.INVISIBLE);

        controller.loadImage();
        String name = profile.getFirstName();
        String age = "";
        try {
            age = String.valueOf(DateUtils.getAgeFromBirthday(profile.getBirthday()));
        } catch (MissingAgeException e) {
        }
        textViewLinkName.setText(name + ", " + age);
        controller.updateDistance();

        if (controller.shouldShowSuperLinkButton()) {
            superlinkButton.setEnabled(true);
        } else {
            superlinkButton.setEnabled(false);
        }
    }

    @Override
    public void updateDistance(Location loggedUserLocation, Location linkLocation) {
        String distanceText = MapUtils.getDistanceTextBetweenLocations(loggedUserLocation, linkLocation);
        textViewLinkDistance.setText(distanceText);
    }

    @Override
    public void showEmptyLinks() {
        linkCard.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        this.disableActions();
    }

    public void showMatch(String matchName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.candidate_match_title);
        builder.setMessage(getString(R.string.candidate_match_message, matchName));
        builder.setCancelable(Boolean.FALSE);
        builder.setPositiveButton(getString(R.string.candidate_match_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                controller.gotoNextLink();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void showImage(Bitmap bitmap) {

        if (bitmap != null) {
            imageViewLinkImage.setImageBitmap(bitmap);
        }else{
            imageViewLinkImage.setImageBitmap(null);
        }
        imageViewLinkImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingImage() {
        progressBarImage.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingImage() {
        imageViewLinkImage.setVisibility(View.GONE);
        progressBarImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        Log.i(TAG, "Notificacion RECIBIDA");

        if (notification!=null) {
            String text;
            if (notification.motive.equals(Notification.CHAT)) {
                text = notification.firstName + ": " + "'" + notification.messageBody + "'";
            }else{
                text = notification.messageBody;
            }
            Snackbar snackbar = Snackbar.make(coordView, text, Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            snackbar.show();
        }

        broadcastReceiver.abortBroadcast();
    }

    @Override
    public void showInactiveAccountAlert() {
        if (isAdded()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.inactive_account_popup_title);
            builder.setCancelable(Boolean.FALSE);
            builder.setPositiveButton(getString(R.string.inactive_account_popup_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void showAdvertisement(String advertName, String advertUrl) {
        hideElements();
        textViewLinkName.setText(advertName);
        textViewLinkDistance.setText(getText(R.string.ad_content));
        final String url = advertUrl;
        buttonGoToAdvertisementSite.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

    }

    private void hideElements() {
        buttonYes.setVisibility(View.GONE);
        buttonNo.setVisibility(View.GONE);
        superlinkButton.setVisibility(View.GONE);
        buttonGoToCandidateProfile.setVisibility(View.GONE);
        buttonGoToAdvertisementSite.setVisibility(View.VISIBLE);
    }

    private void showElements() {
        buttonYes.setVisibility(View.VISIBLE);
        buttonNo.setVisibility(View.VISIBLE);
        superlinkButton.setVisibility(View.VISIBLE);
        buttonGoToCandidateProfile.setVisibility(View.VISIBLE);
        buttonGoToAdvertisementSite.setVisibility(View.GONE);
    }
}
