package com.example.advertisingmanager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_campaignId;
    private TextView tv_campaignName;
    private TextView tv_campaignLink;
    private TextView tv_budget;
    private TextView tv_clickPrice;
    public LinearLayout linearLayout;

    public DataViewHolder(@NonNull View itemView) {
        super(itemView);

        linearLayout = itemView.findViewById(R.id.linear_layout);
        tv_campaignId = itemView.findViewById(R.id.tv_campaign_id);
        tv_campaignName = itemView.findViewById(R.id.tv_campaign_name);
        tv_campaignLink = itemView.findViewById(R.id.tv_link);
        tv_budget = itemView.findViewById(R.id.tv_campaign_budget_count);
        tv_clickPrice = itemView.findViewById(R.id.tv_click_price_count);
    }

    public void setTv_campaignId(int campaignId) {
        tv_campaignId.setText(campaignId + "");
    }

    public void setTv_campaignName(String campaignName) {
        tv_campaignName.setText(campaignName);
    }

    public void setTv_campaignLink(String campaignLink) {
        tv_campaignLink.setText(campaignLink);
    }

    public void setTv_budget(float budget) {
        tv_budget.setText(budget + "$");
    }

    public void setTv_clickPrice(float clickPrice) {
        tv_clickPrice.setText(clickPrice + "$");
    }
}