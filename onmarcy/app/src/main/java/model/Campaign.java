package model;

import android.app.Activity;
import android.os.AsyncTask;

import com.android.onmarcy.Global;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;

public class Campaign {
    @SerializedName("category_code")
    private int categoryCode;

    @SerializedName("title")
    private String title;

    @SerializedName("notes")
    private String notes;

    @SerializedName("age_min")
    private int ageMin;

    @SerializedName("age_max")
    private int ageMax;

    @SerializedName("gender")
    private int gender;

    @SerializedName("duration")
    private int duration;

    @SerializedName("price")
    private int price;
    // $date			 	= $jsonObject["date"];
    // $time		 		= $jsonObject["time"];
    public interface CallbackSelect{
        void success(JSONObject jsonObject);
        void error();
    }

    public interface Callback {
        void success();
        void error();
    }

    public Campaign(JSONObject jsonObject){
        try{
            this.categoryCode = jsonObject.has("category_code") ? jsonObject.getInt("category_code") : 0;
            this.title = jsonObject.has("title") ? jsonObject.getString("title") : "";
            this.notes = jsonObject.has("notes") ? jsonObject.getString("notes") : "";
            this.ageMax = jsonObject.has("age_max") ? jsonObject.getInt("age_max") : 0;
            this.ageMin = jsonObject.has("age_min") ? jsonObject.getInt("age_min") : 0;
            this.gender = jsonObject.has("gender") ? jsonObject.getInt("gender") : 0;
            this.duration = jsonObject.has("duration") ? jsonObject.getInt("duration") : 0;
            this.price = jsonObject.has("price") ? jsonObject.getInt("price") : 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void insert(Activity activity, int category, String title, String notes, int ageMin, int ageMax, int gender, int duration, int price, Boolean useLoading, Callback callback) {
        new insert(activity, category, title, notes, ageMin, ageMax, gender, duration, price, useLoading, callback).execute("v1/campaign/insert");
    }

    private static class insert extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final int category;
        final String title;
        final String notes;
        final int ageMin;
        final int ageMax;
        final int gender;
        final int duration;
        final int price;

        private insert(Activity activity, int category, String title, String notes, int ageMin, int ageMax, int gender, int duration, int price, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.category = category;
            this.title = title;
            this.notes = notes;
            this.ageMin = ageMin;
            this.ageMax = ageMax;
            this.gender = gender;
            this.duration = duration;
            this.price = price;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("socialmedia_code", 1);
                jsonObject.put("category_code", category);
                jsonObject.put("city_code", 205);
                jsonObject.put("title", title);
                jsonObject.put("notes", notes);
                jsonObject.put("age_min", ageMin);
                jsonObject.put("age_max", ageMax);
                jsonObject.put("gender", gender);
                jsonObject.put("date", "2021-03-10");
                jsonObject.put("time", "");
                jsonObject.put("duration", duration);
                jsonObject.put("price", price);
            }
            catch (JSONException e) { e.printStackTrace(); }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(useLoading) Global.hideLoading();
            try {
                JSONObject json = new JSONObject(result);
                if(json.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success();
                }
            }
            catch(Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }
}
