package com.android.onmarcy.campaign;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onmarcy.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {
    private ArrayList<Message> messages = new ArrayList<>();

    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @NotNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_comment, parent, false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.MessageAdapterViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvUsername.setText(message.getUsername());
        holder.tvMessage.setText(message.getMessage());
        holder.tvDate.setText(getIntervalDateTime(message.getDate()));

        if (message.getPhotoUrl().equals("")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.person)
                    .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                    .into(holder.circleImageView);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(message.getPhotoUrl())
                    .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                    .into(holder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvUsername, tvMessage, tvDate;

        public MessageAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvDate = itemView.findViewById(R.id.tv_date);
            circleImageView = itemView.findViewById(R.id.img_profile);
        }
    }

    private String getIntervalDateTime(String date) {
        String[] arrTemp = date.split(" ");
        String tempDate = arrTemp[0];
        String tempTime = arrTemp[1].substring(0, 5);
        String[] arrDate = tempDate.split("-");
        String myString = arrDate[2] + "-" + arrDate[1] + "-" + arrDate[0] + " " + tempTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date datetime;
        Date currentDatetime;
        String text = "";

        try {
            datetime = simpleDateFormat.parse(myString);

            DateFormat mFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            String currentTime = mFormat.format(calendar.getTime());
            currentDatetime = mFormat.parse(currentTime);

            long diff = currentDatetime.getTime() - datetime.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffWeeks = diff / (7 * 24 * 60 * 60 * 1000);
            long diffMonths = (diff / (7 * 24 * 60 * 60 * 1000) / 4);
            long diffYears = diff / (7 * 24 * 60 * 60 * 1000) / (4 * 12);

            if (diffYears > 0) {
                if (diffYears == 1) {
                    text = diffYears + " year ago";
                } else {
                    text = diffYears + " years ago";
                }
            } else {
                if (diffMonths > 0) {
                    if (diffMonths == 1) {
                        text = diffMonths + " month ago";
                    } else {
                        text = diffMonths + " months ago";
                    }
                } else {
                    if (diffWeeks > 0) {
                        if (diffWeeks == 1) {
                            text = diffWeeks + " week ago ";
                        } else {
                            text = diffWeeks + " weeks ago ";
                        }
                    } else {
                        if (diffDays > 0) {
                            if (diffDays == 1) {
                                text = diffDays + " day ago ";
                            } else {
                                text = diffDays + " days ago ";
                            }
                        } else {
                            if (diffHours > 0) {
                                if (diffHours == 1) {
                                    text = diffHours + " hour ago";
                                } else {
                                    text = diffHours + " hours ago";
                                }
                            } else {
                                if (diffMinutes > 0) {
                                    if (diffMinutes == 1) {
                                        text = diffMinutes + " minute ago";
                                    } else {
                                        text = diffMinutes + " minutes ago";
                                    }
                                } else {
                                    if (diffSeconds == 0) {
                                        text = "few seconds ago";
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return text;
    }
}
