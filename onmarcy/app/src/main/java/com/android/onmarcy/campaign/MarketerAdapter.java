package com.android.onmarcy.campaign;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onmarcy.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Approach;
import model.User;

public class MarketerAdapter extends RecyclerView.Adapter<MarketerAdapter.MarketerAdapterViewHolder> {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Approach> approaches = new ArrayList<>();
    private int code;

    public MarketerAdapter(ArrayList<User> users, ArrayList<Approach> approaches, int code) {
        this.users = users;
        this.approaches = approaches;
        this.code = code;
    }

    public interface OnItemCallback{
        void onItemClicked(User user);
        void accept(String username, int code);
    }

    private OnItemCallback onItemCallback;

    public void setOnItemCallback(OnItemCallback onItemCallback) {
        this.onItemCallback = onItemCallback;
    }

    @NonNull
    @NotNull
    @Override
    public MarketerAdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_marketer, parent, false);
        return new MarketerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MarketerAdapter.MarketerAdapterViewHolder holder, int position) {
        User user = users.get(position);
        Approach approach = approaches.get(position);
        holder.tvUsername.setText(user.getUsername());
        if (user.getPhotoUrl().equals("")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.person)
                    .apply(new RequestOptions().override(70, 70))
                    .into(holder.imgMarketer);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(user.getPhotoUrl())
                    .apply(new RequestOptions().override(70, 70))
                    .into(holder.imgMarketer);
        }
        String[] arr = approach.getDate().split(" ");
        String[] dates = arr[0].split("-");
        String[] times = arr[1].split(":");
        holder.tvDate.setText(holder.itemView.getContext().getString(R.string.datetime, dates[2], dates[1], dates[0], times[0], times[1]));

        holder.tvNotes.setText(approach.getNotes());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCallback.onItemClicked(user);
            }
        });

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCallback.accept(user.getUsername(), code);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MarketerAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvDate, tvNotes;
        CircleImageView imgMarketer;
        Button btnAccept;
        public MarketerAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvDate = itemView.findViewById(R.id.tv_date);
            imgMarketer = itemView.findViewById(R.id.img_marketer);
            tvNotes = itemView.findViewById(R.id.tv_notes);
            btnAccept = itemView.findViewById(R.id.btn_accept);
        }
    }
}
