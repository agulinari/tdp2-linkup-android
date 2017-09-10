package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.tddp2.grupo2.linkup.LoginActivity;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LinksFragment extends Fragment{

    private static final String TAG = "LinksFragment";

    //private List<Candidate> candidates;
    private Map<String, String> photos;

    //private CandidateViewHolder candidateViewHolder;
    private TextView emptyView;
    private Context activity;
    private LinearLayout linearLayoutHeaderProgress;
    private ImageButton buttonYes;
    private ImageButton buttonNo;
    private ImageButton reloadButton;

    //private LinksController controller;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        //controller = new CandidatesControllerImpl(this);
        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        View mainView = inflater.inflate(R.layout.fragment_links, container, false);
        mainView.setTag(TAG);

        //candidateViewHolder = new CandidateViewHolder(mainView);
      //  linearLayoutHeaderProgress = (LinearLayout) mainView.findViewById(R.id.linlaHeaderProgress);

        photos = new ConcurrentHashMap<>();

        // ListView
        //emptyView = (TextView) mainView.findViewById(R.id.empty_view);
        //candidates = controller.getCandidates();

        return mainView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
     //   inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
