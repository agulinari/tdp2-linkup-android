package com.tddp2.grupo2.linkup.activity.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.ChatActivity;
import com.tddp2.grupo2.linkup.activity.MyLinkProfileActivity;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.utils.ImageUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;


public class RVNewLinksAdapter  extends RecyclerView.Adapter<RVNewLinksAdapter.NewLinkViewHolder>{

    List<MyLink> myNewLinks;

    public RVNewLinksAdapter(List<MyLink> myChats){
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
        String image = myNewLinks.get(i).getPhoto();
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
                    showSelectActionDialog();
                }
            });
        }

        private void showSelectActionDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle(R.string.select_action);
            builder.setCancelable(Boolean.TRUE);
            //FIXME: No usar positive y negative buttons
            builder.setPositiveButton(R.string.start_chat, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Profile myProfile = ServiceFactory.getMyLinksService().getDatabase().getProfile();
                    String fbidU = myProfile.getFbid();
                    String genderU = myProfile.getGender();
                    String fbidL = fbid.getText().toString();
                    String genderL = gender.getText().toString();
                    if (!(genderL.equals("female") && genderU.equals("male"))) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("USER_ID", fbidU);
                        intent.putExtra("LINK_ID", fbidL);
                        intent.putExtra("LINK_NAME", personName.getText().toString());
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        showWaitWomanDialog();
                    }
                }
            });
            builder.setNegativeButton(R.string.go_to_link_profile, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(context, MyLinkProfileActivity.class);
                    context.startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        private void showWaitWomanDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setCancelable(Boolean.TRUE);
            builder.setTitle(R.string.wait_woman_title);
            builder.setMessage(R.string.wait_woman_description);
            builder.setPositiveButton(R.string.wait_woman_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
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
