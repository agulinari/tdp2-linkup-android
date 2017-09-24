package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tddp2.grupo2.linkup.MyLinksView;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.controller.MyLinksController;
import com.tddp2.grupo2.linkup.model.ChatMessage;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MyLinksFragment extends Fragment implements MyLinksView{

    private static final String TAG = "MyLinksFragment";
    private Context activity;
    private MyLinksController controller;


    @BindView(R.id.rv_newlinks)
    RecyclerView rvNewLinks;

    @BindView(R.id.rv_chatlinks)
    RecyclerView rvChatLinks;

    @BindView(R.id.newlinks_progress)
    LinearLayout newLinksProgressView;

    @BindView(R.id.chats_progress)
    LinearLayout chatsProgressView;

    private List<MyLink> myLinks;
    private List<MyLink> newLinks;
    private List<MyLink> chats;
    private DatabaseReference mDatabase;
    private String userId;
    private RVChatLinksAdapter adapterChats;
    private RVNewLinksAdapter adapterNewLinks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        controller = new MyLinksController(this);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = ServiceFactory.getMyLinksService().getDatabase().getProfile().getFbid();

        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        View mainView = inflater.inflate(R.layout.fragment_mylinks, container, false);
        mainView.setTag(TAG);

        ButterKnife.bind(this, mainView);

        this.newLinksProgressView.setVisibility(View.VISIBLE);
        this.chatsProgressView.setVisibility(View.VISIBLE);

        controller.getMyLinks();

        myLinks = new ArrayList<MyLink>();

        newLinks = new ArrayList<MyLink>();
        chats = new ArrayList<MyLink>();

        //lista de nuevos links
        rvNewLinks.setHasFixedSize(true);
        LinearLayoutManager llmNL = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false);
        rvNewLinks.setLayoutManager(llmNL);
        adapterNewLinks = new RVNewLinksAdapter(newLinks);
        rvNewLinks.setAdapter(adapterNewLinks);

        //lista de chats
        rvChatLinks.setHasFixedSize(true);
        LinearLayoutManager llmCL = new LinearLayoutManager(activity);
        rvChatLinks.setLayoutManager(llmCL);
        adapterChats = new RVChatLinksAdapter(chats);
        rvChatLinks.setAdapter(adapterChats);

        return mainView;
    }

    private void registerListeners() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<MyLink> myChatsUpdate = new ArrayList<MyLink>();

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String fbid = child.getKey();
                    ChatMessage lastMessage = child.getValue(ChatMessage.class);
                    MyLink myLink = new MyLink(fbid, "", "", "", "", "", "");
                    myLink.setLastMessage(lastMessage);
                    myChatsUpdate.add(myLink);
                }
                updateMyChats(myChatsUpdate);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("lastMessages").child(userId).addValueEventListener(postListener);
    }

    private synchronized void updateMyChats(List<MyLink> myChatsUpdate) {

        List<MyLink> filteredNewLinks = new ArrayList<MyLink>();
        List<MyLink> filteredChats = new ArrayList<MyLink>();

        boolean exists = false;
        for (MyLink myLink : myLinks){
            for (MyLink myChat : myChatsUpdate){

                if (myChat.getFbid().equals(myLink.getFbid())){
                    exists = true;
                    ChatMessage lastMessage = myChat.getLastMessage();
                    myLink.setLastMessage(lastMessage);
                    filteredChats.add(myLink);
                    break;
                }
            }
            if (!exists){
                filteredNewLinks.add(myLink);
            }
            exists = false;
        }

        Collections.sort(filteredChats);
        adapterChats.swap(filteredChats);
        adapterNewLinks.swap(filteredNewLinks);
        this.newLinksProgressView.setVisibility(View.GONE);
        this.chatsProgressView.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        //   inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void showMyLinks(MyLinks links) {
        this.myLinks = links.getLinks();
        registerListeners();
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show();

    }
}
