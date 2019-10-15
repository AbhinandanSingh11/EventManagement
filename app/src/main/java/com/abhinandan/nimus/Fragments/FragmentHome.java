package com.abhinandan.nimus.Fragments;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.abhinandan.nimus.R;
import com.abhinandan.nimus.Adapters.FeedRecyclerView;
import com.abhinandan.nimus.Models.EventFeed;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    RecyclerView recyclerView;
    FeedRecyclerView adpater;
    RequestQueue queue;
    JsonArrayRequest arrayRequest;
    ArrayList<EventFeed> feed;
    CardView loader,infoBox;
    Button cancelInfoBox;
    ImageView appInfoImage;

    public FragmentHome() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.ContainerMainScreen);
        loader = view.findViewById(R.id.progressCardViewFH);
        infoBox = view.findViewById(R.id.bottomInfo);
        cancelInfoBox = view.findViewById(R.id.ButtonInfoCancel);
        appInfoImage = view.findViewById(R.id.ImageAppInfo);
        feed = new ArrayList<>();



        queue = Volley.newRequestQueue(getContext());
        fetch();
        queue.add(arrayRequest);
        load();

        cancelInfoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoBox.setVisibility(View.GONE);
            }
        });

        appInfoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoBox.setVisibility(View.VISIBLE);
            }
        });

        return  view;
    }

    void fetch(){
        String url = "https://api.myjson.com/bins/cg5m3";

        arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i =0; i<response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);

                        String posterLink = object.getString("imagelink");
                        String title = object.getString("title");
                        String desc = object.getString("desc");
                        String domainImageLink = object.getString("domainimagelink");
                        String domainName = object.getString("domianname");
                        String date = object.getString("date");
                        String startDate = object.getString("startdate");
                        String endDate = object.getString("enddate");
                        String venue = object.getString("venue");
                        String info_desc = object.getString("infodesc");
                        String highlightOne = object.getString("highOne");
                        String highlightTwo = object.getString("highTwo");
                        String highlightThree = object.getString("highThree");
                        String highlightMain = object.getString("highCen");
                        String startDateFormatTwo = object.getString("startDateFormatTwo");
                        String startTime = object.getString("timeStart");

                        EventFeed eventFeed = new EventFeed(posterLink,title,desc,domainImageLink,domainName,date,startDate,endDate,venue,info_desc,highlightOne,highlightTwo,highlightThree,highlightMain,startDateFormatTwo,startTime);
                        feed.add(eventFeed);

                        loader.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        loader.setVisibility(View.GONE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Error "+error, Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }

    void load(){
        adpater = new FeedRecyclerView(getContext(),feed);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adpater);
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(), "Started", Toast.LENGTH_SHORT).show();

    }
}
