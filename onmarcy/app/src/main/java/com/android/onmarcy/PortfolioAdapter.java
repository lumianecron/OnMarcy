package com.android.onmarcy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import model.Portfolio;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioAdapterViewHolder> {
    private ArrayList<Portfolio> portfolios = new ArrayList<>();

    public PortfolioAdapter(ArrayList<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public interface OnItemCallback{
        void OnItemClicked(Portfolio portfolio);
    }

    OnItemCallback onItemCallback;

    public void setOnItemCallback(OnItemCallback onItemCallback) {
        this.onItemCallback = onItemCallback;
    }

    @NonNull
    @NotNull
    @Override
    public PortfolioAdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portfolio, parent, false);
        return new PortfolioAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PortfolioAdapter.PortfolioAdapterViewHolder holder, int position) {
        Portfolio portfolio = portfolios.get(position);

        Glide.with(holder.itemView.getContext())
                .load(portfolio.getImageUrl())
                .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCallback.OnItemClicked(portfolio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return portfolios.size();
    }

    public class PortfolioAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public PortfolioAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_portfolio);
        }
    }
}
