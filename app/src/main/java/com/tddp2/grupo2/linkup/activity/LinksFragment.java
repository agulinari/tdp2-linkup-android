package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tddp2.grupo2.linkup.LinksView;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.controller.LinksController;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.utils.DateUtils;
import com.tddp2.grupo2.linkup.utils.OnSwipeTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinksFragment extends Fragment implements LinksView{

    private static final String TAG = "LinksFragment";

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

    @BindView(R.id.goToCandidateProfile)
    Button buttonGoToCandidateProfile;

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
                controller.acceptCurrentLink();
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
                progressImage.setVisibility(View.VISIBLE);
                controller.nextLink();
            }
        });

        buttonGoToCandidateProfile.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), LinkProfileActivity.class);
                Bundle b = new Bundle();
                b.putInt("currentLinkIndex", currentLinkIndex);
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
                controller.nextLink();
            }
            public void onSwipeLeft() {
                controller.previousLink();
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
        public void showLink(Profile profile, int currentLinkIndex) {
        this.currentLinkIndex = currentLinkIndex;
        linkCard.setVisibility(View.VISIBLE);
        this.enableActions();
        progressImage.setVisibility(View.INVISIBLE);
        /*String image = profile.getImages().get(0).getImage();
        Bitmap bitmap = ImageUtils.base64ToBitmap(image);
        imageViewLinkImage.setImageBitmap(bitmap);*/
        controller.loadImage();
        String name = profile.getFirstName();
        String age = "";
        try {
            age = String.valueOf(DateUtils.getAgeFromBirthday(profile.getBirthday()));
        } catch (MissingAgeException e) {
        }
        textViewLinkName.setText(name + ", " + age);

        String comment = profile.getComments();
        if (!comment.equals("")) {
            cardViewCommentCard.setVisibility(View.VISIBLE);
            textViewLinkComment.setText(comment);
        } else {
            cardViewCommentCard.setVisibility(View.GONE);
        }

        String occupation = profile.getOccupation();
        if (!occupation.equals("")) {
            cardViewOccupationCard.setVisibility(View.VISIBLE);
            textViewLinkWork.setText(occupation);
        } else {
            cardViewOccupationCard.setVisibility(View.GONE);
        }

        String studies = profile.getEducation();
        if (!studies.equals("")) {
            cardViewEducationCard.setVisibility(View.VISIBLE);
            textViewLinkStudies.setText(studies);
        } else {
            cardViewEducationCard.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyLinks() {
        linkCard.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        buttonYes.setEnabled(Boolean.FALSE);
        buttonNo.setEnabled(Boolean.FALSE);
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
}
