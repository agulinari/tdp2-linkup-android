package com.tddp2.grupo2.linkup.activity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.model.MyLink;

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
        holder.personName.setText(myChats.get(i).getName());
        holder.personPhoto.setImageResource(myChats.get(i).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return myChats.size();
    }

    public static class NewLinkViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        CircleImageView personPhoto;

        NewLinkViewHolder(View itemView) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.newlink_name);
            personPhoto = (CircleImageView)itemView.findViewById(R.id.newlink_image);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
