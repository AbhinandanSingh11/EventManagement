package com.abhinandan.nimus.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.abhinandan.nimus.R;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public UserViewHolder(View itemView) {
        super(itemView);

        mView = itemView;



    }

    public void setDetails(Context ctx, String userName, String fullName, String url){

        TextView user_name = mView.findViewById(R.id.name_text);
        TextView full_name = mView.findViewById(R.id.username_text);
        CircleImageView imageView = mView.findViewById(R.id.profile_image);



        full_name.setText("@"+fullName);
        user_name.setText(userName);

        try {
            byte [] encodeByte=Base64.decode(url,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Glide.with(ctx)
                    .load(bitmap)
                    .placeholder(R.drawable.person_two_tone)
                    .into(imageView);
        } catch(Exception e) {
            Toast.makeText(ctx, "Conversion fail "+e, Toast.LENGTH_SHORT).show();
        }




    }




}
