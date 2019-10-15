package com.abhinandan.nimus.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abhinandan.nimus.R;
import com.bumptech.glide.Glide;


public class test extends AppCompatActivity {

    Button generate;
    ImageView image;
    ImageView back;
    String url;
    String title;
    String desc;
    String date;
    String start_date;
    String end_date;
    String venue;
    String info_desc;
    String hone;
    String htwo;
    String hthree;
    String hmain;
    String startDateTwo;
    String startTime;

    TextView Title;
    TextView StartDate;
    TextView EndDate;
    TextView Venue;
    TextView InfoDesc;
    TextView hOne;
    TextView hTwo;
    TextView hThree;
    TextView hMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));


        generate = findViewById(R.id.ButtonQRGenerator);
        image = findViewById(R.id.ImageViewInfo);
        Title = findViewById(R.id.InfoTitleText);
        StartDate = findViewById(R.id.infoStartDate);
        EndDate = findViewById(R.id.infoEndDate);
        InfoDesc = findViewById(R.id.infoDesc);
        Venue = findViewById(R.id.infoVenue);
        back = findViewById(R.id.ImageBackInfoEvent);
        hOne = findViewById(R.id.hOne);
        hTwo = findViewById(R.id.hTwo);
        hThree = findViewById(R.id.hThree);
        hMain = findViewById(R.id.hMain);


        url = getIntent().getStringExtra("mainImageLink");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        date = getIntent().getStringExtra("date");
        start_date = getIntent().getStringExtra("start_date");
        end_date = getIntent().getStringExtra("end_date");
        venue = getIntent().getStringExtra("venue");
        info_desc = getIntent().getStringExtra("info_desc");
        hone = getIntent().getStringExtra("hOne");
        htwo = getIntent().getStringExtra("hTwo");
        hthree = getIntent().getStringExtra("hThree");
        hmain = getIntent().getStringExtra("hMain");
        startDateTwo = getIntent().getStringExtra("startDateTwo");
        startTime = getIntent().getStringExtra("startTime");




        Glide.with(this)
                .load(url)
                .placeholder(getDrawable(R.drawable.background_primary_grey_with_stars))
                .into(image);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(test.this, ActivityQRcode.class);
                intent.putExtra("title",title);
                intent.putExtra("startdate",start_date);
                intent.putExtra("enddate",end_date);
                intent.putExtra("startdateTwo",startDateTwo);
                intent.putExtra("startTime",startTime);
                intent.putExtra("hOne",hone);
                intent.putExtra("hTwo",htwo);
                intent.putExtra("hThree",hthree);
                intent.putExtra("hMain",hmain);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(test.this,MainActivity.class);
                intent.putExtra("val",0);
                startActivity(intent);
            }
        });

        Title.setText(title);
        StartDate.setText(start_date);
        EndDate.setText(end_date);
        Venue.setText(venue);
        InfoDesc.setText(info_desc);
        hOne.setText(hone);
        hTwo.setText(htwo);
        hThree.setText(hthree);
        hMain.setText(hmain);
    }
}