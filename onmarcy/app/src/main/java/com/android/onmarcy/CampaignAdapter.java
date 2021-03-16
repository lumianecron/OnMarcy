package com.android.onmarcy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import model.Campaign;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder> {
    private ArrayList<Campaign> campaigns;

    public CampaignAdapter(ArrayList<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    @NonNull
    @Override
    public CampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_campaign, parent, false);
        return new CampaignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignViewHolder holder, int position) {
        Campaign campaign = campaigns.get(position);
        holder.tvTitle.setText(campaign.getTitle());
        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        holder.tvPrice.setText("Rp. " + nf.format(campaign.getPrice()) + ",00");
        if(campaign.getStatus() == 1){
            holder.tvStatus.setText(R.string.active);
        }else if(campaign.getStatus() == 2){
            holder.tvStatus.setText(R.string.in_progress);
        }else if(campaign.getStatus() == 3){
            holder.tvStatus.setText(R.string.completed);
        }else if(campaign.getStatus() == 4){
            holder.tvStatus.setText(R.string.pending);
        }else if(campaign.getStatus() == 5){
            holder.tvStatus.setText(R.string.accepted);
        }else if(campaign.getStatus() == 6){
            holder.tvStatus.setText(R.string.rejected);
        }else{
            holder.tvStatus.setText(R.string.inactive);
        }
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    public class CampaignViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvStatus;
        public CampaignViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
