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
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RVNewLinksAdapter  extends RecyclerView.Adapter<RVNewLinksAdapter.NewLinkViewHolder>{

    List<MyLink> myChats;

    RVNewLinksAdapter(List<MyLink> myChats){
        this.myChats = myChats;
    }

    @Override
    public NewLinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newlinks_item, parent, false);
        NewLinkViewHolder pvh = new NewLinkViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(NewLinkViewHolder holder, int i) {
        holder.fbid.setText(myChats.get(i).getFbid());
        holder.personName.setText(myChats.get(i).getName());
        holder.personPhoto.setImageResource(myChats.get(i).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return myChats.size();
    }

    public static class NewLinkViewHolder extends RecyclerView.ViewHolder {
        TextView fbid;
        TextView personName;
        CircleImageView personPhoto;
        private final Context context;

        NewLinkViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            fbid = (TextView) itemView.findViewById(R.id.newlink_fbid);
            personName = (TextView)itemView.findViewById(R.id.newlink_name);
            personPhoto = (CircleImageView)itemView.findViewById(R.id.newlink_image);
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


}
