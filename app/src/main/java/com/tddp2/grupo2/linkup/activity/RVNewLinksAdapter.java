package com.tddp2.grupo2.linkup.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tddp2.grupo2.linkup.ChatActivity;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.utils.ImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RVNewLinksAdapter  extends RecyclerView.Adapter<RVNewLinksAdapter.NewLinkViewHolder>{

    List<MyLink> myNewLinks;

    RVNewLinksAdapter(List<MyLink> myChats){
        this.myNewLinks = myChats;
    }

    @Override
    public NewLinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newlinks_item, parent, false);
        NewLinkViewHolder pvh = new NewLinkViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(NewLinkViewHolder holder, int i) {
        holder.fbid.setText(myNewLinks.get(i).getFbid());
        holder.gender.setText(myNewLinks.get(i).getGender());
        holder.personName.setText(myNewLinks.get(i).getName());
        String image = myNewLinks.get(0).getPhoto();
        Bitmap bitmap = ImageUtils.base64ToBitmap(image);
        holder.personPhoto.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return myNewLinks.size();
    }

    public static class NewLinkViewHolder extends RecyclerView.ViewHolder {
        TextView fbid;
        TextView gender;
        TextView personName;
        CircleImageView personPhoto;
        private final Context context;

        NewLinkViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            fbid = (TextView) itemView.findViewById(R.id.newlink_fbid);
            gender = (TextView) itemView.findViewById(R.id.newlink_gender);
            personName = (TextView)itemView.findViewById(R.id.newlink_name);
            personPhoto = (CircleImageView)itemView.findViewById(R.id.newlink_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Profile myProfile = ServiceFactory.getMyLinksService().getDatabase().getProfile();
                    String fbidU = myProfile.getFbid();
                    String genderU = myProfile.getGender();
                    String fbidL = fbid.getText().toString();
                    String genderL = gender.getText().toString();

                    if (!(genderL.equals("female") && genderU.equals("male"))) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("USER_ID", fbidU);
                        intent.putExtra("LINK_ID", fbidL);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
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
        if (myNewLinks != null && myNewLinks.size()>0)
            myNewLinks.clear();
        myNewLinks.addAll(datas);

        notifyDataSetChanged();

    }

}
