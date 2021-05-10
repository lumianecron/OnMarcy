package com.android.onmarcy.campaign;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onmarcy.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import model.Campaign;

public class PendingCampaignAdapter extends RecyclerView.Adapter<PendingCampaignAdapter.PendingCampaignViewHolder> {
    private ArrayList<Campaign> campaigns;

    public PendingCampaignAdapter(ArrayList<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public interface OnItemCallBack{
        void onLinkClicked(Campaign campaign);
    }

    private OnItemCallBack onItemCallBack;

    public void setOnItemCallBack(OnItemCallBack onItemCallBack) {
        this.onItemCallBack = onItemCallBack;
    }

    @NonNull
    @Override
    public PendingCampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pending_campaign, parent, false);
        return new PendingCampaignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingCampaignViewHolder holder, int position) {
        Campaign campaign = campaigns.get(position);
        holder.tvTitle.setText(campaign.getTitle());
        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        holder.tvPrice.setText(holder.itemView.getContext().getResources().getString(R.string.price_format, nf.format(campaign.getPrice())));
        holder.tvDescription.setText(campaign.getNotes());

        holder.tvLink.setOnClickListener(view -> {
            onItemCallBack.onLinkClicked(campaign);
        });
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    public class PendingCampaignViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvLink, tvDescription;
        public PendingCampaignViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvLink = itemView.findViewById(R.id.tv_link);
        }
    }
}
