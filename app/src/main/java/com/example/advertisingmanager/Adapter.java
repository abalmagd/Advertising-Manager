package com.example.advertisingmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<DataViewHolder> {
    private Context mContext;
    private ArrayList<DataTemplate> myDataTemplate;

    public Adapter(Context mContext, ArrayList<DataTemplate> myDataTemplate) {
        this.mContext = mContext;
        this.myDataTemplate = myDataTemplate;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.campaign_item, parent, false);
        return new DataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DataTemplate currentItem = myDataTemplate.get(position);
        int campaignId = currentItem.getCampaignId();
        String campaignName = currentItem.getCampaignName();
        String campaignLink = currentItem.getCampaignLink();
        float budget = currentItem.getBudget();
        float clickPrice = currentItem.getClickPrice();

        holder.setTv_campaignId(campaignId);
        holder.setTv_campaignName(campaignName);
        holder.setTv_campaignLink(campaignLink);
        holder.setTv_budget(budget);
        holder.setTv_clickPrice(clickPrice);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(campaignLink));
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myDataTemplate.size();
    }
}
