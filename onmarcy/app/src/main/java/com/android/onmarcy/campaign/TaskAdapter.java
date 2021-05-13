package com.android.onmarcy.campaign;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onmarcy.R;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Campaign;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskAdapterViewHolder> {
    private ArrayList<Campaign> campaigns = new ArrayList<Campaign>();

    public TaskAdapter(ArrayList<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public interface OnItemCallBack {
        void onItemClicked(Campaign campaign);
    }

    private OnItemCallBack onItemCallBack;

    public void setOnItemCallBack(OnItemCallBack onItemCallBack) {
        this.onItemCallBack = onItemCallBack;
    }

    @NonNull
    @Override
    public TaskAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_task, parent, false);
        return new TaskAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskAdapterViewHolder holder, int position) {
        Campaign campaign = campaigns.get(position);
        holder.tvTitle.setText(campaign.getTitle());
        holder.tvDesc.setText(campaign.getNotes());
        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        holder.tvPrice.setText(holder.itemView.getContext().getString(R.string.price_format, nf.format(campaign.getPrice())));

        String[] date = campaign.getDate().split("-");
        String myString = holder.itemView.getContext().getString(R.string.date, date[2], date[1], date[0]) + " " + campaign.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date datetime = null;

        try {
            datetime = simpleDateFormat.parse(myString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datetime);
            calendar.add(Calendar.HOUR_OF_DAY, campaign.getDuration());
            SimpleDateFormat mFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            holder.tvDate.setText(mFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(view -> {
            onItemCallBack.onItemClicked(campaign);
        });
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    public class TaskAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvPrice, tvDate;

        public TaskAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_description);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
