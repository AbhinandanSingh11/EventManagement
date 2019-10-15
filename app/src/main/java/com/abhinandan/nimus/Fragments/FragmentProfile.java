package com.abhinandan.nimus.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinandan.nimus.Activities.ActivityTeam;
import com.abhinandan.nimus.FragmentsProfile.FragmentOthers;
import com.abhinandan.nimus.FragmentsProfile.FragmentPhotos;
import com.abhinandan.nimus.Helper.BitmapFunctions;
import com.abhinandan.nimus.Models.User;
import com.abhinandan.nimus.R;
import com.abhinandan.nimus.Activities.ActivitySettings;
import com.abhinandan.nimus.Activities.ActivityEditProfile;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends Fragment {


    private static final String  TAG = FragmentProfile.class.getSimpleName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String fullname,email,uid;
    private Uri imageUri;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    //private String followers,following,likes,post;
    private TextView TVBio;
   // private TextView Followers,Following,Likes,Post;
    private String username,bio,imageString;
    private FragmentManager manager;
    private Fragment fragment;
    private int com = 0;


    public FragmentProfile() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final CircleImageView ProfileImage;
        final TextView Fullname,Username;
        final Button editProfile;
        final ImageButton teamButton;
        final ImageView settings,share,IPost,Featured,PreviewImage,ImagePost,ImageOther;
        final LinearLayout LFeatured,LPost,LBio,LinearLayoutPost,LinearLayoutOther;
        final ViewPager container_main;
        final CardView progress, preview;
        final LinearLayout PreviewProgress;
        final FloatingActionButton ClosePreview;
        final BitmapFunctions bitmapFunctions = null;


        LinearLayoutOther = view.findViewById(R.id.LinearlayoutPost);
        LinearLayoutPost = view.findViewById(R.id.LinearLayoutOther);
        ImageOther = view.findViewById(R.id.ImageViewOther);
        ImagePost = view.findViewById(R.id.ImageViewPost);
        ProfileImage = view.findViewById(R.id.ProfileImgView);
        preview = view.findViewById(R.id.CardViewPreview);
        Fullname = view.findViewById(R.id.FullNameTF);
        editProfile = view.findViewById(R.id.ButtonEditProfile);
        settings = view.findViewById(R.id.ImageSettings);
        share = view.findViewById(R.id.ImageShareApp);
        LFeatured = view.findViewById(R.id.LinearLayoutOther);
        LPost = view.findViewById(R.id.LinearlayoutPost);
        IPost = view.findViewById(R.id.ImageViewPost);
        Featured = view.findViewById(R.id.ImageViewOther);
        container_main = view.findViewById(R.id.Container_main);
        progress = view.findViewById(R.id.progressCardViewFP);
        Username = view.findViewById(R.id.UserNameTF);
        LBio = view.findViewById(R.id.LinearLayoutBioFP);
        TVBio = view.findViewById(R.id.TextBioFP);
        PreviewImage = view.findViewById(R.id.PreviewImage);
        PreviewProgress = view.findViewById(R.id.ProgressPreview);
        teamButton = view.findViewById(R.id.TeamButton);
        manager = getFragmentManager();

        ClosePreview = view.findViewById(R.id.FabClose);

        //Followers = view.findViewById(R.id.textFollowers);
        //Following = view.findViewById(R.id.textFollowing);
        //Likes = view.findViewById(R.id.textLikes);
        //Post = view.findViewById(R.id.textPosts);

        progress.setVisibility(View.VISIBLE);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        fragment = new FragmentPhotos();
        Init(fragment);

        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityTeam.class);
                startActivity(intent);
            }
        });

        LinearLayoutOther.setOnClickListener(view111 -> {
            fragment = new FragmentOthers();
            trans(fragment);
        });


        LinearLayoutPost.setOnClickListener(view110 -> {
            fragment = new FragmentPhotos();
            trans(fragment);
        });

        ImageOther.setOnClickListener(view16 -> {
            fragment = new FragmentOthers();
            trans(fragment);
        });

        ImagePost.setOnClickListener(view17 -> {
            fragment = new FragmentPhotos();
            trans(fragment);
        });

if(user!=null)
        Fullname.setText(user.getDisplayName());
else {
    Toast.makeText(getContext(), "Null user", Toast.LENGTH_SHORT).show();
}

        ClosePreview.setOnClickListener(view18 ->
                preview.setVisibility(View.GONE));



        ProfileImage.setOnClickListener(view19 -> {
            preview.setVisibility(View.VISIBLE);


            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User mUser = dataSnapshot.child("users").child(user.getEmail()).getValue(User.class);

                    if(mUser!= null){



                        //Glide.with(FragmentProfile.this)
                          //      .load(bitmapFunctions.StringToBitMap(mUser.getUrl()))
                            //    .into(PreviewImage);

                        Glide.with(FragmentProfile.this)
                                .load(user.getPhotoUrl())
                                .into(PreviewImage);
                        PreviewProgress.setVisibility(View.GONE);

                    }

                    else{
                        Toast.makeText(getContext(), "user Null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        LPost.setOnClickListener(view12 -> {

        });

        LFeatured.setOnClickListener(view13 -> {

        });

        IPost.setOnClickListener(view14 -> {

        });
        Featured.setOnClickListener(view15 -> {

        });


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               User mUser = dataSnapshot.child("users").child(user.getEmail().replace(".com","dotcom")).getValue(User.class);

                   progress.setVisibility(View.GONE);



                if(mUser != null){
                //following = mUser.getDlApproved();
                //followers = mUser.getEventsAttended();
                //likes = mUser.getDlPending();
                //post = mUser.getReviews();
                username = mUser.getUser_name();
                bio = mUser.getBio();
                fullname = mUser.getFull_name();
                imageString = mUser.getUrl();


                try {
                    SetProfile(ProfileImage,bitmapFunctions.StringToBitMap(imageString));
                }
                catch(Exception e){
                    e.printStackTrace();
                }



                //Following.setText(following);
                //Followers.setText(followers);
                //Likes.setText(likes);
                //Post.setText(post+ " Reviews");
                Username.setText("@"+username);
               //Fullname.setText(user.getDisplayName() + "  (@"+mUser.getUser_name()+")");
                Fullname.setText(fullname);



                     if(bio.isEmpty()  || (bio.equals("No Bio Added Yet"))){
                    LBio.setVisibility(View.GONE);
                }
                else{
                  LBio.setVisibility(View.VISIBLE);
                  TVBio.setText("\" " + bio +" \"");
                }




                }
                else{
                    Toast.makeText(getContext(), "User object is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Transaction Cancelled"+ error, Toast.LENGTH_SHORT).show();
            }
        });







if(user!=null)
        Init(user);

        imageUri = user.getPhotoUrl();
        fullname = user.getDisplayName();

            if(fullname.isEmpty()){
            fullname = fullname + "Please add a fullname";
            Toast.makeText(getContext(), fullname, Toast.LENGTH_SHORT).show();
        }
        share.setOnClickListener(view1->{
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });


        editProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), ActivityEditProfile.class);
            startActivity(intent);

        });
        settings.setOnClickListener(view1->{
            Intent intent = new Intent(view.getContext(), ActivitySettings.class);
            startActivity(intent);
        });







        return view;
    }

private void Init(FirebaseUser User){
    if(User.getDisplayName()!= null){
        fullname = User.getDisplayName();
    }
    else{
        Log.d(TAG, "Init: fullname or user empty");
    }
    
    //if(Objects.requireNonNull(User).getPhotoUrl()!= null){
      //  imageUri = User.getPhotoUrl();
    //}
   // else{
     //   Log.d(TAG, "Init: imageUri or user empty");
       // Toast.makeText(getContext(), "Null link", Toast.LENGTH_SHORT).show();
    //}
    
    if(Objects.requireNonNull(User).getEmail()!= null){
        email = User.getEmail();
    }
    else{
        Log.d(TAG, "Init: email or user empty");
    }
}


private void SetProfile(CircleImageView image, Bitmap bitmap){
        Glide.with(FragmentProfile.this)
                .load(bitmap)
                .placeholder(R.drawable.person_two_tone)
                .into(image);
    }
    

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("val",3);
    }


    private void trans(Fragment frag){
        if(frag!=null){
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.ContainerProfileScreen,frag).commit();
            Toast.makeText(getContext(), "Fragmnt Called", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "call trans fragment null", Toast.LENGTH_SHORT).show();
        }

    }


    private void Init(Fragment frag){

        if(frag == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.ContainerProfileScreen, new FragmentPhotos()).commit();
        }
        else{
            Toast.makeText(getContext(), "Frag Already present", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        com = 0;
    }
}
