package com.abhinandan.nimus.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.abhinandan.nimus.Adapters.MenuAdapter;
import com.abhinandan.nimus.Custom.CustomViewPager;
import com.abhinandan.nimus.Fragments.FragmentHome;
import com.abhinandan.nimus.Fragments.FragmentProfile;
import com.abhinandan.nimus.LandingPages.LandingPage;
import com.abhinandan.nimus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FragmentManager manager;
    private Fragment fragment;
    private TextView link,photo,video,audio;
    private ArrayList<Fragment> list = new ArrayList<>();
    private Integer com =0;
    CustomViewPager viewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getResources().getColor(R.color.whiteTint));


        final ImageView one,two,three,four;
        final ConstraintLayout bottonNav;
        final FloatingActionButton fab;
        final Button cancel;
        final CardView bottom;





        one = findViewById(R.id.imgOne);
        //two = findViewById(R.id.imgTwo);
        //three = findViewById(R.id.imgThree);
        four = findViewById(R.id.imgFour);
        fab = findViewById(R.id.Fab);
        bottonNav = findViewById(R.id.Bottomlayout);
        cancel = findViewById(R.id.ButtonOptionCancel);
        bottom = findViewById(R.id.bottomOptions);
        link = findViewById(R.id.PostLink);
        audio = findViewById(R.id.PostAudio);
        video = findViewById(R.id.PostVideo);
        photo = findViewById(R.id.PostPhoto);
        viewPager = findViewById(R.id.Container_main);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        manager = getSupportFragmentManager();

        viewPager.setPagingEnabled(false);

        setUp(viewPager);
        usercheck();
        //three.setImageDrawable(getResources().getDrawable(R.drawable.twotone_favorite_24,getApplication().getTheme()));


        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked Link", Toast.LENGTH_SHORT).show();
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked Audio", Toast.LENGTH_SHORT).show();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked Video", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,test.class);
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked Photo", Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(view->{
            //Toast.makeText(this, "Clicked Babe", Toast.LENGTH_SHORT).show();
            bottonNav.setVisibility(View.GONE);
            fab.hide();
            bottom.setVisibility(View.VISIBLE);
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.show();
                bottonNav.setVisibility(View.VISIBLE);
                bottom.setVisibility(View.GONE);
            }
        });



        one .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fragment = new FragmentHome();
                //trans(fragment);
                bottonNav.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(0);

            }
        });

      //  two.setOnClickListener(new View.OnClickListener() {
        //    @Override
          //  public void onClick(View view) {
            //    fragment = new FragmentSearch();
              //  trans(fragment);
            //    bottonNav.setVisibility(View.VISIBLE);

     //       }
       // });

      //  three.setOnClickListener(new View.OnClickListener() {
           // @Override
         //   public void onClick(View view) {
            //    fragment = new FragmentLikes();
             //   trans(fragment);
           //     bottonNav.setVisibility(View.VISIBLE);
         //   }
       // });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fragment = new FragmentProfile();
                //trans(fragment);
                viewPager.setCurrentItem(1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.defaultActivityColor,getApplication().getTheme()));
                }
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        Toast.makeText(this, "Started ,man", Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        com = intent.getIntExtra("val",0);
        if(com==1){
            viewPager.setCurrentItem(1);
        }
        if(com==0){
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener!=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void usercheck(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(user==null){
                    Toast.makeText(MainActivity.this, "You need to login first!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    if(!user.isEmailVerified()){
                        Intent intent = new Intent(MainActivity.this, LandingPage.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
    }


    private void trans(Fragment frag){
        if(frag!=null){
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.Container_main,frag).commit();
        }
        else{
            Toast.makeText(this, "call trans fragment null", Toast.LENGTH_SHORT).show();
        }

    }



    void setUp(ViewPager viewPager){
        MenuAdapter adapter = new MenuAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,list);
        list.add(new FragmentHome());
        list.add(new FragmentProfile());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

    }

}