package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.ChatMessage;
import com.tddp2.grupo2.linkup.utils.ImageUtils;
import com.tddp2.grupo2.linkup.utils.LinkupUtils;

import static com.tddp2.grupo2.linkup.LinkupApplication.getContext;

public class ChatActivity extends BroadcastActivity {

    private static final String TAG = "ChatActivity";
    private FirebaseListAdapter<ChatMessage> adapter;

    ListView listOfMessages;
    private String userId;
    private String linkId;
    private String chatId;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra("USER_ID");
        linkId = getIntent().getStringExtra("LINK_ID");
        chatId = LinkupUtils.getChatId(userId, linkId);

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                postMessage(input.getText().toString(), chatId);

                // Clear the input
                input.setText("");
            }
        });

        listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        listOfMessages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                likeMessage(pos);
                return true;
            }
        });

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, mDatabase.child("chats").child(chatId)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                RelativeLayout messageGlobe = (RelativeLayout)v.findViewById(R.id.message_globe);
                RelativeLayout messageGlobeText = (RelativeLayout)v.findViewById(R.id.message_globe_text);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                if (!model.isLiked()) {
                    // Format the date before showing it
                    messageTime.setText(DateFormat.format("HH:mm",
                            model.getMessageTime())+ " ♡");
                }else{
                    messageTime.setText(DateFormat.format("HH:mm",
                            model.getMessageTime())+ " ❤️");
                }

                if (model.getFbid()!=null && model.getFbid().equals(userId)){
                    RelativeLayout.LayoutParams params1 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                    messageUser.setLayoutParams(params1);

                    RelativeLayout.LayoutParams params2 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params2.addRule(RelativeLayout.BELOW, R.id.message_user);
                    params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    messageGlobe.setLayoutParams(params2);

                    if (model.isLiked()) {
                        GradientDrawable messageBox = (GradientDrawable) messageGlobeText.getBackground();
                        int pix = ImageUtils.dpToPx(ChatActivity.this, 2);
                        messageBox.setStroke(pix,ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    }else{
                        GradientDrawable messageBox = (GradientDrawable) messageGlobeText.getBackground();
                        int pix = ImageUtils.dpToPx(ChatActivity.this, 2);
                        messageBox.setStroke(pix,ContextCompat.getColor(getContext(), R.color.chatMessage));
                    }
                    messageUser.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    messageTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }else{
                    RelativeLayout.LayoutParams params1 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

                    messageUser.setLayoutParams(params1);

                    RelativeLayout.LayoutParams params2 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params2.addRule(RelativeLayout.BELOW, R.id.message_user);
                    params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    messageGlobe.setLayoutParams(params2);

                    if (model.isLiked()) {
                        GradientDrawable messageBox = (GradientDrawable) messageGlobeText.getBackground();
                        int pix = ImageUtils.dpToPx(ChatActivity.this, 2);
                        messageBox.setStroke(pix,ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    }else{
                        GradientDrawable messageBox = (GradientDrawable) messageGlobeText.getBackground();
                        int pix = ImageUtils.dpToPx(ChatActivity.this, 2);
                        messageBox.setStroke(pix,ContextCompat.getColor(getContext(), R.color.chatMessage));
                    }
                    messageUser.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    messageTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                }


            }
        };

        listOfMessages.setAdapter(adapter);

        android.support.v7.widget.Toolbar chatToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.chatToolbar);
        chatToolbar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyLinkProfileActivity.class);
                intent.putExtra("LINK_USER_ID", linkId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }

    private void postMessage(String message, final String chatId) {
        // Read the input field and push a new instance
        // of ChatMessage to the Firebase database
        final ChatMessage chatMessage = new ChatMessage(message, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), userId, linkId);

        mDatabase.child("chats")
                .child(chatId)
                .push()
                .setValue(chatMessage);


        mDatabase.child("lastMessages").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.child(userId).child(linkId).setValue(chatMessage);
                mutableData.child(linkId).child(userId).setValue(chatMessage);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });

        Notification n = new Notification(chatMessage.getFbid(), chatMessage.getFbidTo(), "Tienes un mensaje", chatMessage.getMessageText(), chatMessage.getMessageUser(), Notification.CHAT);
        mDatabase.child("notifications")
                .push()
                .setValue(n);

    }

    private void likeMessage(int position) {

        DatabaseReference itemRef = adapter.getRef(position);
        ChatMessage message = adapter.getItem(position);
        message.setLiked(!message.isLiked());
        itemRef.setValue(message);

    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        Log.i(TAG, "Notificacion RECIBIDA");
        if ((notification!=null) && (!notification.fbid.equals(linkId))){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.chatCoordinatorLayout), notification.messageBody, Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            snackbar.show();
        }
        broadcastReceiver.abortBroadcast();

    }
}
