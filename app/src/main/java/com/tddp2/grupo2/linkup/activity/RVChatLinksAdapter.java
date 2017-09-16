package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tddp2.grupo2.linkup.ChatActivity;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

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
        holder.personAge.setText(myChats.get(i).getAge());
        holder.personPhoto.setImageResource(myChats.get(i).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return myChats.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView fbid;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;
        private final Context context;

        PersonViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.card_chatlink);
            fbid = (TextView) itemView.findViewById(R.id.chatlink_fbid);
            personName = (TextView)itemView.findViewById(R.id.chatlink_name);
            personAge = (TextView)itemView.findViewById(R.id.chatlink_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.chatlink_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    MyLinksService service = ServiceFactory.getMyLinksService();
                    String fbidU = service.getDatabase().getProfile().getFbid();
                    String chatId = getChatId(fbidU,fbid.getText().toString());
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("CHAT_ID", chatId);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        public String getChatId(String fbidU, String fbidL){

            String chatId = "";
            if (fbidU.compareTo(fbidL)<=0){
                chatId = fbidU+":"+fbidL;
            }else{
                chatId = fbidL+":"+fbidU;
            }
            return chatId;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}