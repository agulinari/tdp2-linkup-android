package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tddp2.grupo2.linkup.ChatActivity;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.model.ChatMessage;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.utils.ImageUtils;

import java.util.List;


public class RVChatLinksAdapter extends RecyclerView.Adapter<RVChatLinksAdapter.PersonViewHolder>{

    List<MyLink> myChats;

    RVChatLinksAdapter(List<MyLink> myChats){
        this.myChats = myChats;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlinks_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int i) {
        holder.fbid.setText(myChats.get(i).getFbid());
        holder.personName.setText(myChats.get(i).getName());
        ChatMessage lastMessage = myChats.get(i).getLastMessage();
        if (lastMessage != null) {
            holder.personLastMessage.setText(lastMessage.getMessageText());
        }
        String image = myChats.get(i).getPhoto();
        Bitmap bitmap = ImageUtils.base64ToBitmap(image);
        holder.personPhoto.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return myChats.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView fbid;
        TextView personName;
        TextView personLastMessage;
        ImageView personPhoto;
        private final Context context;

        PersonViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.card_chatlink);
            fbid = (TextView) itemView.findViewById(R.id.chatlink_fbid);
            personName = (TextView)itemView.findViewById(R.id.chatlink_name);
            personLastMessage = (TextView)itemView.findViewById(R.id.chatlink_lastmessage);
            personPhoto = (ImageView)itemView.findViewById(R.id.chatlink_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    MyLinksService service = ServiceFactory.getMyLinksService();
                    String fbidU = service.getDatabase().getProfile().getFbid();
                    String fbidL = fbid.getText().toString();
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("USER_ID", fbidU);
                    intent.putExtra("LINK_ID", fbidL);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void swap(List<MyLink> datas)
    {
        if(datas == null)
            return;
        if (myChats != null && myChats.size()>0)
            myChats.clear();
        myChats.addAll(datas);
        notifyDataSetChanged();

    }

}