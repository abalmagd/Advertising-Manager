package com.example.advertisingmanager;

public class DataTemplate {

    private int campaignId;
    private String campaignName;
    private String campaignLink;
    private float budget;
    private float clickPrice;

    public DataTemplate(int campaignId, String campaignName, String campaignLink,
                        float budget, float clickPrice) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.campaignLink = campaignLink;
        this.budget = budget;
        this.clickPrice = clickPrice;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public float getClickPrice() {
        return clickPrice;
    }

    public void setClickPrice(float clickPrice) {
        this.clickPrice = clickPrice;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignLink() {
        return campaignLink;
    }

    public void setCampaignLink(String campaignLink) {
        this.campaignLink = campaignLink;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }
}
