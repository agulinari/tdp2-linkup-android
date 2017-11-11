package com.tddp2.grupo2.linkup.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
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
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.utils.BannedWords;
import com.tddp2.grupo2.linkup.utils.ImageUtils;
import com.tddp2.grupo2.linkup.utils.LinkupUtils;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tddp2.grupo2.linkup.LinkupApplication.getContext;

public class ChatActivity extends BroadcastActivity {

    private static final String TAG = "ChatActivity";
    private FirebaseListAdapter<ChatMessage> adapter;

    ListView listOfMessages;
    private String userId;
    private String linkId;
    private String chatId;
    private DatabaseReference mDatabase;
    private Pattern urlRegex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        urlRegex = Pattern.compile("(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra("USER_ID");
        linkId = getIntent().getStringExtra("LINK_ID");
        chatId = LinkupUtils.getChatId(userId, linkId);

        String linkName = getIntent().getStringExtra("LINK_NAME");
        TextView linkNameTextView = (TextView)findViewById(R.id.chatToolBarName);
        linkNameTextView.setText(linkName);

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
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                RelativeLayout messageGlobe = (RelativeLayout)v.findViewById(R.id.message_globe);
                RelativeLayout messageGlobeText = (RelativeLayout)v.findViewById(R.id.message_globe_text);

                // Set their text
                messageText.setText(model.getMessageText());
                if (!model.isLiked()) {
                    // Format the date before showing it
                    messageTime.setText(DateFormat.format("HH:mm",
                            model.getMessageTime()));
                }else{
                    messageTime.setText(DateFormat.format("HH:mm",
                            model.getMessageTime())+ " \uD83D\uDC4D\uD83C\uDFFC️");
                }

                if (model.getFbid()!=null && model.getFbid().equals(userId)){
                    RelativeLayout.LayoutParams params1 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                    RelativeLayout.LayoutParams params2 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
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
                    messageTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }else{
                    RelativeLayout.LayoutParams params1 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

                    RelativeLayout.LayoutParams params2 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
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
                    messageTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                }


            }
        };

        listOfMessages.setAdapter(adapter);

        android.support.v7.widget.Toolbar chatToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.chatToolbar);
        chatToolbar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(getContext(), LinkProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("LINK_USER_ID", linkId);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void postMessage(String message, final String chatId) {
        // Read the input field and push a new instance
        // of ChatMessage to the Firebase database

        String sanitizedMessage = this.sanitizeMessage(message);

        final ChatMessage chatMessage = new ChatMessage(sanitizedMessage, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), userId, linkId);

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

    private String sanitizeMessage(String message) {
        StringTokenizer st = new StringTokenizer(message, " ");
        StringBuilder sb = new StringBuilder(message.length());
        while (st.hasMoreTokens()) {
            if (sb.length() > 0){
                sb.append(" ");
            }
            String token = st.nextToken();
            if (BannedWords.isBadWord(token.toLowerCase())){
                token = token.replaceAll(".", "*");
            }

            Matcher matcher = urlRegex.matcher(token);
            if (matcher.find()) {
                token = token.replaceAll(".", "*");
            }
            sb.append(token);
        }
        return sb.toString();
    }

    private void likeMessage(int position) {

        DatabaseReference itemRef = adapter.getRef(position);
        ChatMessage message = adapter.getItem(position);
        if (!message.getFbid().equals(this.userId)) {
            message.setLiked(!message.isLiked());
            itemRef.setValue(message);
        }
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        Log.i(TAG, "Notificacion RECIBIDA");
        if ((notification!=null) && (!notification.fbid.equals(linkId))){
            String text;
            if (notification.motive.equals(Notification.CHAT)) {
                text = notification.firstName + ": " + "'" + notification.messageBody + "'";
            }else {
                text = notification.messageBody;
            }
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            Log.i(TAG, "Sending notification");

            if (ServiceFactory.getProfileService()!=null && ServiceFactory.getProfileService().getLocalProfile()!=null){
                String fbid = ServiceFactory.getProfileService().getLocalProfile().getFbid();
                if (!notification.fbidTo.equals(fbid)){
                    return;
                }
                if (notification.motive.equals(Notification.BAN)){
                    ServiceFactory.getLinksService().getDatabase().setActive(false);
                    return;
                }
            }else{
                return;
            }
            // First create Parcel and write your data to it
            Parcel parcel = Parcel.obtain();
            notification.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra("notification",parcel.marshall());

            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_linkup)
                    .setContentTitle(notification.messageTitle)
                    .setVibrate(new long[] { 1000, 1000})
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setPriority(android.app.Notification.PRIORITY_MAX);

            mNotificationManager.notify("default-push", 1, mBuilder.build());
        }
        broadcastReceiver.abortBroadcast();

    }
}
