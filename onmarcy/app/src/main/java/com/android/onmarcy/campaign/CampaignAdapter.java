package com.android.onmarcy.campaign;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onmarcy.Global;
import com.android.onmarcy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import model.Campaign;
import model.User;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder> {
    private ArrayList<Campaign> campaigns;
    private User user;

    public CampaignAdapter(ArrayList<Campaign> campaigns) {
        this.campaigns = campaigns;
        try {
            this.user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnItemCallback {
        void onItemClicked(Campaign campaign);

        void showContent(Campaign campaign);

        void update(Campaign campaign);

        void delete(Campaign campaign);

        void showResult(Campaign campaign);
    }

    private OnItemCallback onItemCallback;

    public void setOnItemCallback(OnItemCallback onItemCallback) {
        this.onItemCallback = onItemCallback;
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
        holder.tvPrice.setText(holder.itemView.getContext().getResources().getString(R.string.price_format, nf.format(campaign.getPrice())));

        if (user.getUserType() == 2) {
            holder.linearUpdate.setVisibility(View.GONE);
            holder.linearDelete.setVisibility(View.GONE);
        }

        if (campaign.getStatus() == 1) {
            holder.tvStatus.setText(R.string.active);
        } else if (campaign.getStatus() == 2) {
            holder.tvStatus.setText(R.string.on_progress);
            holder.linearUpdate.setVisibility(View.GONE);
            holder.linearDelete.setVisibility(View.GONE);
        } else if (campaign.getStatus() == 3) {
            holder.tvStatus.setText(R.string.completed);
            holder.linearUpdate.setVisibility(View.GONE);
            holder.linearDelete.setVisibility(View.GONE);
            holder.linearResult.setVisibility(View.VISIBLE);
        } else if (campaign.getStatus() == 4) {
            holder.tvStatus.setText(R.string.pending);
            holder.linearUpdate.setVisibility(View.GONE);
            holder.linearDelete.setVisibility(View.GONE);
        } else if (campaign.getStatus() == 5) {
            holder.tvStatus.setText(R.string.accepted);
            holder.linearUpdate.setVisibility(View.GONE);
            holder.linearDelete.setVisibility(View.GONE);
            holder.linearResult.setVisibility(View.VISIBLE);
        } else if (campaign.getStatus() == 6) {
            holder.tvStatus.setText(R.string.rejected);
            holder.linearUpdate.setVisibility(View.GONE);
            holder.linearDelete.setVisibility(View.GONE);
            holder.linearResult.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText(R.string.inactive);
            holder.linearUpdate.setVisibility(View.GONE);
            holder.linearDelete.setVisibility(View.GONE);
        }

        if (campaign.getNotes().equals("")) {
            holder.tvDescription.setVisibility(View.GONE);
        }

        holder.tvDescription.setText(campaign.getNotes());

        String[] date = campaign.getDate().split("-");
        holder.tvDate.setText(holder.itemView.getContext().getString(R.string.date, date[2], date[1], date[0]));

        holder.itemView.setOnClickListener(view -> {
            onItemCallback.onItemClicked(campaign);
        });

        holder.btnContent.setOnClickListener(view -> {
            onItemCallback.showContent(campaign);
        });

        holder.btnDelete.setOnClickListener(view -> {
            onItemCallback.delete(campaign);
        });

        holder.btnUpdate.setOnClickListener(view -> {
            onItemCallback.update(campaign);
        });

        holder.btnResult.setOnClickListener(view -> {
            onItemCallback.showResult(campaign);
        });
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    public class CampaignViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearUpdate, linearDelete, linearResult;
        TextView tvTitle, tvPrice, tvStatus, tvDate, tvDescription;
        Button btnContent, btnUpdate, btnDelete, btnResult;

        public CampaignViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDescription = itemView.findViewById(R.id.tv_description);
            btnContent = itemView.findViewById(R.id.btn_content);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnResult = itemView.findViewById(R.id.btn_result);
            linearUpdate = itemView.findViewById(R.id.linear_update);
            linearDelete = itemView.findViewById(R.id.linear_delete);
            linearResult = itemView.findViewById(R.id.linear_result);
        }
    }
}
