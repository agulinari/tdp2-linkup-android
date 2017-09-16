package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.controller.MyLinksController;
import com.tddp2.grupo2.linkup.model.MyLink;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyLinksFragment extends Fragment {

    private static final String TAG = "MyLinksFragment";
    private Context activity;
    private MyLinksController controller;


    @BindView(R.id.rv_newlinks)
    RecyclerView rvNewLinks;

    @BindView(R.id.rv_chatlinks)
    RecyclerView rvChatLinks;

    private List<MyLink> myLinks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        controller = new MyLinksController(this);
        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        View mainView = inflater.inflate(R.layout.fragment_mylinks, container, false);
        mainView.setTag(TAG);

        ButterKnife.bind(this, mainView);

        registerListeners();
        List<MyLink> myLinks = controller.getMyLinks();


        //lista de nuevos links
        rvNewLinks.setHasFixedSize(true);
        LinearLayoutManager llmNL = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false);
        rvNewLinks.setLayoutManager(llmNL);
        RVNewLinksAdapter adapterNewLinks = new RVNewLinksAdapter(myLinks);
        rvNewLinks.setAdapter(adapterNewLinks);

        //lista de chats
        rvChatLinks.setHasFixedSize(true);
        LinearLayoutManager llmCL = new LinearLayoutManager(activity);
        rvChatLinks.setLayoutManager(llmCL);
        RVChatLinksAdapter adapterChats = new RVChatLinksAdapter(myLinks);
        rvChatLinks.setAdapter(adapterChats);

        return mainView;
    }

    private void registerListeners() {
        //TODO: register listeners
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        //   inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
