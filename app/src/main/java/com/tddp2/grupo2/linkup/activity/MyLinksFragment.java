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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.controller.MyLinksController;
import com.tddp2.grupo2.linkup.model.ChatMessage;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.list;



public class MyLinksFragment extends Fragment {

    private static final String TAG = "MyLinksFragment";
    private Context activity;
    private MyLinksController controller;


    @BindView(R.id.rv_newlinks)
    RecyclerView rvNewLinks;

    @BindView(R.id.rv_chatlinks)
    RecyclerView rvChatLinks;

    private List<MyLink> newLinks;
    private List<MyLink> chats;
    private DatabaseReference mDatabase;
    private String userId;
    private RVChatLinksAdapter adapterChats;

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

        registerListeners();
        MyLinks myLinks = controller.getMyLinks();
        newLinks = myLinks.getLinks();
        chats = new ArrayList<MyLink>();

        //lista de nuevos links
        rvNewLinks.setHasFixedSize(true);
        LinearLayoutManager llmNL = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false);
        rvNewLinks.setLayoutManager(llmNL);
        RVNewLinksAdapter adapterNewLinks = new RVNewLinksAdapter(newLinks);
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
                    MyLink myLink = new MyLink(fbid, "", "", 0);
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

        List<MyLink> filteredChats = filterChats(myChatsUpdate);
        Collections.sort(filteredChats);
        adapterChats.swap(filteredChats);

    }

    private List<MyLink> filterChats(List<MyLink> myChatsUpdate) {
        List<MyLink> filteredChats = new ArrayList<MyLink>();
        boolean exists = false;
        for (MyLink myChat : myChatsUpdate){
            for (MyLink myLink : newLinks){
                if (myChat.getFbid().equals(myLink.getFbid())){
                    exists = true;
                    ChatMessage lastMessage = myChat.getLastMessage();
                    myLink.setLastMessage(lastMessage);
                    filteredChats.add(myLink);
                    break;
                }
            }
            exists = false;
        }
        return filteredChats;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        //   inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



}
