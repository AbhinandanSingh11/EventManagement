package com.abhinandan.nimus.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abhinandan.nimus.R;
import com.abhinandan.nimus.Activities.test;
import com.abhinandan.nimus.Models.EventFeed;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FeedRecyclerView extends RecyclerView.Adapter<FeedRecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<EventFeed> events = new ArrayList<>();

    public FeedRecyclerView(Context context, ArrayList<EventFeed> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(events.get(position).getImageUrl())
                .placeholder(R.drawable.gg)
                .centerCrop()
                .into(holder.poster);

        Glide.with(context)
                .load(events.get(position).getDomainImageLink())
                .placeholder(R.drawable.gg)
                .centerCrop()
                .into(holder.domainIamge);

        holder.title.setText(events.get(position).getTitle());

        if(events.get(position).getDesc().length()>100){
            holder.desc.setText(events.get(position).getDesc().substring(0,100)+"...");
        }
        else{
            holder.desc.setText(events.get(position).getDesc());
        }


        holder.domainName.setText(events.get(position).getDomainName());
        holder.feeddate.setText(events.get(position).getDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, test.class);
                intent.putExtra("mainImageLink",events.get(position).getImageUrl());
                intent.putExtra("title",events.get(position).getTitle());
                intent.putExtra("desc",events.get(position).getDesc());
                intent.putExtra("date",events.get(position).getDate());
                intent.putExtra("start_date",events.get(position).getStartDate());
                intent.putExtra("end_date",events.get(position).getEndDate());
                intent.putExtra("venue",events.get(position).getVenue());
                intent.putExtra("info_desc",events.get(position).getInfo_desc());
                intent.putExtra("hOne",events.get(position).getHighlightOne());
                intent.putExtra("hTwo",events.get(position).getHighlightTwo());
                intent.putExtra("hThree",events.get(position).getHighlightThree());
                intent.putExtra("hMain",events.get(position).getHighlightMain());
                intent.putExtra("startDateTwo",events.get(position).getStartDateFormatTwo());
                intent.putExtra("startTime",events.get(position).getStartTime());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster,domainIamge;
        TextView title,desc,domainName,feeddate;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.ImageEvent);
            domainIamge = itemView.findViewById(R.id.feedDomainImage);
            title = itemView.findViewById(R.id.feedTitle);
            desc = itemView.findViewById(R.id.TextDesc);
            domainName = itemView.findViewById(R.id.feedDomainnName);
            feeddate = itemView.findViewById(R.id.feedDate);
            cardView = itemView.findViewById(R.id.RootFeedLayout);

        }
    }
}
